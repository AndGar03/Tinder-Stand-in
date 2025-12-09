package udistrital.avanzada.tinderstandin.usuarios.seguridad;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import udistrital.avanzada.tinderstandin.usuarios.seguridad.UsuarioPrincipal;

import java.security.Key;
import java.util.Date;

/**
 * Clase de utilidad para la generación y validación de tokens JWT.
 * 
 * @author SanSantax
 * @author AndGar03
 */
@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms}")
    private int jwtExpirationMs;

    /**
     * Genera un token JWT para un usuario autenticado.
     *
     * @param authentication la autenticación del usuario
     * @return el token JWT generado
     */
    public String generarTokenJwt(Authentication authentication) {
        UsuarioPrincipal usuarioPrincipal = (UsuarioPrincipal) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((usuarioPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Obtiene la clave de firma para los tokens JWT.
     *
     * @return la clave de firma
     */
    private Key key() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * Obtiene el nombre de usuario a partir de un token JWT.
     *
     * @param token el token JWT
     * @return el nombre de usuario extraído del token
     */
    public String obtenerNombreUsuarioDeTokenJwt(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Valida un token JWT.
     *
     * @param authToken el token a validar
     * @return true si el token es válido, false en caso contrario
     */
    public boolean validarTokenJwt(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Token JWT inválido: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("Token JWT ha expirado: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Token JWT no soportado: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("El token JWT está vacío o es nulo: {}", e.getMessage());
        }
        return false;
    }
}
