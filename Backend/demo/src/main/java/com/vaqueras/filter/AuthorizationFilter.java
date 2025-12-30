package com.vaqueras.filter;

import java.io.IOException;

import com.google.gson.Gson;
import com.vaqueras.model.TokenUser;
import com.vaqueras.service.TokenBlacklistService;
import com.vaqueras.util.JwtUtil;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter("/api/*")
public class AuthorizationFilter implements Filter {
    private final Gson gson = new Gson();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        resp.setContentType("application/json; charset=UTF-8");

        // Preflight (CORS)
        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        String path = getPath(req);

        // rutas Públicos
        if (isPublic(path, req.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        // valido que exista el token
        String token = JwtUtil.extractBearerToken(req);
        if (token == null || !JwtUtil.isValid(token)) {
            deny(resp, HttpServletResponse.SC_UNAUTHORIZED, "Token faltante/ inválido");
            return;
        }
        // valido si el usuario hizo logout previamente
        if (TokenBlacklistService.isRevoked(token)) {
            deny(resp, HttpServletResponse.SC_UNAUTHORIZED, "Sesión cerrada (Token revocado)");
            return;
        }
        //Obtener Usuario y Validar
        TokenUser user = JwtUtil.getUserFromToken(token);

        if (user == null) {
            deny(resp, HttpServletResponse.SC_UNAUTHORIZED, "Token inválido o expirado");
            return;
        }

        // validacion de roles
        String required = requiredRole(path);
        if (required != null && !required.equalsIgnoreCase(user.getRol())) {
            deny(resp, HttpServletResponse.SC_FORBIDDEN, "Acceso denegado: Rol insuficiente");
            return;
        }

        // Inyectar usuario para los Controllers
        req.setAttribute("AUTH_USER", user);

        chain.doFilter(request, response);
    }

    private boolean isPublic(String path, String method) {
        if (path.equals("/api/auth/login")) return true;
        if (path.equals("/api/auth/logout")) return true;
        if (path.equals("/api/usuarios") && "POST".equalsIgnoreCase(method)) return true;
        if ("GET".equalsIgnoreCase(method)) {
            
            //Permitir acceso público a las imágenes 
            if (path.startsWith("/api/banners/imagen")) return true;
            if (path.startsWith("/api/multimedia/imagen")) return true;
            if (path.startsWith("/api/usuarios/avatar")) return true;

            // alfinal por errores uso startsWith para permitir
            // - /api/empresas (Lista)
            // - /api/empresas/5 (Detalle por ID)
            if (path.startsWith("/api/empresas")) return true;            
            if (path.startsWith("/api/categorias")) return true;
            if (path.startsWith("/api/banner")) return true;
            
            // Si los videojuegos también son públicos para ver sin login:
            if (path.startsWith("/api/videojuegos") && "GET".equalsIgnoreCase(method)) return true;            
        }        
        return false;
    }

    private String requiredRole(String path) {
        if (path.startsWith("/api/admin/") || path.equals("/api/admin")) return "ADMIN";
        if (path.startsWith("/api/empresa/") || path.equals("/api/empresa")) return "EMPRESA";
        if (path.startsWith("/api/gamer/") || path.equals("/api/gamer")) return "GAMER";
        return null; 
    }

    private String getPath(HttpServletRequest req) {
        String uri = req.getRequestURI();      // /vaqueras-backend/api/...
        String ctx = req.getContextPath();     // /vaqueras-backend
        return (ctx != null && !ctx.isEmpty()) ? uri.substring(ctx.length()) : uri;
    }

    private void deny(HttpServletResponse resp, int code, String msg) throws IOException {
        resp.setStatus(code);
        resp.getWriter().write(gson.toJson(new Err(msg)));
    }

    private static class Err {
        final String error;
        Err(String error) { this.error = error; }
    }
}
