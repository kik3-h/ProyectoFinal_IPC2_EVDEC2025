package com.vaqueras.service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import com.vaqueras.dao.AdminCategoriaDAO;

public class AdminCategoriaService {
    
    private final AdminCategoriaDAO dao = new AdminCategoriaDAO();

    public List<Object[]> list() { return dao.list(); }

    public int create(String nombre, String descripcion) throws Exception {
        String n = (nombre == null) ? "" : nombre.trim();
        if (n.isEmpty()) throw new IllegalArgumentException("nombre requerido");
        if (n.length() > 50) throw new IllegalArgumentException("nombre máximo 50");

        String d = (descripcion == null) ? null : descripcion.trim();
        if (d != null && d.length() > 200) throw new IllegalArgumentException("descripcion máximo 200");

        try {
            return dao.insert(n, d);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new IllegalStateException("Nombre de categoría ya existe");
        }
    }

    public void update(int id, String nombre, String descripcion) throws Exception {
        if (id <= 0) throw new IllegalArgumentException("id inválido");
        if (!dao.exists(id)) throw new IllegalArgumentException("Categoría no existe");

        String n = (nombre == null) ? "" : nombre.trim();
        if (n.isEmpty()) throw new IllegalArgumentException("nombre requerido");
        if (n.length() > 50) throw new IllegalArgumentException("nombre máximo 50");

        String d = (descripcion == null) ? null : descripcion.trim();
        if (d != null && d.length() > 200) throw new IllegalArgumentException("descripcion máximo 200");

        try {
            dao.update(id, n, d);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new IllegalStateException("Nombre de categoría ya existe");
        }
    }

    public void delete(int id) throws Exception {
        if (id <= 0) throw new IllegalArgumentException("id inválido");
        // Si está en uso por juego_categoria, MySQL lanzará error por FK RESTRICT
        dao.delete(id);
    }

    public boolean exists(int id) { return dao.exists(id); }
}
