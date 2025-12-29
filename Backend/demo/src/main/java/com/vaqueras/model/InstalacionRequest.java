package com.vaqueras.model;

public class InstalacionRequest {
    
    private String estado; // "INSTALADO" o "NO_INSTALADO"

    public InstalacionRequest() {}

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
