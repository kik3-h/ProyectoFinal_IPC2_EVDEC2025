package com.vaqueras.model;

public class LoginRequest {
    private String identifier; // email o nickname
    private String password;
    //constructor vacío para que GSON lo pueda usar
    public LoginRequest() {}

    //Constructor con Datos para facilitar pruebas jasj
    public LoginRequest(String identifier, String password) {
        this.identifier = identifier;
        this.password = password;
    }

    public String getIdentifier() { return identifier; }
    public void setIdentifier(String identifier) { this.identifier = identifier; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
