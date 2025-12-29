package com.vaqueras.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.vaqueras.config.DatabaseConfig;
import com.vaqueras.dao.GrupoFamiliarDAO;
import com.vaqueras.dao.MiembroGrupoDAO;
import com.vaqueras.dao.UsuarioDAO;
import com.vaqueras.model.GrupoDTO;
import com.vaqueras.model.GrupoMemberDTO;


public class GrupoFamiliarService {
    
    private final DatabaseConfig db = new DatabaseConfig();
    private final GrupoFamiliarDAO grupoDAO = new GrupoFamiliarDAO();
    private final MiembroGrupoDAO miembroDAO = new MiembroGrupoDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public int crearGrupo(int idAdminUser, String nombreGrupo) {
        if (nombreGrupo == null || nombreGrupo.trim().isEmpty()) {
            throw new IllegalArgumentException("nombreGrupo requerido");
        }
        String nombre = nombreGrupo.trim();
        if (nombre.length() > 100) throw new IllegalArgumentException("nombreGrupo máximo 100");

        Connection conn = null;
        try {
            conn = db.conectar();
            conn.setAutoCommit(false);

            int idGrupo = grupoDAO.create(conn, nombre, idAdminUser);

            //El admin se agrega como miembro
            miembroDAO.addMember(conn, idGrupo, idAdminUser);

            conn.commit();
            return idGrupo;

        } catch (SQLException e) {
            if (conn != null) { try { conn.rollback(); } catch (SQLException ignored) {} }
            throw new RuntimeException("Error creando grupo: " + e.getMessage(), e);

        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ignored) {}
            }
        }
    }

    public List<GrupoDTO> listarGrupos(int idUser) {
        return grupoDAO.listByUser(idUser);
    }

    public List<GrupoMemberDTO> listarMiembros(int idGrupo, int idUserSolicitante) {
        // permitir ver miembros solo si pertenece al grupo
        if (!miembroDAO.isMember(idGrupo, idUserSolicitante) && !grupoDAO.isAdmin(idGrupo, idUserSolicitante)) {
            throw new SecurityException("No perteneces a este grupo");
        }
        return miembroDAO.listMembers(idGrupo);
    }

    public void agregarMiembro(int idGrupo, int idAdminUser, Integer idUser, String nickname) {
        if (!grupoDAO.isAdmin(idGrupo, idAdminUser)) {
            throw new SecurityException("Solo el admin del grupo puede agregar miembros");
        }

        Integer targetId = idUser;
        if (targetId == null) {
            if (nickname == null || nickname.trim().isEmpty()) {
                throw new IllegalArgumentException("Debe enviar idUser o nickname");
            }
            targetId = usuarioDAO.findIdByNicknameAndRol(nickname.trim(), "GAMER");
            if (targetId == null) throw new IllegalArgumentException("No existe gamer con ese nickname");
        } else {
            if (!usuarioDAO.isRol(targetId, "GAMER")) {
                throw new IllegalArgumentException("El usuario no es GAMER");
            }
        }

        if (miembroDAO.isMember(idGrupo, targetId)) {
            throw new IllegalStateException("El usuario ya es miembro del grupo");
        }

        Connection conn = null;
        try {
            conn = db.conectar();
            conn.setAutoCommit(false);

            miembroDAO.addMember(conn, idGrupo, targetId);

            conn.commit();

        } catch (SQLException e) {
            if (conn != null) { try { conn.rollback(); } catch (SQLException ignored) {} }
            throw new RuntimeException("Error agregando miembro: " + e.getMessage(), e);

        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ignored) {}
            }
        }
    }
}
