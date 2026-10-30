package com.zonasos.repository;

import com.zonasos.model.Comentario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentarioRepository extends MongoRepository<Comentario, String> {
    List<Comentario> findByAlertaIdOrderByFechaAsc(String alertaId);
}