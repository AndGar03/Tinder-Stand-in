package com.tinder.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidad que representa un mensaje entre usuarios que han hecho match.
 */
@Entity
@Table(name = "mensajes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mensaje {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "remitente_id", nullable = false)
    private Usuario remitente;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destinatario_id", nullable = false)
    private Usuario destinatario;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String contenido;
    
    @Column(name = "fecha_envio", nullable = false)
    private LocalDateTime fechaEnvio;
    
    @Column(name = "leido", nullable = false)
    @Builder.Default
    private boolean leido = false;
    
    @PrePersist
    protected void onCreate() {
        this.fechaEnvio = LocalDateTime.now();
    }
    
    // Factory method para crear un nuevo mensaje
    public static Mensaje crearMensaje(Usuario remitente, Usuario destinatario, Match match, String contenido) {
        return Mensaje.builder()
                .remitente(remitente)
                .destinatario(destinatario)
                .match(match)
                .contenido(contenido)
                .build();
    }
}
