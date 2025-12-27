package com.vaqueras.controller;

import java.io.IOException;
import java.time.LocalDate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vaqueras.exception.ConflictException;
import com.vaqueras.model.Empresa;
import com.vaqueras.model.EmpresaCreateRequest;
import com.vaqueras.model.EmpresaUpdateRequest;
import com.vaqueras.model.TokenUser;
import com.vaqueras.service.EmpresaService;
import com.vaqueras.util.JwtUtil;
import com.vaqueras.util.LocalDateAdapter;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/empresas/*")
public class EmpresaController extends HttpServlet{
    private final EmpresaService empresaService = new EmpresaService();

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");

        String path = req.getPathInfo(); // null, "/", "/{id}"

        try {
            if (path == null || "/".equals(path)) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(gson.toJson(empresaService.listAll()));
                return;
            }

            int idEmpresa = parseId(path);
            Empresa e = empresaService.getById(idEmpresa);

            if (e == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"error\":\"Empresa no encontrada\"}");
                return;
            }

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(gson.toJson(e));

        } catch (IllegalArgumentException ex) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"" + ex.getMessage() + "\"}");
        } catch (Exception ex) {
            ex.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Error interno\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");

        // Solo ADMIN puede crear empresa
        TokenUser auth = getAuthUser(req);
        if (auth == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\":\"Token requerido\"}");
            return;
        }
        if (!"ADMIN".equalsIgnoreCase(auth.getRol())) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.getWriter().write("{\"error\":\"No autorizado para crear empresas\"}");
            return;
        }

        try {
            EmpresaCreateRequest body = gson.fromJson(req.getReader(), EmpresaCreateRequest.class);
            Empresa creada = empresaService.createEmpresaConAdmin(body);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(gson.toJson(creada));

        } catch (ConflictException ex) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().write("{\"error\":\"" + ex.getMessage() + "\"}");
        } catch (IllegalArgumentException ex) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"" + ex.getMessage() + "\"}");
        } catch (Exception ex) {
            ex.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Error interno\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");

        TokenUser auth = getAuthUser(req);
        if (auth == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\":\"Token requerido\"}");
            return;
        }

        try {
            String path = req.getPathInfo();
            int idEmpresa = parseId(path);

            // ADMIN o dueño (EMPRESA ligada a esa empresa)
            boolean permitido = "ADMIN".equalsIgnoreCase(auth.getRol())
                    || ("EMPRESA".equalsIgnoreCase(auth.getRol()) && empresaService.isOwner(auth.getIdUser(), idEmpresa));

            if (!permitido) {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                resp.getWriter().write("{\"error\":\"No autorizado para editar esta empresa\"}");
                return;
            }

            EmpresaUpdateRequest body = gson.fromJson(req.getReader(), EmpresaUpdateRequest.class);
            boolean ok = empresaService.updateEmpresa(idEmpresa, body);

            if (!ok) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\":\"No hay campos para actualizar\"}");
                return;
            }

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"message\":\"Empresa actualizada\"}");

        } catch (IllegalArgumentException ex) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"" + ex.getMessage() + "\"}");
        } catch (ConflictException ex) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().write("{\"error\":\"" + ex.getMessage() + "\"}");
        } catch (Exception ex) {
            ex.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Error interno\"}");
        }
    }

    private int parseId(String pathInfo) {
        if (pathInfo == null || pathInfo.equals("/") || !pathInfo.matches("/\\d+")) {
            throw new IllegalArgumentException("ID inválido en la ruta");
        }
        return Integer.parseInt(pathInfo.substring(1));
    }

    private TokenUser getAuthUser(HttpServletRequest req) {
        Object attr = req.getAttribute("auth.user");
        if (attr instanceof TokenUser) return (TokenUser) attr;

        String token = JwtUtil.extractBearerToken(req);
        if (token == null) return null;

        return JwtUtil.getUserFromToken(token); // tu JwtUtil ya valida firma/exp
    }
}
