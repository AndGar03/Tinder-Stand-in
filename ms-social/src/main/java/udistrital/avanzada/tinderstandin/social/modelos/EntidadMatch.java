package udistrital.avanzada.tinderstandin.social.modelos;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad que representa un "match" entre usuarios.
 * 
 * @author AndGar03
 */
@Entity
@Table(name = "matches")
@Getter
@Setter
public class EntidadMatch {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "usuario1_id", nullable = false)
    private Long usuario1Id;
    
    @Column(name = "usuario2_id", nullable = false)
    private Long usuario2Id;
    
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @Column(name = "activo", nullable = false)
    private boolean activo = true;
    
    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
    }
}
