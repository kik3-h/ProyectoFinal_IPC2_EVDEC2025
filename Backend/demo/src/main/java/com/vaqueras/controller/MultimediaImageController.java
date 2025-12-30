package com.vaqueras.controller;

import java.io.IOException;

import com.vaqueras.dao.MultimediaDAO;
import com.vaqueras.model.ImageData;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/multimedia/imagen/*")

public class MultimediaImageController extends HttpServlet{
    
    private final MultimediaDAO dao = new MultimediaDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo(); // "/{id}"
        
        if (path == null || !path.matches("/\\d+")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            int idMultimedia = Integer.parseInt(path.substring(1));
            
            ImageData img = dao.findBlobById(idMultimedia);

            if (img == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType(img.mime());
            resp.setContentLength(img.bytes().length);
            resp.getOutputStream().write(img.bytes());

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
