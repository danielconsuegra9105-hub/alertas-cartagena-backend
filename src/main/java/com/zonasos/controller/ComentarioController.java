package com.zonasos.controller;

import com.zonasos.model.Comentario;
import com.zonasos.repository.ComentarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/comentarios")
public class ComentarioController {

    @Autowired
    private ComentarioRepository comentarioRepository;

    // ✅ CAMBIO: Long → String
    @GetMapping("/alerta/{alertaId}")
    public List<Comentario> getComentariosByAlerta(@PathVariable String alertaId) {
        return comentarioRepository.findByAlertaIdOrderByFechaAsc(alertaId);
    }

    @PostMapping
    public Comentario crearComentario(@RequestBody Comentario comentario) {
        comentario.setFecha(LocalDateTime.now());
        return comentarioRepository.save(comentario);
    }
}