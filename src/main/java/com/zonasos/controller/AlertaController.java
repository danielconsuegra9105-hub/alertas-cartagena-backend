package com.zonasos.controller;

import com.zonasos.model.Alerta;
import com.zonasos.service.AlertaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/alertas")
public class AlertaController {

    private final AlertaService alertaService;

    public AlertaController(AlertaService alertaService) {
        this.alertaService = alertaService;
    }

    // Listado normal (protegido, para admins/operadores)
    @GetMapping
    public List<Alerta> listar() {
        return alertaService.obtenerTodas();
    }

    // 🌟 ENDPOINT PÚBLICO (Para la vista de la comunidad)
    @GetMapping("/publicas")
    public ResponseEntity<List<Map<String, Object>>> obtenerAlertasPublicas() {
        List<Alerta> alertas = alertaService.obtenerTodas();

        // Transformamos a Map para quitar el usuarioId y proteger la privacidad
        List<Map<String, Object>> alertasPublicas = alertas.stream().map(alerta -> {
            Map<String, Object> dto = new HashMap<>();
            dto.put("id", alerta.getId());
            dto.put("titulo", alerta.getTitulo());
            dto.put("descripcion", alerta.getDescripcion());
            dto.put("ubicacionTexto", alerta.getUbicacionTexto());
            dto.put("latitud", alerta.getLatitud());
            dto.put("longitud", alerta.getLongitud());
            dto.put("estado", alerta.getEstado());
            // ❌ NO agregamos el usuarioId
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(alertasPublicas);
    }

    @GetMapping("/{id}")
    public Alerta obtenerPorId(@PathVariable String id) {
        return alertaService.obtenerPorId(id);
    }

    @PostMapping
    public Alerta crear(@RequestBody Alerta alerta) {
        return alertaService.crear(alerta);
    }

    @PutMapping("/{id}")
    public Alerta actualizar(@PathVariable String id, @RequestBody Alerta alerta) {
        return alertaService.actualizar(id, alerta);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable String id) {
        alertaService.eliminar(id);
    }
}