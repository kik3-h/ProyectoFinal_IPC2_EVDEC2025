package com.vaqueras.service;

import java.math.BigDecimal;

import com.vaqueras.dao.AdminComisionDAO;

public class AdminComisionService {

    private final AdminComisionDAO dao = new AdminComisionDAO();

    public BigDecimal globalActual() { return dao.getGlobalActual(); }

    public void setGlobal(BigDecimal pct) throws Exception {
        validarPct(pct);
        dao.setGlobal(pct);
    }

    public BigDecimal empresaActual(int idEmpresa) {
        return dao.getEmpresaActual(idEmpresa);
    }

    public void setEmpresa(int idEmpresa, BigDecimal pct) throws Exception {
        if (idEmpresa <= 0) throw new IllegalArgumentException("idEmpresa inválido");
        if (!dao.empresaExiste(idEmpresa)) throw new IllegalArgumentException("empresa no existe");
        validarPct(pct);
        dao.setEmpresa(idEmpresa, pct);
    }

    private void validarPct(BigDecimal pct) {
        if (pct == null) throw new IllegalArgumentException("porcentaje requerido");
        if (pct.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("porcentaje no puede ser negativo");
        if (pct.compareTo(new BigDecimal("100.00")) > 0) throw new IllegalArgumentException("porcentaje no puede ser > 100");
    }
}
