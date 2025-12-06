package com.tinder.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * Clase base abstracta que representa a una persona en el sistema.
 * Esta clase será heredada por Usuario y en el futuro por Administrador.
 */
@MappedSuperclass
@Getter
@Setter
public abstract class Persona {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    
    @Column(nullable = false, length = 100)
    protected String nombreCompleto;
    
    @Column(nullable = false, unique = true, length = 50)
    protected String email;
    
    @Column(nullable = false, length = 20)
    protected String telefono;
    
    @Column(name = "fecha_nacimiento")
    protected LocalDateTime fechaNacimiento;
    
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    protected LocalDateTime fechaCreacion = LocalDateTime.now();
    
    @Column(name = "fecha_actualizacion")
    protected LocalDateTime fechaActualizacion;
    
    @PreUpdate
    protected void onUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }
}
