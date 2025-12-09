package udistrital.avanzada.tinderstandin.usuarios.entidades;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Clase que representa un rol en el sistema.
 * 
 * @author SanSantax
 * @author AndGar03
 */
@Data
@Entity
@Table(name = "roles")
public class Rol {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 20)
    private RolNombre nombre;
    
    @Column(length = 200)
    private String descripcion;
    
    /**
     * Enumeración de los posibles nombres de roles en el sistema.
     */
    public enum RolNombre {
        ROLE_USUARIO,
        ROLE_ADMIN,
        ROLE_MODERADOR
    }
}
