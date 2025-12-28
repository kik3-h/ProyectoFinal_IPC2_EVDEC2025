package com.vaqueras.model;

public class GamerProfileDTO {
    
    private int idUser;
    private String nickname;
    private String email;
    private String telefono;
    private String fechaNacimiento;
    private String pais;
    private boolean bibliotecaPublica;

    public GamerProfileDTO() {}

    public int getIdUser() { return idUser; }
    public void setIdUser(int idUser) { this.idUser = idUser; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    public boolean isBibliotecaPublica() { return bibliotecaPublica; }
    public void setBibliotecaPublica(boolean bibliotecaPublica) { this.bibliotecaPublica = bibliotecaPublica; }
}

