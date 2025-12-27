package com.vaqueras.model;

public class Empresa {
    private int idEmpresa;
    private String nombreEmpresa;
    private String email;
    private String descripcion;
    private String fechaAfiliacion;

    public Empresa() {}

    public int getIdEmpresa() { return idEmpresa; }
    public void setIdEmpresa(int idEmpresa) { this.idEmpresa = idEmpresa; }

    public String getNombreEmpresa() { return nombreEmpresa; }
    public void setNombreEmpresa(String nombreEmpresa) { this.nombreEmpresa = nombreEmpresa; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getFechaAfiliacion() { return fechaAfiliacion; }
    public void setFechaAfiliacion(String fechaAfiliacion) { this.fechaAfiliacion = fechaAfiliacion; }
}
