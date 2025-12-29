package com.vaqueras.model;

public class ComentarioCreateRequest {
    
    private Integer idComentarioPadre; // opcional
    private String texto;              // opcional
    private Integer calificacion;      // opcional (1..5)

    public ComentarioCreateRequest() {}

    public Integer getIdComentarioPadre() { return idComentarioPadre; }
    public void setIdComentarioPadre(Integer idComentarioPadre) { this.idComentarioPadre = idComentarioPadre; }

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }

    public Integer getCalificacion() { return calificacion; }
    public void setCalificacion(Integer calificacion) { this.calificacion = calificacion; }
}
