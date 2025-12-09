package udistrital.avanzada.tinderstandin.usuarios.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udistrital.avanzada.tinderstandin.usuarios.entidades.Usuario;

import java.util.Optional;

/**
 * Repositorio para la entidad Usuario.
 * 
 * @author SanSantax
 * @author AndGar03
 */
@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
    
    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param username el nombre de usuario a buscar
     * @return un Optional que contiene el usuario si se encuentra
     */
    Optional<Usuario> findByUsername(String username);
    
    /**
     * Verifica si existe un usuario con el nombre de usuario especificado.
     *
     * @param username el nombre de usuario a verificar
     * @return true si existe un usuario con ese nombre de usuario, false en caso contrario
     */
    boolean existsByUsername(String username);
    
    /**
     * Verifica si existe un usuario con el correo electrónico especificado.
     *
     * @param email el correo electrónico a verificar
     * @return true si existe un usuario con ese correo electrónico, false en caso contrario
     */
    boolean existsByEmail(String email);
    
    /**
     * Busca un usuario por su correo electrónico.
     *
     * @param email el correo electrónico a buscar
     * @return un Optional que contiene el usuario si se encuentra
     */
    Optional<Usuario> findByEmail(String email);
    
    /**
     * Busca un usuario por su código de activación.
     *
     * @param codigoActivacion el código de activación a buscar
     * @return un Optional que contiene el usuario si se encuentra
     */
    Optional<Usuario> findByCodigoActivacion(String codigoActivacion);
}
