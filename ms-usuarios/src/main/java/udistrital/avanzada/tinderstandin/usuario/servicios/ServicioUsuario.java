package udistrital.avanzada.tinderstandin.usuario.servicios;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import udistrital.avanzada.tinderstandin.usuario.dto.RegistroRequest;
import udistrital.avanzada.tinderstandin.usuario.modelos.EntidadRol;
import udistrital.avanzada.tinderstandin.usuario.modelos.EntidadUsuario;
import udistrital.avanzada.tinderstandin.usuario.repositorios.RolRepositorio;
import udistrital.avanzada.tinderstandin.usuario.repositorios.UsuarioRepositorio;

import java.util.regex.Pattern;

/**
 * Servicio para gestión de usuarios con validación de contraseñas
 * y envío de correos de bienvenida.
 *
 * @author SanSantax
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ServicioUsuario {
    
    private final UsuarioRepositorio usuarioRepositorio;
    private final RolRepositorio rolRepositorio;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    
    // Expresión regular para validación de contraseña
    // Debe coincidir con la usada en RegistroRequest (@Pattern)
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!*])(?=\\S+$).{8,}$"
    );
    
    /**
     * Valida que la contraseña cumpla con los requisitos de seguridad:
     * - Mínimo 8 caracteres
     * - Al menos 1 mayúscula
     * - Al menos 1 minúscula
     * - Al menos 1 número
     * - Al menos 1 carácter especial (@$!%*?&.)
     *
     * @param password Contraseña a validar
     * @return true si la contraseña es válida, false en caso contrario
     */
    public boolean validarPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        return PASSWORD_PATTERN.matcher(password).matches();
    }
    
    /**
     * Registra un nuevo usuario en el sistema.
     * Valida la contraseña, cifra la información y envía correo de bienvenida.
     *
     * @param registroRequest Datos del usuario a registrar
     * @return Usuario registrado
     * @throws RuntimeException si la contraseña no cumple requisitos o el usuario ya existe
     */
    @Transactional
    public EntidadUsuario registrarUsuario(RegistroRequest registroRequest) {
        log.info("Iniciando registro de usuario: {}", registroRequest.getUsername());
        
        // Validar que el usuario no exista
        if (usuarioRepositorio.existsByUsername(registroRequest.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya está en uso");
        }
        
        if (usuarioRepositorio.existsByEmail(registroRequest.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }
        
        // Validar contraseña (mismas reglas que en RegistroRequest)
        if (!validarPassword(registroRequest.getPassword())) {
            throw new RuntimeException(
                "La contraseña debe tener al menos 8 caracteres, " +
                "1 mayúscula, 1 minúscula, 1 número y 1 carácter especial (@#$%^&+=!*)"
            );
        }
        
        // Crear usuario. La contraseña se procesa a través del PasswordEncoder configurado.
        EntidadUsuario usuario = new EntidadUsuario();
        usuario.setUsername(registroRequest.getUsername());
        usuario.setEmail(registroRequest.getEmail());
        usuario.setPassword(passwordEncoder.encode(registroRequest.getPassword()));
        usuario.setNombreCompleto(registroRequest.getNombreCompleto());
        usuario.setTelefono(registroRequest.getTelefono());
        usuario.setGenero(registroRequest.getGenero());
        usuario.setCiudad(registroRequest.getCiudad());
        usuario.setFechaNacimiento(registroRequest.getFechaNacimiento());
        usuario.setDescripcion(registroRequest.getDescripcion());
        usuario.setHabilitado(true);
        
        // Asignar rol por defecto
        EntidadRol rolUsuario = rolRepositorio
            .findByNombre(EntidadRol.NombreRol.ROLE_USUARIO)
            .orElseGet(() -> {
                EntidadRol nuevoRol = new EntidadRol(EntidadRol.NombreRol.ROLE_USUARIO);
                return rolRepositorio.save(nuevoRol);
            });
        
        usuario.getRoles().add(rolUsuario);
        
        // Guardar usuario
        EntidadUsuario usuarioGuardado = usuarioRepositorio.save(usuario);
        log.info("Usuario registrado exitosamente: {}", usuarioGuardado.getUsername());
        
        // Enviar correo de bienvenida
        enviarCorreoBienvenida(usuarioGuardado);
        
        return usuarioGuardado;
    }
    
    /**
     * Envía un correo de bienvenida al usuario recién registrado.
     *
     * @param usuario Usuario que recibirá el correo
     */
    private void enviarCorreoBienvenida(EntidadUsuario usuario) {
        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setTo(usuario.getEmail());
            mensaje.setSubject("¡Bienvenido a Tinder Stand-in!");
            mensaje.setText(
                "Hola " + usuario.getNombreCompleto() + ",\n\n" +
                "¡Bienvenido a Tinder Stand-in!\n\n" +
                "Tu cuenta ha sido creada exitosamente con el nombre de usuario: " + 
                usuario.getUsername() + "\n\n" +
                "Ahora puedes iniciar sesión y comenzar a conocer gente nueva.\n\n" +
                "Saludos,\n" +
                "El equipo de Tinder Stand-in"
            );
            
            mailSender.send(mensaje);
            log.info("Correo de bienvenida enviado a: {}", usuario.getEmail());
        } catch (Exception e) {
            log.error("Error al enviar correo de bienvenida: {}", e.getMessage());
            // No lanzamos excepción para no interrumpir el registro
        }
    }
    
    /**
     * Obtiene un usuario por su nombre de usuario.
     *
     * @param username Nombre de usuario
     * @return Usuario encontrado
     */
    @Transactional(readOnly = true)
    public EntidadUsuario obtenerPorUsername(String username) {
        return usuarioRepositorio.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));
    }
    
    /**
     * Obtiene un usuario por su ID.
     *
     * @param id ID del usuario
     * @return Usuario encontrado
     */
    @Transactional(readOnly = true)
    public EntidadUsuario obtenerPorId(Long id) {
        return usuarioRepositorio.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }
}
