package com.vaqueras.controller;

import java.io.IOException;
import java.time.LocalDate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vaqueras.service.VideojuegoService;
import com.vaqueras.util.LocalDateAdapter;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/videojuegos/*")
public class VideojuegoController extends HttpServlet {
    
    private final VideojuegoService service = new VideojuegoService();

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");

        String path = req.getPathInfo(); // null, "/", "/{id}"
        try {
            //listamos todos los juegos
            if (path == null || "/".equals(path)) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(gson.toJson(service.listarPublico()));
                return;
            }

            //Listar comentarios de un juego /api/videojuegos/{id}/comentarios
            if (path.matches("/\\d+/comentarios")) {
                // ["", "123", "comentarios"]
                int id = Integer.parseInt(path.split("/")[1]);
                
                // Instanciamos el servicio aquí 
                com.vaqueras.service.ComentarioService cs = new com.vaqueras.service.ComentarioService();
                
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(gson.toJson(cs.listarPublico(id)));
                return; // Importante: retornar para que no siga ejecutando
            }

            if (!path.matches("/\\d+")) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\":\"ID inválido\"}");
                return;
            }

            int id = Integer.parseInt(path.substring(1));
            var detail = service.detallePublico(id);
            if (detail == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"error\":\"Videojuego no encontrado\"}");
                return;
            }

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(gson.toJson(detail));

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Error interno\"}");
        }
    }
}
