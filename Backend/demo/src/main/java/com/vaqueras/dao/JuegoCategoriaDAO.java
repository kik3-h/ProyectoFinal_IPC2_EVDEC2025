package com.vaqueras.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class JuegoCategoriaDAO {
    
    public void replaceCategorias(Connection conn, int idVideojuego, List<Integer> categoriaIds) throws SQLException {
        // borrar existentes
        try (PreparedStatement del = conn.prepareStatement("DELETE FROM juego_categoria WHERE id_videojuego = ?")) {
            del.setInt(1, idVideojuego);
            del.executeUpdate();
        }

        if (categoriaIds == null || categoriaIds.isEmpty()) return;

        String insSql = "INSERT INTO juego_categoria (id_videojuego, id_categoria) VALUES (?,?)";
        try (PreparedStatement ins = conn.prepareStatement(insSql)) {
            for (Integer idCat : categoriaIds) {
                ins.setInt(1, idVideojuego);
                ins.setInt(2, idCat);
                ins.addBatch();
            }
            ins.executeBatch();
        }
    }
}
