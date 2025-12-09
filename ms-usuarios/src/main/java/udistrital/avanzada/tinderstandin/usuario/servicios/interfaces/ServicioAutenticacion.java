package udistrital.avanzada.tinderstandin.usuario.servicios.interfaces;

import udistrital.avanzada.tinderstandin.usuario.dto.JwtResponse;
import udistrital.avanzada.tinderstandin.usuario.dto.LoginRequest;
import udistrital.avanzada.tinderstandin.usuario.dto.RegistroRequest;

/**
 * Interfaz para el servicio de autenticación.
 * 
 * @author SanSantax
 */
public interface ServicioAutenticacion {
    
    /**
     * Autentica un usuario y genera un token JWT.
     * 
     * @param loginRequest Datos de inicio de sesión
     * @return Respuesta con el token JWT y datos del usuario
     */
    JwtResponse autenticarUsuario(LoginRequest loginRequest);
    
    /**
     * Registra un nuevo usuario en el sistema.
     * 
     * @param registroRequest Datos de registro del nuevo usuario
     * @return Mensaje de éxito o error
     */
    String registrarUsuario(RegistroRequest registroRequest);
    
    /**
     * Verifica si un nombre de usuario ya está en uso.
     * 
     * @param username Nombre de usuario a verificar
     * @return true si el nombre de usuario ya está en uso, false en caso contrario
     */
    boolean existePorUsername(String username);
    
    /**
     * Verifica si un correo electrónico ya está registrado.
     * 
     * @param email Correo electrónico a verificar
     * @return true si el correo ya está registrado, false en caso contrario
     */
    boolean existePorEmail(String email);
}
