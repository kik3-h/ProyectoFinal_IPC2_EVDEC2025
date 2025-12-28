package com.vaqueras.model;

import java.time.LocalDate;
import java.util.List;

public class VideojuegoCreateRequest {
    private String titulo;
    private String descripcion;
    private double precio;
    private String recursosMinimos;
    private LocalDate fechaLanzamiento;

    private String clasificacionEdad; // 'E','T','M'
    private int edadMinima;
    private String estado;

    private List<Integer> categoriaIds;

    // Multimedia
    private String portadaUrl;          // tipo PORTADA (requerida)
    private List<String> galeriaUrls;   // tipo GALERIA (opcional)

    public VideojuegoCreateRequest() {}

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public String getRecursosMinimos() { return recursosMinimos; }
    public void setRecursosMinimos(String recursosMinimos) { this.recursosMinimos = recursosMinimos; }

    public LocalDate getFechaLanzamiento() { return fechaLanzamiento; }
    public void setFechaLanzamiento(LocalDate fechaLanzamiento) { this.fechaLanzamiento = fechaLanzamiento; }

    public String getClasificacionEdad() { return clasificacionEdad; }
    public void setClasificacionEdad(String clasificacionEdad) { this.clasificacionEdad = clasificacionEdad; }

    public int getEdadMinima() { return edadMinima; }
    public void setEdadMinima(int edadMinima) { this.edadMinima = edadMinima; }

    public List<Integer> getCategoriaIds() { return categoriaIds; }
    public void setCategoriaIds(List<Integer> categoriaIds) { this.categoriaIds = categoriaIds; }

    public String getPortadaUrl() { return portadaUrl; }
    public void setPortadaUrl(String portadaUrl) { this.portadaUrl = portadaUrl; }

    public List<String> getGaleriaUrls() { return galeriaUrls; }
    public void setGaleriaUrls(List<String> galeriaUrls) { this.galeriaUrls = galeriaUrls; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
