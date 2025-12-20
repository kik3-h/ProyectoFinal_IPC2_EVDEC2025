package com.vaqueras.model;

public class LoginResponse {
    
    private String token;
    private int idUser;
    private String nickname;
    private String email;
    private String rol;

    public LoginResponse() {}

    public LoginResponse(String token, int idUser, String nickname, String email, String rol) {
        this.token = token;
        this.idUser = idUser;
        this.nickname = nickname;
        this.email = email;
        this.rol = rol;
    }

    public String getToken() { return token; }
    public int getIdUser() { return idUser; }
    public String getNickname() { return nickname; }
    public String getEmail() { return email; }
    public String getRol() { return rol; }
}
