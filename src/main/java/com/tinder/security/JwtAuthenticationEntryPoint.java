package com.tinder.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinder.dto.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Maneja los errores de autenticación no autorizados.
 * Se encarga de enviar una respuesta de error cuando un usuario no autenticado
 * intenta acceder a un recurso protegido.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Método invocado cuando un usuario no autenticado intenta acceder a un recurso protegido.
     *
     * @param request la solicitud HTTP
     * @param response la respuesta HTTP
     * @param authException la excepción de autenticación
     * @throws IOException si hay un error de E/S al escribir la respuesta
     * @throws ServletException si ocurre un error en el servlet
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        
        ErrorResponse errorResponse = new ErrorResponse(
            "No autorizado",
            "Se requiere autenticación para acceder a este recurso",
            HttpServletResponse.SC_UNAUTHORIZED,
            request.getRequestURI()
        );
        
        response.getOutputStream()
                .println(objectMapper.writeValueAsString(errorResponse));
    }
}
