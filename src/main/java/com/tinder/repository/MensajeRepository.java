package com.tinder.repository;

import com.tinder.model.Match;
import com.tinder.model.Mensaje;
import com.tinder.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la entidad Mensaje.
 */
@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Long> {
    
    /**
     * Busca todos los mensajes entre dos usuarios, ordenados por fecha de envío descendente.
     *
     * @param remitenteId ID del remitente
     * @param destinatarioId ID del destinatario
     * @param pageable configuración de paginación
     * @return página de mensajes
     */
    @Query("SELECT m FROM Mensaje m WHERE " +
           "(m.remitente.id = :remitenteId AND m.destinatario.id = :destinatarioId) OR " +
           "(m.remitente.id = :destinatarioId AND m.destinatario.id = :remitenteId) " +
           "ORDER BY m.fechaEnvio DESC")
    Page<Mensaje> findConversacion(
            @Param("remitenteId") Long remitenteId,
            @Param("destinatarioId") Long destinatarioId,
            Pageable pageable
    );
    
    /**
     * Busca todos los mensajes de un match específico, ordenados por fecha de envío ascendente.
     *
     * @param matchId ID del match
     * @return lista de mensajes del match
     */
    @Query("SELECT m FROM Mensaje m WHERE m.match.id = :matchId ORDER BY m.fechaEnvio ASC")
    List<Mensaje> findByMatchId(@Param("matchId") Long matchId);
    
    /**
     * Busca el último mensaje de cada conversación de un usuario.
     *
     * @param usuarioId ID del usuario
     * @return lista de los últimos mensajes de cada conversación
     */
    @Query("SELECT m FROM Mensaje m WHERE m.id IN (" +
           "SELECT MAX(m2.id) FROM Mensaje m2 WHERE " +
           "m2.remitente.id = :usuarioId OR m2.destinatario.id = :usuarioId " +
           "GROUP BY " +
           "CASE " +
           "WHEN m2.remitente.id = :usuarioId THEN m2.destinatario.id " +
           "ELSE m2.remitente.id " +
           "END" +
           ")")
    List<Mensaje> findUltimosMensajesPorConversacion(@Param("usuarioId") Long usuarioId);
    
    /**
     * Cuenta el número de mensajes no leídos que ha recibido un usuario.
     *
     * @param usuarioId ID del usuario
     * @return número de mensajes no leídos
     */
    @Query("SELECT COUNT(m) FROM Mensaje m WHERE m.destinatario.id = :usuarioId AND m.leido = false")
    long countMensajesNoLeidos(@Param("usuarioId") Long usuarioId);
    
    /**
     * Marca como leídos todos los mensajes de una conversación.
     *
     * @param remitenteId ID del remitente
     * @param destinatarioId ID del destinatario
     * @return número de mensajes actualizados
     */
    @Query("UPDATE Mensaje m SET m.leido = true WHERE m.remitente.id = :remitenteId AND m.destinatario.id = :destinatarioId AND m.leido = false")
    int marcarComoLeidos(
            @Param("remitenteId") Long remitenteId,
            @Param("destinatarioId") Long destinatarioId
    );
    
    /**
     * Busca todos los mensajes no leídos que un usuario ha recibido de otro usuario específico.
     *
     * @param remitenteId ID del remitente
     * @param destinatarioId ID del destinatario
     * @return lista de mensajes no leídos
     */
    @Query("SELECT m FROM Mensaje m WHERE m.remitente.id = :remitenteId AND m.destinatario.id = :destinatarioId AND m.leido = false")
    List<Mensaje> findMensajesNoLeidos(
            @Param("remitenteId") Long remitenteId,
            @Param("destinatarioId") Long destinatarioId
    );
}
