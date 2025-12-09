# Tinder-Stand-in

Proyecto Maven multi-módulo con tres microservicios Spring Boot y un frontend estático:

- `ms-usuarios` (gestión de usuarios y autenticación básica, puerto 8081).
- `ms-social` (swiping/likes/matches, puerto 8082, se comunica con ms-usuarios via HTTP).
- `ms-multimedia` (gestión de fotos de usuario, puerto 8083).
- `frontend-app` (HTML/CSS/JS que consume los tres microservicios via `fetch`).

## Requisitos previos

- Java 17 instalado.
- Maven disponible en la línea de comandos.
- MySQL (por ejemplo XAMPP) ejecutándose en `localhost:3306`.

## Base de datos

1. Crear el esquema y tablas ejecutando en MySQL:

   ```sql
   SOURCE data/script_creacion_bd.sql;
   ```

   o, si lo prefieres, el script unificado:

   ```sql
   SOURCE data/script_unificado.sql;
   ```

2. Credenciales de ejemplo y parámetros de conexión se documentan en:

   - `data/configuracion_db.txt`
   - `data/conexion.txt`

3. Ajusta, si es necesario, las propiedades de conexión en:

   - `ms-usuarios/src/main/resources/application.properties`
   - `ms-social/src/main/resources/application.properties`
   - `ms-multimedia/src/main/resources/application.properties`

## Compilación del proyecto

Desde la raíz del repositorio:

```bash
mvn clean package
```

Esto compila el POM padre y los módulos `ms-usuarios`, `ms-social` y `ms-multimedia`.

## Ejecución de los microservicios

En tres terminales distintas:

```bash
# ms-usuarios (puerto 8081)
cd ms-usuarios
mvn spring-boot:run

# ms-social (puerto 8082)
cd ms-social
mvn spring-boot:run

# ms-multimedia (puerto 8083)
cd ms-multimedia
mvn spring-boot:run
```

Comprueba que todos arrancan sin errores y pueden conectarse a MySQL.

## Ejecución del frontend

El frontend es estático y vive en `frontend-app`. Puedes servirlo, por ejemplo, con Python:

```bash
cd frontend-app
python -m http.server 5500
```

Luego abre en el navegador:

- `http://localhost:5500/index.html` (pantalla de registro).
- `http://localhost:5500/swiping.html` (pantalla de swiping).
- `http://localhost:5500/matches.html` (listado de matches).

Las URLs base usadas en `frontend-app/app.js` son:

- `http://localhost:8081` para ms-usuarios.
- `http://localhost:8082` para ms-social.

Si cambias puertos, actualiza también `app.js` y los `application.properties`.

## Autores

Los roles de los autores se documentan en `Docs/integrantes.txt`.
