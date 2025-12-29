package com.vaqueras.model;

import java.math.BigDecimal;

public class CompraResponse {
    
    private int idVenta;
    private int idVideojuego;
    private BigDecimal precioFinal;
    private BigDecimal retencionPlataforma;
    private BigDecimal ingresoEmpresa;
    private String tipoComision;
    private BigDecimal porcentajeAplicado;
    private BigDecimal saldoNuevo;

    public CompraResponse() {}

    public int getIdVenta() { return idVenta; }
    public void setIdVenta(int idVenta) { this.idVenta = idVenta; }

    public int getIdVideojuego() { return idVideojuego; }
    public void setIdVideojuego(int idVideojuego) { this.idVideojuego = idVideojuego; }

    public BigDecimal getPrecioFinal() { return precioFinal; }
    public void setPrecioFinal(BigDecimal precioFinal) { this.precioFinal = precioFinal; }

    public BigDecimal getRetencionPlataforma() { return retencionPlataforma; }
    public void setRetencionPlataforma(BigDecimal retencionPlataforma) { this.retencionPlataforma = retencionPlataforma; }

    public BigDecimal getIngresoEmpresa() { return ingresoEmpresa; }
    public void setIngresoEmpresa(BigDecimal ingresoEmpresa) { this.ingresoEmpresa = ingresoEmpresa; }

    public String getTipoComision() { return tipoComision; }
    public void setTipoComision(String tipoComision) { this.tipoComision = tipoComision; }

    public BigDecimal getPorcentajeAplicado() { return porcentajeAplicado; }
    public void setPorcentajeAplicado(BigDecimal porcentajeAplicado) { this.porcentajeAplicado = porcentajeAplicado; }

    public BigDecimal getSaldoNuevo() { return saldoNuevo; }
    public void setSaldoNuevo(BigDecimal saldoNuevo) { this.saldoNuevo = saldoNuevo; }
}
