package udistrital.avanzada.tinderstandin.usuario.servicios;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import udistrital.avanzada.tinderstandin.usuario.modelos.EntidadUsuario;
import udistrital.avanzada.tinderstandin.usuario.repositorios.UsuarioRepositorio;
import udistrital.avanzada.tinderstandin.usuario.seguridad.UserDetailsImpl;

/**
 * Servicio para cargar detalles de usuario para Spring Security.
 *
 * @author SanSantax
 */
@Service
@RequiredArgsConstructor
public class ServicioDetallesUsuario implements UserDetailsService {
    
    private final UsuarioRepositorio usuarioRepositorio;
    
    /**
     * Carga un usuario por su nombre de usuario.
     *
     * @param username Nombre de usuario
     * @return Detalles del usuario para Spring Security
     * @throws UsernameNotFoundException si el usuario no existe
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        EntidadUsuario usuario = usuarioRepositorio.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado con nombre de usuario: " + username));
        
        return UserDetailsImpl.build(usuario);
    }
}
