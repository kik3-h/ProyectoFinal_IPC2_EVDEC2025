package com.vaqueras.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.vaqueras.config.DatabaseConfig;
import com.vaqueras.model.Categoria;

public class CategoriaDAO {
    private final DatabaseConfig dbConfig = new DatabaseConfig();

    public List<Categoria> findAll() {
        String sql = """
            SELECT id_categoria, nombre, descripcion
            FROM categoria
            ORDER BY nombre ASC
        """;

        List<Categoria> out = new ArrayList<>();

        try (Connection con = dbConfig.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Categoria c = new Categoria();
                c.setIdCategoria(rs.getInt("id_categoria"));
                c.setNombre(rs.getString("nombre"));
                c.setDescripcion(rs.getString("descripcion"));
                out.add(c);
            }
            return out;

        } catch (Exception e) {
            throw new RuntimeException("Error al listar categorias: " + e.getMessage(), e);
        }
    }

    public Categoria findById(int id) {
        String sql = """
            SELECT id_categoria, nombre, descripcion
            FROM categoria
            WHERE id_categoria = ?
        """;

        try (Connection con = dbConfig.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Categoria c = new Categoria();
                    c.setIdCategoria(rs.getInt("id_categoria"));
                    c.setNombre(rs.getString("nombre"));
                    c.setDescripcion(rs.getString("descripcion"));
                    return c;
                }
            }
            return null;

        } catch (Exception e) {
            throw new RuntimeException("Error al obtener categoria: " + e.getMessage(), e);
        }
    }

    public boolean existsByNombre(String nombre) {
        String sql = "SELECT 1 FROM categoria WHERE nombre = ?";

        try (Connection con = dbConfig.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al validar nombre categoria: " + e.getMessage(), e);
        }
    }

    public boolean existsByNombreExceptId(String nombre, int excludeId) {
        String sql = "SELECT 1 FROM categoria WHERE nombre = ? AND id_categoria <> ?";

        try (Connection con = dbConfig.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setInt(2, excludeId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al validar nombre categoria: " + e.getMessage(), e);
        }
    }

    public int create(Categoria c) {
        String sql = "INSERT INTO categoria (nombre, descripcion) VALUES (?, ?)";

        try (Connection con = dbConfig.conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, c.getNombre());
            ps.setString(2, c.getDescripcion());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
            return 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new IllegalArgumentException("Ya existe una categoría con ese nombre");
        } catch (Exception e) {
            throw new RuntimeException("Error al crear categoria: " + e.getMessage(), e);
        }
    }

    public boolean update(Categoria c) {
        String sql = "UPDATE categoria SET nombre = ?, descripcion = ? WHERE id_categoria = ?";

        try (Connection con = dbConfig.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getNombre());
            ps.setString(2, c.getDescripcion());
            ps.setInt(3, c.getIdCategoria());

            return ps.executeUpdate() > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new IllegalArgumentException("Ya existe una categoría con ese nombre");
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar categoria: " + e.getMessage(), e);
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM categoria WHERE id_categoria = ?";

        try (Connection con = dbConfig.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            // FK juego_categoria ON DELETE RESTRICT
            throw new IllegalStateException("No se puede eliminar: la categoría está asociada a videojuegos");
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar categoria: " + e.getMessage(), e);
        }
    }
}
