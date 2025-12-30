package com.vaqueras.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.vaqueras.config.DatabaseConfig;
import com.vaqueras.model.BannerPrincipal;
import com.vaqueras.model.ImageData;

public class BannerDAO {
    private final DatabaseConfig dbConfig = new DatabaseConfig();

    public List<BannerPrincipal> findActive() {
        String sql = """
            SELECT id_banner, id_videojuego, imagen_url, posicion, activo,
            (imagen_blob IS NOT NULL) AS has_blob
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
            SELECT id_banner, id_videojuego, imagen_url, posicion, activo,
            (imagen_blob IS NOT NULL) AS has_blob
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
            SELECT id_banner, id_videojuego, imagen_url, posicion, activo,
            (imagen_blob IS NOT NULL) AS has_blob
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

    // --- NUEVOS MÉTODOS PARA BLOB ---

    public boolean updateBlob(int idBanner, byte[] bytes, String mime) {
        // Al guardar un BLOB, vaciamos la imagen_url para que la lógica del map() funcione
        String sql = "UPDATE banner_principal SET imagen_blob = ?, imagen_mime = ?, imagen_url = '' WHERE id_banner = ?";
        try (Connection con = dbConfig.conectar();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBytes(1, bytes);
            ps.setString(2, mime);
            ps.setInt(3, idBanner);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error actualizando imagen banner: " + e.getMessage(), e);
        }
    }

    public ImageData findBlob(int idBanner) {
        String sql = "SELECT imagen_blob, imagen_mime FROM banner_principal WHERE id_banner = ?";
        try (Connection con = dbConfig.conectar();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idBanner);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                
                byte[] blob = rs.getBytes("imagen_blob");
                String mime = rs.getString("imagen_mime");
                
                if (blob == null || blob.length == 0 || mime == null || mime.isBlank()) return null;
                
                return new ImageData(blob, mime);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo imagen banner: " + e.getMessage(), e);
        }
    }

    // MAP ACTUALIZADO para funciones BLOB
    private BannerPrincipal map(ResultSet rs) throws Exception {
        BannerPrincipal b = new BannerPrincipal();
        b.setIdBanner(rs.getInt("id_banner"));

        int idJuego = rs.getInt("id_videojuego");
        b.setIdVideojuego(rs.wasNull() ? null : idJuego);

        b.setPosicion(rs.getInt("posicion"));
        b.setActivo(rs.getBoolean("activo"));

        // LÓGICA DE IMAGEN HÍBRIDA URL vs BLOB
        boolean hasBlob = rs.getBoolean("has_blob");
        String url = rs.getString("imagen_url");

        if (hasBlob && (url == null || url.isBlank())) {
            // Si tiene blob y no tiene URL externa, generamos la URL interna
            url = "/vaqueras-backend/api/banners/imagen/" + b.getIdBanner();
        }
        b.setImagenUrl(url);
        return b;
    }
}
