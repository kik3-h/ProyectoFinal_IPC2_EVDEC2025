package com.vaqueras.model;

public class TokenUser {
    private int idUser;
    private String nickname;
    private String rol;

    public TokenUser() {}

    public TokenUser(int idUser, String nickname, String rol) {
        this.idUser = idUser;
        this.nickname = nickname;
        this.rol = rol;
    }

    public int getIdUser() { return idUser; }
    public String getNickname() { return nickname; }
    public String getRol() { return rol; }
}
