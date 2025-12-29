package com.vaqueras.controller;

import java.io.IOException;

import com.google.gson.Gson;
import com.vaqueras.model.Categoria;
import com.vaqueras.service.CategoriaService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
//en este servlet podemos ver todas las categorias creadas, crear nuevas categorias, actualizar y eliminar categorias en los otros servlets no
@WebServlet("/api/admin/categorias/*")
public class AdminCategoriaController extends HttpServlet {
    private final CategoriaService service = new CategoriaService();
    private final Gson gson = new Gson();

    // GET para listar todas las categorías para la tabla de administración
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");
        // Usamos listarPublico() porque devuelve todas las categorías ordenadas por nombre
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(gson.toJson(service.listarPublico()));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");       
        try {
            Categoria c = gson.fromJson(req.getReader(), Categoria.class);
            int id = service.crear(c);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write("{\"message\":\"Categoria creada correctamente\",\"idCategoria\":" + id + "}");

        } catch (IllegalStateException e) { // Captura nombres duplicados
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (IllegalArgumentException e) { // Captura validaciones vacías/nulas
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Error interno del servidor\"}");
        }
    }

    // PUT para actualizar una categoría existente (/api/admin/categorias/{id})
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");
        String pathInfo = req.getPathInfo(); 

        try {
            int id = parseId(pathInfo);
            Categoria c = gson.fromJson(req.getReader(), Categoria.class);
            
            service.actualizar(id, c);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"message\":\"Categoría actualizada correctamente\"}");

        } catch (IllegalStateException e) { // Nombres duplicados
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"ID de categoría inválido\"}");
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Error interno del servidor\"}");
        }
    }

    // DELETE en Eliminar una categoría (/api/admin/categorias/{id})
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");
        String pathInfo = req.getPathInfo();

        try {
            int id = parseId(pathInfo);
            
            service.eliminar(id);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"message\":\"Categoría eliminada correctamente\"}");

        } catch (IllegalStateException e) { 
            // Esto ocurre si la categoría está en uso por un videojuego (FK Restrict)
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"ID de categoría inválido\"}");
        } catch (IllegalArgumentException e) { // Si no existe el ID
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Error interno del servidor\"}");
        }
    }

    // Método auxiliar para extraer el ID de la URL
    private int parseId(String pathInfo) {
        if (pathInfo == null || pathInfo.equals("/") || pathInfo.isBlank()) {
            throw new NumberFormatException();
        }
        String[] parts = pathInfo.split("/");
        // parts[0] es vacío, parts[1] es el ID
        if (parts.length < 2) {
            throw new NumberFormatException();
        }
        return Integer.parseInt(parts[1]);
    }
}
