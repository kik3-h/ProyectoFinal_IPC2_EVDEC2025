package com.vaqueras.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.vaqueras.config.DatabaseConfig;
import com.vaqueras.model.BannerPrincipal;

public class BannerDAO {
    private final DatabaseConfig dbConfig = new DatabaseConfig();

    public List<BannerPrincipal> findActive() {
        String sql = """
            SELECT id_banner, id_videojuego, imagen_url, posicion, activo
            FROM banner_principal
            WHERE activo = TRUE
            ORDER BY posicion ASC, id_banner ASC
        """;

        List<BannerPrincipal> out = new ArrayList<>();

        try (Connection con = dbConfig.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                out.add(map(rs));
            }
            return out;

        } catch (Exception e) {
            throw new RuntimeException("Error al listar banner activo: " + e.getMessage(), e);
        }
    }

    public List<BannerPrincipal> findAll() {
        String sql = """
            SELECT id_banner, id_videojuego, imagen_url, posicion, activo
            FROM banner_principal
            ORDER BY posicion ASC, id_banner ASC
        """;

        List<BannerPrincipal> out = new ArrayList<>();

        try (Connection con = dbConfig.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                out.add(map(rs));
            }
            return out;

        } catch (Exception e) {
            throw new RuntimeException("Error al listar banner: " + e.getMessage(), e);
        }
    }

    public BannerPrincipal findById(int id) {
        String sql = """
            SELECT id_banner, id_videojuego, imagen_url, posicion, activo
            FROM banner_principal
            WHERE id_banner = ?
        """;

        try (Connection con = dbConfig.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
            return null;

        } catch (Exception e) {
            throw new RuntimeException("Error al obtener banner: " + e.getMessage(), e);
        }
    }

    public int create(BannerPrincipal b) {
        String sql = """
            INSERT INTO banner_principal (id_videojuego, imagen_url, posicion, activo)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection con = dbConfig.conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (b.getIdVideojuego() == null) ps.setNull(1, java.sql.Types.INTEGER);
            else ps.setInt(1, b.getIdVideojuego());

            ps.setString(2, b.getImagenUrl());
            ps.setInt(3, b.getPosicion());
            ps.setBoolean(4, b.isActivo());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
            return 0;

        } catch (Exception e) {
            throw new RuntimeException("Error al crear banner: " + e.getMessage(), e);
        }
    }

    public boolean update(BannerPrincipal b) {
        String sql = """
            UPDATE banner_principal
            SET id_videojuego = ?, imagen_url = ?, posicion = ?, activo = ?
            WHERE id_banner = ?
        """;

        try (Connection con = dbConfig.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (b.getIdVideojuego() == null) ps.setNull(1, java.sql.Types.INTEGER);
            else ps.setInt(1, b.getIdVideojuego());

            ps.setString(2, b.getImagenUrl());
            ps.setInt(3, b.getPosicion());
            ps.setBoolean(4, b.isActivo());
            ps.setInt(5, b.getIdBanner());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar banner: " + e.getMessage(), e);
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM banner_principal WHERE id_banner = ?";

        try (Connection con = dbConfig.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar banner: " + e.getMessage(), e);
        }
    }

    private BannerPrincipal map(ResultSet rs) throws Exception {
        BannerPrincipal b = new BannerPrincipal();
        b.setIdBanner(rs.getInt("id_banner"));

        int idJuego = rs.getInt("id_videojuego");
        b.setIdVideojuego(rs.wasNull() ? null : idJuego);

        b.setImagenUrl(rs.getString("imagen_url"));
        b.setPosicion(rs.getInt("posicion"));
        b.setActivo(rs.getBoolean("activo"));
        return b;
    }
}
