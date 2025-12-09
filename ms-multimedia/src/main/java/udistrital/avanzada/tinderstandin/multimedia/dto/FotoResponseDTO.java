package udistrital.avanzada.tinderstandin.multimedia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO de respuesta con información de una foto.
 *
 * @author AndGar03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FotoResponseDTO {
    
    /**
     * Identificador único de la foto.
     */
    private Long id;
    
    /**
     * URL de la foto almacenada.
     */
    private String url;
    
    /**
     * ID del usuario propietario.
     */
    private Long usuarioId;
    
    /**
     * Fecha de creación de la foto.
     */
    private LocalDateTime fechaCreacion;
}
