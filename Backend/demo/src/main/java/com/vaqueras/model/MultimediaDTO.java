package com.vaqueras.model;

public class MultimediaDTO {

    private int idMultimedia;
    private String urlImagen;
    private String tipo;

    public MultimediaDTO() {}

    public MultimediaDTO(int idMultimedia, String urlImagen, String tipo) {
        this.idMultimedia = idMultimedia;
        this.urlImagen = urlImagen;
        this.tipo = tipo;
    }

    public int getIdMultimedia() { return idMultimedia; }
    public String getUrlImagen() { return urlImagen; }
    public String getTipo() { return tipo; }
    
}
