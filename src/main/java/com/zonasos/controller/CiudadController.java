package com.zonasos.controller;

import com.zonasos.model.Ciudad;
import com.zonasos.service.CiudadService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ciudades")
public class CiudadController {

    private final CiudadService ciudadService;

    public CiudadController(CiudadService ciudadService) {
        this.ciudadService = ciudadService;
    }

    @GetMapping
    public List<Ciudad> listar() {
        return ciudadService.obtenerTodas();
    }

    // ✅ CAMBIO: Long → String
    @GetMapping("/{id}")
    public Ciudad obtenerPorId(@PathVariable String id) {
        return ciudadService.obtenerPorId(id);
    }

    @PostMapping
    public Ciudad crear(@RequestBody Ciudad ciudad) {
        return ciudadService.crear(ciudad);
    }

    // ✅ CAMBIO: Long → String
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable String id) {
        ciudadService.eliminar(id);
    }
}