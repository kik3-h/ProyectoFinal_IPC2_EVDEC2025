package com.vaqueras.model;

public class EmpresaUpdateRequest {
    private String nombreEmpresa;
    private String email;
    private String descripcion;

    public EmpresaUpdateRequest() {}

    public String getNombreEmpresa() { return nombreEmpresa; }
    public void setNombreEmpresa(String nombreEmpresa) { this.nombreEmpresa = nombreEmpresa; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
