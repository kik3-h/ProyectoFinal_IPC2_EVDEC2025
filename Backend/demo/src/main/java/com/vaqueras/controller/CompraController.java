package com.vaqueras.controller;

import java.io.IOException;

import com.google.gson.Gson;
import com.vaqueras.model.CompraRequest;
import com.vaqueras.model.TokenUser;
import com.vaqueras.service.CompraService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/gamer/compras")

public class CompraController extends HttpServlet{
    
    private final CompraService service = new CompraService();
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");

        Integer idUser = getAuthUserId(req);
        if (idUser == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\":\"Token requerido\"}");
            return;
        }

        try {
            CompraRequest body = gson.fromJson(req.getReader(), CompraRequest.class);
            var result = service.comprar(idUser, body);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(gson.toJson(result));

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
