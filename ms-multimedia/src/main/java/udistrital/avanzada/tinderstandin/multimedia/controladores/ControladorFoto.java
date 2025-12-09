package udistrital.avanzada.tinderstandin.multimedia.controladores;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import udistrital.avanzada.tinderstandin.multimedia.dto.CrearFotoDTO;
import udistrital.avanzada.tinderstandin.multimedia.dto.FotoResponseDTO;
import udistrital.avanzada.tinderstandin.multimedia.dto.MensajeRespuesta;
import udistrital.avanzada.tinderstandin.multimedia.servicios.ServicioFoto;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para operaciones CRUD de fotos.
 * Gestiona todas las peticiones relacionadas con fotos de perfil.
 *
 * @author AndGar03
 */
@RestController
@RequestMapping("/api/fotos")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
@Slf4j
public class ControladorFoto {
    
    private final ServicioFoto servicioFoto;
    
    /**
     * Crea una nueva foto en el sistema.
     *
     * @param crearFotoDTO Datos de la foto a crear
     * @return ResponseEntity con la foto creada
     */
    @PostMapping
    public ResponseEntity<FotoResponseDTO> crearFoto(@Valid @RequestBody CrearFotoDTO crearFotoDTO) {
        log.info("Solicitud de creación de foto para usuario: {}", crearFotoDTO.getUsuarioId());
        try {
            FotoResponseDTO fotoCreada = servicioFoto.crearFoto(crearFotoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(fotoCreada);
        } catch (Exception e) {
            log.error("Error al crear foto: {}", e.getMessage());
            throw new RuntimeException("Error al crear la foto: " + e.getMessage());
        }
    }
    
    /**
     * Obtiene todas las fotos de un usuario específico.
     *
     * @param usuarioId ID del usuario
     * @return ResponseEntity con la lista de fotos del usuario
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<FotoResponseDTO>> obtenerFotosPorUsuario(@PathVariable Long usuarioId) {
        log.info("Solicitud de obtención de fotos del usuario: {}", usuarioId);
        try {
            List<FotoResponseDTO> fotos = servicioFoto.obtenerFotosPorUsuario(usuarioId);
            return ResponseEntity.ok(fotos);
        } catch (Exception e) {
            log.error("Error al obtener fotos del usuario {}: {}", usuarioId, e.getMessage());
            throw new RuntimeException("Error al obtener las fotos: " + e.getMessage());
        }
    }
    
    /**
     * Obtiene una foto específica por su ID.
     *
     * @param id ID de la foto
     * @return ResponseEntity con la foto solicitada
     */
    @GetMapping("/{id}")
    public ResponseEntity<FotoResponseDTO> obtenerFotoPorId(@PathVariable Long id) {
        log.info("Solicitud de obtención de foto con ID: {}", id);
        try {
            FotoResponseDTO foto = servicioFoto.obtenerFotoPorId(id);
            return ResponseEntity.ok(foto);
        } catch (RuntimeException e) {
            log.error("Error al obtener foto {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    /**
     * Actualiza la URL de una foto existente.
     *
     * @param id ID de la foto a actualizar
     * @param requestBody Mapa con la nueva URL
     * @return ResponseEntity con la foto actualizada
     */
    @PutMapping("/{id}")
    public ResponseEntity<FotoResponseDTO> actualizarFoto(
            @PathVariable Long id,
            @RequestBody Map<String, String> requestBody) {
        log.info("Solicitud de actualización de foto con ID: {}", id);
        try {
            String nuevaUrl = requestBody.get("url");
            if (nuevaUrl == null || nuevaUrl.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            FotoResponseDTO fotoActualizada = servicioFoto.actualizarFoto(id, nuevaUrl);
            return ResponseEntity.ok(fotoActualizada);
        } catch (RuntimeException e) {
            log.error("Error al actualizar foto {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    /**
     * Elimina una foto del sistema.
     *
     * @param id ID de la foto a eliminar
     * @return ResponseEntity con mensaje de confirmación
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<MensajeRespuesta> eliminarFoto(@PathVariable Long id) {
        log.info("Solicitud de eliminación de foto con ID: {}", id);
        try {
            servicioFoto.eliminarFoto(id);
            return ResponseEntity.ok(new MensajeRespuesta("Foto eliminada exitosamente"));
        } catch (RuntimeException e) {
            log.error("Error al eliminar foto {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MensajeRespuesta("Foto no encontrada"));
        }
    }
}
