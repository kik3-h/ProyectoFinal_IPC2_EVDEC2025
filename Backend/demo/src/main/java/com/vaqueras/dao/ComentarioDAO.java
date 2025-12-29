package com.vaqueras.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.vaqueras.config.DatabaseConfig;
import com.vaqueras.model.ComentarioDTO;

public class ComentarioDAO {
    
    private final DatabaseConfig db = new DatabaseConfig();

    public int insert(Connection conn, int idUser, int idVideojuego, Integer idPadre, String texto, Integer calificacion) throws SQLException {
        String sql = """
            INSERT INTO comentario (id_user, id_videojuego, id_comentario_padre, texto, calificacion, texto_visible)
            VALUES (?,?,?,?,?, TRUE)
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, idUser);
            ps.setInt(2, idVideojuego);

            if (idPadre == null) ps.setNull(3, Types.INTEGER);
            else ps.setInt(3, idPadre);

            ps.setString(4, texto);
            if (calificacion == null) ps.setNull(5, Types.INTEGER);
            else ps.setInt(5, calificacion);

            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        }
        throw new SQLException("No se generó id_comentario");
    }

    public boolean comentarioExisteEnJuego(Connection conn, int idComentario, int idVideojuego) throws SQLException {
        String sql = "SELECT 1 FROM comentario WHERE id_comentario = ? AND id_videojuego = ? LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idComentario);
            ps.setInt(2, idVideojuego);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public List<ComentarioDTO> listVisibleByVideojuego(int idVideojuego) {
        String sql = """
            SELECT c.id_comentario, c.id_user, u.nickname, c.id_videojuego, c.id_comentario_padre,
                c.texto, c.calificacion, c.texto_visible, c.fecha
            FROM comentario c
            JOIN usuario u ON u.id_user = c.id_user
            WHERE c.id_videojuego = ? AND c.texto_visible = TRUE
            ORDER BY c.fecha ASC, c.id_comentario ASC
        """;

        List<ComentarioDTO> out = new ArrayList<>();

        try (Connection conn = db.conectar();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idVideojuego);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ComentarioDTO dto = new ComentarioDTO();
                    dto.setIdComentario(rs.getInt("id_comentario"));
                    dto.setIdUser(rs.getInt("id_user"));
                    dto.setNickname(rs.getString("nickname"));
                    dto.setIdVideojuego(rs.getInt("id_videojuego"));

                    int padre = rs.getInt("id_comentario_padre");
                    dto.setIdComentarioPadre(rs.wasNull() ? null : padre);

                    dto.setTexto(rs.getString("texto"));
                    int cal = rs.getInt("calificacion");
                    dto.setCalificacion(rs.wasNull() ? null : cal);

                    dto.setTextoVisible(rs.getBoolean("texto_visible"));
                    dto.setFecha(rs.getString("fecha"));
                    out.add(dto);
                }
            }
            return out;

        } catch (SQLException e) {
            throw new RuntimeException("Error listando comentarios: " + e.getMessage(), e);
        }
    }

    public double avgRatingVisible(int idVideojuego) {
        String sql = """
            SELECT AVG(calificacion) AS avg_cal
            FROM comentario
            WHERE id_videojuego = ? AND texto_visible = TRUE AND calificacion IS NOT NULL
        """;

        try (Connection conn = db.conectar();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idVideojuego);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return 0.0;
                double v = rs.getDouble("avg_cal");
                return rs.wasNull() ? 0.0 : v;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error promedio calificación: " + e.getMessage(), e);
        }
    }

    public int countRatingsVisible(int idVideojuego) {
        String sql = """
            SELECT COUNT(*) AS total
            FROM comentario
            WHERE id_videojuego = ? AND texto_visible = TRUE AND calificacion IS NOT NULL
        """;

        try (Connection conn = db.conectar();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idVideojuego);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return 0;
                return rs.getInt("total");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error conteo calificaciones: " + e.getMessage(), e);
        }
    }
}
