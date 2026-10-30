package com.zonasos.repository;

import com.zonasos.model.TipoEmergencia;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoEmergenciaRepository extends MongoRepository<TipoEmergencia, String> {
}