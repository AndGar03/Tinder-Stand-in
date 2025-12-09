package udistrital.avanzada.tinderstandin.usuarios.entidades;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

/**
 * Clase base que representa a una persona en el sistema.
 * 
 * @author SanSantax
 * @author AndGar03
 */
@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "personas")
public abstract class Persona {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Column(nullable = false, length = 100)
    private String apellido;
    
    @Column(unique = true, nullable = false, length = 100)
    private String email;
    
    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;
    
    @Column(length = 20)
    private String telefono;
    
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDate fechaCreacion = LocalDate.now();
    
    @Column(name = "fecha_actualizacion")
    private LocalDate fechaActualizacion;
    
    @PreUpdate
    protected void onUpdate() {
        this.fechaActualizacion = LocalDate.now();
    }
}
