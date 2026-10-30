package com.zonasos.repository;

import com.zonasos.model.Alerta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertaRepository extends MongoRepository<Alerta, String> {
}