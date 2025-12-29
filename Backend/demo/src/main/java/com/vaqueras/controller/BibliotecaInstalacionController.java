package com.vaqueras.controller;

import java.io.IOException;

import com.google.gson.Gson;
import com.vaqueras.model.InstalacionRequest;
import com.vaqueras.model.TokenUser;
import com.vaqueras.service.BibliotecaService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/gamer/biblioteca/*")

public class BibliotecaInstalacionController extends HttpServlet{

    private final BibliotecaService service = new BibliotecaService();
    private final Gson gson = new Gson();

    private Integer getAuthUserId(HttpServletRequest req) {
        TokenUser user = (TokenUser) req.getAttribute("AUTH_USER");
        if (user == null) return null;
        return user.getIdUser();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");

        Integer idUser = getAuthUserId(req);
        if (idUser == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\":\"Token requerido\"}");
            return;
        }

        // Esperado: /{idVideojuego}/instalacion
        String path = req.getPathInfo();
        if (path == null || !path.matches("/\\d+/instalacion")) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"error\":\"Ruta inválida\"}");
            return;
        }

        try {
            String[] parts = path.split("/");
            int idVideojuego = Integer.parseInt(parts[1]);

            InstalacionRequest body = gson.fromJson(req.getReader(), InstalacionRequest.class);
            String estado = body != null ? body.getEstado() : null;

            String nuevoEstado = service.actualizarInstalacionPropietario(idUser, idVideojuego, estado);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"message\":\"Instalación actualizada\",\"estado\":\"" + nuevoEstado + "\"}");

        } catch (SecurityException e) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");

        } catch (IllegalStateException e) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");

        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Error interno\"}");
        }
    }
}
