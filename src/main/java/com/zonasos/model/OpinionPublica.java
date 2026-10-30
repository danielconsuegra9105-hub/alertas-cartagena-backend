package com.zonasos.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;

@Document(collection = "opiniones_publicas")
public class OpinionPublica {

    @Id
    private String id;

    private String comentario;
    private Boolean verificacion;
    private LocalDateTime fecha;

    @DBRef
    private Alerta alerta;

    @DBRef
    private Usuario usuario;

    // Constructor vacío
    public OpinionPublica() {}

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public Boolean getVerificacion() { return verificacion; }
    public void setVerificacion(Boolean verificacion) { this.verificacion = verificacion; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public Alerta getAlerta() { return alerta; }
    public void setAlerta(Alerta alerta) { this.alerta = alerta; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}