package udistrital.avanzada.tinderstandin.usuarios.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import udistrital.avanzada.tinderstandin.usuarios.entidades.Usuario;
import udistrital.avanzada.tinderstandin.usuarios.repositorios.UsuarioRepositorio;
import udistrital.avanzada.tinderstandin.usuarios.seguridad.UsuarioPrincipal;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.findByEmail(email)
                .orElseThrow(() -> 
                    new UsernameNotFoundException("Usuario no encontrado con el correo: " + email)
                );

        return UsuarioPrincipal.build(usuario);
    }
}
