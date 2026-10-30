package com.zonasos.controller;

import com.zonasos.model.TipoEmergencia;
import com.zonasos.service.TipoEmergenciaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-emergencia")
public class TipoEmergenciaController {

    private final TipoEmergenciaService tipoEmergenciaService;

    public TipoEmergenciaController(TipoEmergenciaService tipoEmergenciaService) {
        this.tipoEmergenciaService = tipoEmergenciaService;
    }

    @GetMapping
    public List<TipoEmergencia> listar() {
        return tipoEmergenciaService.obtenerTodos();
    }

    // ✅ CAMBIO: Long → String
    @GetMapping("/{id}")
    public TipoEmergencia obtenerPorId(@PathVariable String id) {
        return tipoEmergenciaService.obtenerPorId(id);
    }

    @PostMapping
    public TipoEmergencia crear(@RequestBody TipoEmergencia tipoEmergencia) {
        return tipoEmergenciaService.crear(tipoEmergencia);
    }

    // ✅ CAMBIO: Long → String
    @PutMapping("/{id}")
    public TipoEmergencia actualizar(@PathVariable String id, @RequestBody TipoEmergencia tipoEmergencia) {
        return tipoEmergenciaService.actualizar(id, tipoEmergencia);
    }

    // ✅ CAMBIO: Long → String
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable String id) {
        tipoEmergenciaService.eliminar(id);
    }
}