package com.zonasos.service;

import com.zonasos.model.Alerta;
import com.zonasos.repository.AlertaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlertaService {

    @Autowired
    private AlertaRepository alertaRepository;

    public List<Alerta> obtenerTodas() {
        return alertaRepository.findAll();
    }

    //  CAMBIO: Long → String
    public Alerta obtenerPorId(String id) {
        return alertaRepository.findById(id).orElse(null);
    }

    public Alerta crear(Alerta alerta) {
        alerta.setFechaHoraCreacion(LocalDateTime.now());
        alerta.setEstado("ALERTA");
        return alertaRepository.save(alerta);
    }

    //  CAMBIO: Long → String
    public Alerta actualizar(String id, Alerta alerta) {
        Alerta alertaExistente = alertaRepository.findById(id).orElse(null);

        if (alertaExistente == null) {
            throw new RuntimeException("Alerta no encontrada con ID: " + id);
        }

        if (alerta.getEstado() != null) {
            String nuevoEstado = alerta.getEstado();
            alertaExistente.setEstado(nuevoEstado);

            if (nuevoEstado.equals("ATENCION")) {
                alertaExistente.setFechaHoraAtencion(LocalDateTime.now());
            } else if (nuevoEstado.equals("RESUELTO")) {
                alertaExistente.setFechaHoraResolucion(LocalDateTime.now());
            }
        }

        return alertaRepository.save(alertaExistente);
    }

    //  CAMBIO: Long → String
    public void eliminar(String id) {
        alertaRepository.deleteById(id);
    }
}