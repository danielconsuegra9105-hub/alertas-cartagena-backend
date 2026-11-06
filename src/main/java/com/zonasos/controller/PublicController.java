package com.zonasos.controller;

import com.zonasos.model.Alerta;
import com.zonasos.model.OpinionPublica;
import com.zonasos.service.AlertaService;
import com.zonasos.repository.OpinionPublicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/public")
@CrossOrigin(origins = "*") // 🌟 Permite que el Frontend de Angular (puerto 4200) se conecte sin errores de CORS
public class PublicController {

    @Autowired
    private AlertaService alertaService;

    @Autowired
    private OpinionPublicaRepository opinionRepository;

    /**
     * 1. Obtiene la lista de todas las alertas reportadas.
     * Ruta: GET /api/public/alertas
     */
    @GetMapping("/alertas")
    public ResponseEntity<List<Alerta>> obtenerAlertasPublicas() {
        List<Alerta> alertas = alertaService.obtenerTodas();
        return ResponseEntity.ok(alertas);
    }

    /**
     * 2. Obtiene los comentarios y evidencias de una alerta específica.
     * Ruta: GET /api/public/comentarios/{alertaId}
     */
    @GetMapping("/comentarios/{alertaId}")
    public ResponseEntity<List<OpinionPublica>> obtenerComentariosPorAlerta(@PathVariable String alertaId) {
        // El método findByAlertaId debe estar definido en OpinionPublicaRepository
        List<OpinionPublica> comentarios = opinionRepository.findByAlertaId(alertaId);
        return ResponseEntity.ok(comentarios);
    }

    /**
     * 3. Permite que cualquier ciudadano cree un comentario o suba una evidencia.
     * Ruta: POST /api/public/comentarios
     */
    @PostMapping("/comentarios")
    public ResponseEntity<OpinionPublica> crearComentarioPublico(@RequestBody OpinionPublica nuevaOpinion) {
        // Guarda la opinión en la colección 'opiniones_publicas' de MongoDB
        OpinionPublica guardado = opinionRepository.save(nuevaOpinion);
        return ResponseEntity.ok(guardado);
    }

    /**
     * Endpoint informativo sobre el sistema.
     * Ruta: GET /api/public/info
     */
    @GetMapping("/info")
    public Map<String, String> info() {
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Bienvenido a ZonaSoS - Sistema de gestión de emergencias de Cartagena");
        response.put("version", "1.0.0");
        response.put("estado", "Operativo");
        return response;
    }

    /**
     * Endpoint de verificación de estado del servidor.
     * Ruta: GET /api/public/estado
     */
    @GetMapping("/estado")
    public Map<String, String> estado() {
        Map<String, String> response = new HashMap<>();
        response.put("estado", "Sistema disponible");
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        return response;
    }
}
