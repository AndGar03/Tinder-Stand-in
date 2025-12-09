package udistrital.avanzada.tinderstandin.multimedia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la creación de una nueva foto.
 *
 * @author AndGar03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearFotoDTO {
    
    /**
     * URL de la foto a almacenar.
     */
    @NotBlank(message = "La URL de la foto no puede estar vacía")
    private String url;
    
    /**
     * ID del usuario al que pertenece la foto.
     */
    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;
}
