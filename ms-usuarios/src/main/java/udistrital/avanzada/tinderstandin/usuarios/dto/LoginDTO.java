package udistrital.avanzada.tinderstandin.usuarios.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO para la solicitud de inicio de sesión.
 * 
 * @author SanSantax
 * @author AndGar03
 */
@Data
public class LoginDTO {
    
    @NotBlank(message = "El nombre de usuario o correo electrónico es obligatorio")
    private String usernameOrEmail;
    
    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}
