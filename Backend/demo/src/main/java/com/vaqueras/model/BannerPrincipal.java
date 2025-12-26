package com.vaqueras.model;

public class BannerPrincipal {
    private int idBanner;
    private Integer idVideojuego; // puede ser null
    private String imagenUrl;
    private int posicion;
    private boolean activo = true;

    public BannerPrincipal() {}

    public int getIdBanner() { return idBanner; }
    public void setIdBanner(int idBanner) { this.idBanner = idBanner; }

    public Integer getIdVideojuego() { return idVideojuego; }
    public void setIdVideojuego(Integer idVideojuego) { this.idVideojuego = idVideojuego; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public int getPosicion() { return posicion; }
    public void setPosicion(int posicion) { this.posicion = posicion; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
