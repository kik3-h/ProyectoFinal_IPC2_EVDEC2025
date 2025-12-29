package com.vaqueras.model;

public class PrestamoCreateRequest {
    
    private int idVideojuego;
    private int idUserReceptor;

    public PrestamoCreateRequest() {}

    public int getIdVideojuego() { return idVideojuego; }
    public void setIdVideojuego(int idVideojuego) { this.idVideojuego = idVideojuego; }

    public int getIdUserReceptor() { return idUserReceptor; }
    public void setIdUserReceptor(int idUserReceptor) { this.idUserReceptor = idUserReceptor; }
}
