package com.vaqueras.controller;

import java.io.IOException;
import java.math.BigDecimal;

import com.google.gson.Gson;
import com.vaqueras.model.RecargaRequest;
import com.vaqueras.model.TokenUser;
import com.vaqueras.service.CarteraService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/gamer/cartera/*")
public class CarteraController extends HttpServlet{
    
    private final CarteraService service = new CarteraService();
    private final Gson gson = new Gson();

    // --- MÉTODO AUXILIAR PARA OBTENER EL ID correctamente--
    private Integer getAuthUserId(HttpServletRequest req) {
        TokenUser user = (TokenUser) req.getAttribute("AUTH_USER");
        if (user == null) return null;      
        return user.getIdUser();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");

        Integer idUser = getAuthUserId(req);
        if (idUser == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\":\"Token requerido\"}");
            return;
        }

        String path = req.getPathInfo(); // null, "/", "/recargas"
        try {
            if (path == null || "/".equals(path)) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(gson.toJson(service.getCartera(idUser)));
                return;
            }

            if ("/recargas".equals(path)) {
                int limit = 50;
                String q = req.getParameter("limit");
                if (q != null && q.matches("\\d+")) limit = Integer.parseInt(q);

                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(gson.toJson(service.historial(idUser, limit)));
                return;
            }

            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"error\":\"Ruta no válida\"}");

        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Error interno\"}");
        }
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

        String path = req.getPathInfo(); // "/recargas"
        if (path == null) path = "/";

        try {
            if (!"/recargas".equals(path)) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"error\":\"Ruta no válida\"}");
                return;
            }

            RecargaRequest body = gson.fromJson(req.getReader(), RecargaRequest.class);
            BigDecimal monto = body != null ? body.getMonto() : null;

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(gson.toJson(service.recargar(idUser, monto)));

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
