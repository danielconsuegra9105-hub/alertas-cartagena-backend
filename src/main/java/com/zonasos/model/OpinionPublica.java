package com.zonasos.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "opiniones_publicas")
public class OpinionPublica {

    @Id
    private String id;

    private String comentario;
    private Boolean verificacion;
    private LocalDateTime fecha;
    private String imagenesUrls;
    
    // 🌟 NUEVOS CAMPOS: Ahora son Strings simples en lugar de @DBRef
    private String alertaId;
    private String usuarioId;
    private String usuarioNombre;

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

    public String getImagenesUrls() { return imagenesUrls; }
    public void setImagenesUrls(String imagenesUrls) { this.imagenesUrls = imagenesUrls; }

    // 🌟 GETTERS Y SETTERS DE LOS NUEVOS CAMPOS
    public String getAlertaId() { return alertaId; }
    public void setAlertaId(String alertaId) { this.alertaId = alertaId; }

    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    public String getUsuarioNombre() { return usuarioNombre; }
    public void setUsuarioNombre(String usuarioNombre) { this.usuarioNombre = usuarioNombre; }
}