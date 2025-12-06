package com.tinder.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidad que representa un match entre dos usuarios en el sistema.
 */
@Entity
@Table(name = "matches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Match {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_origen_id", nullable = false)
    private Usuario usuarioOrigen;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_destino_id", nullable = false)
    private Usuario usuarioDestino;
    
    @Column(name = "fecha_match", nullable = false)
    private LocalDateTime fechaMatch;
    
    @Column(name = "es_super_like")
    private boolean esSuperLike;
    
    @Column(name = "match_recibido")
    private boolean matchRecibido;
    
    @PrePersist
    protected void onCreate() {
        this.fechaMatch = LocalDateTime.now();
    }
    
    // Constructor para crear un nuevo match
    public static Match crearMatch(Usuario origen, Usuario destino, boolean esSuperLike) {
        return Match.builder()
                .usuarioOrigen(origen)
                .usuarioDestino(destino)
                .esSuperLike(esSuperLike)
                .matchRecibido(false)
                .build();
    }
}
