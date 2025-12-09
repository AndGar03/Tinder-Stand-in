package udistrital.avanzada.tinderstandin.usuarios.entidades;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.HashSet;
import java.util.Set;

/**
 * Clase que representa a un usuario del sistema.
 * 
 * @author SanSantax
 * @author AndGar03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "usuarios")
@PrimaryKeyJoinColumn(name = "persona_id")
public class Usuario extends Persona {
    
    @Column(unique = true, nullable = false, length = 50)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    @Column(name = "foto_perfil")
    private String fotoPerfil;
    
    @Column(name = "biografia", length = 500)
    private String biografia;
    
    @Column(name = "activo", nullable = false)
    private boolean activo = false;
    
    @Column(name = "codigo_activacion", length = 64)
    private String codigoActivacion;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "usuario_roles",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private Set<Rol> roles = new HashSet<>();
    
    // Getters y setters personalizados si son necesarios
    
    /**
     * Añade un rol al usuario.
     * 
     * @param rol El rol a añadir
     */
    public void agregarRol(Rol rol) {
        this.roles.add(rol);
    }
    
    /**
     * Elimina un rol del usuario.
     * 
     * @param rol El rol a eliminar
     */
    public void eliminarRol(Rol rol) {
        this.roles.remove(rol);
    }
}
