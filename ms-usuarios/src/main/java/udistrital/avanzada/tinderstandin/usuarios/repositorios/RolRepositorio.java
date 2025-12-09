package udistrital.avanzada.tinderstandin.usuarios.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udistrital.avanzada.tinderstandin.usuarios.entidades.Rol;
import udistrital.avanzada.tinderstandin.usuarios.entidades.Rol.RolNombre;

import java.util.Optional;

/**
 * Repositorio para la entidad Rol.
 * 
 * @author SanSantax
 * @author AndGar03
 */
@Repository
public interface RolRepositorio extends JpaRepository<Rol, Long> {
    
    /**
     * Busca un rol por su nombre.
     *
     * @param rolNombre el nombre del rol a buscar
     * @return un Optional que contiene el rol si se encuentra
     */
    Optional<Rol> findByNombre(RolNombre rolNombre);
}
