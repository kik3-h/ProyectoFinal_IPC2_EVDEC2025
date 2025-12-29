package com.vaqueras.model;

public class AddMemberRequest {
    
    private Integer idUser;     // opcional
    private String nickname;    // opcional

    public AddMemberRequest() {}

    public Integer getIdUser() { return idUser; }
    public void setIdUser(Integer idUser) { this.idUser = idUser; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
}
