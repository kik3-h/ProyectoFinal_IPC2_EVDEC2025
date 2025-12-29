package com.vaqueras.model;

public class ComentarioDTO {
    
    private int idComentario;
    private int idUser;
    private String nickname;

    private int idVideojuego;
    private Integer idComentarioPadre;

    private String texto;
    private Integer calificacion;
    private boolean textoVisible;
    private String fecha;

    public ComentarioDTO() {}

    public int getIdComentario() { return idComentario; }
    public void setIdComentario(int idComentario) { this.idComentario = idComentario; }

    public int getIdUser() { return idUser; }
    public void setIdUser(int idUser) { this.idUser = idUser; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public int getIdVideojuego() { return idVideojuego; }
    public void setIdVideojuego(int idVideojuego) { this.idVideojuego = idVideojuego; }

    public Integer getIdComentarioPadre() { return idComentarioPadre; }
    public void setIdComentarioPadre(Integer idComentarioPadre) { this.idComentarioPadre = idComentarioPadre; }

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }

    public Integer getCalificacion() { return calificacion; }
    public void setCalificacion(Integer calificacion) { this.calificacion = calificacion; }

    public boolean isTextoVisible() { return textoVisible; }
    public void setTextoVisible(boolean textoVisible) { this.textoVisible = textoVisible; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
}
