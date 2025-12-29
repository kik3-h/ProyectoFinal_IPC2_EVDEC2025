package com.vaqueras.model;

public class GrupoMemberDTO {
    
    private int idUser;
    private String nickname;
    private String fechaIngreso;

    public GrupoMemberDTO() {}

    public GrupoMemberDTO(int idUser, String nickname, String fechaIngreso) {
        this.idUser = idUser;
        this.nickname = nickname;
        this.fechaIngreso = fechaIngreso;
    }

    public int getIdUser() { return idUser; }
    public String getNickname() { return nickname; }
    public String getFechaIngreso() { return fechaIngreso; }
}
