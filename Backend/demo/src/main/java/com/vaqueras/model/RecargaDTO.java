package com.vaqueras.model;

import java.math.BigDecimal;

public class RecargaDTO {
    private int idRecarga;
    private BigDecimal montoRecargado;
    private String fechaRecarga;

    public RecargaDTO() {}

    public RecargaDTO(int idRecarga, BigDecimal montoRecargado, String fechaRecarga) {
        this.idRecarga = idRecarga;
        this.montoRecargado = montoRecargado;
        this.fechaRecarga = fechaRecarga;
    }

    public int getIdRecarga() { return idRecarga; }
    public BigDecimal getMontoRecargado() { return montoRecargado; }
    public String getFechaRecarga() { return fechaRecarga; }
}
