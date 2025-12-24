package com.vaqueras.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.vaqueras.model.TokenUser;
import com.vaqueras.util.JwtUtil;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/auth/session")
public class SessionController extends HttpServlet{
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");

        String token = JwtUtil.extractBearerToken(req);

        if (token == null || !JwtUtil.isValid(token)) {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"authenticated\":false}");
            return;
        }

        TokenUser user = JwtUtil.getUserFromToken(token);
        
        if (user == null) {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"authenticated\":false}");
            return;
        }

        Map<String, Object> out = new HashMap<>();
        out.put("authenticated", true);
        out.put("user", user);

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(gson.toJson(out));
    }
}
