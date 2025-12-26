package com.vaqueras.service;

import java.util.List;

import com.vaqueras.dao.UsuarioDAO;
import com.vaqueras.model.Usuario;

public class AdminUsuarioService {
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public List<Usuario> listarUsuarios() {
        return usuarioDAO.findAll();
    }

    public Usuario getUsuario(int id) {
        return usuarioDAO.findById(id);
    }

    public void cambiarEstado(int id, String estado) {
        if (estado == null ||!"ACTIVO".equals(estado) && !"BLOQUEADO".equals(estado)) {
            throw new IllegalArgumentException("estado debe ser ACTIVO o BLOQUEADO");
        }

        boolean ok = usuarioDAO.updateEstadoCuenta(id, estado);
        if (!ok) {
            throw new IllegalArgumentException("No se pudo actualizar o Usuario no encontrado");
        }
    }
}
