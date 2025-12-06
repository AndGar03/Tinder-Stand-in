package com.tinder.repository;

import com.tinder.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Usuario.
 * Proporciona métodos para acceder y manipular datos de usuarios.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    /**
     * Busca un usuario por su nickname (nombre de usuario) ignorando mayúsculas/minúsculas.
     *
     * @param nickname el nombre de usuario a buscar (case-insensitive)
     * @return un Optional que contiene el usuario si se encuentra
     */
    Optional<Usuario> findByNicknameIgnoreCase(String nickname);
    
    /**
     * Busca un usuario por su nickname (nombre de usuario).
     *
     * @param nickname el nombre de usuario a buscar
     * @return un Optional que contiene el usuario si se encuentra
     */
    Optional<Usuario> findByNickname(String nickname);
    
    /**
     * Verifica si existe un usuario con el nickname proporcionado.
     *
     * @param nickname el nombre de usuario a verificar
     * @return true si existe un usuario con ese nickname, false en caso contrario
     */
    boolean existsByNickname(String nickname);
    
    /**
     * Verifica si existe un usuario con el email proporcionado ignorando mayúsculas/minúsculas.
     *
     * @param email el email a verificar (case-insensitive)
     * @return true si existe un usuario con ese email, false en caso contrario
     */
    boolean existsByEmailIgnoreCase(String email);
    
    /**
     * Verifica si existe un usuario con el email proporcionado.
     *
     * @param email el email a verificar
     * @return true si existe un usuario con ese email, false en caso contrario
     */
    boolean existsByEmail(String email);
    
    /**
     * Busca usuarios que coincidan con los criterios de búsqueda.
     *
     * @param ciudad la ciudad para filtrar (opcional)
     * @param edadMin la edad mínima (opcional)
     * @param edadMax la edad máxima (opcional)
     * @return una lista de usuarios que coinciden con los criterios
     */
    @Query("SELECT u FROM Usuario u WHERE " +
           "(:ciudad IS NULL OR u.ciudad = :ciudad) AND " +
           "(u.fechaNacimiento IS NOT NULL AND " +
           "(:edadMin IS NULL OR FUNCTION('YEAR', CURRENT_DATE) - FUNCTION('YEAR', u.fechaNacimiento) >= :edadMin) AND " +
           "(:edadMax IS NULL OR FUNCTION('YEAR', CURRENT_DATE) - FUNCTION('YEAR', u.fechaNacimiento) <= :edadMax))")
    List<Usuario> buscarUsuariosPorFiltros(
            @Param("ciudad") String ciudad,
            @Param("edadMin") Integer edadMin,
            @Param("edadMax") Integer edadMax
    );
    
    /**
     * Busca usuarios que coincidan con un término de búsqueda en nombre o nickname.
     *
     * @param query el término de búsqueda
     * @return una lista de usuarios que coinciden con el término de búsqueda
     */
    @Query("SELECT u FROM Usuario u WHERE " +
           "LOWER(u.nombreCompleto) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(u.nickname) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Usuario> buscarPorNombreOUsername(@Param("query") String query);
    
    /**
     * Busca usuarios que aún no han sido evaluados (like/dislike) por el usuario actual.
     *
     * @param usuarioId el ID del usuario actual
     * @return una lista de usuarios que aún no han sido evaluados
     */
    @Query("SELECT u FROM Usuario u WHERE u.id != :usuarioId AND u.activo = true AND u.id NOT IN " +
           "(SELECT m.usuarioDestino.id FROM Match m WHERE m.usuarioOrigen.id = :usuarioId) AND " +
           "u.id NOT IN (SELECT b.usuarioBloqueado.id FROM Bloqueo b WHERE b.usuarioBloqueador.id = :usuarioId)")
    List<Usuario> buscarUsuariosNoEvaluados(@Param("usuarioId") Long usuarioId);
    
    /**
     * Busca usuarios por ciudad (ignorando mayúsculas/minúsculas).
     *
     * @param ciudad la ciudad para filtrar
     * @return una lista de usuarios que viven en la ciudad especificada
     */
    List<Usuario> findByCiudadIgnoreCase(String ciudad);
    
    /**
     * Busca usuarios por rango de fechas de nacimiento.
     *
     * @param fechaInicio la fecha de nacimiento mínima (más reciente)
     * @param fechaFin la fecha de nacimiento máxima (más antigua)
     * @return una lista de usuarios dentro del rango de fechas de nacimiento
     */
    List<Usuario> findByFechaNacimientoBetween(LocalDate fechaInicio, LocalDate fechaFin);
    
    /**
     * Busca usuarios con paginación.
     *
     * @param pageable la configuración de paginación
     * @return una página de usuarios
     */
    Page<Usuario> findAll(Pageable pageable);
    
    /**
     * Busca usuarios activos con paginación.
     *
     * @param activo si es true, devuelve solo usuarios activos; si es false, solo inactivos
     * @param pageable la configuración de paginación
     * @return una página de usuarios activos
     */
    Page<Usuario> findByActivo(boolean activo, Pageable pageable);
    
    /**
     * Actualiza la última fecha de inicio de sesión del usuario.
     *
     * @param usuarioId el ID del usuario
     * @param fechaUltimoLogin la nueva fecha de último inicio de sesión
     */
    @Modifying
    @Query("UPDATE Usuario u SET u.fechaUltimoLogin = :fechaUltimoLogin WHERE u.id = :usuarioId")
    void actualizarUltimoLogin(@Param("usuarioId") Long usuarioId, @Param("fechaUltimoLogin") LocalDate fechaUltimoLogin);
    
    /**
     * Busca usuarios por rol.
     *
     * @param rol el nombre del rol
     * @return una lista de usuarios con el rol especificado
     */
    @Query("SELECT u FROM Usuario u JOIN u.roles r WHERE r.nombre = :rol")
    List<Usuario> findByRol(@Param("rol") String rol);
}
