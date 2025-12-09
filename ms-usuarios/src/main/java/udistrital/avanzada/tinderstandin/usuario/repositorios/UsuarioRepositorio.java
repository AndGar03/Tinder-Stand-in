package udistrital.avanzada.tinderstandin.usuario.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udistrital.avanzada.tinderstandin.usuario.modelos.EntidadUsuario;

import java.util.Optional;

/**
 * Repositorio para la entidad Usuario.
 * 
 * @author SanSantax
 */
@Repository
public interface UsuarioRepositorio extends JpaRepository<EntidadUsuario, Long> {
    
    /**
     * Busca un usuario por su nombre de usuario.
     * 
     * @param username Nombre de usuario a buscar
     * @return Un Optional que contiene el usuario si se encuentra
     */
    Optional<EntidadUsuario> findByUsername(String username);
    
    /**
     * Verifica si existe un usuario con el nombre de usuario especificado.
     * 
     * @param username Nombre de usuario a verificar
     * @return true si existe, false en caso contrario
     */
    Boolean existsByUsername(String username);
    
    /**
     * Verifica si existe un usuario con el correo electrónico especificado.
     * 
     * @param email Correo electrónico a verificar
     * @return true si existe, false en caso contrario
     */
    Boolean existsByEmail(String email);
}
