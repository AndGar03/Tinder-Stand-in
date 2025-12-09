package udistrital.avanzada.tinderstandin.social.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import udistrital.avanzada.tinderstandin.social.modelos.EntidadMatch;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Match.
 * 
 * @author AndGar03
 */
@Repository
public interface MatchRepositorio extends JpaRepository<EntidadMatch, Long> {
    
    /**
     * Verifica si existe un match entre dos usuarios.
     * 
     * @param usuario1Id ID del primer usuario
     * @param usuario2Id ID del segundo usuario
     * @return true si existe el match, false en caso contrario
     */
    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END " +
           "FROM EntidadMatch m WHERE " +
           "(m.usuario1Id = :usuario1Id AND m.usuario2Id = :usuario2Id) OR " +
           "(m.usuario1Id = :usuario2Id AND m.usuario2Id = :usuario1Id)")
    boolean existeMatchEntreUsuarios(
        @Param("usuario1Id") Long usuario1Id, 
        @Param("usuario2Id") Long usuario2Id
    );
    
    /**
     * Busca un match entre dos usuarios.
     * 
     * @param usuario1Id ID del primer usuario
     * @param usuario2Id ID del segundo usuario
     * @return Optional con el match si existe
     */
    @Query("SELECT m FROM EntidadMatch m WHERE " +
           "(m.usuario1Id = :usuario1Id AND m.usuario2Id = :usuario2Id) OR " +
           "(m.usuario1Id = :usuario2Id AND m.usuario2Id = :usuario1Id)")
    Optional<EntidadMatch> buscarMatchEntreUsuarios(
        @Param("usuario1Id") Long usuario1Id, 
        @Param("usuario2Id") Long usuario2Id
    );
    
    /**
     * Obtiene todos los matches de un usuario.
     * 
     * @param usuarioId ID del usuario
     * @return Lista de matches
     */
    @Query("SELECT m FROM EntidadMatch m WHERE " +
           "m.usuario1Id = :usuarioId OR m.usuario2Id = :usuarioId")
    List<EntidadMatch> buscarMatchesPorUsuarioId(@Param("usuarioId") Long usuarioId);
    
    /**
     * Alias para buscar matches de un usuario (usado por ServicioSocial).
     * 
     * @param usuarioId ID del usuario
     * @return Lista de matches
     */
    default List<EntidadMatch> findMatchesByUsuarioId(Long usuarioId) {
        return buscarMatchesPorUsuarioId(usuarioId);
    }
    
    /**
     * Alias para verificar si existe match (usado por ServicioSocial).
     * 
     * @param usuario1Id ID del primer usuario
     * @param usuario2Id ID del segundo usuario
     * @return true si existe match
     */
    default boolean existeMatch(Long usuario1Id, Long usuario2Id) {
        return existeMatchEntreUsuarios(usuario1Id, usuario2Id);
    }
}
