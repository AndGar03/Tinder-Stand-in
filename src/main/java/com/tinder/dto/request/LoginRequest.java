package com.tinder.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO para la solicitud de inicio de sesión.
 * Contiene las credenciales del usuario (nombre de usuario y contraseña).
 */
@Data
public class LoginRequest {
    
    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    private String username;

    @NotBlank(message = "La contraseña no puede estar vacía")
    private String password;
}
