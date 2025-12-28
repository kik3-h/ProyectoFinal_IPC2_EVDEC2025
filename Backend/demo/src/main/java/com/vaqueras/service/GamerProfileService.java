package com.vaqueras.service;

import com.vaqueras.dao.UsuarioDAO;
import com.vaqueras.model.GamerProfileDTO;
import com.vaqueras.model.GamerProfileUpdateRequest;

public class GamerProfileService {
    
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public GamerProfileDTO getPerfil(int idUser) {
        return usuarioDAO.findGamerProfileById(idUser);
    }

    public void updatePerfil(int idUser, GamerProfileUpdateRequest req) {
        if (req == null) throw new IllegalArgumentException("Body requerido");
        boolean ok = usuarioDAO.updateGamerProfile(idUser, req.getTelefono(), req.getPais(), req.getBibliotecaPublica());
        if (!ok) throw new IllegalArgumentException("No hay campos para actualizar");
    }
}
