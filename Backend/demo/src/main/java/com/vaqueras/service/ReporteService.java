package com.vaqueras.service;

import java.util.List;
import java.util.Map;

import com.vaqueras.dao.ReporteDAO;

public class ReporteService {
    
    private final ReporteDAO dao = new ReporteDAO();

    public Object[] resumenVentas(String desde, String hasta) {
        if (desde != null && !desde.matches("\\d{4}-\\d{2}-\\d{2}")) throw new IllegalArgumentException("desde inválido");
        if (hasta != null && !hasta.matches("\\d{4}-\\d{2}-\\d{2}")) throw new IllegalArgumentException("hasta inválido");
        return dao.resumenVentas(desde, hasta);
    }

    
    public List<Map<String, Object>> topJuegos(int limit) { 
        return dao.topJuegos(limit); 
    }

   
    public List<Map<String, Object>> topEmpresas(int limit) { 
        return dao.topEmpresas(limit); 
    }
    
}
