package com.vaqueras.model;

public class CompraRequest {
    
    private int idVideojuego;
    private String fechaCompra; // opcional: "yyyy-MM-dd HH:mm:ss" o "yyyy-MM-dd"

    public CompraRequest() {}

    public int getIdVideojuego() { return idVideojuego; }
    public void setIdVideojuego(int idVideojuego) { this.idVideojuego = idVideojuego; }

    public String getFechaCompra() { return fechaCompra; }
    public void setFechaCompra(String fechaCompra) { this.fechaCompra = fechaCompra; }
}
