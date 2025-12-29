package com.vaqueras.service;

import java.sql.Connection;
import java.util.List;

import com.vaqueras.config.DatabaseConfig;
import com.vaqueras.dao.BibliotecaDAO;
import com.vaqueras.model.BibliotecaItemDTO;

public class BibliotecaService {
    
    private final DatabaseConfig db = new DatabaseConfig();
    private final BibliotecaDAO dao = new BibliotecaDAO();

    public List<BibliotecaItemDTO> listar(int idUser) {
        try (Connection conn = db.conectar()) {
            return dao.listByUser(conn, idUser);
        } catch (Exception e) {
            throw new RuntimeException("Error listando biblioteca: " + e.getMessage(), e);
        }
    }
}
