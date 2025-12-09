package udistrital.avanzada.tinderstandin.multimedia.servicios;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import udistrital.avanzada.tinderstandin.multimedia.dto.CrearFotoDTO;
import udistrital.avanzada.tinderstandin.multimedia.dto.FotoResponseDTO;
import udistrital.avanzada.tinderstandin.multimedia.modelos.EntidadFoto;
import udistrital.avanzada.tinderstandin.multimedia.repositorios.RepositorioFoto;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio para gestión de fotos.
 *
 * @author AndGar03
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ServicioFotoImpl implements ServicioFoto {
    
    private final RepositorioFoto repositorioFoto;
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public FotoResponseDTO crearFoto(CrearFotoDTO crearFotoDTO) {
        log.info("Creando nueva foto para el usuario {}", crearFotoDTO.getUsuarioId());
        
        EntidadFoto foto = new EntidadFoto();
        foto.setUrl(crearFotoDTO.getUrl());
        foto.setUsuarioId(crearFotoDTO.getUsuarioId());
        
        EntidadFoto fotoGuardada = repositorioFoto.save(foto);
        log.info("Foto creada con ID: {}", fotoGuardada.getId());
        
        return convertirADTO(fotoGuardada);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<FotoResponseDTO> obtenerFotosPorUsuario(Long usuarioId) {
        log.info("Obteniendo fotos del usuario {}", usuarioId);
        List<EntidadFoto> fotos = repositorioFoto.findByUsuarioId(usuarioId);
        return fotos.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public FotoResponseDTO obtenerFotoPorId(Long id) {
        log.info("Obteniendo foto con ID: {}", id);
        EntidadFoto foto = repositorioFoto.findById(id)
                .orElseThrow(() -> new RuntimeException("Foto no encontrada con ID: " + id));
        return convertirADTO(foto);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public FotoResponseDTO actualizarFoto(Long id, String nuevaUrl) {
        log.info("Actualizando foto con ID: {}", id);
        EntidadFoto foto = repositorioFoto.findById(id)
                .orElseThrow(() -> new RuntimeException("Foto no encontrada con ID: " + id));
        
        foto.setUrl(nuevaUrl);
        EntidadFoto fotoActualizada = repositorioFoto.save(foto);
        log.info("Foto actualizada exitosamente");
        
        return convertirADTO(fotoActualizada);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void eliminarFoto(Long id) {
        log.info("Eliminando foto con ID: {}", id);
        if (!repositorioFoto.existsById(id)) {
            throw new RuntimeException("Foto no encontrada con ID: " + id);
        }
        repositorioFoto.deleteById(id);
        log.info("Foto eliminada exitosamente");
    }
    
    /**
     * Convierte una entidad de foto a su DTO de respuesta.
     *
     * @param foto Entidad a convertir
     * @return DTO con la información de la foto
     */
    private FotoResponseDTO convertirADTO(EntidadFoto foto) {
        return new FotoResponseDTO(
                foto.getId(),
                foto.getUrl(),
                foto.getUsuarioId(),
                foto.getFechaCreacion()
        );
    }
}
