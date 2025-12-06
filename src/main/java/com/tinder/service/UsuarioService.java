package com.tinder.service;

import com.tinder.model.Usuario;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz del servicio para la gestión de usuarios.
 * Define las operaciones disponibles para el manejo de usuarios en el sistema.
 */
public interface UsuarioService {

    /**
     * Guarda un nuevo usuario en el sistema.
     *
     * @param usuario el usuario a guardar
     * @return el usuario guardado
     */
    Usuario guardarUsuario(Usuario usuario);

    /**
     * Busca un usuario por su ID.
     *
     * @param id el ID del usuario a buscar
     * @return un Optional que contiene el usuario si se encuentra, o vacío si no
     */
    Optional<Usuario> buscarPorId(Long id);

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param username el nombre de usuario a buscar
     * @return un Optional que contiene el usuario si se encuentra, o vacío si no
     */
    Optional<Usuario> buscarPorUsername(String username);

    /**
     * Busca un usuario por su correo electrónico.
     *
     * @param email el correo electrónico a buscar
     * @return un Optional que contiene el usuario si se encuentra, o vacío si no
     */
    Optional<Usuario> buscarPorEmail(String email);

    /**
     * Verifica si existe un usuario con el nombre de usuario dado.
     *
     * @param username el nombre de usuario a verificar
     * @return true si existe un usuario con ese nombre de usuario, false en caso contrario
     */
    boolean existePorUsername(String username);

    /**
     * Verifica si existe un usuario con el correo electrónico dado.
     *
     * @param email el correo electrónico a verificar
     * @return true si existe un usuario con ese correo electrónico, false en caso contrario
     */
    boolean existePorEmail(String email);

    /**
     * Obtiene todos los usuarios registrados en el sistema.
     *
     * @return una lista con todos los usuarios
     */
    List<Usuario> obtenerTodosLosUsuarios();

    /**
     * Actualiza la información de un usuario existente.
     *
     * @param usuario el usuario con la información actualizada
     * @return el usuario actualizado
     */
    Usuario actualizarUsuario(Usuario usuario);

    /**
     * Elimina un usuario por su ID.
     *
     * @param id el ID del usuario a eliminar
     */
    void eliminarUsuario(Long id);

    /**
     * Busca usuarios que coincidan con los criterios de búsqueda.
     *
     * @param query la cadena de búsqueda
     * @return una lista de usuarios que coinciden con la búsqueda
     */
    List<Usuario> buscarUsuarios(String query);

    /**
     * Busca usuarios por ciudad.
     *
     * @param ciudad la ciudad para filtrar
     * @return una lista de usuarios que viven en la ciudad especificada
     */
    List<Usuario> buscarPorCiudad(String ciudad);

    /**
     * Busca usuarios por rango de edad.
     *
     * @param edadMin la edad mínima
     * @param edadMax la edad máxima
     * @return una lista de usuarios dentro del rango de edades especificado
     */
    List<Usuario> buscarPorRangoEdad(int edadMin, int edadMax);

    /**
     * Activa o desactiva un usuario.
     *
     * @param id el ID del usuario
     * @param activo true para activar, false para desactivar
     * @return el usuario actualizado
     */
    Usuario cambiarEstadoUsuario(Long id, boolean activo);
}
