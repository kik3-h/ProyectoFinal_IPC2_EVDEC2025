package com.vaqueras.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map; 

import com.vaqueras.config.DatabaseConfig;

public class ReporteDAO {
    
    private final DatabaseConfig db = new DatabaseConfig();

    public Object[] resumenVentas(String desde, String hasta) {
        String sql = """
            SELECT
            COUNT(*) AS total_ventas,
            COALESCE(SUM(precio_final), 0) AS monto_total,
            COALESCE(SUM(retencion_plataforma), 0) AS ingreso_plataforma,
            COALESCE(SUM(ingreso_empresa), 0) AS ingreso_empresas
            FROM venta
            WHERE (? IS NULL OR DATE(fecha_compra) >= DATE(?))
            AND (? IS NULL OR DATE(fecha_compra) <= DATE(?))
        """;

        try (Connection conn = db.conectar();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, desde); ps.setString(2, desde);
            ps.setString(3, hasta); ps.setString(4, hasta);

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return new Object[]{
                        rs.getInt("total_ventas"),
                        rs.getBigDecimal("monto_total"),
                        rs.getBigDecimal("ingreso_plataforma"),
                        rs.getBigDecimal("ingreso_empresas")
                };
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error resumen ventas: " + e.getMessage(), e);
        }
    }

    public List<Map<String, Object>> topJuegos(int limit) {
        String sql = """
            SELECT v.id_videojuego, j.titulo,
            COUNT(*) AS total_compras,
            COALESCE(SUM(v.precio_final), 0) AS monto
            FROM venta v
            JOIN videojuego j ON j.id_videojuego = v.id_videojuego
            GROUP BY v.id_videojuego, j.titulo
            ORDER BY monto DESC, total_compras DESC
            LIMIT ?
        """;

        List<Map<String, Object>> out = new ArrayList<>();
        try (Connection conn = db.conectar();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Math.max(1, Math.min(limit, 50)));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Uso un Map para dar nombre a los campos
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("idVideojuego", rs.getInt("id_videojuego"));
                    row.put("titulo", rs.getString("titulo"));
                    row.put("totalCompras", rs.getInt("total_compras"));
                    row.put("montoGenerado", rs.getBigDecimal("monto"));
                    
                    out.add(row);
                }
            }
            return out;
        } catch (SQLException e) {
            throw new RuntimeException("Error top juegos: " + e.getMessage(), e);
        }
    }

    public List<Map<String, Object>>topEmpresas(int limit) {
        String sql = """
            SELECT v.id_empresa, e.nombre_empresa,
            COUNT(*) AS total_ventas,
            COALESCE(SUM(v.ingreso_empresa), 0) AS ingreso
            FROM venta v
            JOIN empresa e ON e.id_empresa = v.id_empresa
            GROUP BY v.id_empresa, e.nombre_empresa
            ORDER BY ingreso DESC, total_ventas DESC
            LIMIT ?
        """;

        List<Map<String, Object>> out = new ArrayList<>();
        try (Connection conn = db.conectar();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Math.max(1, Math.min(limit, 50)));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("idEmpresa", rs.getInt("id_empresa"));
                    row.put("nombreEmpresa", rs.getString("nombre_empresa"));
                    row.put("totalVentas", rs.getInt("total_ventas"));
                    row.put("ingresoTotal", rs.getBigDecimal("ingreso"));
                    
                    out.add(row);
                }
            }
            return out;
        } catch (SQLException e) {
            throw new RuntimeException("Error top empresas: " + e.getMessage(), e);
        }
    }
}
