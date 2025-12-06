package com.tinder.repository;

import com.tinder.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para la entidad Rol.
 * Proporciona métodos para acceder y manipular datos de roles de usuario.
 */
@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
    
    /**
     * Busca un rol por su nombre.
     *
     * @param nombre el nombre del rol a buscar
     * @return un Optional que contiene el rol si se encuentra
     */
    Optional<Rol> findByNombre(Rol.RolNombre nombre);
    
    /**
     * Verifica si existe un rol con el nombre especificado.
     *
     * @param nombre el nombre del rol a verificar
     * @return true si existe un rol con ese nombre, false en caso contrario
     */
    boolean existsByNombre(Rol.RolNombre nombre);
}
