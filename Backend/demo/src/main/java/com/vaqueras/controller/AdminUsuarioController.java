package com.vaqueras.controller;

import java.io.IOException;
import java.time.LocalDate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vaqueras.model.Usuario;
import com.vaqueras.service.AdminUsuarioService;
import com.vaqueras.util.LocalDateAdapter;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/admin/usuarios/*")
public class AdminUsuarioController extends HttpServlet {
    private final AdminUsuarioService service = new AdminUsuarioService();

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");

        String pathInfo = req.getPathInfo(); // null, "/", "/5"
        try {
            if (pathInfo == null || "/".equals(pathInfo)) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(gson.toJson(service.listarUsuarios()));
                return;
            }

            String[] parts = pathInfo.split("/");
            // ["", "5"]
            if (parts.length == 2) {
                int id = Integer.parseInt(parts[1]);
                Usuario u = service.getUsuario(id);
                if (u == null) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().write("{\"error\":\"Usuario no encontrado\"}");
                    return;
                }
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(gson.toJson(u));
                return;
            }

            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"error\":\"Ruta inválida\"}");

        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"ID inválido\"}");
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Error interno\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");

        // /api/admin/usuarios/{id}/estado
        String pathInfo = req.getPathInfo(); // "/5/estado"
        try {
            if (pathInfo == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"error\":\"Ruta inválida\"}");
                return;
            }

            String[] parts = pathInfo.split("/");
            // ["", "1", "estado"]
            if (parts.length == 3 && "estado".equals(parts[2])) {
                
                //Esto puede lanzar NumberFormatException
                int id = Integer.parseInt(parts[1]);

                /*@SuppressWarnings("unchecked")
                Map<String, String> body = gson.fromJson(req.getReader(), Map.class);
                // Validación extra por si envían un JSON vacío
                if (body == null || !body.containsKey("estado")) {
                    throw new IllegalArgumentException("El campo 'estado' es obligatorio");
                }
                String estado = body.get("estado");*/
                EstadoDTO body = gson.fromJson(req.getReader(), EstadoDTO.class);
                if (body == null || body.estado == null || body.estado.isBlank()) {
                    throw new IllegalArgumentException("El campo 'estado' es obligatorio");
                }

                service.cambiarEstado(id, body.estado);
                Usuario usuarioActualizado = service.getUsuario(id);
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(gson.toJson(usuarioActualizado));
                return;
            }

            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"error\":\"Ruta inválida\"}");

        // Captura errores al convertir el ID ("abc" a int
        } catch (NumberFormatException e) {
         
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"ID inválido: debe ser un número entero\"}");

        } catch (IllegalArgumentException e) {
            
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");

        } catch (Exception e) {
            // Captura cualquier otro error imprevisto
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Error interno\"}");
        }
    }

    // Clase extra para leer el JSON de forma segura
    private static class EstadoDTO {
        String estado;
    }
}
