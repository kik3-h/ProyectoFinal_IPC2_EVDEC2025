package com.vaqueras.model;
import java.math.BigDecimal; // Import necesario para manejar valores monetarios

public class CarteraDTO {
    
    private int idUser;
    private BigDecimal saldoActual;
    private String ultimaActualizacion;

    public CarteraDTO() {}

    public CarteraDTO(int idUser, BigDecimal saldoActual, String ultimaActualizacion) {
        this.idUser = idUser;
        this.saldoActual = saldoActual;
        this.ultimaActualizacion = ultimaActualizacion;
    }

    public int getIdUser() { return idUser; }
    public void setIdUser(int idUser) { this.idUser = idUser; }

    public BigDecimal getSaldoActual() { return saldoActual; }
    public void setSaldoActual(BigDecimal saldoActual) { this.saldoActual = saldoActual; }

    public String getUltimaActualizacion() { return ultimaActualizacion; }
    public void setUltimaActualizacion(String ultimaActualizacion) { this.ultimaActualizacion = ultimaActualizacion; }
}
