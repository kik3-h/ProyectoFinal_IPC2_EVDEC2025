package com.vaqueras.controller;

import java.io.IOException;

import com.google.gson.Gson;
import com.vaqueras.model.AddMemberRequest;
import com.vaqueras.model.GrupoCreateRequest;
import com.vaqueras.model.InstalacionRequest;
import com.vaqueras.model.PrestamoCreateRequest;
import com.vaqueras.model.TokenUser;
import com.vaqueras.service.GrupoFamiliarService;
import com.vaqueras.service.PrestamoService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/gamer/grupos/*")

public class GrupoFamiliarController extends HttpServlet {
    
    private final GrupoFamiliarService grupoService = new GrupoFamiliarService();
    private final PrestamoService prestamoService = new PrestamoService();
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");

        Integer idUser = getAuthUserId(req);
        if (idUser == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\":\"Token requerido\"}");
            return;
        }

        String path = req.getPathInfo(); // null, "/", "/{id}/miembros"
        try {
            if (path == null || "/".equals(path)) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(gson.toJson(grupoService.listarGrupos(idUser)));
                return;
            }

            if (path.matches("/\\d+/miembros")) {
                int idGrupo = Integer.parseInt(path.split("/")[1]);
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(gson.toJson(grupoService.listarMiembros(idGrupo, idUser)));
                return;
            }

            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"error\":\"Ruta inválida\"}");

        } catch (SecurityException e) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");

        Integer idUser = getAuthUserId(req);
        if (idUser == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\":\"Token requerido\"}");
            return;
        }

        String path = req.getPathInfo(); // null, "/", "/{id}/miembros", "/{id}/prestamos"
        if (path == null) path = "/";

        try {
            // Crear grupo
            if ("/".equals(path)) {
                GrupoCreateRequest body = gson.fromJson(req.getReader(), GrupoCreateRequest.class);
                int idGrupo = grupoService.crearGrupo(idUser, body != null ? body.getNombreGrupo() : null);

                resp.setStatus(HttpServletResponse.SC_CREATED);
                resp.getWriter().write("{\"message\":\"Grupo creado\",\"idGrupo\":" + idGrupo + "}");
                return;
            }

            // Agregar miembro
            if (path.matches("/\\d+/miembros")) {
                int idGrupo = Integer.parseInt(path.split("/")[1]);
                AddMemberRequest body = gson.fromJson(req.getReader(), AddMemberRequest.class);

                grupoService.agregarMiembro(
                        idGrupo,
                        idUser,
                        body != null ? body.getIdUser() : null,
                        body != null ? body.getNickname() : null
                );

                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("{\"message\":\"Miembro agregado\"}");
                return;
            }

            // Prestar juego a miembro
            if (path.matches("/\\d+/prestamos")) {
                int idGrupo = Integer.parseInt(path.split("/")[1]);
                PrestamoCreateRequest body = gson.fromJson(req.getReader(), PrestamoCreateRequest.class);

                if (body == null) throw new IllegalArgumentException("Body requerido");
                prestamoService.prestar(idGrupo, idUser, body.getIdUserReceptor(), body.getIdVideojuego());

                resp.setStatus(HttpServletResponse.SC_CREATED);
                resp.getWriter().write("{\"message\":\"Préstamo creado\"}");
                return;
            }

            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"error\":\"Ruta inválida\"}");

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

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");

        Integer idUser = getAuthUserId(req);
        if (idUser == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\":\"Token requerido\"}");
            return;
        }

        // Ruta: /{idGrupo}/prestamos/{idVideojuego}/instalacion
        String path = req.getPathInfo();
        if (path == null || !path.matches("/\\d+/prestamos/\\d+/instalacion")) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"error\":\"Ruta inválida\"}");
            return;
        }

        try {
            String[] parts = path.split("/");
            int idGrupo = Integer.parseInt(parts[1]);
            int idVideojuego = Integer.parseInt(parts[3]);

            InstalacionRequest body = gson.fromJson(req.getReader(), InstalacionRequest.class);
            prestamoService.actualizarInstalacionPrestamo(idGrupo, idUser, idVideojuego, body != null ? body.getEstado() : null);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"message\":\"Estado de instalación actualizado\"}");

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

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");

        Integer idUser = getAuthUserId(req);
        if (idUser == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\":\"Token requerido\"}");
            return;
        }

        // Ruta: /{idGrupo}/prestamos/{idVideojuego}
        String path = req.getPathInfo();
        if (path == null || !path.matches("/\\d+/prestamos/\\d+")) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"error\":\"Ruta inválida\"}");
            return;
        }

        try {
            String[] parts = path.split("/");
            int idGrupo = Integer.parseInt(parts[1]);
            int idVideojuego = Integer.parseInt(parts[3]);

            prestamoService.devolverPrestamo(idGrupo, idUser, idVideojuego);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"message\":\"Préstamo devuelto\"}");

        } catch (SecurityException e) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
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
