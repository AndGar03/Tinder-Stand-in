package com.tinder.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * DTO para la respuesta de autenticación exitosa.
 * Contiene el token JWT y la información básica del usuario autenticado.
 */
@Data
public class JwtResponse {
    
    /**
     * Token JWT generado para la autenticación.
     */
    private String token;
    
    /**
     * Tipo de token (siempre será "Bearer").
     */
    private final String type = "Bearer";
    
    /**
     * ID único del usuario.
     */
    private Long id;
    
    /**
     * Nombre de usuario del usuario autenticado.
     */
    private String username;
    
    /**
     * Correo electrónico del usuario.
     */
    private String email;
    
    /**
     * Lista de roles/permisos del usuario.
     */
    private List<String> roles;
    
    /**
     * Constructor para crear una respuesta JWT.
     *
     * @param token el token de acceso JWT
     * @param id el ID del usuario
     * @param username el nombre de usuario
     * @param email el correo electrónico del usuario
     * @param roles los roles del usuario
     */
    public JwtResponse(String token, Long id, String username, String email, List<String> roles) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
    
    /**
     * Obtiene el token con el prefijo "Bearer ".
     *
     * @return el token con el prefijo
     */
    public String getToken() {
        return type + " " + token;
    }
}
