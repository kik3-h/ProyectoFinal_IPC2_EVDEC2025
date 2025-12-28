package com.vaqueras.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.vaqueras.config.DatabaseConfig;
import com.vaqueras.model.RecargaDTO;

public class RecargaCarteraDAO {
    
    private final DatabaseConfig db = new DatabaseConfig();

    public void insert(Connection conn, int idUser, BigDecimal monto) throws SQLException {
        String sql = "INSERT INTO recarga_cartera (id_user, monto_recargado) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUser);
            ps.setBigDecimal(2, monto);
            ps.executeUpdate();
        }
    }

    public List<RecargaDTO> listByUser(int idUser, int limit) {
        String sql = """
            SELECT id_recarga, monto_recargado, fecha_recarga
            FROM recarga_cartera
            WHERE id_user = ?
            ORDER BY fecha_recarga DESC, id_recarga DESC
            LIMIT ?
        """;

        List<RecargaDTO> out = new ArrayList<>();

        try (Connection conn = db.conectar();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUser);
            ps.setInt(2, Math.max(1, Math.min(limit, 200)));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new RecargaDTO(
                            rs.getInt("id_recarga"),
                            rs.getBigDecimal("monto_recargado"),
                            rs.getString("fecha_recarga")
                    ));
                }
            }
            return out;

        } catch (SQLException e) {
            throw new RuntimeException("Error listando recargas: " + e.getMessage(), e);
        }
    }
}
