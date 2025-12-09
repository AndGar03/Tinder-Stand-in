package udistrital.avanzada.tinderstandin.social.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para información de likes.
 *
 * @author AndGar03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeResponseDTO {
    
    /**
     * ID del like.
     */
    private Long id;
    
    /**
     * ID del usuario que dio like.
     */
    private Long usuarioOrigenId;
    
    /**
     * ID del usuario que recibió el like.
     */
    private Long usuarioDestinoId;
    
    /**
     * Fecha de creación del like.
     */
    private LocalDateTime fechaCreacion;
    
    /**
     * Indica si se generó un match como resultado.
     */
    private boolean esMatch;
}
