package com.tinder.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Clase que representa una respuesta de error estandarizada para la API.
 * Incluye información sobre el error, como el mensaje, la ruta, la marca de tiempo y el código de estado HTTP.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    
    /**
     * Marca de tiempo del error.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    
    /**
     * Código de estado HTTP.
     */
    private int status;
    
    /**
     * Tipo de error.
     */
    private String error;
    
    /**
     * Mensaje descriptivo del error.
     */
    private String message;
    
    /**
     * Ruta donde ocurrió el error.
     */
    private String path;
    
    /**
     * Constructor para crear una respuesta de error.
     *
     * @param error     el tipo de error
     * @param message   el mensaje descriptivo
     * @param status    el código de estado HTTP
     * @param path      la ruta donde ocurrió el error
     */
    public ErrorResponse(String error, String message, int status, String path) {
        this.timestamp = LocalDateTime.now();
        this.error = error;
        this.message = message;
        this.status = status;
        this.path = path;
    }
    
    /**
     * Constructor para crear una respuesta de error con un mensaje simple.
     *
     * @param message el mensaje descriptivo del error
     */
    public ErrorResponse(String message) {
        this.timestamp = LocalDateTime.now();
        this.message = message;
    }
    
    /**
     * Crea una respuesta de error para un recurso no encontrado.
     *
     * @param message el mensaje descriptivo
     * @param path    la ruta donde ocurrió el error
     * @return una instancia de ErrorResponse
     */
    public static ErrorResponse notFound(String message, String path) {
        return new ErrorResponse("Recurso no encontrado", message, 404, path);
    }
    
    /**
     * Crea una respuesta de error para una solicitud incorrecta.
     *
     * @param message el mensaje descriptivo
     * @param path    la ruta donde ocurrió el error
     * @return una instancia de ErrorResponse
     */
    public static ErrorResponse badRequest(String message, String path) {
        return new ErrorResponse("Solicitud incorrecta", message, 400, path);
    }
    
    /**
     * Crea una respuesta de error para un error interno del servidor.
     *
     * @param message el mensaje descriptivo
     * @param path    la ruta donde ocurrió el error
     * @return una instancia de ErrorResponse
     */
    public static ErrorResponse internalServerError(String message, String path) {
        return new ErrorResponse("Error interno del servidor", message, 500, path);
    }
    
    /**
     * Crea una respuesta de error para un acceso no autorizado.
     *
     * @param message el mensaje descriptivo
     * @param path    la ruta donde ocurrió el error
     * @return una instancia de ErrorResponse
     */
    public static ErrorResponse unauthorized(String message, String path) {
        return new ErrorResponse("No autorizado", message, 401, path);
    }
    
    /**
     * Crea una respuesta de error para un acceso prohibido.
     *
     * @param message el mensaje descriptivo
     * @param path    la ruta donde ocurrió el error
     * @return una instancia de ErrorResponse
     */
    public static ErrorResponse forbidden(String message, String path) {
        return new ErrorResponse("Acceso denegado", message, 403, path);
    }
}
