package com.zonasos.service;

import com.zonasos.model.OpinionPublica;
import com.zonasos.repository.OpinionPublicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OpinionPublicaService {

    @Autowired
    private OpinionPublicaRepository opinionPublicaRepository;

    public List<OpinionPublica> obtenerTodas() {
        return opinionPublicaRepository.findAll();
    }

    //  CAMBIO: Long → String
    public OpinionPublica obtenerPorId(String id) {
        return opinionPublicaRepository.findById(id).orElse(null);
    }

    public OpinionPublica crear(OpinionPublica opinionPublica) {
        // Lógica de negocio: fecha automática
        opinionPublica.setFecha(LocalDateTime.now());
        return opinionPublicaRepository.save(opinionPublica);
    }

    //  CAMBIO: Long → String
    public OpinionPublica actualizar(String id, OpinionPublica opinionPublica) {
        opinionPublica.setId(id);
        return opinionPublicaRepository.save(opinionPublica);
    }

    //  CAMBIO: Long → String
    public void eliminar(String id) {
        opinionPublicaRepository.deleteById(id);
    }
        public List<OpinionPublica> obtenerPorAlerta(String alertaId) {
        return opinionPublicaRepository.findByAlertaId(alertaId);
    }
}