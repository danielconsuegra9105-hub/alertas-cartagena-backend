package com.zonasos.controller;

import com.zonasos.model.Usuario;
import com.zonasos.security.JwtService;
import com.zonasos.service.UsuarioService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final UsuarioService usuarioService;

    public AuthController(AuthenticationManager authenticationManager,
UserDetailsService userDetailsService,
JwtService jwtService,
UsuarioService usuarioService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuario) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usuario.getCorreo(), usuario.getPassword())
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(usuario.getCorreo());
            String token = jwtService.generateToken(userDetails);

            Usuario usuarioDB = usuarioService.buscarPorCorreo(usuario.getCorreo());

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("rol", usuarioDB.getRol());
            response.put("nombre", usuarioDB.getNombre());
            response.put("id", usuarioDB.getId());
            response.put("correo", usuarioDB.getCorreo());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }
    }

    // Inicio del login OAuth desde la app móvil (Flutter):
    // marca la sesión como "móvil" y continúa con el flujo normal de Google/GitHub.
    @GetMapping("/mobile/{provider}")
    public void iniciarOAuthMovil(@PathVariable String provider,
                            HttpSession session,
                            HttpServletResponse response) throws IOException {
        if (!provider.equals("google") && !provider.equals("github")) {
            response.sendError(400, "Proveedor no soportado");
            return;
        }
        session.setAttribute("client", "mobile");
        response.sendRedirect("/oauth2/authorization/" + provider);
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registro(@RequestBody Usuario usuario) {
        try {
            Usuario existente = usuarioService.buscarPorCorreo(usuario.getCorreo());
            if (existente != null) {
                return ResponseEntity.badRequest().body("El correo ya está registrado");
            }

            Usuario nuevoUsuario = usuarioService.crear(usuario);

            UserDetails userDetails = userDetailsService.loadUserByUsername(nuevoUsuario.getCorreo());
            String token = jwtService.generateToken(userDetails);

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("rol", nuevoUsuario.getRol());
            response.put("nombre", nuevoUsuario.getNombre());
            response.put("id", nuevoUsuario.getId());
            response.put("correo", nuevoUsuario.getCorreo());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al registrar usuario: " + e.getMessage());
        }
    }
}