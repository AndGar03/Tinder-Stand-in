package udistrital.avanzada.tinderstandin.social.servicios;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import udistrital.avanzada.tinderstandin.social.dto.LikeResponseDTO;
import udistrital.avanzada.tinderstandin.social.dto.MatchResponseDTO;
import udistrital.avanzada.tinderstandin.social.modelos.EntidadLike;
import udistrital.avanzada.tinderstandin.social.modelos.EntidadMatch;
import udistrital.avanzada.tinderstandin.social.repositorios.LikeRepositorio;
import udistrital.avanzada.tinderstandin.social.repositorios.MatchRepositorio;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar la lógica de likes y matches.
 * Utiliza RestTemplate para comunicarse con el microservicio de usuarios.
 *
 * @author AndGar03
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ServicioSocial {
    
    private final LikeRepositorio likeRepositorio;
    private final MatchRepositorio matchRepositorio;
    private final RestTemplate restTemplate;
    
    @Value("${ms.usuarios.url}")
    private String msUsuariosUrl;
    
    /**
     * Crea un like de un usuario hacia otro.
     * Si existe un like recíproco, crea automáticamente un match.
     *
     * @param usuarioOrigenId ID del usuario que da like
     * @param usuarioDestinoId ID del usuario que recibe el like
     * @return DTO con información del like creado y si generó match
     */
    @Transactional
    public LikeResponseDTO crearLike(Long usuarioOrigenId, Long usuarioDestinoId) {
        log.info("Usuario {} dando like a usuario {}", usuarioOrigenId, usuarioDestinoId);
        
        // Validar que no se haga like a sí mismo
        if (usuarioOrigenId.equals(usuarioDestinoId)) {
            throw new RuntimeException("No puedes dar like a ti mismo");
        }
        
        // Verificar si ya existe el like
        if (likeRepositorio.existsByUsuarioOrigenIdAndUsuarioDestinoId(usuarioOrigenId, usuarioDestinoId)) {
            throw new RuntimeException("Ya existe un like de este usuario");
        }
        
        // Crear el like
        EntidadLike like = new EntidadLike();
        like.setUsuarioOrigenId(usuarioOrigenId);
        like.setUsuarioDestinoId(usuarioDestinoId);
        EntidadLike likeGuardado = likeRepositorio.save(like);
        
        // Verificar si existe un like recíproco
        boolean existeLikeReciproco = likeRepositorio
                .existsByUsuarioOrigenIdAndUsuarioDestinoId(usuarioDestinoId, usuarioOrigenId);
        
        boolean esMatch = false;
        if (existeLikeReciproco) {
            // Verificar que no exista ya un match
            boolean existeMatch = matchRepositorio.existeMatch(usuarioOrigenId, usuarioDestinoId);
            if (!existeMatch) {
                crearMatch(usuarioOrigenId, usuarioDestinoId);
                esMatch = true;
                log.info("¡Match creado entre {} y {}!", usuarioOrigenId, usuarioDestinoId);
            }
        }
        
        return new LikeResponseDTO(
                likeGuardado.getId(),
                likeGuardado.getUsuarioOrigenId(),
                likeGuardado.getUsuarioDestinoId(),
                likeGuardado.getFechaCreacion(),
                esMatch
        );
    }
    
    /**
     * Crea un match entre dos usuarios.
     *
     * @param usuario1Id ID del primer usuario
     * @param usuario2Id ID del segundo usuario
     */
    private void crearMatch(Long usuario1Id, Long usuario2Id) {
        EntidadMatch match = new EntidadMatch();
        match.setUsuario1Id(Math.min(usuario1Id, usuario2Id));
        match.setUsuario2Id(Math.max(usuario1Id, usuario2Id));
        match.setActivo(true);
        matchRepositorio.save(match);
    }
    
    /**
     * Obtiene todos los matches de un usuario con información completa.
     * Consulta el microservicio de usuarios para obtener datos adicionales.
     *
     * @param usuarioId ID del usuario
     * @return Lista de matches con información de usuarios
     */
    @Transactional(readOnly = true)
    public List<MatchResponseDTO> obtenerMatchesDeUsuario(Long usuarioId) {
        log.info("Obteniendo matches del usuario: {}", usuarioId);
        
        List<EntidadMatch> matches = matchRepositorio.findMatchesByUsuarioId(usuarioId);
        
        return matches.stream().map(match -> {
            MatchResponseDTO dto = new MatchResponseDTO();
            dto.setId(match.getId());
            dto.setUsuario1Id(match.getUsuario1Id());
            dto.setUsuario2Id(match.getUsuario2Id());
            dto.setFechaCreacion(match.getFechaCreacion());
            dto.setHayMatch(match.isActivo());
            
            // Obtener información de usuarios desde ms-usuarios usando RestTemplate
            try {
                Map<String, Object> usuario1Info = obtenerInfoUsuario(match.getUsuario1Id());
                Map<String, Object> usuario2Info = obtenerInfoUsuario(match.getUsuario2Id());
                
                // Aquí podrías agregar los datos al DTO si los necesitas
                // dto.setUsuario1Info(usuario1Info);
                // dto.setUsuario2Info(usuario2Info);
            } catch (Exception e) {
                log.error("Error al obtener información de usuarios: {}", e.getMessage());
            }
            
            return dto;
        }).collect(Collectors.toList());
    }
    
    /**
     * Obtiene información de un usuario desde el microservicio de usuarios.
     * Utiliza RestTemplate para la comunicación entre microservicios.
     *
     * @param usuarioId ID del usuario
     * @return Mapa con la información del usuario
     */
    private Map<String, Object> obtenerInfoUsuario(Long usuarioId) {
        try {
            String url = msUsuariosUrl + "/api/auth/usuario/" + usuarioId;
            log.debug("Consultando información de usuario en: {}", url);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            return response;
        } catch (Exception e) {
            log.error("Error al consultar ms-usuarios para usuario {}: {}", usuarioId, e.getMessage());
            throw new RuntimeException("Error al obtener información del usuario");
        }
    }
    
    /**
     * Obtiene los likes recibidos por un usuario.
     *
     * @param usuarioId ID del usuario
     * @return Lista de likes recibidos
     */
    @Transactional(readOnly = true)
    public List<LikeResponseDTO> obtenerLikesRecibidos(Long usuarioId) {
        log.info("Obteniendo likes recibidos por usuario: {}", usuarioId);
        
        List<EntidadLike> likes = likeRepositorio.findByUsuarioDestinoId(usuarioId);
        
        return likes.stream().map(like -> new LikeResponseDTO(
                like.getId(),
                like.getUsuarioOrigenId(),
                like.getUsuarioDestinoId(),
                like.getFechaCreacion(),
                false
        )).collect(Collectors.toList());
    }
}
