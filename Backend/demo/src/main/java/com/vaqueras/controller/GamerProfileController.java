package com.vaqueras.controller;

import java.io.IOException;

import com.google.gson.Gson;
import com.vaqueras.model.GamerProfileUpdateRequest;
import com.vaqueras.model.TokenUser;
import com.vaqueras.service.GamerProfileService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/gamer/perfil")
public class GamerProfileController extends HttpServlet{
    
    private final GamerProfileService service = new GamerProfileService();
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

        try {
            var perfil = service.getPerfil(idUser);
            if (perfil == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"error\":\"Usuario no encontrado\"}");
                return;
            }

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(gson.toJson(perfil));

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Error interno\"}");
        }
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

        try {
            GamerProfileUpdateRequest body = gson.fromJson(req.getReader(), GamerProfileUpdateRequest.class);
            service.updatePerfil(idUser, body);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"message\":\"Perfil actualizado\"}");

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
