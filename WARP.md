# WARP.md

This file provides guidance to WARP (warp.dev) when working with code in this repository.

## Proyecto y arquitectura general

Este repositorio implementa un clon de Tinder usando una arquitectura de microservicios basada en Spring Boot 3.x y Maven multi-módulo.

- `pom.xml` (raíz): POM padre con packaging `pom`, dependencias comunes (Spring Boot, Spring Cloud, JPA, Security, Mail, JWT, Lombok, MySQL) y definición de módulos.
- Módulo actual definido: `ms-usuarios/` (microservicio de usuarios, autenticación y autorización).
- Módulos esperados a futuro (no presentes o no declarados aún en este repo): `ms-social` y `ms-multimedia`, siguiendo el patrón `udistrital.avanzada.tinderstandin.[modulo]`.

El enfoque de diseño es:
- Cada microservicio es un proyecto Spring Boot independiente con su propio `application.yml`/`application.properties`, puerto y configuración de base de datos.
- Se comparte un conjunto de dependencias gestionadas desde el POM padre para mantener versiones consistentes.
- Paquetes Java bajo `udistrital.avanzada.tinderstandin.[modulo].*`.

## Arquitectura de `ms-usuarios`

Ruta base del módulo: `ms-usuarios/`.

### Entrada de la aplicación

- `src/main/java/udistrital/avanzada/tinderstandin/usuarios/MsUsuariosApplication.java`
  - `@SpringBootApplication`, `@EnableDiscoveryClient` y `@EntityScan` sobre `entidades`.
  - Es la clase main del microservicio de usuarios.

### Capa de dominio (entidades JPA)

Paquete: `udistrital.avanzada.tinderstandin.usuarios.entidades`.

- `Persona` (abstracta)
  - Mapeada a tabla `personas` (estrategia `JOINED`).
  - Campos básicos: `id`, `nombre`, `apellido`, `email`, `fechaNacimiento`, `telefono`, `fechaCreacion`, `fechaActualizacion` con `@PreUpdate`.
  - Es la raíz de la jerarquía de usuarios.

- `Usuario` (extiende `Persona`)
  - Tabla `usuarios` con `@PrimaryKeyJoinColumn("persona_id")`.
  - Campos adicionales: `username`, `password`, `fotoPerfil`, `biografia`, `activo`, `codigoActivacion`.
  - Relación `@ManyToMany(fetch = EAGER)` con `Rol` mediante tabla `usuario_roles`.
  - Métodos auxiliares `agregarRol` y `eliminarRol` para gestionar roles.

- `Rol`
  - Tabla `roles`.
  - Campo `nombre` es un `@Enumerated(EnumType.STRING)` del enum interno `RolNombre { ROLE_USUARIO, ROLE_ADMIN, ROLE_MODERADOR }`.

### Capa de repositorios

Paquete: `udistrital.avanzada.tinderstandin.usuarios.repositorios`.

- `UsuarioRepositorio extends JpaRepository<Usuario, Long>`
  - Búsquedas por `username`, `email`, `codigoActivacion` y validaciones de existencia (`existsByUsername`, `existsByEmail`).
  - Este repositorio es la fuente única de verdad de usuarios para este microservicio.

- `RolRepositorio extends JpaRepository<Rol, Long>`
  - Búsqueda de roles por `Rol.RolNombre`.

### Capa de seguridad y autenticación

Paquete: `udistrital.avanzada.tinderstandin.usuarios.configuracion` y `seguridad`.

- `SecurityConfig` (extiende `WebSecurityConfigurerAdapter`)
  - Integra `UserDetailsServiceImpl` como proveedor de usuarios.
  - Configura `PasswordEncoder` (BCrypt).
  - Deshabilita CSRF, usa sesiones stateless y añade `JwtAuthenticationFilter` antes de `UsernamePasswordAuthenticationFilter`.
  - Rutas públicas: `/api/auth/**`, documentación OpenAPI (`/v3/api-docs/**`, `/swagger-ui/**`, `/swagger-ui.html`). Resto autenticado.

- `UserDetailsServiceImpl` (en `servicios`)
  - Implementa `UserDetailsService`.
  - Carga usuarios por email a través de `UsuarioRepositorio`.
  - Envuelve el dominio en `UsuarioPrincipal` (objeto de seguridad).

- `UsuarioPrincipal` (en `seguridad`)
  - Implementa `UserDetails`.
  - Adapta la entidad `Usuario` a la API de Spring Security, mapeando `roles` a `GrantedAuthority`.

- `JwtUtils` (en `seguridad`)
  - Generación y validación de tokens JWT usando `io.jsonwebtoken`.
  - Usa una clave simétrica derivada de una cadena de configuración y expiración configurables.
  - Proporciona métodos para generar tokens, obtener el usuario desde el token y validarlo.

