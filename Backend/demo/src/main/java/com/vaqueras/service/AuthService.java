package com.vaqueras.service;

import com.vaqueras.dao.UsuarioDAO;
import com.vaqueras.model.LoginRequest;
import com.vaqueras.model.LoginResponse;
import com.vaqueras.model.Usuario;
import com.vaqueras.util.JwtUtil;
import com.vaqueras.util.PasswordUtil;

public class AuthService {
    private final UsuarioDAO usuarioDAO = new UsuarioDAO(); //creo una instancia del DAO

    public LoginResponse login(LoginRequest req) {    
        // Validar datos de entrada
        if (req == null || isBlank(req.getIdentifier()) || isBlank(req.getPassword())) {
            throw new IllegalArgumentException("Credenciales incompletas");
        }

        // Buscar usuario en DB
        Usuario u = usuarioDAO.findForLogin(req.getIdentifier());
        
        // Si no existe el usuario
        if (u == null) {
            // Usamos SecurityException para diferenciar errores de seguridad
            throw new SecurityException("Credenciales inválidas");
        }

        //Verificar contraseña (Hash)
        String hashed = PasswordUtil.hashPassword(req.getPassword());
        if (!hashed.equals(u.getPassword())) {
            throw new SecurityException("Credenciales inválidas");
        }
        // generar Token JWT 
        String token = JwtUtil.generateToken(u.getIdUser(), u.getNickname(), u.getRol());

        // 5. Retornar respuesta con Token (como en la v1)
        return new LoginResponse(
            token, 
            u.getIdUser(), 
            u.getNickname(), 
            u.getEmail(), 
            u.getRol()
        );
    }

    // Método auxiliar para validar cadenas vacías
    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    
}
