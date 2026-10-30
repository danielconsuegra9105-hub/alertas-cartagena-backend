package com.zonasos.service;

import com.zonasos.model.Usuario;
import com.zonasos.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    //  CAMBIO: Long → String
    public Usuario obtenerPorId(String id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public Usuario buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    public Usuario crear(Usuario usuario) {
        usuario.setFechaRegistro(LocalDateTime.now());

        if (usuario.getRol() == null || usuario.getRol().isEmpty()) {
            usuario.setRol("CIUDADANO");
        }

        // ENCRIPTAR CONTRASEÑA CON BCrypt
        if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }

        return usuarioRepository.save(usuario);
    }

    //  CAMBIO: Long → String
    public Usuario actualizar(String id, Usuario usuario) {
        Usuario existente = usuarioRepository.findById(id).orElse(null);
        if (existente != null) {
            if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
                usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            } else {
                usuario.setPassword(existente.getPassword());
            }
            usuario.setId(id);
            return usuarioRepository.save(usuario);
        }
        return null;
    }

    // CAMBIO: Long → String
    public void eliminar(String id) {
        usuarioRepository.deleteById(id);
    }
}