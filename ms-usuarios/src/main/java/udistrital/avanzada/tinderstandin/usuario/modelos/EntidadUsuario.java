package udistrital.avanzada.tinderstandin.usuario.modelos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidad que representa a un usuario del sistema, hereda de EntidadPersona.
 * 
 * @author SanSantax
 */
@Getter
@Setter
@Entity
@Table(name = "usuarios", 
       uniqueConstraints = {
           @UniqueConstraint(columnNames = "username"),
           @UniqueConstraint(columnNames = "email")
       })
public class EntidadUsuario extends EntidadPersona {
    
    @Column(nullable = false, length = 50, unique = true)
    private String username;
    
    @Column(nullable = false, length = 120)
    private String password;
    
    @Column(name = "fecha_creacion")
    @CreationTimestamp
    private LocalDateTime fechaCreacion;
    
    @Column(name = "fecha_actualizacion")
    @UpdateTimestamp
    private LocalDateTime fechaActualizacion;
    
    @Column(name = "ultimo_acceso")
    private LocalDateTime ultimoAcceso;
    
    @Column(name = "cuenta_expirada")
    private boolean cuentaExpirada = false;
    
    @Column(name = "cuenta_bloqueada")
    private boolean cuentaBloqueada = false;
    
    @Column(name = "credenciales_expiradas")
    private boolean credencialesExpiradas = false;
    
    @Column(name = "habilitado")
    private boolean habilitado = true;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "usuarios_roles",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private Set<EntidadRol> roles = new HashSet<>();
    
    // Constructores
    public EntidadUsuario() {
    }
    
    public EntidadUsuario(String nombreCompleto, String email, String telefono, 
                         String username, String password) {
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.telefono = telefono;
        this.username = username;
        this.password = password;
    }
    
    // Métodos de utilidad para manejo de roles
    public void agregarRol(EntidadRol rol) {
        this.roles.add(rol);
        rol.getUsuarios().add(this);
    }
    
    public void eliminarRol(EntidadRol rol) {
        this.roles.remove(rol);
        rol.getUsuarios().remove(this);
    }
}
