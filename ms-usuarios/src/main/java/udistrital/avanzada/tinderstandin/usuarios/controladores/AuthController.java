package udistrital.avanzada.tinderstandin.usuarios.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import udistrital.avanzada.tinderstandin.usuarios.dto.JwtResponse;
import udistrital.avanzada.tinderstandin.usuarios.dto.LoginDTO;
import udistrital.avanzada.tinderstandin.usuarios.dto.RegistroUsuarioDTO;
import udistrital.avanzada.tinderstandin.usuarios.entidades.Rol;
import udistrital.avanzada.tinderstandin.usuarios.entidades.Usuario;
import udistrital.avanzada.tinderstandin.usuarios.repositorios.RolRepositorio;
import udistrital.avanzada.tinderstandin.usuarios.repositorios.UsuarioRepositorio;
import udistrital.avanzada.tinderstandin.usuarios.seguridad.JwtUtils;
import udistrital.avanzada.tinderstandin.usuarios.seguridad.UsuarioPrincipal;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

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

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsernameOrEmail(), loginDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generarTokenJwt(authentication);
        
        UsuarioPrincipal userDetails = (UsuarioPrincipal) authentication.getPrincipal();        
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(
                jwt, 
                userDetails.getId(),
                userDetails.getUsername(), 
                userDetails.getEmail(), 
                roles));
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registrarUsuario(@RequestBody RegistroUsuarioDTO registroDTO) {
        if (usuarioRepositorio.existsByEmail(registroDTO.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: El correo ya está en uso");
        }

        // Crear nuevo usuario
        Usuario usuario = new Usuario();
        usuario.setNombre(registroDTO.getNombre());
        usuario.setApellido(registroDTO.getApellido());
        usuario.setEmail(registroDTO.getEmail());
        usuario.setFechaNacimiento(LocalDate.parse(registroDTO.getFechaNacimiento()));
        usuario.setTelefono(registroDTO.getTelefono());
        usuario.setUsername(registroDTO.getUsername());
        usuario.setPassword(passwordEncoder.encode(registroDTO.getPassword()));

        Set<Rol> roles = new HashSet<>();

        Rol rolUsuario = rolRepositorio.findByNombre(Rol.RolNombre.ROLE_USUARIO)
                .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
        roles.add(rolUsuario);

        usuario.setRoles(roles);
        usuarioRepositorio.save(usuario);

        return ResponseEntity.ok("Usuario registrado exitosamente");
    }
}
