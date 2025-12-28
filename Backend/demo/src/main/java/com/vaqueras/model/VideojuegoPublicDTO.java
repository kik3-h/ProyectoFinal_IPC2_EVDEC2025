package com.vaqueras.model;

public class VideojuegoPublicDTO {
    
    private int idVideojuego;
    private String titulo;
    private double precio;
    private String clasificacionEdad;
    private int edadMinima;
    private String estado;

    private int idEmpresa;
    private String nombreEmpresa;

    private String portadaUrl;

    public VideojuegoPublicDTO() {}

    public int getIdVideojuego() { return idVideojuego; }
    public void setIdVideojuego(int idVideojuego) { this.idVideojuego = idVideojuego; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public String getClasificacionEdad() { return clasificacionEdad; }
    public void setClasificacionEdad(String clasificacionEdad) { this.clasificacionEdad = clasificacionEdad; }

    public int getEdadMinima() { return edadMinima; }
    public void setEdadMinima(int edadMinima) { this.edadMinima = edadMinima; }

    public int getIdEmpresa() { return idEmpresa; }
    public void setIdEmpresa(int idEmpresa) { this.idEmpresa = idEmpresa; }

    public String getNombreEmpresa() { return nombreEmpresa; }
    public void setNombreEmpresa(String nombreEmpresa) { this.nombreEmpresa = nombreEmpresa; }

    public String getPortadaUrl() { return portadaUrl; }
    public void setPortadaUrl(String portadaUrl) { this.portadaUrl = portadaUrl; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
