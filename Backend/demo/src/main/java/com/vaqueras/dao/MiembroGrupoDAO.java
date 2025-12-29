package com.vaqueras.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.vaqueras.config.DatabaseConfig;
import com.vaqueras.model.GrupoMemberDTO;

public class MiembroGrupoDAO {
    
    private final DatabaseConfig db = new DatabaseConfig();

    public void addMember(Connection conn, int idGrupo, int idUser) throws SQLException {
        String sql = "INSERT INTO miembro_grupo (id_grupo, id_user) VALUES (?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idGrupo);
            ps.setInt(2, idUser);
            ps.executeUpdate();
        }
    }

    public boolean isMember(int idGrupo, int idUser) {
        String sql = "SELECT 1 FROM miembro_grupo WHERE id_grupo = ? AND id_user = ? LIMIT 1";
        try (Connection conn = db.conectar();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idGrupo);
            ps.setInt(2, idUser);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error validando miembro: " + e.getMessage(), e);
        }
    }

    public List<GrupoMemberDTO> listMembers(int idGrupo) {
        String sql = """
            SELECT u.id_user, u.nickname, mg.fecha_ingreso
            FROM miembro_grupo mg
            JOIN usuario u ON u.id_user = mg.id_user
            WHERE mg.id_grupo = ?
            ORDER BY mg.fecha_ingreso ASC, u.nickname ASC
        """;
        List<GrupoMemberDTO> out = new ArrayList<>();

        try (Connection conn = db.conectar();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idGrupo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new GrupoMemberDTO(
                            rs.getInt("id_user"),
                            rs.getString("nickname"),
                            rs.getString("fecha_ingreso")
                    ));
                }
            }
            return out;

        } catch (SQLException e) {
            throw new RuntimeException("Error listando miembros: " + e.getMessage(), e);
        }
    }

    public int countInstaladosEnGrupo(Connection conn, int idGrupo, int idVideojuego) throws SQLException {
        String sql = """
            SELECT COUNT(*) AS total
            FROM biblioteca b
            JOIN miembro_grupo mg ON mg.id_user = b.id_user
            WHERE mg.id_grupo = ?
            AND b.id_videojuego = ?
            AND b.estado_instalacion = 'INSTALADO'
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idGrupo);
            ps.setInt(2, idVideojuego);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return 0;
                return rs.getInt("total");
            }
        }
    }
    //agrego este metodo para contar instalaciones prestadas en grupo
    public int countInstaladosPrestadosEnGrupo(Connection conn, int idGrupo, int idVideojuego) throws SQLException {
    String sql = """
        SELECT COUNT(*) AS total
        FROM biblioteca b
        JOIN miembro_grupo mg ON mg.id_user = b.id_user
        WHERE mg.id_grupo = ?
        AND b.id_videojuego = ?
        AND b.estado_instalacion = 'INSTALADO'
        AND b.es_propietario = FALSE
    """;
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, idGrupo);
        ps.setInt(2, idVideojuego);
        try (ResultSet rs = ps.executeQuery()) {
            if (!rs.next()) return 0;
            return rs.getInt("total");
        }
    }
}
}
