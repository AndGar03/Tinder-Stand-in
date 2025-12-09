package udistrital.avanzada.tinderstandin.multimedia.servicios;

import udistrital.avanzada.tinderstandin.multimedia.dto.CrearFotoDTO;
import udistrital.avanzada.tinderstandin.multimedia.dto.FotoResponseDTO;

import java.util.List;

/**
 * Interfaz del servicio para gestión de fotos.
 *
 * @author AndGar03
 */
public interface ServicioFoto {
    
    /**
     * Crea una nueva foto en el sistema.
     *
     * @param crearFotoDTO Datos de la foto a crear
     * @return DTO con la información de la foto creada
     */
    FotoResponseDTO crearFoto(CrearFotoDTO crearFotoDTO);
    
    /**
     * Obtiene todas las fotos de un usuario específico.
     *
     * @param usuarioId ID del usuario
     * @return Lista de fotos del usuario
     */
    List<FotoResponseDTO> obtenerFotosPorUsuario(Long usuarioId);
    
    /**
     * Obtiene una foto por su ID.
     *
     * @param id ID de la foto
     * @return DTO con la información de la foto
     */
    FotoResponseDTO obtenerFotoPorId(Long id);
    
    /**
     * Actualiza la URL de una foto existente.
     *
     * @param id ID de la foto a actualizar
     * @param nuevaUrl Nueva URL de la foto
     * @return DTO con la información actualizada
     */
    FotoResponseDTO actualizarFoto(Long id, String nuevaUrl);
    
    /**
     * Elimina una foto del sistema.
     *
     * @param id ID de la foto a eliminar
     */
    void eliminarFoto(Long id);
}
