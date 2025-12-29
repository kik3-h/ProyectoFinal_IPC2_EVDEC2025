package com.vaqueras.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CompraDAO {
    
    public static class JuegoCompraInfo {
        public int idEmpresa;
        public BigDecimal precio;
        public String estado;
        public String clasificacionEdad;
        public int edadMinima;
    }

    public JuegoCompraInfo getJuegoInfo(Connection conn, int idVideojuego) throws SQLException {
        String sql = """
            SELECT id_empresa, precio, estado, clasificacion_edad, edad_minima
            FROM videojuego
            WHERE id_videojuego = ?
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idVideojuego);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                JuegoCompraInfo info = new JuegoCompraInfo();
                info.idEmpresa = rs.getInt("id_empresa");
                info.precio = rs.getBigDecimal("precio");
                info.estado = rs.getString("estado");
                info.clasificacionEdad = rs.getString("clasificacion_edad");
                info.edadMinima = rs.getInt("edad_minima");
                return info;
            }
        }
    }
}
