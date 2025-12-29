package com.vaqueras.controller;

import java.io.IOException;

import com.google.gson.Gson;
import com.vaqueras.model.ComentarioCreateRequest;
import com.vaqueras.model.TokenUser;
import com.vaqueras.service.ComentarioService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/gamer/videojuegos/*")

public class GamerComentarioController extends HttpServlet {
    
    private final ComentarioService service = new ComentarioService();
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

        // Path esperado: /{idVideojuego}/comentarios
        String path = req.getPathInfo();
        if (path == null || !path.matches("/\\d+/comentarios")) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"error\":\"Ruta inválida\"}");
            return;
        }

        int idVideojuego = Integer.parseInt(path.split("/")[1]);

        try {
            ComentarioCreateRequest body = gson.fromJson(req.getReader(), ComentarioCreateRequest.class);
            int idComentario = service.crearComentario(idUser, idVideojuego, body);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write("{\"message\":\"Comentario creado\",\"idComentario\":" + idComentario + "}");

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
