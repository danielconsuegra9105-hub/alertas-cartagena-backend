package com.zonasos.service;

import com.zonasos.model.TipoEmergencia;
import com.zonasos.repository.TipoEmergenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TipoEmergenciaService {

    @Autowired
    private TipoEmergenciaRepository tipoEmergenciaRepository;

    public List<TipoEmergencia> obtenerTodos() {
        return tipoEmergenciaRepository.findAll();
    }

    //  CAMBIO: Long → String
    public TipoEmergencia obtenerPorId(String id) {
        return tipoEmergenciaRepository.findById(id).orElse(null);
    }

    public TipoEmergencia crear(TipoEmergencia tipoEmergencia) {
        return tipoEmergenciaRepository.save(tipoEmergencia);
    }

    //  CAMBIO: Long → String
    public TipoEmergencia actualizar(String id, TipoEmergencia tipoEmergencia) {
        tipoEmergencia.setId(id);
        return tipoEmergenciaRepository.save(tipoEmergencia);
    }

    //  CAMBIO: Long → String
    public void eliminar(String id) {
        tipoEmergenciaRepository.deleteById(id);
    }
}