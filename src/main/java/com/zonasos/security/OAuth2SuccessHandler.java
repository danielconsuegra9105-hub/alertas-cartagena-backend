package com.zonasos.security;

import com.zonasos.model.Usuario;
import com.zonasos.repository.UsuarioRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    public OAuth2SuccessHandler(JwtService jwtService, UsuarioRepository usuarioRepository) {
        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        try {
            // 1. Extraer los datos que nos da el proveedor (Google o GitHub)
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

            String email = oAuth2User.getAttribute("email");
            String name = oAuth2User.getAttribute("name");
            String login = oAuth2User.getAttribute("login"); // GitHub usa 'login'

            // 2. Plan de respaldo si el correo o el nombre vienen vacíos (típico en GitHub)
            if (email == null || email.isEmpty()) {
                if (login != null) {
                    email = login + "@github.com";
                } else {
                    email = "usuario_oauth@zonasos.com"; // Último recurso
                }
            }

            if (name == null || name.isEmpty()) {
                name = (login != null) ? login : email.split("@")[0];
            }

            // 3. Buscar si el usuario ya existe en MongoDB
            Usuario usuario = usuarioRepository.findByCorreo(email);

            // 4. Si no existe, lo creamos como CIUDADANO
            if (usuario == null) {
                usuario = new Usuario();
                usuario.setCorreo(email);
                usuario.setNombre(name);
                usuario.setRol("CIUDADANO");
                usuario.setPassword(""); // Sin contraseña porque usa OAuth
                usuario.setFechaRegistro(LocalDateTime.now());
                usuarioRepository.save(usuario);
            }

            // 5. Generar nuestro token JWT
            String token = jwtService.generateTokenForOAuth(usuario.getCorreo(), usuario.getRol());

            // 6. Detectar si la petición proviene de la App Móvil (Android/Flutter) o del Frontend Web (Angular)
            String userAgent = request.getHeader("User-Agent");
            String referer = request.getHeader("Referer");

            boolean esMovil = (userAgent != null && (userAgent.contains("Android") || userAgent.contains("Mobile"))) 
                    || (referer == null || !referer.contains("4200"));

            String targetUrl;
            if (esMovil) {
                // Redirección con Deep Link para Flutter (capturado por flutter_web_auth_2)
                targetUrl = UriComponentsBuilder.fromUriString("zonasos://oauth-success")
                        .queryParam("token", token)
                        .queryParam("email", usuario.getCorreo())
                        .queryParam("rol", usuario.getRol())
                        .build().toUriString();
            } else {
                // Redirección para Angular Web
                targetUrl = "http://localhost:4200/oauth-success?token=" + token;
            }

            getRedirectStrategy().sendRedirect(request, response, targetUrl);

        } catch (Exception e) {
            // 🚨 SI ALGO FALLA, evita el error 500 y manda al usuario al login correspondiente
            System.err.println("Error en OAuth2SuccessHandler: " + e.getMessage());
            e.printStackTrace();

            String userAgent = request.getHeader("User-Agent");
            if (userAgent != null && (userAgent.contains("Android") || userAgent.contains("Mobile"))) {
                getRedirectStrategy().sendRedirect(request, response, "zonasos://oauth-error");
            } else {
                getRedirectStrategy().sendRedirect(request, response, "http://localhost:4200/login?error=oauth");
            }
        }
    }
}