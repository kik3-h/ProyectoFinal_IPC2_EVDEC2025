package com.vaqueras.service;

import com.vaqueras.dao.UsuarioDAO;
import com.vaqueras.model.Usuario;
import com.vaqueras.util.PasswordUtil;

public class UsuarioService {
    private final UsuarioDAO usuarioDAO = new UsuarioDAO(); //creando usuario nuevo

    public void registrarUsuario(Usuario u) {

        if (usuarioDAO.existsByEmailOrNickname(u.getEmail(), u.getNickname())) { //obtiene los datos
            throw new IllegalArgumentException("Usuario ya existe");
        }

        u.setPassword(PasswordUtil.hashPassword(u.getPassword()));
        usuarioDAO.save(u);
    }
}
