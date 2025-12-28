package com.vaqueras.model;

import java.time.LocalDate;
import java.util.List;

public class VideojuegoDetailDTO {

    private int idVideojuego;
    private int idEmpresa;
    private String nombreEmpresa;

    private String titulo;
    private String descripcion;
    private double precio;
    private String recursosMinimos;
    private LocalDate fechaLanzamiento;

    private String estado;
    private String clasificacionEdad;
    private int edadMinima;

    private List<Categoria> categorias;
    private List<MultimediaDTO> multimedia;

    public VideojuegoDetailDTO() {}

    public int getIdVideojuego() { return idVideojuego; }
    public void setIdVideojuego(int idVideojuego) { this.idVideojuego = idVideojuego; }

    public int getIdEmpresa() { return idEmpresa; }
    public void setIdEmpresa(int idEmpresa) { this.idEmpresa = idEmpresa; }

    public String getNombreEmpresa() { return nombreEmpresa; }
    public void setNombreEmpresa(String nombreEmpresa) { this.nombreEmpresa = nombreEmpresa; }

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

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getClasificacionEdad() { return clasificacionEdad; }
    public void setClasificacionEdad(String clasificacionEdad) { this.clasificacionEdad = clasificacionEdad; }

    public int getEdadMinima() { return edadMinima; }
    public void setEdadMinima(int edadMinima) { this.edadMinima = edadMinima; }

    public List<Categoria> getCategorias() { return categorias; }
    public void setCategorias(List<Categoria> categorias) { this.categorias = categorias; }

    public List<MultimediaDTO> getMultimedia() { return multimedia; }
    public void setMultimedia(List<MultimediaDTO> multimedia) { this.multimedia = multimedia; }
    
}
