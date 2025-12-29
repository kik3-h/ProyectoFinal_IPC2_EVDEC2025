package com.vaqueras.controller;

import java.io.IOException;

import com.google.gson.Gson;
import com.vaqueras.service.BannerService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/api/banners")

public class PublicBannerController extends HttpServlet {
    
    private final BannerService service = new BannerService();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");
        resp.setStatus(200);
        resp.getWriter().write(gson.toJson(service.listarPublico()));
    }
}
