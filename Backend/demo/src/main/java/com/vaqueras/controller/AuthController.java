package com.vaqueras.controller;

import java.io.IOException;

import com.google.gson.Gson;
import com.vaqueras.model.LoginRequest;
import com.vaqueras.model.LoginResponse;
import com.vaqueras.service.AuthService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/auth/login")
public class AuthController extends HttpServlet{

    private final AuthService authService = new AuthService();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");

        try {
            LoginRequest body = gson.fromJson(req.getReader(), LoginRequest.class);

            if (body == null) {
                throw new IllegalArgumentException("El cuerpo de la solicitud está vacío");
            }
            
            LoginResponse login = authService.login(body); // Debe incluir token
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(gson.toJson(login));

        } catch (SecurityException e) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\":\"" + escape(e.getMessage()) + "\"}");

        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"" + escape(e.getMessage()) + "\"}");

        } catch (Exception e) {
            // 500 - Error interno del servidor
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Error interno del servidor\"}");
        }
    }

    private String escape(String s) {
        return s == null ? "" : s.replace("\"", "\\\"");
    }
}
