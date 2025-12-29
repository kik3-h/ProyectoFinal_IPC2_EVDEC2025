package com.vaqueras.model;

public class BannerRequest {
     private Integer idVideojuego; // puede ser null
    private String imagenUrl;
    private Integer posicion;
    private Boolean activo;

    public BannerRequest() {}

    public Integer getIdVideojuego() { return idVideojuego; }
    public void setIdVideojuego(Integer idVideojuego) { this.idVideojuego = idVideojuego; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public Integer getPosicion() { return posicion; }
    public void setPosicion(Integer posicion) { this.posicion = posicion; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}
