package com.vaqueras.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.vaqueras.config.DatabaseConfig;

public class UsuarioEmpresaDAO {
    private final DatabaseConfig db = new DatabaseConfig();

    public boolean isUsuarioLigadoAEmpresa(int idUser, int idEmpresa) {
        String sql = "SELECT 1 FROM usuario_empresa WHERE id_user = ? AND id_empresa = ? LIMIT 1";
        try (Connection conn = db.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUser);
            ps.setInt(2, idEmpresa);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error verificando dueño empresa: " + e.getMessage(), e);
        }
    }

    // para transacción en el create
    public void link(Connection conn, int idUser, int idEmpresa, String cargo) throws SQLException {
    String sql = "INSERT INTO usuario_empresa (id_user, id_empresa, cargo) VALUES (?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUser);
            ps.setInt(2, idEmpresa);
            ps.setString(3, cargo);
            ps.executeUpdate();
        }
    }

    public Integer findFirstEmpresaIdByUser(int idUser) {
    String sql = "SELECT id_empresa FROM usuario_empresa WHERE id_user = ? ORDER BY id_usuario_empresa ASC LIMIT 1";
    try (Connection conn = db.conectar();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, idUser);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt("id_empresa");
        }
        return null;
    } catch (SQLException e) {
        throw new RuntimeException("Error obteniendo empresa del usuario: " + e.getMessage(), e);
    }
}
}
