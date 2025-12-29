package com.vaqueras.controller;

import java.io.IOException;

import com.google.gson.Gson;
import com.vaqueras.service.ReporteService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/admin/reportes/*")

public class AdminReporteController extends HttpServlet {
    
    private final ReporteService service = new ReporteService();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");

        String path = req.getPathInfo();
        if (path == null) path = "/";

        try {
            if ("/ventas/resumen".equals(path)) {
                String desde = req.getParameter("desde");
                String hasta = req.getParameter("hasta");
                Object[] r = service.resumenVentas(desde, hasta);

                String json = String.format(
                        "{\"totalVentas\":%d,\"montoTotal\":%s,\"ingresoPlataforma\":%s,\"ingresoEmpresas\":%s}",
                        (Integer) r[0], r[1].toString(), r[2].toString(), r[3].toString()
                );

                resp.setStatus(200);
                resp.getWriter().write(json);
                return;
            }

            if ("/top-juegos".equals(path)) {
                int limit = 10;
                String q = req.getParameter("limit");
                if (q != null && q.matches("\\d+")) limit = Integer.parseInt(q);

                resp.setStatus(200);
                resp.getWriter().write(gson.toJson(service.topJuegos(limit)));
                return;
            }

            if ("/top-empresas".equals(path)) {
                int limit = 10;
                String q = req.getParameter("limit");
                if (q != null && q.matches("\\d+")) limit = Integer.parseInt(q);

                resp.setStatus(200);
                resp.getWriter().write(gson.toJson(service.topEmpresas(limit)));
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
