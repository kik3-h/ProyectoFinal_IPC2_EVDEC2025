package com.vaqueras.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.vaqueras.config.DatabaseConfig;
import com.vaqueras.model.Categoria;

public class VideojuegoReadDAO {
    
    private final DatabaseConfig db = new DatabaseConfig();

    public List<Categoria> findCategoriasByVideojuego(int idVideojuego) {
        String sql = """
            SELECT c.id_categoria, c.nombre, c.descripcion
            FROM juego_categoria jc
            JOIN categoria c ON c.id_categoria = jc.id_categoria
            WHERE jc.id_videojuego = ?
            ORDER BY c.nombre ASC
        """;

        List<Categoria> out = new ArrayList<>();

        try (Connection conn = db.conectar();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idVideojuego);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Categoria c = new Categoria();
                    c.setIdCategoria(rs.getInt("id_categoria"));
                    c.setNombre(rs.getString("nombre"));
                    c.setDescripcion(rs.getString("descripcion"));
                    out.add(c);
                }
            }
            return out;

        } catch (SQLException e) {
            throw new RuntimeException("Error listando categorias del juego: " + e.getMessage(), e);
        }
    }
}
