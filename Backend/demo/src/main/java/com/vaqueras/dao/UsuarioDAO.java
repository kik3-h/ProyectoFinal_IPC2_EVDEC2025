package com.vaqueras.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.vaqueras.config.DatabaseConfig;
import com.vaqueras.model.Usuario;

public class UsuarioDAO {
    private final DatabaseConfig dbConfig = new DatabaseConfig(); //genero la conexion a la DB
    public boolean existsByEmailOrNickname(String email, String nickname) { // verifica si un usuario ya existe por email o nickname
        String sql = """
            SELECT 1 FROM usuario
            WHERE email = ? OR nickname = ?
        """;

        try (Connection con = dbConfig.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, nickname);
            ResultSet  rs = ps.executeQuery();  
            return rs.next();
            //return ps.executeQuery().next();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void save(Usuario u) {
        String sql = """
            INSERT INTO usuario
            (nickname, email, password, telefono, fecha_nacimiento, pais, rol, estado_cuenta)
            VALUES (?, ?, ?, ?, ?, ?, ?, 'ACTIVO')
        """;

        try (Connection con = dbConfig.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getNickname());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getPassword());
            ps.setString(4, u.getTelefono());
            ps.setDate(5, Date.valueOf(u.getFechaNacimiento()));
            ps.setString(6, u.getPais());
            ps.setString(7, u.getRol());

            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Usuario findForLogin(String identifier) {
        String sql = """
            SELECT * FROM usuario
            WHERE (email = ? OR nickname = ?)
            AND estado_cuenta = 'ACTIVO'
        """;

        try (Connection con = dbConfig.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, identifier);
            ps.setString(2, identifier);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUser(rs.getInt("id_user"));
                u.setNickname(rs.getString("nickname"));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));
                u.setRol(rs.getString("rol"));
                return u;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    //método para obtener todos los usuarios
    public List<Usuario> findAll() {
    String sql = """
        SELECT id_user, nickname, email, telefono, fecha_nacimiento, pais, rol, estado_cuenta
        FROM usuario
        ORDER BY id_user DESC
    """;

    List<Usuario> out = new ArrayList<>();

    try (Connection con = dbConfig.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Usuario u = new Usuario();
            u.setIdUser(rs.getInt("id_user"));
            u.setNickname(rs.getString("nickname"));
            u.setEmail(rs.getString("email"));
            u.setTelefono(rs.getString("telefono"));
            u.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
            u.setPais(rs.getString("pais"));
            u.setRol(rs.getString("rol"));
            u.setEstadoCuenta(rs.getString("estado_cuenta"));
            out.add(u);
        }
        return out;

    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}

public Usuario findById(int id) {
    String sql = """
        SELECT id_user, nickname, email, telefono, fecha_nacimiento, pais, rol, estado_cuenta
        FROM usuario
        WHERE id_user = ?
    """;

    try (Connection con = dbConfig.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            Usuario u = new Usuario();
            u.setIdUser(rs.getInt("id_user"));
            u.setNickname(rs.getString("nickname"));
            u.setEmail(rs.getString("email"));
            u.setTelefono(rs.getString("telefono"));
            u.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
            u.setPais(rs.getString("pais"));
            u.setRol(rs.getString("rol"));
            u.setEstadoCuenta(rs.getString("estado_cuenta"));
            return u;
        }
        return null;

    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}

public boolean updateEstadoCuenta(int idUser, String estado) {
    String sql = "UPDATE usuario SET estado_cuenta = ? WHERE id_user = ?";

   try (java.sql.Connection con = dbConfig.conectar();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, estado);
            ps.setInt(2, idUser);

            int rows = ps.executeUpdate();
            return rows > 0; // Retorna true si actualizó al menos una fila

        } catch (Exception e) {
            e.printStackTrace(); 
            throw new RuntimeException("Error SQL al actualizar estado: " + e.getMessage());
        }

    }
}