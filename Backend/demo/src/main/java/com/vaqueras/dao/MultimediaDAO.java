package com.vaqueras.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.vaqueras.config.DatabaseConfig;
import com.vaqueras.model.MultimediaDTO;

public class MultimediaDAO {
    
    private final DatabaseConfig db = new DatabaseConfig();

    public void replaceMultimedia(Connection conn, int idVideojuego, String portadaUrl, List<String> galeriaUrls) throws SQLException {

        // borrar todas las multimedia anteriores
        try (PreparedStatement del = conn.prepareStatement("DELETE FROM multimedia WHERE id_videojuego = ?")) {
            del.setInt(1, idVideojuego);
            del.executeUpdate();
        }

        // insertar portada
        try (PreparedStatement ins = conn.prepareStatement(
                "INSERT INTO multimedia (id_videojuego, url_imagen, tipo) VALUES (?,?, 'PORTADA')")) {
            ins.setInt(1, idVideojuego);
            ins.setString(2, portadaUrl);
            ins.executeUpdate();
        }

        if (galeriaUrls == null || galeriaUrls.isEmpty()) return;

        try (PreparedStatement ins = conn.prepareStatement(
                "INSERT INTO multimedia (id_videojuego, url_imagen, tipo) VALUES (?,?, 'GALERIA')")) {

            for (String url : galeriaUrls) {
                ins.setInt(1, idVideojuego);
                ins.setString(2, url);
                ins.addBatch();
            }
            ins.executeBatch();
        }
    }

    public List<MultimediaDTO> findByVideojuego(int idVideojuego) {
        String sql = """
            SELECT id_multimedia, url_imagen, tipo
            FROM multimedia
            WHERE id_videojuego = ?
            ORDER BY
              CASE tipo WHEN 'PORTADA' THEN 1 WHEN 'GALERIA' THEN 2 ELSE 3 END,
              id_multimedia ASC
        """;

        List<MultimediaDTO> out = new ArrayList<>();

        try (Connection conn = db.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idVideojuego);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new MultimediaDTO(
                            rs.getInt("id_multimedia"),
                            rs.getString("url_imagen"),
                            rs.getString("tipo")
                    ));
                }
            }
            return out;

        } catch (SQLException e) {
            throw new RuntimeException("Error listando multimedia: " + e.getMessage(), e);
        }
    }
}
