package com.zonasos.repository;

import com.zonasos.model.OpinionPublica;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpinionPublicaRepository extends MongoRepository<OpinionPublica, String> {
}