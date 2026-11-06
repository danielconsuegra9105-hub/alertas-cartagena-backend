package com.zonasos.controller;

import com.zonasos.model.OpinionPublica;
import com.zonasos.service.OpinionPublicaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/opiniones")
public class OpinionPublicaController {

    private final OpinionPublicaService opinionPublicaService;

    public OpinionPublicaController(OpinionPublicaService opinionPublicaService) {
        this.opinionPublicaService = opinionPublicaService;
    }

    @GetMapping
    public List<OpinionPublica> listar() {
        return opinionPublicaService.obtenerTodas();
    }

    // ✅ CAMBIO: Long → String
    @GetMapping("/{id}")
    public OpinionPublica obtenerPorId(@PathVariable String id) {
        return opinionPublicaService.obtenerPorId(id);
    }

    @PostMapping
    public OpinionPublica crear(@RequestBody OpinionPublica opinionPublica) {
        return opinionPublicaService.crear(opinionPublica);
    }

    // ✅ CAMBIO: Long → String
    @PutMapping("/{id}")
    public OpinionPublica actualizar(@PathVariable String id, @RequestBody OpinionPublica opinionPublica) {
        return opinionPublicaService.actualizar(id, opinionPublica);
    }

    // ✅ CAMBIO: Long → String
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable String id) {
        opinionPublicaService.eliminar(id);
    }
        // 🌟 MÉTODO NUEVO: Buscar opiniones por el ID de la alerta
    @GetMapping("/alerta/{alertaId}")
    public List<OpinionPublica> obtenerPorAlerta(@PathVariable String alertaId) {
        return opinionPublicaService.obtenerPorAlerta(alertaId);
    }
}