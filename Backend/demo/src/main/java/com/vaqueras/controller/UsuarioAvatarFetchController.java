package com.vaqueras.controller;

import java.io.IOException;

import com.vaqueras.dao.UsuarioAvatarDAO;
import com.vaqueras.model.ImageData;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/usuarios/avatar/*")

public class UsuarioAvatarFetchController extends HttpServlet {
    
    private final UsuarioAvatarDAO dao = new UsuarioAvatarDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String path = req.getPathInfo(); // "/{id}"
        if (path == null || !path.matches("/\\d+")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("application/json; charset=UTF-8");
            resp.getWriter().write("{\"error\":\"ID inválido\"}");
            return;
        }

        int idUser = Integer.parseInt(path.substring(1));
        ImageData img = dao.findAvatar(idUser);

        if (img == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.setContentType("application/json; charset=UTF-8");
            resp.getWriter().write("{\"error\":\"Avatar no encontrado\"}");
            return;
        }

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType(img.mime());
        resp.getOutputStream().write(img.bytes());
    }
}
