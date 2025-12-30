package com.vaqueras.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.vaqueras.config.DatabaseConfig;
import com.vaqueras.model.ImageData;

public class UsuarioAvatarDAO {
    
    private final DatabaseConfig db = new DatabaseConfig();

    public boolean updateAvatar(int idUser, byte[] bytes, String mime) {
        String sql = "UPDATE usuario SET avatar_blob = ?, avatar_mime = ? WHERE id_user = ?";
        try (Connection conn = db.conectar();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBytes(1, bytes);
            ps.setString(2, mime);
            ps.setInt(3, idUser);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            throw new RuntimeException("Error actualizando avatar: " + e.getMessage(), e);
        }
    }

    public ImageData findAvatar(int idUser) {
        String sql = "SELECT avatar_blob, avatar_mime FROM usuario WHERE id_user = ?";
        try (Connection conn = db.conectar();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUser);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                byte[] blob = rs.getBytes("avatar_blob");
                String mime = rs.getString("avatar_mime");
                if (blob == null || blob.length == 0 || mime == null || mime.isBlank()) return null;

                return new ImageData(blob, mime);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo avatar: " + e.getMessage(), e);
        }
    }

    public boolean clearAvatar(int idUser) {
        String sql = "UPDATE usuario SET avatar_blob = NULL, avatar_mime = NULL WHERE id_user = ?";
        try (Connection conn = db.conectar();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUser);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error eliminando avatar: " + e.getMessage(), e);
        }
    }
}
