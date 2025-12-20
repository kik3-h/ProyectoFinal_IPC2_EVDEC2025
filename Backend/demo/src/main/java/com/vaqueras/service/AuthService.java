package com.vaqueras.service;

import com.vaqueras.dao.UsuarioDAO;
import com.vaqueras.model.LoginResponse;
import com.vaqueras.model.Usuario;
import com.vaqueras.util.JwtUtil;
import com.vaqueras.util.PasswordUtil;

public class AuthService {
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public LoginResponse login(String identifier, String rawPassword) {
        if (identifier == null || identifier.isBlank() || rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("Credenciales incompletas");
        }

        Usuario u = usuarioDAO.findForLogin(identifier);
        if (u == null) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }

        String hashed = PasswordUtil.hashPassword(rawPassword);
        if (!hashed.equals(u.getPassword())) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }

        String token = JwtUtil.generateToken(u.getIdUser(), u.getNickname(), u.getRol());
        return new LoginResponse(token, u.getIdUser(), u.getNickname(), u.getEmail(), u.getRol());
    }
}
