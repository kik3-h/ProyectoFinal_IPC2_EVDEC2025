package com.vaqueras.service;

import java.sql.Connection;
import java.sql.SQLException;

import com.vaqueras.config.DatabaseConfig;
import com.vaqueras.dao.BibliotecaDAO;
import com.vaqueras.dao.GrupoFamiliarDAO;
import com.vaqueras.dao.InstalacionPrestamoDAO;
import com.vaqueras.dao.MiembroGrupoDAO;

public class PrestamoService {
    
    private static final int MAX_INSTALACIONES_POR_JUEGO_EN_GRUPO = 2;

    private final DatabaseConfig db = new DatabaseConfig();

    private final GrupoFamiliarDAO grupoDAO = new GrupoFamiliarDAO();
    private final MiembroGrupoDAO miembroDAO = new MiembroGrupoDAO();
    private final BibliotecaDAO bibliotecaDAO = new BibliotecaDAO();
    private final InstalacionPrestamoDAO instalacionDAO = new InstalacionPrestamoDAO();

    public void prestar(int idGrupo, int idUserPrestador, int idUserReceptor, int idVideojuego) {
        // Ambos deben ser miembros del grupo
        if (!miembroDAO.isMember(idGrupo, idUserPrestador) && !grupoDAO.isAdmin(idGrupo, idUserPrestador)) {
            throw new SecurityException("No perteneces a este grupo");
        }
        if (!miembroDAO.isMember(idGrupo, idUserReceptor) && !grupoDAO.isAdmin(idGrupo, idUserReceptor)) {
            throw new IllegalArgumentException("El receptor no pertenece al grupo");
        }

        Connection conn = null;
        try {
            conn = db.conectar();
            conn.setAutoCommit(false);

            // Prestador debe ser propietario del juego
            if (!bibliotecaDAO.isOwner(conn, idUserPrestador, idVideojuego)) {
                throw new SecurityException("Solo puedes prestar juegos que hayas comprado");
            }

            // Receptor no debe tener ya el juego
            if (bibliotecaDAO.hasAny(conn, idUserReceptor, idVideojuego)) {
                throw new IllegalStateException("El receptor ya tiene ese juego en su biblioteca");
            }

            // Insertar en biblioteca como NO propietario
            bibliotecaDAO.insertBorrowed(conn, idUserReceptor, idVideojuego);

            // Crear registro lógico de préstamo (instalación inicial NO_INSTALADO)
            instalacionDAO.upsert(conn, idUserReceptor, idVideojuego, "NO_INSTALADO");

            conn.commit();

        } catch (SQLException e) {
            if (conn != null) { try { conn.rollback(); } catch (SQLException ignored) {} }
            throw new RuntimeException("Error en préstamo: " + e.getMessage(), e);

        } finally {
            if (conn != null) { try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ignored) {} }
        }
    }

    public void actualizarInstalacionPrestamo(int idGrupo, int idUserBorrower, int idVideojuego, String estado) {
        if (estado == null) throw new IllegalArgumentException("estado requerido");
        String st = estado.trim().toUpperCase();
        if (!st.equals("INSTALADO") && !st.equals("NO_INSTALADO")) {
            throw new IllegalArgumentException("estado debe ser INSTALADO o NO_INSTALADO");
        }

        if (!miembroDAO.isMember(idGrupo, idUserBorrower) && !grupoDAO.isAdmin(idGrupo, idUserBorrower)) {
            throw new SecurityException("No perteneces a este grupo");
        }

        Connection conn = null;
        try {
            conn = db.conectar();
            conn.setAutoCommit(false);

            // Debe tener el juego prestado (no propietario)
            if (!bibliotecaDAO.isBorrowed(conn, idUserBorrower, idVideojuego)) {
                throw new IllegalArgumentException("No tienes este juego como préstamo en tu biblioteca");
            }

            String actual = bibliotecaDAO.getEstadoInstalacion(conn, idUserBorrower, idVideojuego);

            if (st.equals("INSTALADO") && !"INSTALADO".equals(actual)) {

            int instalados = miembroDAO.countInstaladosEnGrupo(conn, idGrupo, idVideojuego);
            if (instalados >= MAX_INSTALACIONES_POR_JUEGO_EN_GRUPO) {
                throw new IllegalStateException("Límite de instalaciones alcanzado para este juego en el grupo");
            }

            // Regla extra para que solo 1 usuario PRESTADO puede tenerlo INSTALADO (el otro cupo es para el propietario)
            int prestadosInstalados = miembroDAO.countInstaladosPrestadosEnGrupo(conn, idGrupo, idVideojuego);
            if (prestadosInstalados >= 1) {
                throw new IllegalStateException("Ya hay un miembro con el préstamo instalado; solo se permite 1 prestado + propietario");
            }
        }

            bibliotecaDAO.updateEstadoInstalacion(conn, idUserBorrower, idVideojuego, st);
            instalacionDAO.upsert(conn, idUserBorrower, idVideojuego, st);

            conn.commit();

        } catch (SQLException e) {
            if (conn != null) { try { conn.rollback(); } catch (SQLException ignored) {} }
            throw new RuntimeException("Error actualizando instalación: " + e.getMessage(), e);

        } finally {
            if (conn != null) { try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ignored) {} }
        }
    }

    public void devolverPrestamo(int idGrupo, int idUserBorrower, int idVideojuego) {
        if (!miembroDAO.isMember(idGrupo, idUserBorrower) && !grupoDAO.isAdmin(idGrupo, idUserBorrower)) {
            throw new SecurityException("No perteneces a este grupo");
        }

        Connection conn = null;
        try {
            conn = db.conectar();
            conn.setAutoCommit(false);

            if (!bibliotecaDAO.isBorrowed(conn, idUserBorrower, idVideojuego)) {
                throw new IllegalArgumentException("No tienes ese préstamo activo");
            }

            instalacionDAO.delete(conn, idUserBorrower, idVideojuego);
            bibliotecaDAO.deleteBorrowed(conn, idUserBorrower, idVideojuego);

            conn.commit();

        } catch (SQLException e) {
            if (conn != null) { try { conn.rollback(); } catch (SQLException ignored) {} }
            throw new RuntimeException("Error devolviendo préstamo: " + e.getMessage(), e);

        } finally {
            if (conn != null) { try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ignored) {} }
        }
    }
}
