package com.vaqueras.controller;

import java.io.IOException;

import com.vaqueras.dao.BannerDAO;
import com.vaqueras.model.ImageData;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/banners/imagen/*")

public class BannerImageController extends HttpServlet {
    
    private final BannerDAO dao = new BannerDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        if (path == null || !path.matches("/\\d+")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("application/json; charset=UTF-8");
            resp.getWriter().write("{\"error\":\"ID inválido\"}");
            return;
        }

        int id = Integer.parseInt(path.substring(1));
        ImageData img = dao.findBlob(id);

        if (img == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.setContentType("application/json; charset=UTF-8");
            resp.getWriter().write("{\"error\":\"Imagen no encontrada\"}");
            return;
        }

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType(img.mime());
        resp.getOutputStream().write(img.bytes());
    }
}
