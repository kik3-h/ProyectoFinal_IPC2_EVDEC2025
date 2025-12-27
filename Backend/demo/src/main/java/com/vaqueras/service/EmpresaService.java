package com.vaqueras.service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.vaqueras.config.DatabaseConfig;
import com.vaqueras.dao.EmpresaDAO;
import com.vaqueras.dao.UsuarioEmpresaDAO;
import com.vaqueras.model.Empresa;
import com.vaqueras.model.EmpresaCreateRequest;
import com.vaqueras.model.EmpresaUpdateRequest;
import com.vaqueras.util.PasswordUtil;

public class EmpresaService {
    private final DatabaseConfig db = new DatabaseConfig();
    private final EmpresaDAO empresaDAO = new EmpresaDAO();
    private final UsuarioEmpresaDAO usuarioEmpresaDAO = new UsuarioEmpresaDAO();

    public Empresa createEmpresaConAdmin(EmpresaCreateRequest body) {
    validarCreate(body);

    //  default EXACTO como en la DB 
    String cargo = (body.getCargo() == null || body.getCargo().isBlank())
            ? "Administrador_Empresa"
            : body.getCargo().trim();

    Connection conn = null;
    try {
        conn = db.conectar();
        conn.setAutoCommit(false);

        int idEmpresa = insertEmpresa(conn, body);
        int idUser = insertUsuarioEmpresaAdmin(conn, body);

        // AQUÍ se inserta en usuario_empresa
        usuarioEmpresaDAO.link(conn, idUser, idEmpresa, cargo);

        conn.commit();

        Empresa creada = empresaDAO.findById(idEmpresa);
        if (creada == null) throw new RuntimeException("Empresa creada pero no encontrada");
        return creada;

    } catch (SQLException e) {
        //  rollback explícito
        if (conn != null) {
            try { conn.rollback(); } catch (SQLException ignored) {}
        }

        if (e.getErrorCode() == 1062) {
            throw new com.vaqueras.exception.ConflictException(
                "Duplicado: nombre/email ya existe en empresa o usuario o link usuario_empresa"
            );
        }
        throw new RuntimeException("Error creando empresa: " + e.getMessage(), e);

    } finally {
        if (conn != null) {
            try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ignored) {}
        }
    }
}

    public Empresa getById(int idEmpresa) {
        return empresaDAO.findById(idEmpresa);
    }

    public java.util.List<Empresa> listAll() {
        return empresaDAO.findAll();
    }

    public boolean updateEmpresa(int idEmpresa, EmpresaUpdateRequest body) {
        if (body == null) throw new IllegalArgumentException("Body requerido");

        String nombre = (body.getNombreEmpresa() != null && !body.getNombreEmpresa().isBlank())
                ? body.getNombreEmpresa().trim() : null;
        String email = (body.getEmail() != null && !body.getEmail().isBlank())
                ? body.getEmail().trim() : null;
        String desc = (body.getDescripcion() != null) ? body.getDescripcion().trim() : null;

        return empresaDAO.updatePartial(idEmpresa, nombre, email, desc);
    }

    public boolean isOwner(int idUser, int idEmpresa) {
        return usuarioEmpresaDAO.isUsuarioLigadoAEmpresa(idUser, idEmpresa);
    }

    private void validarCreate(EmpresaCreateRequest body) {
        if (body == null) throw new IllegalArgumentException("Body requerido");
        if (body.getEmpresa() == null) throw new IllegalArgumentException("empresa requerida");
        if (body.getAdmin() == null) throw new IllegalArgumentException("admin requerido");

        if (isBlank(body.getEmpresa().getNombreEmpresa())) throw new IllegalArgumentException("nombreEmpresa requerido");
        if (isBlank(body.getEmpresa().getEmail())) throw new IllegalArgumentException("email empresa requerido");

        if (isBlank(body.getAdmin().getNickname())) throw new IllegalArgumentException("nickname admin requerido");
        if (isBlank(body.getAdmin().getEmail())) throw new IllegalArgumentException("email admin requerido");
        if (isBlank(body.getAdmin().getPassword())) throw new IllegalArgumentException("password admin requerido");
        if (body.getAdmin().getFechaNacimiento() == null) throw new IllegalArgumentException("fechaNacimiento admin requerida");
    }

    private int insertEmpresa(Connection conn, EmpresaCreateRequest body) throws SQLException {
        String sql = "INSERT INTO empresa (nombre_empresa, email, descripcion) VALUES (?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, body.getEmpresa().getNombreEmpresa().trim());
            ps.setString(2, body.getEmpresa().getEmail().trim());
            ps.setString(3, body.getEmpresa().getDescripcion());

            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        }
        throw new SQLException("No se generó id_empresa");
    }

    private int insertUsuarioEmpresaAdmin(Connection conn, EmpresaCreateRequest body) throws SQLException {
        String sql = """
            INSERT INTO usuario (nickname, email, password, telefono, fecha_nacimiento, pais, rol, estado_cuenta)
            VALUES (?,?,?,?,?,?, 'EMPRESA', 'ACTIVO')
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, body.getAdmin().getNickname().trim());
            ps.setString(2, body.getAdmin().getEmail().trim());
            ps.setString(3, PasswordUtil.hashPassword(body.getAdmin().getPassword()));
            ps.setString(4, body.getAdmin().getTelefono()); // nullable
            ps.setDate(5, Date.valueOf(body.getAdmin().getFechaNacimiento()));
            ps.setString(6, body.getAdmin().getPais()); // nullable

            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        }
        throw new SQLException("No se generó id_user");
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
