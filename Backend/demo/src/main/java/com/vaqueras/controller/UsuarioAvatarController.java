package com.vaqueras.controller;

import java.io.IOException;

import com.google.gson.Gson;
import com.vaqueras.dao.UsuarioAvatarDAO;
import com.vaqueras.model.TokenUser;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet("/api/usuarios/me/avatar")
@MultipartConfig(
    maxFileSize = 2 * 1024 * 1024,        // 2MB
    maxRequestSize = 3 * 1024 * 1024      // 3MB
)

public class UsuarioAvatarController extends HttpServlet{
    
    private final UsuarioAvatarDAO dao = new UsuarioAvatarDAO();
    private final Gson gson = new Gson();

    private Integer getAuthUserId(HttpServletRequest req) {
        TokenUser user = (TokenUser) req.getAttribute("AUTH_USER");
        return user == null ? null : user.getIdUser();
    }

    private boolean isAllowedMime(String mime) {
        if (mime == null) return false;
        return mime.equalsIgnoreCase("image/png")
            || mime.equalsIgnoreCase("image/jpeg")
            || mime.equalsIgnoreCase("image/webp");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");

        Integer idUser = getAuthUserId(req);
        if (idUser == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\":\"Token requerido\"}");
            return;
        }

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
                resp.getWriter().write("{\"error\":\"Tipo de imagen no permitido. Use PNG/JPEG/WEBP\"}");
                return;
            }

            byte[] bytes = file.getInputStream().readAllBytes();
            boolean ok = dao.updateAvatar(idUser, bytes, mime);

            if (!ok) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"error\":\"Usuario no encontrado\"}");
                return;
            }

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"message\":\"Avatar actualizado\"}");

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(gson.toJson(new Err("Error interno")));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");

        Integer idUser = getAuthUserId(req);
        if (idUser == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\":\"Token requerido\"}");
            return;
        }

        boolean ok = dao.clearAvatar(idUser);
        resp.setStatus(ok ? HttpServletResponse.SC_OK : HttpServletResponse.SC_NOT_FOUND);
        resp.getWriter().write(ok ? "{\"message\":\"Avatar eliminado\"}" : "{\"error\":\"Usuario no encontrado\"}");
    }

    private static class Err { final String error; Err(String e){this.error=e;} }
}
