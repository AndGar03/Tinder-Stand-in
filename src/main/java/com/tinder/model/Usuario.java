package com.tinder.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidad que representa a un usuario en el sistema Tinder.
 * Hereda de la clase Persona.
 */
@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario extends Persona {
    
    @Column(nullable = false, unique = true, length = 50)
    private String nickname;
    
    @Column(nullable = false, length = 255)
    private String password;
    
    @Column(name = "fecha_union", nullable = false, updatable = false)
    @CreationTimestamp
    @Builder.Default
    private LocalDate fechaUnion = LocalDate.now();
    
    @Column(length = 100)
    private String ciudad;
    
    @Column(columnDefinition = "TEXT")
    private String biografia;
    
    @Column(name = "foto_perfil_url", length = 255)
    private String fotoPerfilUrl;
    
    @Column(name = "activo", nullable = false)
    @Builder.Default
    private boolean activo = true;
    
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "usuario_roles",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    @Builder.Default
    private Set<Rol> roles = new HashSet<>();
    
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Foto> fotos = new HashSet<>();
    
    @OneToMany(mappedBy = "usuarioOrigen", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Match> matchesEnviados = new HashSet<>();
    
    @OneToMany(mappedBy = "usuarioDestino", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Match> matchesRecibidos = new HashSet<>();
    
    @OneToMany(mappedBy = "remitente", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Mensaje> mensajesEnviados = new HashSet<>();
    
    @OneToMany(mappedBy = "destinatario", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Mensaje> mensajesRecibidos = new HashSet<>();
    
    // Métodos de utilidad
    public void agregarRol(Rol rol) {
        this.roles.add(rol);
        rol.getUsuarios().add(this);
    }
    
    public void eliminarRol(Rol rol) {
        this.roles.remove(rol);
        rol.getUsuarios().remove(this);
    }
    
    public void agregarFoto(Foto foto) {
        this.fotos.add(foto);
        foto.setUsuario(this);
    }
    
    public void eliminarFoto(Foto foto) {
        this.fotos.remove(foto);
        foto.setUsuario(null);
    }
}
