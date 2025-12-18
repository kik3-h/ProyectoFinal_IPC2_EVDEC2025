package com.vaqueras.controller;

import java.io.IOException;
import java.time.LocalDate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vaqueras.model.Usuario;
import com.vaqueras.service.UsuarioService;
import com.vaqueras.util.LocalDateAdapter;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/usuarios")
public class UsuarioController extends HttpServlet {

    private final UsuarioService usuarioService = new UsuarioService(); // Servicio para manejar la lógica de usuarios
    // Gson configurado con adaptador para LocalDate
    private final Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException { // Manejar la creación de un nuevo usuario
            // servlet dopost par ainsertar la informacion del usuario
        Usuario u = gson.fromJson(req.getReader(), Usuario.class);

        try { // Intenta registrar el usuario
            usuarioService.registrarUsuario(u);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write("{\"message\":\"Usuario creado correctamente kike\"}");
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
