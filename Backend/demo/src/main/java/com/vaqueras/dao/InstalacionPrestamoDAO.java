package com.vaqueras.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InstalacionPrestamoDAO {
    
    public void upsert(Connection conn, int idUserPrestando, int idVideojuego, String estado) throws SQLException {
        // no hay UNIQUE en tabla, así que hacemos: delete + insert para mantener 1 fila
        try (PreparedStatement del = conn.prepareStatement(
                "DELETE FROM instalacion_prestamo WHERE id_user_prestando = ? AND id_videojuego = ?")) {
            del.setInt(1, idUserPrestando);
            del.setInt(2, idVideojuego);
            del.executeUpdate();
        }

        try (PreparedStatement ins = conn.prepareStatement(
                "INSERT INTO instalacion_prestamo (id_user_prestando, id_videojuego, estado) VALUES (?,?,?)")) {
            ins.setInt(1, idUserPrestando);
            ins.setInt(2, idVideojuego);
            ins.setString(3, estado);
            ins.executeUpdate();
        }
    }

    public void delete(Connection conn, int idUserPrestando, int idVideojuego) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM instalacion_prestamo WHERE id_user_prestando = ? AND id_videojuego = ?")) {
            ps.setInt(1, idUserPrestando);
            ps.setInt(2, idVideojuego);
            ps.executeUpdate();
        }
    }
}
