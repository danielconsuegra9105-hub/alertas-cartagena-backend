package com.zonasos.repository;

import com.zonasos.model.Ciudad;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CiudadRepository extends MongoRepository<Ciudad, String> {
}