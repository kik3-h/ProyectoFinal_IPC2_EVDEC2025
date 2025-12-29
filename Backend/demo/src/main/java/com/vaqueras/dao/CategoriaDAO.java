package com.vaqueras.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException; // Usamos SQLException estándar
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.vaqueras.config.DatabaseConfig;
import com.vaqueras.model.Categoria;

public class CategoriaDAO {
    
    private final DatabaseConfig dbConfig = new DatabaseConfig();

    // 1. LISTAR TODAS (Usado por Admin y Públicos)
    public List<Categoria> findAll() {
        String sql = "SELECT id_categoria, nombre, descripcion FROM categoria ORDER BY nombre ASC";
        List<Categoria> out = new ArrayList<>();

        try (Connection con = dbConfig.conectar();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                out.add(new Categoria(
                    rs.getInt("id_categoria"),
                    rs.getString("nombre"),
                    rs.getString("descripcion")
                ));
            }
            return out;
        } catch (Exception e) {
            throw new RuntimeException("Error al listar categorias: " + e.getMessage(), e);
        }
    }

    // 2. BUSCAR POR ID
    public Categoria findById(int id) {
        String sql = "SELECT id_categoria, nombre, descripcion FROM categoria WHERE id_categoria = ?";
        try (Connection con = dbConfig.conectar();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Categoria(
                        rs.getInt("id_categoria"),
                        rs.getString("nombre"),
                        rs.getString("descripcion")
                    );
                }
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener categoria: " + e.getMessage(), e);
        }
    }

    // 3. CREAR (Con retorno de ID generado)
    public int insert(Categoria c) {
        String sql = "INSERT INTO categoria (nombre, descripcion) VALUES (?, ?)";
        try (Connection con = dbConfig.conectar();
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, c.getNombre());
            ps.setString(2, c.getDescripcion());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
            return 0; // Si no generó ID

        } catch (SQLException e) {
            // Manejo específico de duplicados
            if (e.getMessage().contains("Duplicate entry")) {
                throw new IllegalArgumentException("Ya existe una categoría con ese nombre");
            }
            throw new RuntimeException("Error al crear categoria: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error general al crear categoria: " + e.getMessage(), e);
        }
    }

    // 4. ACTUALIZAR
    public boolean update(Categoria c) {
        String sql = "UPDATE categoria SET nombre = ?, descripcion = ? WHERE id_categoria = ?";
        try (Connection con = dbConfig.conectar();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getNombre());
            ps.setString(2, c.getDescripcion());
            ps.setInt(3, c.getIdCategoria());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                throw new IllegalArgumentException("Ya existe una categoría con ese nombre");
            }
            throw new RuntimeException("Error al actualizar categoria: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error general al actualizar: " + e.getMessage(), e);
        }
    }

    // 5. ELIMINAR
    public boolean delete(int id) {
        String sql = "DELETE FROM categoria WHERE id_categoria = ?";
        try (Connection con = dbConfig.conectar();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            // Manejo de restricción de llave foránea (FK)
            // Error code 1451 en MySQL suele ser foreign key constraint fail
            if (e.getErrorCode() == 1451 || e.getMessage().contains("foreign key")) {
                throw new IllegalStateException("No se puede eliminar: la categoría está asociada a videojuegos");
            }
            throw new RuntimeException("Error al eliminar categoria: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error general al eliminar: " + e.getMessage(), e);
        }
    }
}