package com.vaqueras.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.vaqueras.config.DatabaseConfig;

public class AdminCategoriaDAO {
    
    private final DatabaseConfig db = new DatabaseConfig();

    public List<Object[]> list() {
        String sql = "SELECT id_categoria, nombre, descripcion FROM categoria ORDER BY nombre ASC";
        List<Object[]> out = new ArrayList<>();

        try (Connection conn = db.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                out.add(new Object[]{
                        rs.getInt("id_categoria"),
                        rs.getString("nombre"),
                        rs.getString("descripcion")
                });
            }
            return out;

        } catch (SQLException e) {
            throw new RuntimeException("Error listando categorías: " + e.getMessage(), e);
        }
    }

    public int insert(String nombre, String descripcion) throws SQLException {
        String sql = "INSERT INTO categoria (nombre, descripcion) VALUES (?,?)";
        try (Connection conn = db.conectar();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        }
        throw new SQLException("No se generó id_categoria");
    }

    public void update(int id, String nombre, String descripcion) throws SQLException {
        String sql = "UPDATE categoria SET nombre = ?, descripcion = ? WHERE id_categoria = ?";
        try (Connection conn = db.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.setInt(3, id);
            int n = ps.executeUpdate();
            if (n == 0) throw new SQLException("Categoría no encontrada");
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM categoria WHERE id_categoria = ?";
        try (Connection conn = db.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public boolean exists(int id) {
        String sql = "SELECT 1 FROM categoria WHERE id_categoria = ? LIMIT 1";
        try (Connection conn = db.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException e) {
            throw new RuntimeException("Error validando categoría: " + e.getMessage(), e);
        }
    }
}
