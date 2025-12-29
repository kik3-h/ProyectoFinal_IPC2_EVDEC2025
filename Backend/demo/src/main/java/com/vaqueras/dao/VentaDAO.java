package com.vaqueras.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class VentaDAO {
    
    public int insert(Connection conn, int idUser, int idEmpresa, int idVideojuego, Timestamp fechaCompra,
                    BigDecimal precioFinal, BigDecimal retencion, BigDecimal ingresoEmpresa,
                    String tipoComision, BigDecimal porcentajeAplicado) throws SQLException {

        String sql = """
            INSERT INTO venta
            (id_user, id_empresa, id_videojuego, fecha_compra, precio_final, retencion_plataforma,
            ingreso_empresa, tipo_comision, porcentaje_aplicado)
            VALUES (?,?,?,?,?,?,?,?,?)
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, idUser);
            ps.setInt(2, idEmpresa);
            ps.setInt(3, idVideojuego);
            ps.setTimestamp(4, fechaCompra);
            ps.setBigDecimal(5, precioFinal);
            ps.setBigDecimal(6, retencion);
            ps.setBigDecimal(7, ingresoEmpresa);
            ps.setString(8, tipoComision);
            ps.setBigDecimal(9, porcentajeAplicado);
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        }
        throw new SQLException("No se generó id_venta");
    }
}
