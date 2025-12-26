package com.vaqueras.service;

import java.util.List;

import com.vaqueras.dao.BannerDAO;
import com.vaqueras.model.BannerPrincipal;

public class BannerService {
    private final BannerDAO dao = new BannerDAO();

    public List<BannerPrincipal> listarPublico() {
        return dao.findActive();
    }

    public List<BannerPrincipal> listarAdmin() {
        return dao.findAll();
    }

    public int crear(BannerPrincipal b) {
        validar(b);
        if (b.getImagenUrl().trim().isEmpty()) throw new IllegalArgumentException("imagenUrl es obligatoria");
        b.setImagenUrl(b.getImagenUrl().trim());
        return dao.create(b);
    }

    public void actualizar(int id, BannerPrincipal b) {
        validar(b);

        BannerPrincipal actual = dao.findById(id);
        if (actual == null) throw new IllegalArgumentException("Banner no encontrado");

        b.setIdBanner(id);
        b.setImagenUrl(b.getImagenUrl().trim());

        boolean ok = dao.update(b);
        if (!ok) throw new IllegalArgumentException("Banner no encontrado");
    }

    public void eliminar(int id) {
        BannerPrincipal actual = dao.findById(id);
        if (actual == null) throw new IllegalArgumentException("Banner no encontrado");

        boolean ok = dao.delete(id);
        if (!ok) throw new IllegalArgumentException("Banner no encontrado");
    }

    private void validar(BannerPrincipal b) {
        if (b == null) throw new IllegalArgumentException("Body requerido");
        if (b.getImagenUrl() == null || b.getImagenUrl().trim().isEmpty())
            throw new IllegalArgumentException("imagenUrl es obligatoria");
        if (b.getImagenUrl().length() > 255)
            throw new IllegalArgumentException("imagenUrl máximo 255 caracteres");
        if (b.getPosicion() <= 0)
            throw new IllegalArgumentException("posicion debe ser > 0");
        // idVideojuego puede ser null
    }
}
