package udistrital.avanzada.tinderstandin.usuario.controladores;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import udistrital.avanzada.tinderstandin.usuario.dto.JwtResponse;
import udistrital.avanzada.tinderstandin.usuario.dto.LoginRequest;
import udistrital.avanzada.tinderstandin.usuario.dto.MensajeRespuesta;
import udistrital.avanzada.tinderstandin.usuario.dto.RegistroRequest;
import udistrital.avanzada.tinderstandin.usuario.modelos.EntidadUsuario;
import udistrital.avanzada.tinderstandin.usuario.seguridad.JwtUtils;
import udistrital.avanzada.tinderstandin.usuario.seguridad.UserDetailsImpl;
import udistrital.avanzada.tinderstandin.usuario.servicios.ServicioUsuario;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controlador REST para operaciones de autenticación.
 * Gestiona registro de nuevos usuarios e inicio de sesión.
 *
 * @author SanSantax
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
@Slf4j
public class ControladorAutenticacion {
    
    private final AuthenticationManager authenticationManager;
    private final ServicioUsuario servicioUsuario;
    private final JwtUtils jwtUtils;
    
    /**
     * Endpoint para iniciar sesión.
     *
     * @param loginRequest Credenciales del usuario
     * @return ResponseEntity con el token JWT y la información del usuario
     */
    @PostMapping("/login")
    public ResponseEntity<?> iniciarSesion(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Intento de inicio de sesión para el usuario: {}", loginRequest.getUsername());
        
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generarTokenJWT(authentication);
            
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());
            
            log.info("Inicio de sesión exitoso para: {}", loginRequest.getUsername());
            
            return ResponseEntity.ok(new JwtResponse(
                    jwt,
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    roles
            ));
        } catch (Exception e) {
            log.error("Error en inicio de sesión para {}: {}", loginRequest.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MensajeRespuesta("Credenciales inválidas"));
        }
    }
    
    /**
     * Endpoint para registrar un nuevo usuario.
     *
     * @param registroRequest Datos del nuevo usuario
     * @return ResponseEntity con mensaje de éxito o error
     */
    @PostMapping("/registro")
    public ResponseEntity<?> registrarUsuario(@Valid @RequestBody RegistroRequest registroRequest) {
        log.info("Solicitud de registro de nuevo usuario: {}", registroRequest.getUsername());
        
        try {
            EntidadUsuario usuarioRegistrado = servicioUsuario.registrarUsuario(registroRequest);
            log.info("Usuario registrado exitosamente: {}", usuarioRegistrado.getUsername());

            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Usuario registrado exitosamente");
            respuesta.put("id", usuarioRegistrado.getId());
            respuesta.put("username", usuarioRegistrado.getUsername());
            // En entorno escolar se devuelve la contraseña enviada, almacenada en texto plano
            respuesta.put("password", registroRequest.getPassword());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        } catch (RuntimeException e) {
            log.error("Error al registrar usuario: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new MensajeRespuesta(e.getMessage()));
        } catch (Exception e) {
            log.error("Error inesperado al registrar usuario: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MensajeRespuesta("Error al procesar el registro"));
        }
    }
    
    /**
     * Endpoint para obtener información de un usuario por ID (para otros microservicios).
     *
     * @param id ID del usuario
     * @return ResponseEntity con la información del usuario
     */
    @GetMapping("/usuario/{id}")
    public ResponseEntity<?> obtenerUsuarioPorId(@PathVariable Long id) {
        log.info("Solicitud de información del usuario con ID: {}", id);
        
        try {
            EntidadUsuario usuario = servicioUsuario.obtenerPorId(id);
            
            // Crear un DTO simplificado para evitar exponer información sensible
            var usuarioDTO = new java.util.HashMap<String, Object>();
            usuarioDTO.put("id", usuario.getId());
            usuarioDTO.put("username", usuario.getUsername());
            usuarioDTO.put("email", usuario.getEmail());
            usuarioDTO.put("nombreCompleto", usuario.getNombreCompleto());
            usuarioDTO.put("genero", usuario.getGenero());
            usuarioDTO.put("ciudad", usuario.getCiudad());
            usuarioDTO.put("descripcion", usuario.getDescripcion());
            usuarioDTO.put("fotoPerfil", usuario.getFotoPerfil());
            
            return ResponseEntity.ok(usuarioDTO);
        } catch (RuntimeException e) {
            log.error("Error al obtener usuario con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MensajeRespuesta("Usuario no encontrado"));
        }
    }
}
