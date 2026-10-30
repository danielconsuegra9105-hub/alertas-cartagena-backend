package com.zonasos.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "comentarios")
public class Comentario {

    @Id
    private String id;

    private String texto;
    private LocalDateTime fecha;
    private String autorNombre;
    private String autorRol;
    private String alertaId;  // Cambia de Long a String
    private String imagenesUrls;

    // Constructor vacío
    public Comentario() {}

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public String getAutorNombre() { return autorNombre; }
    public void setAutorNombre(String autorNombre) { this.autorNombre = autorNombre; }

    public String getAutorRol() { return autorRol; }
    public void setAutorRol(String autorRol) { this.autorRol = autorRol; }

    public String getAlertaId() { return alertaId; }
    public void setAlertaId(String alertaId) { this.alertaId = alertaId; }

    public String getImagenesUrls() { return imagenesUrls; }
    public void setImagenesUrls(String imagenesUrls) { this.imagenesUrls = imagenesUrls; }
}