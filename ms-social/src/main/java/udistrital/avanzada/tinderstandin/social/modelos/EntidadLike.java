package udistrital.avanzada.tinderstandin.social.modelos;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad que representa un "like" entre usuarios.
 * 
 * @author AndGar03
 */
@Entity
@Table(name = "likes")
@Getter
@Setter
public class EntidadLike {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "usuario_origen_id", nullable = false)
    private Long usuarioOrigenId;
    
    @Column(name = "usuario_destino_id", nullable = false)
    private Long usuarioDestinoId;
    
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
    }
}
