package com.zonasos.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    @GetMapping("/info")
    public Map<String, String> info() {
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Bienvenido a ZonaSoS - Sistema de gestión de emergencias de Cartagena");
        response.put("version", "1.0.0");
        response.put("estado", "Operativo");
        return response;
    }

    @GetMapping("/estado")
    public Map<String, String> estado() {
        Map<String, String> response = new HashMap<>();
        response.put("estado", "Sistema disponible");
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        return response;
    }
}