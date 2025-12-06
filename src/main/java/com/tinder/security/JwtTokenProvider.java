package com.tinder.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Proporciona métodos para generar, validar y extraer información de tokens JWT.
 */
@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationInMs;

    private Key key;

    /**
     * Inicializa la clave secreta para firmar los tokens JWT.
     */
    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * Genera un token JWT para un usuario autenticado.
     *
     * @param authentication la autenticación del usuario
     * @return el token JWT generado
     */
    public String generateToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .claim("authorities", authorities)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Obtiene el nombre de usuario a partir de un token JWT.
     *
     * @param token el token JWT
     * @return el nombre de usuario
     */
    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    /**
     * Valida un token JWT.
     *
     * @param authToken el token a validar
     * @return true si el token es válido, false en caso contrario
     */
    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            log.error("Firma JWT no válida");
        } catch (MalformedJwtException ex) {
            log.error("Token JWT no válido");
        } catch (ExpiredJwtException ex) {
            log.error("Token JWT expirado");
        } catch (UnsupportedJwtException ex) {
            log.error("Token JWT no compatible");
        } catch (IllegalArgumentException ex) {
            log.error("La cadena de reclamaciones JWT está vacía");
        }
        return false;
    }

    /**
     * Obtiene el tiempo de expiración del token en milisegundos.
     *
     * @return el tiempo de expiración en milisegundos
     */
    public long getJwtExpirationInMs() {
        return jwtExpirationInMs;
    }
}
