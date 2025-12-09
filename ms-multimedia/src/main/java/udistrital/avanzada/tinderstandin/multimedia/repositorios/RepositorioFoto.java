package udistrital.avanzada.tinderstandin.multimedia.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udistrital.avanzada.tinderstandin.multimedia.modelos.EntidadFoto;

import java.util.List;

/**
 * Repositorio para la entidad Foto.
 * 
 * @author AndGar03
 */
@Repository
public interface RepositorioFoto extends JpaRepository<EntidadFoto, Long> {
    
    /**
     * Busca todas las fotos de un usuario específico.
     * 
     * @param usuarioId ID del usuario
     * @return Lista de fotos del usuario
     */
    List<EntidadFoto> findByUsuarioId(Long usuarioId);
    
    /**
     * Verifica si un usuario tiene alguna foto.
     * 
     * @param usuarioId ID del usuario
     * @return true si el usuario tiene al menos una foto, false en caso contrario
     */
    boolean existsByUsuarioId(Long usuarioId);
    
    /**
     * Elimina todas las fotos de un usuario.
     * 
     * @param usuarioId ID del usuario
     * @return Número de fotos eliminadas
     */
    long deleteByUsuarioId(Long usuarioId);
}
