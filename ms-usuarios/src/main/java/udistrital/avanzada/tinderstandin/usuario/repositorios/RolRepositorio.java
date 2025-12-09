package udistrital.avanzada.tinderstandin.usuario.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udistrital.avanzada.tinderstandin.usuario.modelos.EntidadRol;

import java.util.Optional;

/**
 * Repositorio para la entidad Rol.
 * 
 * @author SanSantax
 */
@Repository
public interface RolRepositorio extends JpaRepository<EntidadRol, Long> {
    
    /**
     * Busca un rol por su nombre.
     * 
     * @param nombre Nombre del rol a buscar
     * @return Un Optional que contiene el rol si se encuentra
     */
    Optional<EntidadRol> findByNombre(EntidadRol.NombreRol nombre);
}
