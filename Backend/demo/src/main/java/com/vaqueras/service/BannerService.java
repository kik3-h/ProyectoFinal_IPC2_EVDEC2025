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

    // Método necesario para el Controller de Admin (Editar)
    public BannerPrincipal obtenerPorId(int id) {
        return dao.findById(id);
    }

    public int crear(BannerPrincipal b) {
        validar(b);
        // MODIFICADO: Si es null, asignamos cadena vacía ""
        String url = (b.getImagenUrl() == null) ? "" : b.getImagenUrl().trim();
        b.setImagenUrl(url);
        return dao.create(b);
    }

    public void actualizar(int id, BannerPrincipal b) {
        validar(b);

        BannerPrincipal actual = dao.findById(id);
        if (actual == null) throw new IllegalArgumentException("Banner no encontrado");

        b.setIdBanner(id);

        // MODIFICADO donde Si es null, asignamos cadena vacía ""
        String url = (b.getImagenUrl() == null) ? "" : b.getImagenUrl().trim();
        b.setImagenUrl(url);

        boolean ok = dao.update(b);
        if (!ok) throw new IllegalArgumentException("No se pudo actualizar el banner");
    }

    public void eliminar(int id) {
        BannerPrincipal actual = dao.findById(id);
        if (actual == null) throw new IllegalArgumentException("Banner no encontrado");

        boolean ok = dao.delete(id);
        if (!ok) throw new IllegalArgumentException("No se pudo eliminar el banner");
    }

    private void validar(BannerPrincipal b) {
        if (b == null) throw new IllegalArgumentException("Body requerido");
        // MODIFICADO: Ya no validamos que sea obligatorio ni vacío.
        // Solo validamos longitud SI es que trae algo.
        if (b.getImagenUrl() != null && b.getImagenUrl().length() > 255)
            throw new IllegalArgumentException("imagenUrl máximo 255 caracteres");
            
        if (b.getPosicion() <= 0)
            throw new IllegalArgumentException("posicion debe ser > 0");
        
        // idVideojuego puede ser null
    }
}
