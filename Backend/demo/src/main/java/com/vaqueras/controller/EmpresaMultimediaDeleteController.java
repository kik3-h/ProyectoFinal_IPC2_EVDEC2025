package com.vaqueras.controller;

import java.io.IOException;

import com.vaqueras.dao.MultimediaDAO;
import com.vaqueras.dao.UsuarioEmpresaDAO;
import com.vaqueras.dao.VideojuegoDAO;
import com.vaqueras.model.TokenUser;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/empresa/multimedia/*")

public class EmpresaMultimediaDeleteController extends HttpServlet{
    
    private final MultimediaDAO multimediaDAO = new MultimediaDAO();
    private final VideojuegoDAO videojuegoDAO = new VideojuegoDAO();
    private final UsuarioEmpresaDAO usuarioEmpresaDAO = new UsuarioEmpresaDAO();

    private Integer getAuthUserId(HttpServletRequest req) {
        TokenUser user = (TokenUser) req.getAttribute("AUTH_USER");
        return user == null ? null : user.getIdUser();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");

        Integer idUser = getAuthUserId(req);
        if (idUser == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\":\"Token requerido\"}");
            return;
        }

        String path = req.getPathInfo(); // "/{idMultimedia}"
        if (path == null || !path.matches("/\\d+")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"ID inválido\"}");
            return;
        }

        int idMultimedia = Integer.parseInt(path.substring(1));

        Integer idVideojuego = multimediaDAO.findVideojuegoIdByMultimedia(idMultimedia);
        if (idVideojuego == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"error\":\"Multimedia no encontrada\"}");
            return;
        }

        Integer idEmpresa = usuarioEmpresaDAO.findFirstEmpresaIdByUser(idUser);
        if (idEmpresa == null || !videojuegoDAO.belongsToEmpresa(idVideojuego, idEmpresa)) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.getWriter().write("{\"error\":\"No tienes permiso sobre este videojuego\"}");
            return;
        }

        boolean ok = multimediaDAO.deleteById(idMultimedia);
        resp.setStatus(ok ? 200 : 404);
        resp.getWriter().write(ok ? "{\"message\":\"Multimedia eliminada\"}" : "{\"error\":\"No se pudo eliminar\"}");
    }

}
