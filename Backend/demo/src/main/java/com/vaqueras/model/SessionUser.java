package com.vaqueras.model;

public class SessionUser {
    
    private int idUser;
    private String nickname;
    private String email;
    private String rol;

    public SessionUser() {}

    public SessionUser(int idUser, String nickname, String email, String rol) {
        this.idUser = idUser;
        this.nickname = nickname;
        this.email = email;
        this.rol = rol;
    }

    public int getIdUser() { return idUser; }
    public void setIdUser(int idUser) { this.idUser = idUser; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}
