package com.vaqueras.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.vaqueras.config.DatabaseConfig;

public class AdminComisionDAO {
    
    private final DatabaseConfig db = new DatabaseConfig();

    public BigDecimal getGlobalActual() {
        String sql = """
            SELECT porcentaje_global
            FROM comision_global
            ORDER BY fecha_vigencia DESC, id_comision_global DESC
            LIMIT 1
        """;
        try (Connection conn = db.conectar();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            if (!rs.next()) return new BigDecimal("15.00");
            return rs.getBigDecimal("porcentaje_global");
        } catch (SQLException e) {
            throw new RuntimeException("Error obteniendo comisión global: " + e.getMessage(), e);
        }
    }

    public void setGlobal(BigDecimal porcentaje) throws SQLException {
        String sql = "INSERT INTO comision_global (porcentaje_global) VALUES (?)";
        try (Connection conn = db.conectar();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, porcentaje);
            ps.executeUpdate();
        }
    }

    public BigDecimal getEmpresaActual(int idEmpresa) {
        String sql = """
            SELECT porcentaje_especifico
            FROM comision_empresa
            WHERE id_empresa = ?
            ORDER BY fecha_vigencia DESC, id_comision_empresa DESC
            LIMIT 1
        """;
        try (Connection conn = db.conectar();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEmpresa);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return rs.getBigDecimal("porcentaje_especifico");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error obteniendo comisión empresa: " + e.getMessage(), e);
        }
    }

    public void setEmpresa(int idEmpresa, BigDecimal porcentaje) throws SQLException {
        String sql = "INSERT INTO comision_empresa (id_empresa, porcentaje_especifico) VALUES (?,?)";
        try (Connection conn = db.conectar();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEmpresa);
            ps.setBigDecimal(2, porcentaje);
            ps.executeUpdate();
        }
    }

    public boolean empresaExiste(int idEmpresa) {
        String sql = "SELECT 1 FROM empresa WHERE id_empresa = ? LIMIT 1";
        try (Connection conn = db.conectar();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEmpresa);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException e) {
            throw new RuntimeException("Error validando empresa: " + e.getMessage(), e);
        }
    }
}
