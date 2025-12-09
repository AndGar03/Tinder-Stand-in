package udistrital.avanzada.tinderstandin.usuario.servicios.implementaciones;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import udistrital.avanzada.tinderstandin.usuario.dto.JwtResponse;
import udistrital.avanzada.tinderstandin.usuario.dto.LoginRequest;
import udistrital.avanzada.tinderstandin.usuario.dto.RegistroRequest;
import udistrital.avanzada.tinderstandin.usuario.modelos.EntidadRol;
import udistrital.avanzada.tinderstandin.usuario.modelos.EntidadUsuario;
import udistrital.avanzada.tinderstandin.usuario.repositorios.RolRepositorio;
import udistrital.avanzada.tinderstandin.usuario.repositorios.UsuarioRepositorio;
import udistrital.avanzada.tinderstandin.usuario.seguridad.JwtUtils;
import udistrital.avanzada.tinderstandin.usuario.servicios.interfaces.ServicioAutenticacion;
import udistrital.avanzada.tinderstandin.usuario.seguridad.UserDetailsImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de autenticación.
 * 
 * @author SanSantax
 */
@Service
public class ServicioAutenticacionImpl implements ServicioAutenticacion {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private RolRepositorio rolRepositorio;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public JwtResponse autenticarUsuario(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generarTokenJWT(authentication);
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();        
        List<String> roles = userDetails.getAuthorities().stream()
            .map(item -> item.getAuthority())
            .collect(Collectors.toList());

        return new JwtResponse(
            jwt,
            userDetails.getId(),
            userDetails.getUsername(),
            userDetails.getEmail(),
            roles
        );
    }

    @Override
    @Transactional
    public String registrarUsuario(RegistroRequest registroRequest) {
        if (usuarioRepositorio.existsByUsername(registroRequest.getUsername())) {
            return "Error: El nombre de usuario ya está en uso!";
        }

        if (usuarioRepositorio.existsByEmail(registroRequest.getEmail())) {
            return "Error: El correo electrónico ya está en uso!";
        }

        // Crear nuevo usuario
        EntidadUsuario usuario = new EntidadUsuario(
            registroRequest.getNombreCompleto(),
            registroRequest.getEmail(),
            registroRequest.getTelefono(),
            registroRequest.getUsername(),
            passwordEncoder.encode(registroRequest.getPassword())
        );

        Set<EntidadRol> roles = new HashSet<>();
        
        // Asignar rol de usuario por defecto
        EntidadRol rolUsuario = rolRepositorio.findByNombre(EntidadRol.NombreRol.ROLE_USUARIO)
            .orElseThrow(() -> new RuntimeException("Error: No se encontró el rol."));
        
        roles.add(rolUsuario);
        usuario.setRoles(roles);
        
        usuarioRepositorio.save(usuario);

        return "Usuario registrado exitosamente!";
    }

    @Override
    public boolean existePorUsername(String username) {
        return usuarioRepositorio.existsByUsername(username);
    }

    @Override
    public boolean existePorEmail(String email) {
        return usuarioRepositorio.existsByEmail(email);
    }
}
