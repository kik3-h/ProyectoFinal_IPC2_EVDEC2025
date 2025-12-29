package com.vaqueras.service;

import java.sql.Connection;
import java.sql.SQLException;

import com.vaqueras.config.DatabaseConfig;
import com.vaqueras.dao.BibliotecaDAO;
import com.vaqueras.dao.ComentarioDAO;
import com.vaqueras.model.ComentariosResponse;
import com.vaqueras.model.ComentarioCreateRequest;

public class ComentarioService {
    
    private final DatabaseConfig db = new DatabaseConfig();
    private final ComentarioDAO comentarioDAO = new ComentarioDAO();
    private final BibliotecaDAO bibliotecaDAO = new BibliotecaDAO();

    public ComentariosResponse listarPublico(int idVideojuego) {
        double avg = comentarioDAO.avgRatingVisible(idVideojuego);
        int total = comentarioDAO.countRatingsVisible(idVideojuego);
        var comentarios = comentarioDAO.listVisibleByVideojuego(idVideojuego);
        return new ComentariosResponse(avg, total, comentarios);
    }

    public int crearComentario(int idUser, int idVideojuego, ComentarioCreateRequest req) {
        if (req == null) throw new IllegalArgumentException("Body requerido");

        String texto = (req.getTexto() != null) ? req.getTexto().trim() : null;
        Integer cal = req.getCalificacion();

        if ((texto == null || texto.isEmpty()) && cal == null) {
            throw new IllegalArgumentException("Debe enviar texto y/o calificacion");
        }
        if (cal != null && (cal < 1 || cal > 5)) {
            throw new IllegalArgumentException("calificacion debe estar entre 1 y 5");
        }

        Connection conn = null;
        try {
            conn = db.conectar();
            conn.setAutoCommit(false);

            // Regla solo comentar si es PROPIETARIO del juego en biblioteca
            if (!bibliotecaDAO.isOwner(conn, idUser, idVideojuego)) {
                throw new SecurityException("Solo puedes comentar/calificar juegos que hayas comprado");
            }

            Integer padre = req.getIdComentarioPadre();
            if (padre != null) {
                boolean ok = comentarioDAO.comentarioExisteEnJuego(conn, padre, idVideojuego);
                if (!ok) throw new IllegalArgumentException("idComentarioPadre no pertenece a este videojuego");
            }

            int idComentario = comentarioDAO.insert(conn, idUser, idVideojuego, padre, texto, cal);
            conn.commit();
            return idComentario;

        } catch (SQLException e) {
            if (conn != null) { try { conn.rollback(); } catch (SQLException ignored) {} }
            throw new RuntimeException("Error creando comentario: " + e.getMessage(), e);

        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ignored) {}
            }
        }
    }
}
