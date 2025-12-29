package com.vaqueras.model;

public class GrupoDTO {
    
    private int idGrupo;
    private String nombreGrupo;
    private int idAdminUser;
    private String adminNickname;
    private String fechaCreacion;

    public GrupoDTO() {}

    public int getIdGrupo() { return idGrupo; }
    public void setIdGrupo(int idGrupo) { this.idGrupo = idGrupo; }

    public String getNombreGrupo() { return nombreGrupo; }
    public void setNombreGrupo(String nombreGrupo) { this.nombreGrupo = nombreGrupo; }

    public int getIdAdminUser() { return idAdminUser; }
    public void setIdAdminUser(int idAdminUser) { this.idAdminUser = idAdminUser; }

    public String getAdminNickname() { return adminNickname; }
    public void setAdminNickname(String adminNickname) { this.adminNickname = adminNickname; }

    public String getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(String fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
