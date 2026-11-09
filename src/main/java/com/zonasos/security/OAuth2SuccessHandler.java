package com.zonasos.security;

import com.zonasos.model.Usuario;
import com.zonasos.repository.UsuarioRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
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
    private final String frontendUrl;

    public OAuth2SuccessHandler(JwtService jwtService,
                                UsuarioRepository usuarioRepository,
                                @Value("${app.frontend-url}") String frontendUrl) {
        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
        // Quita "/" final para no generar "//oauth-success"
        this.frontendUrl = frontendUrl.replaceAll("/+$", "");
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        try {
            // 1. Datos del proveedor (Google o GitHub)
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

            String email = oAuth2User.getAttribute("email");
            String name = oAuth2User.getAttribute("name");
            String login = oAuth2User.getAttribute("login"); // GitHub usa 'login'

            // 2. Respaldo si el correo o el nombre vienen vacíos (típico en GitHub)
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

            // 3. Buscar usuario en MongoDB
            Usuario usuario = usuarioRepository.findByCorreo(email);

            // 4. Si no existe, crearlo como CIUDADANO
            if (usuario == null) {
                usuario = new Usuario();
                usuario.setCorreo(email);
                usuario.setNombre(name);
                usuario.setRol("CIUDADANO");
                usuario.setPassword(""); // Sin contraseña porque usa OAuth
                usuario.setFechaRegistro(LocalDateTime.now());
                usuarioRepository.save(usuario);
            }

            // 5. Generar JWT
            String token = jwtService.generateTokenForOAuth(usuario.getCorreo(), usuario.getRol());

            // 6. Redirigir al frontend web (Angular). La URL viene de app.frontend-url.
            //    TODO Flutter: cuando se retome la app móvil, agregar aquí el deep link zonasos://oauth-success
            //    decidiendo por un parámetro explícito, no por el Referer.
            String targetUrl = UriComponentsBuilder.fromUriString(frontendUrl)
                    .path("/oauth-success")
                    .queryParam("token", token)
                    .build().toUriString();

            getRedirectStrategy().sendRedirect(request, response, targetUrl);

        } catch (Exception e) {
            System.err.println("Error en OAuth2SuccessHandler: " + e.getMessage());
            e.printStackTrace();
            getRedirectStrategy().sendRedirect(request, response, frontendUrl + "/login?error=oauth");
        }
    }
}