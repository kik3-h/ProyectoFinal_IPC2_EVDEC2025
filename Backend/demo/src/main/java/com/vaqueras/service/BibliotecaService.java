package com.vaqueras.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.vaqueras.config.DatabaseConfig;
import com.vaqueras.dao.BibliotecaDAO;
import com.vaqueras.dao.GrupoFamiliarDAO;
import com.vaqueras.dao.MiembroGrupoDAO;
import com.vaqueras.model.BibliotecaItemDTO;


public class BibliotecaService {
    private static final int MAX_INSTALACIONES_POR_JUEGO_EN_GRUPO = 2;
    private final DatabaseConfig db = new DatabaseConfig();
    private final BibliotecaDAO dao = new BibliotecaDAO();

    private final GrupoFamiliarDAO grupoDAO = new GrupoFamiliarDAO();
    private final MiembroGrupoDAO miembroDAO = new MiembroGrupoDAO();

    public List<BibliotecaItemDTO> listar(int idUser) {
        try (Connection conn = db.conectar()) {
            return dao.listByUser(conn, idUser);
        } catch (Exception e) {
            throw new RuntimeException("Error listando biblioteca: " + e.getMessage(), e);
        }
    }

    // mejoro el proceso de instalar/desinstalar SOLO si es propietario
    public String actualizarInstalacionPropietario(int idUser, int idVideojuego, String estado) {
        if (idUser <= 0) throw new IllegalArgumentException("idUser inválido");
        if (idVideojuego <= 0) throw new IllegalArgumentException("idVideojuego inválido");
        if (estado == null) throw new IllegalArgumentException("estado requerido");

        String st = estado.trim().toUpperCase();
        if (!st.equals("INSTALADO") && !st.equals("NO_INSTALADO")) {
            throw new IllegalArgumentException("estado debe ser INSTALADO o NO_INSTALADO");
        }

        Connection conn = null;
        try {
            conn = db.conectar();
            conn.setAutoCommit(false);

            // Debe tener el juego y ser propietario
            if (!dao.isOwner(conn, idUser, idVideojuego)) {
                throw new SecurityException("Solo el propietario puede instalar desde este endpoint");
            }

            String actual = dao.getEstadoInstalacion(conn, idUser, idVideojuego);
            if (actual == null) {
                throw new IllegalArgumentException("El juego no existe en tu biblioteca");
            }

            // Si va a pasar de NO_INSTALADO -> INSTALADO, validar límite por grupo
            if (st.equals("INSTALADO") && !"INSTALADO".equalsIgnoreCase(actual)) {
                List<Integer> grupos = grupoDAO.listIdsByUser(conn, idUser);

                for (Integer idGrupo : grupos) {
                    int instalados = miembroDAO.countInstaladosEnGrupo(conn, idGrupo, idVideojuego);
                    if (instalados >= MAX_INSTALACIONES_POR_JUEGO_EN_GRUPO) {
                        throw new IllegalStateException(
                            "Límite de instalaciones alcanzado para este juego en el grupo " + idGrupo
                        );
                    }
                }
            }

            dao.updateEstadoInstalacion(conn, idUser, idVideojuego, st);

            conn.commit();
            return st;

        } catch (SQLException e) {
            if (conn != null) { try { conn.rollback(); } catch (SQLException ignored) {} }
            throw new RuntimeException("Error actualizando instalación: " + e.getMessage(), e);

        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ignored) {}
            }
        }
    }
}
