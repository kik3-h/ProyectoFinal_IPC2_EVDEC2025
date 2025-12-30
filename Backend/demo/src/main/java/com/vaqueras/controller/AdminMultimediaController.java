package com.vaqueras.controller;

import java.io.IOException;

import com.google.gson.Gson;
import com.vaqueras.dao.MultimediaDAO;
import com.vaqueras.dao.UsuarioEmpresaDAO;
import com.vaqueras.dao.VideojuegoDAO;
import com.vaqueras.model.TokenUser;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet("/api/empresa/multimedia")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024, maxRequestSize = 10 * 1024 * 1024) // 5MB

public class AdminMultimediaController extends HttpServlet{
    
    private final MultimediaDAO multimediaDAO = new MultimediaDAO();
    private final VideojuegoDAO videojuegoDAO = new VideojuegoDAO();
    private final UsuarioEmpresaDAO usuarioEmpresaDAO = new UsuarioEmpresaDAO();
    private final Gson gson = new Gson();

    private Integer getAuthUserId(HttpServletRequest req) {
        TokenUser user = (TokenUser) req.getAttribute("AUTH_USER");
        return user == null ? null : user.getIdUser();
    }

    private boolean isAllowedMime(String mime) {
        if (mime == null) return false;
        return mime.equalsIgnoreCase("image/png") || 
            mime.equalsIgnoreCase("image/jpeg") || 
            mime.equalsIgnoreCase("image/webp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");

        Integer idUser = getAuthUserId(req);
        if (idUser == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\":\"Token requerido\"}");
            return;
        }

        try {
            // Leer parámetros del formulario multipart
            String idVideojuegoStr = req.getParameter("idVideojuego");
            String tipo = req.getParameter("tipo"); // PORTADA o GALERIA
            Part file = req.getPart("file");

            // Validaciones básicas
            if (idVideojuegoStr == null || tipo == null || file == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\":\"Faltan parámetros (idVideojuego, tipo, file)\"}");
                return;
            }

            int idVideojuego = Integer.parseInt(idVideojuegoStr);
            if (!"PORTADA".equals(tipo) && !"GALERIA".equals(tipo)) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\":\"Tipo debe ser PORTADA o GALERIA\"}");
                return;
            }

            String mime = file.getContentType();
            if (!isAllowedMime(mime)) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\":\"Formato de imagen no permitido\"}");
                return;
            }

            // Validar Permisos: ¿El juego pertenece a la empresa del usuario?
            Integer idEmpresa = usuarioEmpresaDAO.findFirstEmpresaIdByUser(idUser);
            if (idEmpresa == null || !videojuegoDAO.belongsToEmpresa(idVideojuego, idEmpresa)) {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                resp.getWriter().write("{\"error\":\"No tienes permiso sobre este videojuego\"}");
                return;
            }

            // Guardar
            byte[] bytes = file.getInputStream().readAllBytes();
            int idMedia = multimediaDAO.createBlob(idVideojuego, tipo, bytes, mime);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write("{\"message\":\"Imagen subida\", \"idMultimedia\":" + idMedia + "}");

        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"ID de videojuego inválido\"}");
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Error interno al subir imagen\"}");
        }
    }
}
