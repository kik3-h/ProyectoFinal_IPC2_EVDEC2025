package com.vaqueras.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.vaqueras.config.DatabaseConfig;
import com.vaqueras.dao.AdminCategoriaDAO;
import com.vaqueras.dao.JuegoCategoriaDAO; // (asumiendo que ya lo tienes por commits previos)

public class ModeracionService {
    
    private final DatabaseConfig db = new DatabaseConfig();
    private final AdminCategoriaDAO categoriaDAO = new AdminCategoriaDAO();
    private final JuegoCategoriaDAO juegoCatDAO = new JuegoCategoriaDAO();

    public void reemplazarCategorias(int idVideojuego, List<Integer> categorias) {
        if (idVideojuego <= 0) throw new IllegalArgumentException("idVideojuego inválido");
        if (categorias == null || categorias.isEmpty()) throw new IllegalArgumentException("categorias requeridas");

        for (Integer c : categorias) {
            if (c == null || c <= 0) throw new IllegalArgumentException("idCategoria inválido en lista");
            if (!categoriaDAO.exists(c)) throw new IllegalArgumentException("Categoría no existe: " + c);
        }

        Connection conn = null;
        try {
            conn = db.conectar();
            conn.setAutoCommit(false);

            juegoCatDAO.replaceCategorias(conn, idVideojuego, categorias);

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) { try { conn.rollback(); } catch (SQLException ignored) {} }
            throw new RuntimeException("Error moderando categorías: " + e.getMessage(), e);
        } finally {
            if (conn != null) { try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ignored) {} }
        }
    }
}
