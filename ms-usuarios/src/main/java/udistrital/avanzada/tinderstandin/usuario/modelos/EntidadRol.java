package udistrital.avanzada.tinderstandin.usuario.modelos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Entidad que representa un rol en el sistema.
 * 
 * @author SanSantax
 */
@Getter
@Setter
@Entity
@Table(name = "roles")
public class EntidadRol {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20, unique = true, nullable = false)
    private NombreRol nombre;
    
    @ManyToMany(mappedBy = "roles")
    private Set<EntidadUsuario> usuarios = new HashSet<>();
    
    // Enumeración para los nombres de roles predefinidos
    public enum NombreRol {
        ROLE_USUARIO,
        ROLE_MODERADOR,
        ROLE_ADMIN
    }
    
    // Constructores
    public EntidadRol() {
    }
    
    public EntidadRol(NombreRol nombre) {
        this.nombre = nombre;
    }
}
