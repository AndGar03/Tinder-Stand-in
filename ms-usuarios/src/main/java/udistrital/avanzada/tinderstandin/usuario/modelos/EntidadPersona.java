package udistrital.avanzada.tinderstandin.usuario.modelos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Clase base que representa a una persona en el sistema.
 * 
 * @author SanSantax
 */
@Getter
@Setter
@MappedSuperclass
public abstract class EntidadPersona {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    
    @Column(nullable = false, length = 100)
    protected String nombreCompleto;
    
    @Column(unique = true, nullable = false, length = 100)
    protected String email;
    
    @Column(length = 20)
    protected String telefono;
    
    @Column(length = 200)
    protected String descripcion;
    
    @Column(length = 1)
    protected String genero;
    
    @Column(length = 100)
    protected String ciudad;
    
    @Column(name = "fecha_nacimiento")
    protected String fechaNacimiento;
    
    @Column(name = "foto_perfil")
    protected String fotoPerfil;
}
