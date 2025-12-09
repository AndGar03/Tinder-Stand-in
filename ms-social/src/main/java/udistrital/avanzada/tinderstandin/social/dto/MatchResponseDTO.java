package udistrital.avanzada.tinderstandin.social.dto;

import java.time.LocalDateTime;

/**
 * DTO para la respuesta de un match.
 * 
 * @author AndGar03
 */
public class MatchResponseDTO {
    
    private Long id;
    private Long usuario1Id;
    private Long usuario2Id;
    private LocalDateTime fechaCreacion;
    private boolean hayMatch;
    
    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuario1Id() {
        return usuario1Id;
    }

    public void setUsuario1Id(Long usuario1Id) {
        this.usuario1Id = usuario1Id;
    }

    public Long getUsuario2Id() {
        return usuario2Id;
    }

    public void setUsuario2Id(Long usuario2Id) {
        this.usuario2Id = usuario2Id;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public boolean isHayMatch() {
        return hayMatch;
    }

    public void setHayMatch(boolean hayMatch) {
        this.hayMatch = hayMatch;
    }
}
