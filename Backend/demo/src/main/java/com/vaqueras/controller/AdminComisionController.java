package com.vaqueras.controller;

import java.io.IOException;

import com.google.gson.Gson;
import com.vaqueras.model.ComisionRequest;
import com.vaqueras.service.AdminComisionService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/admin/comisiones/*")

public class AdminComisionController extends HttpServlet{
    
    private final AdminComisionService service = new AdminComisionService();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");
        String path = req.getPathInfo();
        if (path == null) path = "/";

        try {
            if ("/global".equals(path)) {
                resp.setStatus(200);
                resp.getWriter().write("{\"porcentaje\":" + service.globalActual() + "}");
                return;
            }

            if (path.matches("/empresa/\\d+")) {
                int idEmpresa = Integer.parseInt(path.split("/")[2]);
                var pct = service.empresaActual(idEmpresa);
                if (pct == null) {
                    resp.setStatus(200);
                    resp.getWriter().write("{\"porcentaje\":null}");
                } else {
                    resp.setStatus(200);
                    resp.getWriter().write("{\"porcentaje\":" + pct + "}");
                }
                return;
            }

            resp.setStatus(404);
            resp.getWriter().write("{\"error\":\"Ruta inválida\"}");

        } catch (IllegalArgumentException e) {
            resp.setStatus(400);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            resp.getWriter().write("{\"error\":\"Error interno\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");
        String path = req.getPathInfo();
        if (path == null) path = "/";

        try {
            ComisionRequest body = gson.fromJson(req.getReader(), ComisionRequest.class);

            if ("/global".equals(path)) {
                service.setGlobal(body != null ? body.getPorcentaje() : null);
                resp.setStatus(201);
                resp.getWriter().write("{\"message\":\"Comisión global actualizada\"}");
                return;
            }

            if (path.matches("/empresa/\\d+")) {
                int idEmpresa = Integer.parseInt(path.split("/")[2]);
                service.setEmpresa(idEmpresa, body != null ? body.getPorcentaje() : null);

                resp.setStatus(201);
                resp.getWriter().write("{\"message\":\"Comisión de empresa actualizada\"}");
                return;
            }

            resp.setStatus(404);
            resp.getWriter().write("{\"error\":\"Ruta inválida\"}");

        } catch (IllegalArgumentException e) {
            resp.setStatus(400);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            resp.getWriter().write("{\"error\":\"Error interno\"}");
        }
    }
}
