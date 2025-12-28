package com.vaqueras.model;

public class GamerProfileUpdateRequest {
    
    private String telefono;
    private String pais;
    private Boolean bibliotecaPublica;

    public GamerProfileUpdateRequest() {}

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    public Boolean getBibliotecaPublica() { return bibliotecaPublica; }
    public void setBibliotecaPublica(Boolean bibliotecaPublica) { this.bibliotecaPublica = bibliotecaPublica; }
}
