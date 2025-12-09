package udistrital.avanzada.tinderstandin.social.dto;

import jakarta.validation.constraints.NotNull;

/**
 * DTO para la creación de un like.
 * 
 * @author AndGar03
 */
public class CrearLikeDTO {
    
    @NotNull(message = "El ID del usuario destino es obligatorio")
    private Long usuarioDestinoId;

    // Getters y Setters
    public Long getUsuarioDestinoId() {
        return usuarioDestinoId;
    }

    public void setUsuarioDestinoId(Long usuarioDestinoId) {
        this.usuarioDestinoId = usuarioDestinoId;
    }
}
