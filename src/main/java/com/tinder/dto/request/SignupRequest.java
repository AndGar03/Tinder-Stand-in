package com.tinder.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * DTO para la solicitud de registro de usuario.
 * Contiene todos los campos necesarios para registrar un nuevo usuario en el sistema.
 */
@Data
public class SignupRequest {
    
    @NotBlank(message = "El nombre completo no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    private String nombreCompleto;

    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", 
             message = "El nombre de usuario solo puede contener letras, números, puntos, guiones bajos y guiones")
    private String username;

    @NotBlank(message = "El correo electrónico no puede estar vacío")
    @Email(message = "El formato del correo electrónico no es válido")
    @Size(max = 100, message = "El correo electrónico no puede tener más de 100 caracteres")
    private String email;

    @NotBlank(message = "El teléfono no puede estar vacío")
    @Pattern(regexp = "^[0-9+\\(\\).\\- ]+$", 
             message = "El formato del teléfono no es válido")
    private String telefono;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,}$",
        message = "La contraseña debe contener al menos una letra mayúscula, una minúscula, un número y un carácter especial"
    )
    private String password;

    @AssertTrue(message = "Debes aceptar los términos y condiciones")
    private boolean terminosAceptados;

    // Campos opcionales
    private String ciudad;
    private String biografia;
}
