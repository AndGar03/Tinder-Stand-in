package udistrital.avanzada.tinderstandin.usuario.dto;

/**
 * DTO para mensajes de respuesta genéricos.
 * 
 * @author SanSantax
 */
public class MensajeRespuesta {
    
    private String mensaje;
    
    public MensajeRespuesta(String mensaje) {
        this.mensaje = mensaje;
    }
    
    // Getters y Setters
    public String getMensaje() {
        return mensaje;
    }
    
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
