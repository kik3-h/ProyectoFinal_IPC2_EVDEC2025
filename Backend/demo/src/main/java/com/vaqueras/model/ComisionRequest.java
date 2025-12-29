package com.vaqueras.model;

import java.math.BigDecimal;

public class ComisionRequest {
    
    private BigDecimal porcentaje;

    public ComisionRequest() {}

    public BigDecimal getPorcentaje() { return porcentaje; }
    public void setPorcentaje(BigDecimal porcentaje) { this.porcentaje = porcentaje; }
}
