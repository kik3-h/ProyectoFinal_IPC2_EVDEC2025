package com.vaqueras.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.vaqueras.config.DatabaseConfig;
import com.vaqueras.model.Empresa;

public class EmpresaDAO {
    private final DatabaseConfig db = new DatabaseConfig();

    public List<Empresa> findAll() {
        String sql = """
            SELECT id_empresa, nombre_empresa, email, descripcion, fecha_afiliacion
            FROM empresa
            ORDER BY nombre_empresa
        """;
        List<Empresa> list = new ArrayList<>();

        try (Connection conn = db.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Empresa e = map(rs);
                list.add(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listando empresas: " + e.getMessage(), e);
        }
        return list;
    }

    public Empresa findById(int idEmpresa) {
        String sql = """
            SELECT id_empresa, nombre_empresa, email, descripcion, fecha_afiliacion
            FROM empresa
            WHERE id_empresa = ?
        """;
        try (Connection conn = db.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idEmpresa);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error buscando empresa: " + e.getMessage(), e);
        }
        return null;
    }

    public boolean updatePartial(int idEmpresa, String nombreEmpresa, String email, String descripcion) {
        StringBuilder sql = new StringBuilder("UPDATE empresa SET ");
        List<Object> params = new ArrayList<>();

        if (nombreEmpresa != null) { sql.append("nombre_empresa = ?, "); params.add(nombreEmpresa); }
        if (email != null)        { sql.append("email = ?, ");         params.add(email); }
        if (descripcion != null)  { sql.append("descripcion = ?, ");   params.add(descripcion); }

        if (params.isEmpty()) return false;

        sql.setLength(sql.length() - 2);
        sql.append(" WHERE id_empresa = ?");
        params.add(idEmpresa);

        try (Connection conn = db.conectar();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error actualizando empresa: " + e.getMessage(), e);
        }
    }

    private Empresa map(ResultSet rs) throws SQLException {
        Empresa e = new Empresa();
        e.setIdEmpresa(rs.getInt("id_empresa"));
        e.setNombreEmpresa(rs.getString("nombre_empresa"));
        e.setEmail(rs.getString("email"));
        e.setDescripcion(rs.getString("descripcion"));
        e.setFechaAfiliacion(rs.getString("fecha_afiliacion"));
        return e;
    }
}
