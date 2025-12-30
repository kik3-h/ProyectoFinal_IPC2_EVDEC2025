package com.vaqueras.controller;

import java.io.IOException;

import com.vaqueras.dao.BannerDAO;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet("/api/admin/banners/imagen/*")
@MultipartConfig(maxFileSize = 3 * 1024 * 1024, maxRequestSize = 4 * 1024 * 1024)

public class AdminBannerImageController extends HttpServlet{
    
    private final BannerDAO dao = new BannerDAO();

    private boolean isAllowedMime(String mime) {
        if (mime == null) return false;
        return mime.equalsIgnoreCase("image/png")
            || mime.equalsIgnoreCase("image/jpeg")
            || mime.equalsIgnoreCase("image/webp");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");

        String path = req.getPathInfo(); // "/{id}"
        if (path == null || !path.matches("/\\d+")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"ID inválido\"}");
            return;
        }

        int id = Integer.parseInt(path.substring(1));

        try {
            Part file = req.getPart("file");
            if (file == null || file.getSize() <= 0) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\":\"Archivo requerido (file)\"}");
                return;
            }

            String mime = file.getContentType();
            if (!isAllowedMime(mime)) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\":\"Tipo inválido. Use PNG/JPEG/WEBP\"}");
                return;
            }

            byte[] bytes = file.getInputStream().readAllBytes();
            boolean ok = dao.updateBlob(id, bytes, mime);

            resp.setStatus(ok ? HttpServletResponse.SC_OK : HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write(ok ? "{\"message\":\"Imagen de banner actualizada\"}" : "{\"error\":\"Banner no encontrado\"}");

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Error interno\"}");
        }
    }
}
