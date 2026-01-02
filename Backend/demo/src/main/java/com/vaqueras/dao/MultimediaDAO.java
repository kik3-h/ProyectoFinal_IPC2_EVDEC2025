package com.vaqueras.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.vaqueras.config.DatabaseConfig;
import com.vaqueras.model.ImageData;
import com.vaqueras.model.MultimediaDTO;

public class MultimediaDAO {
    
    private final DatabaseConfig db = new DatabaseConfig();

    public void replaceMultimedia(Connection conn, int idVideojuego, String portadaUrl, List<String> galeriaUrls) throws SQLException {

        // borrar todas las multimedia anteriores
        try (PreparedStatement del = conn.prepareStatement("DELETE FROM multimedia WHERE id_videojuego = ?")) {
            del.setInt(1, idVideojuego);
            del.executeUpdate();
        }

        // insertar portada url 
        if (portadaUrl != null && !portadaUrl.isBlank()) {
            try (PreparedStatement ins = conn.prepareStatement(
                    "INSERT INTO multimedia (id_videojuego, url_imagen, tipo) VALUES (?,?, 'PORTADA')")) {
                ins.setInt(1, idVideojuego);
                ins.setString(2, portadaUrl);
                ins.executeUpdate();
            }
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
            SELECT id_multimedia, url_imagen, tipo,
            (imagen_blob IS NOT NULL) AS has_blob
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

                    int id = rs.getInt("id_multimedia");
                    String url = rs.getString("url_imagen");
                    boolean hasBlob = rs.getBoolean("has_blob");

                    // LOGICA HÍBRIDA
                    if (hasBlob && (url == null || url.isBlank())) {
                        url = "/vaqueras-backend/api/public/imagenes/" + id;
                    }

                    out.add(new MultimediaDTO(
                            id,
                            url,
                            rs.getString("tipo")
                    ));
                }
            }
            return out;

        } catch (SQLException e) {
            throw new RuntimeException("Error listando multimedia: " + e.getMessage(), e);
        }
    }

    // Nuevos metodos para Blob

    public int createBlob(int idVideojuego, String tipo, byte[] bytes, String mime) {
        String deleteOldCover = "DELETE FROM multimedia WHERE id_videojuego = ? AND tipo = 'PORTADA'";
        String sql = "INSERT INTO multimedia (id_videojuego, url_imagen, tipo, imagen_blob, imagen_mime) VALUES (?,?,?,?,?)";
        
        try (Connection conn = db.conectar()) {

        // ✅ si es PORTADA, borra la portada anterior del juego
        if ("PORTADA".equalsIgnoreCase(tipo)) {
            try (PreparedStatement del = conn.prepareStatement(deleteOldCover)) {
                del.setInt(1, idVideojuego);
                del.executeUpdate();
            }
        }

            try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, idVideojuego);
                ps.setString(2, ""); // URL vacía porque es blob
                ps.setString(3, tipo.toUpperCase());
                ps.setBytes(4, bytes);
                ps.setString(5, mime);

                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) return rs.getInt(1);
                }
                throw new SQLException("No se generó id_multimedia");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error guardando imagen multimedia: " + e.getMessage(), e);
        }
    }

    public ImageData findBlobById(int idMultimedia) {
        String sql = "SELECT imagen_blob, imagen_mime FROM multimedia WHERE id_multimedia = ?";
        try (Connection conn = db.conectar();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idMultimedia);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                
                byte[] blob = rs.getBytes("imagen_blob");
                String mime = rs.getString("imagen_mime");
                
                if (mime == null || mime.isBlank()) mime = "application/octet-stream";
                if (blob == null || blob.length == 0) return null;
                
                return new ImageData(blob, mime);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error leyendo imagen multimedia: " + e.getMessage(), e);
        }
    }

    public Integer findVideojuegoIdByMultimedia(int idMultimedia) {
        String sql = "SELECT id_videojuego FROM multimedia WHERE id_multimedia = ?";
        try (Connection conn = db.conectar();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMultimedia);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return rs.getInt("id_videojuego");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error buscando multimedia: " + e.getMessage(), e);
        }
    }

    public boolean deleteById(int idMultimedia) {
        String sql = "DELETE FROM multimedia WHERE id_multimedia = ?";
        try (Connection conn = db.conectar();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMultimedia);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error eliminando multimedia: " + e.getMessage(), e);
        }
    }

}
