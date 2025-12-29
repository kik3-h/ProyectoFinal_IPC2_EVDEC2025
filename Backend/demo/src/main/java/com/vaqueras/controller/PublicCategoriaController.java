package com.vaqueras.controller;

import java.io.IOException;

import com.google.gson.Gson;
import com.vaqueras.service.AdminCategoriaService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
//servlet que me permite ver las categorias creadas unicamente de manera publica sin rol asignado
@WebServlet("/api/public/categorias")

public class PublicCategoriaController extends HttpServlet{
    
    private final AdminCategoriaService service = new AdminCategoriaService();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");
        resp.setStatus(200);
        resp.getWriter().write(gson.toJson(service.list()));
    }
}
