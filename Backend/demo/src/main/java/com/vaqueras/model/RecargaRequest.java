package com.vaqueras.model;

import java.math.BigDecimal;

public class RecargaRequest {
    private BigDecimal monto;

    public RecargaRequest() {}

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
}
