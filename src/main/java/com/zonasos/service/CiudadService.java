package com.zonasos.service;

import com.zonasos.model.Ciudad;
import com.zonasos.repository.CiudadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CiudadService {

    @Autowired
    private CiudadRepository ciudadRepository;

    public List<Ciudad> obtenerTodas() {
        return ciudadRepository.findAll();
    }

    //  CAMBIO: Long → String
    public Ciudad obtenerPorId(String id) {
        return ciudadRepository.findById(id).orElse(null);
    }

    public Ciudad crear(Ciudad ciudad) {
        return ciudadRepository.save(ciudad);
    }

    //  CAMBIO: Long → String
    public void eliminar(String id) {
        ciudadRepository.deleteById(id);
    }
}