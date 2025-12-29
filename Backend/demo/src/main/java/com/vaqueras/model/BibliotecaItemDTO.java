package com.vaqueras.model;

public class BibliotecaItemDTO {
    
    private int idBiblioteca;
    private int idUser;
    private int idVideojuego;
    private String titulo;
    private String fechaAdquisicion;
    private String estadoInstalacion;
    private boolean esPropietario;
    private String portadaUrl;

    public BibliotecaItemDTO() {}

    public BibliotecaItemDTO(int idBiblioteca, int idUser,int idVideojuego, String titulo, String fechaAdquisicion,
                            String estadoInstalacion, boolean esPropietario, String portadaUrl) {
        this.idBiblioteca = idBiblioteca;
        this.idUser = idUser;
        this.idVideojuego = idVideojuego;
        this.titulo = titulo;
        this.fechaAdquisicion = fechaAdquisicion;
        this.estadoInstalacion = estadoInstalacion;
        this.esPropietario = esPropietario;
        this.portadaUrl = portadaUrl;
    }

    public int getIdBiblioteca() { return idBiblioteca; }
    public int getIdUser() { return idUser; }
    public int getIdVideojuego() { return idVideojuego; }
    public String getTitulo() { return titulo; }
    public String getFechaAdquisicion() { return fechaAdquisicion; }
    public String getEstadoInstalacion() { return estadoInstalacion; }
    public boolean isEsPropietario() { return esPropietario; }
    public String getPortadaUrl() { return portadaUrl; }
}
