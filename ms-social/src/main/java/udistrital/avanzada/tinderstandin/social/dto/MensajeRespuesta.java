package udistrital.avanzada.tinderstandin.social.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para mensajes de respuesta simples.
 *
 * @author AndGar03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MensajeRespuesta {
    
    /**
     * Mensaje de respuesta.
     */
    private String mensaje;
}
