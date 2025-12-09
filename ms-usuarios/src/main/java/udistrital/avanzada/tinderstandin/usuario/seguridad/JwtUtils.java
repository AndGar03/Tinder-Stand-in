package udistrital.avanzada.tinderstandin.usuario.seguridad;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import udistrital.avanzada.tinderstandin.usuario.seguridad.UserDetailsImpl;

import java.security.Key;
import java.util.Date;

/**
 * Utilidades para el manejo de JWT (JSON Web Tokens).
 * 
 * @author SanSantax
 */
@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpirationMs;

    /**
     * Genera un token JWT a partir de la autenticación del usuario.
     * 
     * @param authentication Objeto de autenticación de Spring Security
     * @return Token JWT como String
     */
    public String generarTokenJWT(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Obtiene la clave de firma para los tokens JWT.
     * 
     * @return Clave de firma
     */
    private Key key() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * Obtiene el nombre de usuario a partir de un token JWT.
     * 
     * @param token Token JWT
     * @return Nombre de usuario
     */
    public String obtenerUsernameDeTokenJWT(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Valida un token JWT.
     * 
     * @param authToken Token a validar
     * @return true si el token es válido, false en caso contrario
     */
    public boolean validarTokenJWT(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Token JWT inválido: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("Token JWT ha expirado: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Token JWT no soportado: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("El token JWT está vacío: {}", e.getMessage());
        }

        return false;
    }
}
