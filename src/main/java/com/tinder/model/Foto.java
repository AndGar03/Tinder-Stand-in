package com.tinder.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidad que representa una foto de perfil de un usuario.
 */
@Entity
@Table(name = "fotos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Foto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    @Column(name = "url_imagen", nullable = false, length = 255)
    private String urlImagen;
    
    @Column(name = "es_principal", nullable = false)
    @Builder.Default
    private boolean esPrincipal = false;
    
    @Column(name = "fecha_subida", nullable = false, updatable = false)
    private LocalDateTime fechaSubida;
    
    @Column(name = "descripcion", length = 255)
    private String descripcion;
    
    @PrePersist
    protected void onCreate() {
        this.fechaSubida = LocalDateTime.now();
    }
    
    // Factory method para crear una nueva foto
    public static Foto crearFoto(Usuario usuario, String urlImagen, String descripcion, boolean esPrincipal) {
        return Foto.builder()
                .usuario(usuario)
                .urlImagen(urlImagen)
                .descripcion(descripcion)
                .esPrincipal(esPrincipal)
                .build();
    }
}
