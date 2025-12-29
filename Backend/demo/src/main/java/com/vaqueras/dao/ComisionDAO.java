package com.vaqueras.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ComisionDAO {
    
    public BigDecimal getEmpresaComision(Connection conn, int idEmpresa) throws SQLException {
        String sql = """
            SELECT porcentaje_especifico
            FROM comision_empresa
            WHERE id_empresa = ?
            ORDER BY fecha_vigencia DESC, id_comision_empresa DESC
            LIMIT 1
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEmpresa);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return rs.getBigDecimal("porcentaje_especifico");
            }
        }
    }

    public BigDecimal getGlobalComision(Connection conn) throws SQLException {
        String sql = """
            SELECT porcentaje_global
            FROM comision_global
            ORDER BY fecha_vigencia DESC, id_comision_global DESC
            LIMIT 1
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            if (!rs.next()) return new BigDecimal("15.00");
            return rs.getBigDecimal("porcentaje_global");
        }
    }
}
