package com.zonasos.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "alertas")
public class Alerta {

    @Id
    private String id;

    private String titulo;
    private String descripcion;
    private String estado;

    // Ubicación para el mapa
    private String ubicacionTexto;
    private Double latitud;
    private Double longitud;

    // Fechas
    private LocalDateTime fechaHoraCreacion;
    private LocalDateTime fechaHoraAtencion;
    private LocalDateTime fechaHoraResolucion;

    // ✅ IDs como String (sin @DBRef)
    private String usuarioId;
    private String tipoEmergenciaId;
    private String ciudadId;

    // Constructor vacío
    public Alerta() {}

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getUbicacionTexto() { return ubicacionTexto; }
    public void setUbicacionTexto(String ubicacionTexto) { this.ubicacionTexto = ubicacionTexto; }

    public Double getLatitud() { return latitud; }
    public void setLatitud(Double latitud) { this.latitud = latitud; }

    public Double getLongitud() { return longitud; }
    public void setLongitud(Double longitud) { this.longitud = longitud; }

    public LocalDateTime getFechaHoraCreacion() { return fechaHoraCreacion; }
    public void setFechaHoraCreacion(LocalDateTime fechaHoraCreacion) { this.fechaHoraCreacion = fechaHoraCreacion; }

    public LocalDateTime getFechaHoraAtencion() { return fechaHoraAtencion; }
    public void setFechaHoraAtencion(LocalDateTime fechaHoraAtencion) { this.fechaHoraAtencion = fechaHoraAtencion; }

    public LocalDateTime getFechaHoraResolucion() { return fechaHoraResolucion; }
    public void setFechaHoraResolucion(LocalDateTime fechaHoraResolucion) { this.fechaHoraResolucion = fechaHoraResolucion; }

    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    public String getTipoEmergenciaId() { return tipoEmergenciaId; }
    public void setTipoEmergenciaId(String tipoEmergenciaId) { this.tipoEmergenciaId = tipoEmergenciaId; }

    public String getCiudadId() { return ciudadId; }
    public void setCiudadId(String ciudadId) { this.ciudadId = ciudadId; }
}