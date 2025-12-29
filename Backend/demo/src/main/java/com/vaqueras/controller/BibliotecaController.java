package com.vaqueras.controller;

import java.io.IOException;

import com.google.gson.Gson;
import com.vaqueras.model.TokenUser;
import com.vaqueras.service.BibliotecaService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/gamer/biblioteca")

public class BibliotecaController extends HttpServlet{
    
    private final BibliotecaService service = new BibliotecaService();
    private final Gson gson = new Gson();

    // --- MÉTODO AUXILIAR PARA OBTENER EL ID DEL USUARIO ---
    private Integer getAuthUserId(HttpServletRequest req) {
        //Buscamos el objeto con la llave CORRECTA "AUTH_USER"
        TokenUser user = (TokenUser) req.getAttribute("AUTH_USER");
        if (user == null) return null;       
        //Retornamos su ID
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
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(gson.toJson(service.listar(idUser)));
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Error interno\"}");
        }
    }
}
