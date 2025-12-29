package com.vaqueras.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.vaqueras.config.DatabaseConfig;
import com.vaqueras.model.GrupoDTO;

public class GrupoFamiliarDAO {
    
    private final DatabaseConfig db = new DatabaseConfig();

    public int create(Connection conn, String nombreGrupo, int idAdminUser) throws SQLException {
        String sql = "INSERT INTO grupo_familiar (nombre_grupo, id_admin_user) VALUES (?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nombreGrupo);
            ps.setInt(2, idAdminUser);
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        }
        throw new SQLException("No se generó id_grupo");
    }

    public boolean isAdmin(int idGrupo, int idUser) {
        String sql = "SELECT 1 FROM grupo_familiar WHERE id_grupo = ? AND id_admin_user = ? LIMIT 1";
        try (Connection conn = db.conectar();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idGrupo);
            ps.setInt(2, idUser);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error validando admin grupo: " + e.getMessage(), e);
        }
    }

    public List<GrupoDTO> listByUser(int idUser) {
        String sql = """
            SELECT DISTINCT g.id_grupo, g.nombre_grupo, g.id_admin_user, u.nickname AS admin_nick, g.fecha_creacion
            FROM grupo_familiar g
            JOIN usuario u ON u.id_user = g.id_admin_user
            LEFT JOIN miembro_grupo mg ON mg.id_grupo = g.id_grupo
            WHERE g.id_admin_user = ? OR mg.id_user = ?
            ORDER BY g.fecha_creacion DESC, g.id_grupo DESC
        """;

        List<GrupoDTO> out = new ArrayList<>();
        try (Connection conn = db.conectar();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUser);
            ps.setInt(2, idUser);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    GrupoDTO dto = new GrupoDTO();
                    dto.setIdGrupo(rs.getInt("id_grupo"));
                    dto.setNombreGrupo(rs.getString("nombre_grupo"));
                    dto.setIdAdminUser(rs.getInt("id_admin_user"));
                    dto.setAdminNickname(rs.getString("admin_nick"));
                    dto.setFechaCreacion(rs.getString("fecha_creacion"));
                    out.add(dto);
                }
            }
            return out;

        } catch (SQLException e) {
            throw new RuntimeException("Error listando grupos: " + e.getMessage(), e);
        }
    }
    //agruegue esto por mejora en proceso de instalacion compartida con limite de 2
    public List<Integer> listIdsByUser(Connection conn, int idUser) throws SQLException {
    String sql = """
        SELECT DISTINCT g.id_grupo
        FROM grupo_familiar g
        LEFT JOIN miembro_grupo mg ON mg.id_grupo = g.id_grupo
        WHERE g.id_admin_user = ? OR mg.id_user = ?
        ORDER BY g.id_grupo DESC
    """;

    List<Integer> out = new ArrayList<>();
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, idUser);
        ps.setInt(2, idUser);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(rs.getInt("id_grupo"));
        }
    }
    return out;
    }
}
