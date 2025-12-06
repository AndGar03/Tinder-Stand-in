package com.tinder.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO para respuestas de mensajes simples.
 * Se utiliza para enviar mensajes de éxito o error al cliente.
 */
@Data
@AllArgsConstructor
public class MessageResponse {
    
    /**
     * El mensaje de respuesta.
     */
    private String message;
    
    /**
     * Crea una nueva respuesta de mensaje.
     *
     * @param message el mensaje a enviar
     * @return una nueva instancia de MessageResponse
     */
    public static MessageResponse of(String message) {
        return new MessageResponse(message);
    }
}
