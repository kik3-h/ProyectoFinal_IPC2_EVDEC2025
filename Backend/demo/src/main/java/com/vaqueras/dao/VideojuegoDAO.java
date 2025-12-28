package com.vaqueras.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.vaqueras.config.DatabaseConfig;
import com.vaqueras.model.VideojuegoDetailDTO;
import com.vaqueras.model.VideojuegoPublicDTO;

public class VideojuegoDAO {
    private final DatabaseConfig db = new DatabaseConfig();

    public int insert(Connection conn, int idEmpresa, String titulo, String descripcion, double precio,
                      String recursosMinimos, LocalDate fechaLanzamiento,
                      String clasificacionEdad, int edadMinima, String estado) throws SQLException {

        String sql = """
            INSERT INTO videojuego (id_empresa, titulo, descripcion, precio, recursos_minimos, fecha_lanzamiento,
                                   clasificacion_edad, edad_minima, estado)
            VALUES (?,?,?,?,?,?,?,?,?)
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, idEmpresa);
            ps.setString(2, titulo);
            ps.setString(3, descripcion);
            ps.setDouble(4, precio);
            ps.setString(5, recursosMinimos);

            if (fechaLanzamiento == null) ps.setNull(6, Types.DATE);
            else ps.setDate(6, Date.valueOf(fechaLanzamiento));

            ps.setString(7, clasificacionEdad);
            ps.setInt(8, edadMinima);
            ps.setString(9, estado);

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        }
        throw new SQLException("No se generó id_videojuego");
    }

    public boolean belongsToEmpresa(int idVideojuego, int idEmpresa) {
        String sql = "SELECT 1 FROM videojuego WHERE id_videojuego = ? AND id_empresa = ? LIMIT 1";
        try (Connection conn = db.conectar();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idVideojuego);
            ps.setInt(2, idEmpresa);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error verificando dueño del videojuego: " + e.getMessage(), e);
        }
    }

    public boolean update(Connection conn, int idVideojuego, int idEmpresa, String titulo, String descripcion,
                          Double precio, String recursosMinimos, LocalDate fechaLanzamiento,
                          String clasificacionEdad, Integer edadMinima, String estado) throws SQLException {

        StringBuilder sb = new StringBuilder("UPDATE videojuego SET ");
        List<Object> params = new ArrayList<>();

        if (titulo != null) { sb.append("titulo = ?, "); params.add(titulo); }
        if (descripcion != null) { sb.append("descripcion = ?, "); params.add(descripcion); }
        if (precio != null) { sb.append("precio = ?, "); params.add(precio); }
        if (recursosMinimos != null) { sb.append("recursos_minimos = ?, "); params.add(recursosMinimos); }
        if (fechaLanzamiento != null) { sb.append("fecha_lanzamiento = ?, "); params.add(Date.valueOf(fechaLanzamiento)); }
        if (clasificacionEdad != null) { sb.append("clasificacion_edad = ?, "); params.add(clasificacionEdad); }
        if (edadMinima != null) { sb.append("edad_minima = ?, "); params.add(edadMinima); }
        if (estado != null) { sb.append("estado = ?, "); params.add(estado); }

        if (params.isEmpty()) return false;

        sb.setLength(sb.length() - 2);
        sb.append(" WHERE id_videojuego = ? AND id_empresa = ?");
        params.add(idVideojuego);
        params.add(idEmpresa);

        try (PreparedStatement ps = conn.prepareStatement(sb.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            return ps.executeUpdate() > 0;
        }
    }

    public boolean suspend(Connection conn, int idVideojuego, int idEmpresa) throws SQLException {
        String sql = "UPDATE videojuego SET estado = 'SUSPENDIDO' WHERE id_videojuego = ? AND id_empresa = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idVideojuego);
            ps.setInt(2, idEmpresa);
            return ps.executeUpdate() > 0;
        }
    }

    public List<VideojuegoPublicDTO> listPublicActivos() {
        String sql = """
            SELECT v.id_videojuego, v.titulo, v.precio, v.clasificacion_edad, v.edad_minima,
                   e.id_empresa, e.nombre_empresa,
                   m.url_imagen AS portada_url
            FROM videojuego v
            JOIN empresa e ON e.id_empresa = v.id_empresa
            LEFT JOIN multimedia m ON m.id_videojuego = v.id_videojuego AND m.tipo = 'PORTADA'
            WHERE v.estado = 'ACTIVO'
            ORDER BY v.id_videojuego DESC
        """;

        List<VideojuegoPublicDTO> out = new ArrayList<>();

        try (Connection conn = db.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                VideojuegoPublicDTO dto = new VideojuegoPublicDTO();
                dto.setIdVideojuego(rs.getInt("id_videojuego"));
                dto.setTitulo(rs.getString("titulo"));
                dto.setPrecio(rs.getDouble("precio"));
                dto.setClasificacionEdad(rs.getString("clasificacion_edad"));
                dto.setEdadMinima(rs.getInt("edad_minima"));
                dto.setIdEmpresa(rs.getInt("id_empresa"));
                dto.setNombreEmpresa(rs.getString("nombre_empresa"));
                dto.setPortadaUrl(rs.getString("portada_url"));
                out.add(dto);
            }
            return out;

        } catch (SQLException e) {
            throw new RuntimeException("Error listando videojuegos: " + e.getMessage(), e);
        }
    }

    public VideojuegoDetailDTO getBaseDetail(int idVideojuego) {
        String sql = """
            SELECT v.id_videojuego, v.id_empresa, e.nombre_empresa,
                   v.titulo, v.descripcion, v.precio, v.recursos_minimos, v.fecha_lanzamiento,
                   v.estado, v.clasificacion_edad, v.edad_minima
            FROM videojuego v
            JOIN empresa e ON e.id_empresa = v.id_empresa
            WHERE v.id_videojuego = ?
        """;

        try (Connection conn = db.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idVideojuego);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                VideojuegoDetailDTO d = new VideojuegoDetailDTO();
                d.setIdVideojuego(rs.getInt("id_videojuego"));
                d.setIdEmpresa(rs.getInt("id_empresa"));
                d.setNombreEmpresa(rs.getString("nombre_empresa"));
                d.setTitulo(rs.getString("titulo"));
                d.setDescripcion(rs.getString("descripcion"));
                d.setPrecio(rs.getDouble("precio"));
                d.setRecursosMinimos(rs.getString("recursos_minimos"));

                Date f = rs.getDate("fecha_lanzamiento");
                if (f != null) d.setFechaLanzamiento(f.toLocalDate());

                d.setEstado(rs.getString("estado"));
                d.setClasificacionEdad(rs.getString("clasificacion_edad"));
                d.setEdadMinima(rs.getInt("edad_minima"));
                return d;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error obteniendo videojuego: " + e.getMessage(), e);
        }
    }
}
