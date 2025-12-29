package com.vaqueras.controller;

import java.io.IOException;

import com.google.gson.Gson;
import com.vaqueras.model.BannerPrincipal;
import com.vaqueras.service.BannerService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/admin/banners/*")
public class AdminBannerController extends HttpServlet {
    
    private final BannerService service = new BannerService();
    private final Gson gson = new Gson();

    // Listar todos los banners (para el panel de administración)
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");
        
        // Usamos listarAdmin() que llama a dao.findAll()
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(gson.toJson(service.listarAdmin()));
    }

    // Crear nuevo banner
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");
        try {
            // Convertimos el JSON directamente al modelo BannerPrincipal
            BannerPrincipal b = gson.fromJson(req.getReader(), BannerPrincipal.class);
            
            // El servicio se encarga de validar y llamar al DAO
            int id = service.crear(b);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write("{\"message\":\"Banner creado correctamente\",\"idBanner\":" + id + "}");

        } catch (IllegalArgumentException e) {
            // Captura validaciones del servicio (URL vacía, posición negativa, etc.)
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Error interno del servidor\"}");
        }
    }

    // Actualizar banner existente (/api/admin/banners/{id})
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");
        
        String pathInfo = req.getPathInfo(); // Esperamos "/{id}"
        
        try {
            int id = parseId(pathInfo);
            BannerPrincipal b = gson.fromJson(req.getReader(), BannerPrincipal.class);
            
            service.actualizar(id, b);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"message\":\"Banner actualizado correctamente\"}");

        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"ID de banner inválido\"}");
        } catch (IllegalArgumentException e) {
            // Captura si el banner no existe o datos inválidos
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // O Not Found si prefieres diferenciar
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Error interno del servidor\"}");
        }
    }

    //  Eliminar banner (/api/admin/banners/{id})
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");
        
        String pathInfo = req.getPathInfo();
        
        try {
            int id = parseId(pathInfo);
            
            service.eliminar(id);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"message\":\"Banner eliminado correctamente\"}");

        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"ID de banner inválido\"}");
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Error interno del servidor\"}");
        }
    }

    // Método auxiliar para extraer ID de la URL
    private int parseId(String pathInfo) {
        if (pathInfo == null || pathInfo.equals("/") || pathInfo.isBlank()) {
            throw new NumberFormatException();
        }
        String[] parts = pathInfo.split("/");
        if (parts.length < 2) {
            throw new NumberFormatException();
        }
        return Integer.parseInt(parts[1]);
    }
}
