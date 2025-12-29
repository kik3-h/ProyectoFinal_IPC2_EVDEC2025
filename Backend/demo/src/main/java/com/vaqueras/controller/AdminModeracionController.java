package com.vaqueras.controller;

import java.io.IOException;

import com.google.gson.Gson;
import com.vaqueras.model.ModeracionCategoriasRequest;
import com.vaqueras.service.ModeracionService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/admin/videojuegos/*")

public class AdminModeracionController extends HttpServlet {
    
    private final ModeracionService service = new ModeracionService();
    private final Gson gson = new Gson();

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");

        // Ruta: /{id}/categorias
        String path = req.getPathInfo();
        if (path == null || !path.matches("/\\d+/categorias")) {
            resp.setStatus(404);
            resp.getWriter().write("{\"error\":\"Ruta inválida\"}");
            return;
        }

        int idVideojuego = Integer.parseInt(path.split("/")[1]);

        try {
            ModeracionCategoriasRequest body = gson.fromJson(req.getReader(), ModeracionCategoriasRequest.class);
            service.reemplazarCategorias(idVideojuego, body != null ? body.getCategorias() : null);

            resp.setStatus(200);
            resp.getWriter().write("{\"message\":\"Categorías del juego actualizadas\"}");

        } catch (IllegalArgumentException e) {
            resp.setStatus(400);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            resp.getWriter().write("{\"error\":\"Error interno\"}");
        }
    }
}
