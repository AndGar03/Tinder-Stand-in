package com.tinder.service.impl;

import com.tinder.model.Rol;
import com.tinder.model.Usuario;
import com.tinder.repository.UsuarioRepository;
import com.tinder.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio de usuarios.
 * Proporciona la lógica de negocio para la gestión de usuarios.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Usuario guardarUsuario(Usuario usuario) {
        log.info("Guardando nuevo usuario: {}", usuario.getUsername());
        
        // Encriptar contraseña si es un nuevo usuario o se está actualizando
        if (usuario.getId() == null || !usuario.getPassword().startsWith("$2a$")) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }
        
        // Si es un nuevo usuario, establecer la fecha de unión
        if (usuario.getFechaUnion() == null) {
            usuario.setFechaUnion(LocalDate.now());
        }
        
        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorId(Long id) {
        log.debug("Buscando usuario por ID: {}", id);
        return usuarioRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorUsername(String username) {
        log.debug("Buscando usuario por nombre de usuario: {}", username);
        return usuarioRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorEmail(String email) {
        log.debug("Buscando usuario por correo electrónico: {}", email);
        return usuarioRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorUsername(String username) {
        log.debug("Verificando existencia de nombre de usuario: {}", username);
        return usuarioRepository.existsByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorEmail(String email) {
        log.debug("Verificando existencia de correo electrónico: {}", email);
        return usuarioRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> obtenerTodosLosUsuarios() {
        log.debug("Obteniendo todos los usuarios");
        return usuarioRepository.findAll();
    }

    @Override
    @Transactional
    public Usuario actualizarUsuario(Usuario usuario) {
        log.info("Actualizando usuario con ID: {}", usuario.getId());
        
        // Verificar si el usuario existe
        Usuario usuarioExistente = usuarioRepository.findById(usuario.getId())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con ID: " + usuario.getId()));
        
        // Actualizar solo los campos permitidos
        usuarioExistente.setNombreCompleto(usuario.getNombreCompleto());
        usuarioExistente.setEmail(usuario.getEmail());
        usuarioExistente.setTelefono(usuario.getTelefono());
        usuarioExistente.setCiudad(usuario.getCiudad());
        usuarioExistente.setBiografia(usuario.getBiografia());
        
        // Actualizar la contraseña solo si se proporcionó una nueva
        if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
            usuarioExistente.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }
        
        return usuarioRepository.save(usuarioExistente);
    }

    @Override
    @Transactional
    public void eliminarUsuario(Long id) {
        log.info("Eliminando usuario con ID: {}", id);
        
        // Verificar si el usuario existe
        if (!usuarioRepository.existsById(id)) {
            throw new UsernameNotFoundException("Usuario no encontrado con ID: " + id);
        }
        
        usuarioRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> buscarUsuarios(String query) {
        log.debug("Buscando usuarios con query: {}", query);
        return usuarioRepository.buscarPorNombreOUsername(query);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> buscarPorCiudad(String ciudad) {
        log.debug("Buscando usuarios en la ciudad: {}", ciudad);
        return usuarioRepository.findByCiudadIgnoreCase(ciudad);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> buscarPorRangoEdad(int edadMin, int edadMax) {
        log.debug("Buscando usuarios con edades entre {} y {} años", edadMin, edadMax);
        
        // Calcular fechas de nacimiento para el rango de edades
        LocalDate fechaHoy = LocalDate.now();
        LocalDate fechaNacimientoMax = fechaHoy.minusYears(edadMin);
        LocalDate fechaNacimientoMin = fechaHoy.minusYears(edadMax + 1).plusDays(1);
        
        return usuarioRepository.findByFechaNacimientoBetween(fechaNacimientoMin, fechaNacimientoMax);
    }

    @Override
    @Transactional
    public Usuario cambiarEstadoUsuario(Long id, boolean activo) {
        log.info("{} usuario con ID: {}", activo ? "Activando" : "Desactivando", id);
        
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con ID: " + id));
        
        usuario.setActivo(activo);
        return usuarioRepository.save(usuario);
    }

    /**
     * Carga los detalles del usuario por nombre de usuario.
     * Utilizado por Spring Security para la autenticación.
     *
     * @param username el nombre de usuario
     * @return los detalles del usuario
     * @throws UsernameNotFoundException si el usuario no se encuentra
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con nombre de usuario: " + username));
        
        if (!usuario.isActivo()) {
            throw new UsernameNotFoundException("El usuario está deshabilitado");
        }
        
        return usuario;
    }
}
