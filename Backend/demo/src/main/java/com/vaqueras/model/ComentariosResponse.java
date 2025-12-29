package com.vaqueras.model;

import java.util.List;

public class ComentariosResponse {
    
    private double promedioCalificacion;
    private int totalCalificaciones;
    private List<ComentarioDTO> comentarios;

    public ComentariosResponse() {}

    public ComentariosResponse(double promedioCalificacion, int totalCalificaciones, List<ComentarioDTO> comentarios) {
        this.promedioCalificacion = promedioCalificacion;
        this.totalCalificaciones = totalCalificaciones;
        this.comentarios = comentarios;
    }

    public double getPromedioCalificacion() { return promedioCalificacion; }
    public int getTotalCalificaciones() { return totalCalificaciones; }
    public List<ComentarioDTO> getComentarios() { return comentarios; }
}
