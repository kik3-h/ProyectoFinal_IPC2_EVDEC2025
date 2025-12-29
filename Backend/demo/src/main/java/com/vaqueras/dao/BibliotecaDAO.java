package com.vaqueras.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.vaqueras.model.BibliotecaItemDTO;

public class BibliotecaDAO {
    
    public boolean exists(Connection conn, int idUser, int idVideojuego) throws SQLException {
        String sql = "SELECT 1 FROM biblioteca WHERE id_user = ? AND id_videojuego = ? LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUser);
            ps.setInt(2, idVideojuego);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void insert(Connection conn, int idUser, int idVideojuego) throws SQLException {
        String sql = """
            INSERT INTO biblioteca (id_user, id_videojuego, estado_instalacion, es_propietario)
            VALUES (?, ?, 'NO_INSTALADO', TRUE)
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUser);
            ps.setInt(2, idVideojuego);
            ps.executeUpdate();
        }
    }

    public List<BibliotecaItemDTO> listByUser(Connection conn, int idUser) throws SQLException {
        String sql = """
            SELECT b.id_biblioteca, b.id_user, b.id_videojuego, v.titulo, b.fecha_adquisicion,
                b.estado_instalacion, b.es_propietario,
                m.url_imagen AS portada_url
            FROM biblioteca b
            JOIN videojuego v ON v.id_videojuego = b.id_videojuego
            LEFT JOIN multimedia m ON m.id_videojuego = v.id_videojuego AND m.tipo = 'PORTADA'
            WHERE b.id_user = ?
            ORDER BY b.fecha_adquisicion DESC, b.id_biblioteca DESC
        """;

        List<BibliotecaItemDTO> out = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUser);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new BibliotecaItemDTO(
                            rs.getInt("id_biblioteca"),
                            rs.getInt("id_user"),
                            rs.getInt("id_videojuego"),
                            rs.getString("titulo"),
                            rs.getString("fecha_adquisicion"),
                            rs.getString("estado_instalacion"),
                            rs.getBoolean("es_propietario"),
                            rs.getString("portada_url")
                    ));
                }
            }
        }
        return out;
    }
}
