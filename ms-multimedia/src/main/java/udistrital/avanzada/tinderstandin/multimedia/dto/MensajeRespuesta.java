package udistrital.avanzada.tinderstandin.multimedia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para respuestas con mensajes simples.
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
