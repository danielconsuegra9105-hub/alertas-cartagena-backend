package com.zonasos.config;

import com.zonasos.model.Usuario;
import com.zonasos.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        System.out.println("🔧 Cargando usuarios por defecto...");

        // USUARIO ADMIN
        crearUsuarioSiNoExiste(
                "Administrador ZonaSoS",
                "admin@zonasos.com",
                "3001234567",
                "admin123",
                "OPERADOR"
        );

        // USUARIO OPERADOR
        crearUsuarioSiNoExiste(
                "Operador ZonaSoS",
                "operador@zonasos.com",
                "3007654321",
                "operador123",
                "OPERADOR"
        );

        // USUARIO CIUDADANO
        crearUsuarioSiNoExiste(
                "Ciudadano ZonaSoS",
                "ciudadano@zonasos.com",
                "3009876543",
                "ciudadano123",
                "CIUDADANO"
        );

        System.out.println("Usuarios por defecto cargados.");
    }

    private void crearUsuarioSiNoExiste(String nombre, String correo, String telefono, String password, String rol) {
        Usuario existente = usuarioRepository.findByCorreo(correo);

        if (existente == null) {
            Usuario usuario = new Usuario();
            usuario.setNombre(nombre);
            usuario.setCorreo(correo);
            usuario.setTelefono(telefono);
            usuario.setPassword(passwordEncoder.encode(password));
            usuario.setRol(rol);
            usuario.setFechaRegistro(LocalDateTime.now());

            usuarioRepository.save(usuario);
            System.out.println("✅ " + rol + " creado: " + correo + " / " + password);
        } else {
            System.out.println("ℹ️ " + rol + " ya existe: " + correo);
        }
    }
}