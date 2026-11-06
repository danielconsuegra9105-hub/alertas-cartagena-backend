package com.zonasos.repository;

import com.zonasos.model.OpinionPublica;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OpinionPublicaRepository extends MongoRepository<OpinionPublica, String> {
    // Busca por el campo alertaId que ahora es String
    List<OpinionPublica> findByAlertaId(String alertaId);
}