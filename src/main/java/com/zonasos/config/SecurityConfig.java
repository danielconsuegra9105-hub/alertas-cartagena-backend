package com.zonasos.config;

import com.zonasos.security.JwtAuthenticationFilter;
import com.zonasos.security.OAuth2SuccessHandler; // 🌟 NUEVO IMPORT
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final OAuth2SuccessHandler oAuth2SuccessHandler; // 🌟 INYECTAR

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, OAuth2SuccessHandler oAuth2SuccessHandler) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                // ✅ MANEJO DE SESIONES (IF_REQUIRED es necesario para el OAuth de Google)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .maximumSessions(1)
                        .expiredUrl("/login?expired=true")
                )

                // 🌟 ACTIVAR OAUTH2 LOGIN
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(oAuth2SuccessHandler)
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/public/**").permitAll()

                        // 🌟 PERMITIR RUTAS DE OAUTH (Google/GitHub)
                        .requestMatchers("/oauth2/**", "/login/oauth2/**").permitAll()

                        // 🌟 RUTA PÚBLICA PARA LA COMUNIDAD (Sin token)
                        .requestMatchers(HttpMethod.GET, "/api/alertas/publicas").permitAll()

                        // RUTAS PROTEGIDAS
                        .requestMatchers("/api/alertas/**").hasAnyRole("CIUDADANO", "OPERADOR", "ADMIN")
                        .requestMatchers("/api/ciudades/**").hasAnyRole("ADMIN", "OPERADOR")
                        .requestMatchers("/api/tipos-emergencia/**").hasAnyRole("ADMIN", "OPERADOR")
                        .requestMatchers("/api/opiniones/**").hasAnyRole("CIUDADANO", "OPERADOR", "ADMIN")
                        .requestMatchers("/api/comentarios/**").hasAnyRole("OPERADOR", "ADMIN")
                        .requestMatchers("/api/usuarios/**").hasAnyRole("ADMIN", "OPERADOR")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}