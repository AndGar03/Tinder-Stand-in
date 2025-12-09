package udistrital.avanzada.tinderstandin.social.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udistrital.avanzada.tinderstandin.social.modelos.EntidadLike;

import java.util.Optional;

/**
 * Repositorio para la entidad Like.
 * 
 * @author AndGar03
 */
@Repository
public interface LikeRepositorio extends JpaRepository<EntidadLike, Long> {
    
    /**
     * Verifica si existe un like entre dos usuarios.
     * 
     * @param usuarioOrigenId ID del usuario que dio like
     * @param usuarioDestinoId ID del usuario que recibió like
     * @return true si existe el like, false en caso contrario
     */
    boolean existsByUsuarioOrigenIdAndUsuarioDestinoId(Long usuarioOrigenId, Long usuarioDestinoId);
    
    /**
     * Busca un like entre dos usuarios.
     * 
     * @param usuarioOrigenId ID del usuario que dio like
     * @param usuarioDestinoId ID del usuario que recibió like
     * @return Optional con el like si existe
     */
    Optional<EntidadLike> findByUsuarioOrigenIdAndUsuarioDestinoId(Long usuarioOrigenId, Long usuarioDestinoId);
    
    /**
     * Obtiene todos los likes recibidos por un usuario.
     *
     * @param usuarioDestinoId ID del usuario que recibe likes
     * @return Lista de likes recibidos
     */
    java.util.List<EntidadLike> findByUsuarioDestinoId(Long usuarioDestinoId);
}
