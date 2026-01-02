package com.vaqueras.controller;

import java.io.IOException;

import com.vaqueras.dao.MultimediaDAO;
import com.vaqueras.model.ImageData;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// RUTA PÚBLICA: No debe ser interceptada por tu filtro de autenticación
@WebServlet("/api/public/imagenes/*")

public class PublicImageController extends HttpServlet{
    private final MultimediaDAO multimediaDAO = new MultimediaDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        
        // Obtener el ID de la URL /api/public/imagenes/5
        String path = req.getPathInfo(); // devuelve "/5"
        
        if (path == null || !path.matches("/\\d+")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de imagen inválido");
            return;
        }

        try {
            int idMultimedia = Integer.parseInt(path.substring(1)); 

            // Buscar los bytes en la Base de Datos
            ImageData img = multimediaDAO.findBlobById(idMultimedia);

            if (img == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Imagen no encontrada");
                return;
            }
            // Configurar la respuesta HTTP
            resp.setContentType(img.mime()); 
            resp.setContentLength(img.bytes().length);
            
            // Escribir los bytes al navegador
            resp.getOutputStream().write(img.bytes());

        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID debe ser numérico");
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error sirviendo imagen");
        }
    }
}
