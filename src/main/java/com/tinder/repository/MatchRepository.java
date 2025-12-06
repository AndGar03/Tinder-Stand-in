package com.tinder.repository;

import com.tinder.model.Match;
import com.tinder.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Match.
 */
@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    
    /**
     * Busca un match entre dos usuarios específicos.
     *
     * @param usuarioOrigenId ID del usuario origen
     * @param usuarioDestinoId ID del usuario destino
     * @return un Optional que contiene el match si existe
     */
    @Query("SELECT m FROM Match m WHERE " +
           "(m.usuarioOrigen.id = :usuarioOrigenId AND m.usuarioDestino.id = :usuarioDestinoId) OR " +
           "(m.usuarioOrigen.id = :usuarioDestinoId AND m.usuarioDestino.id = :usuarioOrigenId)")
    Optional<Match> findMatchBetweenUsers(
            @Param("usuarioOrigenId") Long usuarioOrigenId,
            @Param("usuarioDestinoId") Long usuarioDestinoId
    );
    
    /**
     * Busca todos los matches de un usuario donde haya habido match mutuo.
     *
     * @param usuarioId ID del usuario
     * @return lista de matches mutuos
     */
    @Query("SELECT m FROM Match m WHERE " +
           "(m.usuarioOrigen.id = :usuarioId OR m.usuarioDestino.id = :usuarioId) AND " +
           "EXISTS (SELECT m2 FROM Match m2 WHERE " +
           "((m2.usuarioOrigen.id = m.usuarioDestino.id AND m2.usuarioDestino.id = m.usuarioOrigen.id) OR " +
           "(m2.usuarioOrigen.id = m.usuarioOrigen.id AND m2.usuarioDestino.id = m.usuarioDestino.id)) AND " +
           "m2.id != m.id)")
    List<Match> findMutualMatchesByUsuarioId(@Param("usuarioId") Long usuarioId);
    
    /**
     * Verifica si existe un match entre dos usuarios.
     *
     * @param usuarioOrigenId ID del usuario origen
     * @param usuarioDestinoId ID del usuario destino
     * @return true si existe un match, false en caso contrario
     */
    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM Match m WHERE " +
           "(m.usuarioOrigen.id = :usuarioOrigenId AND m.usuarioDestino.id = :usuarioDestinoId) OR " +
           "(m.usuarioOrigen.id = :usuarioDestinoId AND m.usuarioDestino.id = :usuarioOrigenId)")
    boolean existsMatchBetweenUsers(
            @Param("usuarioOrigenId") Long usuarioOrigenId,
            @Param("usuarioDestinoId") Long usuarioDestinoId
    );
    
    /**
     * Busca todos los matches de un usuario (tanto enviados como recibidos).
     *
     * @param usuarioId ID del usuario
     * @return lista de matches
     */
    @Query("SELECT m FROM Match m WHERE m.usuarioOrigen.id = :usuarioId OR m.usuarioDestino.id = :usuarioId")
    List<Match> findAllByUsuarioId(@Param("usuarioId") Long usuarioId);
    
    /**
     * Busca todos los likes que un usuario ha recibido pero aún no ha respondido.
     *
     * @param usuarioId ID del usuario
     * @return lista de matches pendientes
     */
    @Query("SELECT m FROM Match m WHERE m.usuarioDestino.id = :usuarioId AND m.matchRecibido = false")
    List<Match> findPendingMatchesByUsuarioId(@Param("usuarioId") Long usuarioId);
    
    /**
     * Cuenta el número de likes que un usuario ha recibido pero aún no ha respondido.
     *
     * @param usuarioId ID del usuario
     * @return número de likes pendientes
     */
    @Query("SELECT COUNT(m) FROM Match m WHERE m.usuarioDestino.id = :usuarioId AND m.matchRecibido = false")
    long countPendingMatchesByUsuarioId(@Param("usuarioId") Long usuarioId);
}