- `JwtAuthenticationFilter` (en `seguridad`)
  - Filtro `OncePerRequestFilter` que:
    - Extrae el token del header `Authorization` ("Bearer ...").
    - Valida el JWT y, si es válido, carga el usuario vía `UserDetailsServiceImpl`.
    - Pone la autenticación en el `SecurityContext`.

> Nota: hay cierta divergencia de nombres entre los métodos de `JwtUtils` y los usados desde el filtro/controladores en este repo. Antes de extender la funcionalidad de autenticación, revisa y alinea esos métodos (nombres, paquetes, propiedades de configuración).

### Capa de transporte (DTOs) y controladores

Paquete: `udistrital.avanzada.tinderstandin.usuarios.dto`.

- `RegistroUsuarioDTO`
  - Carga útil para registro de usuario, con validaciones detalladas (tamaño, formato email, regex de username y contraseña fuerte, teléfono, fecha de nacimiento).

- `LoginDTO`
  - Carga útil para login con username/email y password (ambos `@NotBlank`).

- `JwtResponse`
  - Respuesta de autenticación: token, tipo, id, username, email y lista de roles.

Controlador principal actual:

- `AuthController` (en `controladores`)
  - `POST /api/auth/login` — autentica al usuario usando `AuthenticationManager`, genera JWT y devuelve `JwtResponse`.
  - `POST /api/auth/registro` — registra un nuevo usuario, asigna roles por defecto o en base a la entrada y persiste mediante `UsuarioRepositorio` y `RolRepositorio`.

### Configuración del microservicio

- `src/main/resources/application.yml` en `ms-usuarios`:
  - Puerto: `8080` y context-path `/api/usuarios`.
  - Configuración de datasource MySQL (BD `tinder_standin`).
  - Configuración básica de JPA (dialecto MySQL8, `ddl-auto=update`, SQL visible y formateado).
  - Bloques para JWT y correo electrónico, más configuración de Eureka (descubrimiento de servicios).

Ten en cuenta que:
- El archivo mezcla dos bloques `spring:` (uno para la aplicación y datasource/JPA y otro para mail). Si ajustas configuración, mantén una estructura consistente de YAML para evitar sobrescribir secciones.
- La configuración de JWT en `application.yml` y las propiedades leídas por `JwtUtils` pueden no coincidir exactamente; valida nombres de propiedades antes de depender de ellas.

## Comandos habituales (Maven / Spring Boot)

Todos los comandos se ejecutan desde la raíz del proyecto (`tinder-stand-in-parent`) salvo que se indique lo contrario.

### Compilación y empaquetado

- Compilar y empaquetar todo el proyecto (incluyendo módulos):
  - `./mvnw clean package` (Unix) o `mvnw.cmd clean package` (Windows).

- Compilar solo el módulo `ms-usuarios`:
  - `mvnw.cmd -pl ms-usuarios -am clean package`

### Ejecutar microservicios

- Ejecutar `ms-usuarios` desde la raíz vía Maven:
  - `mvnw.cmd -pl ms-usuarios spring-boot:run`

- Ejecutar `ms-usuarios` desde dentro del módulo:
  - `cd ms-usuarios`
  - `mvnw.cmd spring-boot:run` (o `mvn spring-boot:run` si Maven está instalado globalmente).

Asegúrate de que MySQL esté corriendo y que la base de datos `tinder_standin` exista con las credenciales configuradas en `application.yml`.

### Tests

Actualmente no hay tests definidos en el módulo (`src/test/java` no contiene clases). Si se añaden:

- Ejecutar todos los tests de todos los módulos:
  - `mvnw.cmd test`

- Ejecutar tests solo para `ms-usuarios`:
  - `mvnw.cmd -pl ms-usuarios test`

- Ejecutar un test específico (una vez que existan tests JUnit en `ms-usuarios`):
  - `mvnw.cmd -pl ms-usuarios -Dtest=NombreDeLaClaseDeTest test`

## Consideraciones para futuras extensiones

Cuando se agreguen nuevos microservicios (`ms-social`, `ms-multimedia`):

- Decláralos como módulos hijos en el `pom.xml` raíz bajo `<modules>`.
- Mantén el mismo esquema de paquetes: `udistrital.avanzada.tinderstandin.social` y `udistrital.avanzada.tinderstandin.multimedia`.
- Cada módulo debe tener su propio `pom.xml` con `parent` apuntando al POM raíz.
- Cada módulo debe tener su propio archivo de configuración (`application.yml` o `.properties`) con puerto distinto.

Al tocar seguridad/autenticación:
- Reutiliza `JwtUtils`, `UsuarioPrincipal` y `UserDetailsServiceImpl` o extrae componentes compartidos en un módulo común si se comparten entre microservicios.
- Alinea nomenclatura de propiedades JWT entre configuración y código antes de propagar cambios.
