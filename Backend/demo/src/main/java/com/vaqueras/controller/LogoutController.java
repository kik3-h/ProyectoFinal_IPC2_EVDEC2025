package com.vaqueras.controller;

import java.io.IOException;

import com.vaqueras.service.TokenBlacklistService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/auth/logout")
public class LogoutController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");

        String token = (String) req.getAttribute("auth.token");
        Long exp = (Long) req.getAttribute("auth.exp");

        if (token != null && exp != null) {
            TokenBlacklistService.revoke(token, exp);
        }

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write("{\"message\":\"Logout realizado correctamente (se ha borrado el token del user)\"}");
    }
}
