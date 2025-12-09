package udistrital.avanzada.tinderstandin.social.controladores;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import udistrital.avanzada.tinderstandin.social.dto.CrearLikeDTO;
import udistrital.avanzada.tinderstandin.social.dto.LikeResponseDTO;
import udistrital.avanzada.tinderstandin.social.dto.MatchResponseDTO;
import udistrital.avanzada.tinderstandin.social.dto.MensajeRespuesta;
import udistrital.avanzada.tinderstandin.social.servicios.ServicioSocial;

import java.util.List;

/**
 * Controlador REST para operaciones sociales: likes y matches.
 * Comunica el frontend con la lógica de ServicioSocial.
 *
 *  @author AndGar03
 */
@RestController
@RequestMapping("/api/social")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
@Slf4j
public class ControladorSocial {

    private final ServicioSocial servicioSocial;

    /**
     * Crea un nuevo like desde un usuario origen hacia un usuario destino.
     * El ID de origen se recibe como parámetro y el de destino en el cuerpo.
     */
    @PostMapping("/likes")
    public ResponseEntity<?> crearLike(
            @RequestParam("usuarioOrigenId") Long usuarioOrigenId,
            @Valid @RequestBody CrearLikeDTO crearLikeDTO) {
        log.info("Usuario {} enviando like a {}", usuarioOrigenId, crearLikeDTO.getUsuarioDestinoId());
        try {
            LikeResponseDTO response = servicioSocial.crearLike(usuarioOrigenId, crearLikeDTO.getUsuarioDestinoId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            log.error("Error al crear like: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new MensajeRespuesta(e.getMessage()));
        } catch (Exception e) {
            log.error("Error inesperado al crear like: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MensajeRespuesta("Error interno al crear el like"));
        }
    }

    /**
     * Obtiene todos los matches de un usuario.
     */
    @GetMapping("/matches/{usuarioId}")
    public ResponseEntity<List<MatchResponseDTO>> obtenerMatches(@PathVariable Long usuarioId) {
        log.info("Obteniendo matches para usuario {}", usuarioId);
        List<MatchResponseDTO> matches = servicioSocial.obtenerMatchesDeUsuario(usuarioId);
        return ResponseEntity.ok(matches);
    }

    /**
     * Obtiene todos los likes recibidos por un usuario.
     */
    @GetMapping("/likes/recibidos/{usuarioId}")
    public ResponseEntity<List<LikeResponseDTO>> obtenerLikesRecibidos(@PathVariable Long usuarioId) {
        log.info("Obteniendo likes recibidos para usuario {}", usuarioId);
        List<LikeResponseDTO> likes = servicioSocial.obtenerLikesRecibidos(usuarioId);
        return ResponseEntity.ok(likes);
    }
}
