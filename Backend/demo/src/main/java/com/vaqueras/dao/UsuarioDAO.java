package com.vaqueras.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
}