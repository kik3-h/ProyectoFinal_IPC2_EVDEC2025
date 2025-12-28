package com.vaqueras.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.vaqueras.config.DatabaseConfig;
import com.vaqueras.model.CarteraDTO;

public class CarteraDAO {
    
    private final DatabaseConfig db = new DatabaseConfig();

    public void ensureExists(Connection conn, int idUser) throws SQLException {
        String check = "SELECT 1 FROM cartera WHERE id_user = ? LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(check)) {
            ps.setInt(1, idUser);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return;
            }
        }

        String ins = "INSERT INTO cartera (id_user, saldo_actual) VALUES (?, 0.00)";
        try (PreparedStatement ps = conn.prepareStatement(ins)) {
            ps.setInt(1, idUser);
            ps.executeUpdate();
        }
    }

    public CarteraDTO findByUser(int idUser) {
        String sql = """
            SELECT id_user, saldo_actual, ultima_actualizacion
            FROM cartera
            WHERE id_user = ?
        """;

        try (Connection conn = db.conectar();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUser);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return new CarteraDTO(
                        rs.getInt("id_user"),
                        rs.getBigDecimal("saldo_actual"),
                        rs.getString("ultima_actualizacion")
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error obteniendo cartera: " + e.getMessage(), e);
        }
    }

    public BigDecimal getSaldo(Connection conn, int idUser) throws SQLException {
        String sql = "SELECT saldo_actual FROM cartera WHERE id_user = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUser);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return rs.getBigDecimal("saldo_actual");
            }
        }
    }

    public void addSaldo(Connection conn, int idUser, BigDecimal monto) throws SQLException {
        String sql = "UPDATE cartera SET saldo_actual = saldo_actual + ? WHERE id_user = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, monto);
            ps.setInt(2, idUser);
            ps.executeUpdate();
        }
    }
}
