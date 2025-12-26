package com.vaqueras.service;

import java.util.List;

import com.vaqueras.dao.CategoriaDAO;
import com.vaqueras.model.Categoria;

public class CategoriaService {
    private final CategoriaDAO dao = new CategoriaDAO();

    public List<Categoria> listar() {
        return dao.findAll();
    }

    public Categoria get(int id) {
        return dao.findById(id);
    }

    public int crear(Categoria c) {
        validar(c);

        String nombre = c.getNombre().trim();
        c.setNombre(nombre);

        if (dao.existsByNombre(nombre)) {
            throw new IllegalArgumentException("Ya existe una categoría con ese nombre");
        }

        return dao.create(c);
    }

    public void actualizar(int id, Categoria c) {
        validar(c);

        Categoria actual = dao.findById(id);
        if (actual == null) throw new IllegalArgumentException("Categoría no encontrada");

        String nombre = c.getNombre().trim();
        c.setNombre(nombre);
        c.setIdCategoria(id);

        if (dao.existsByNombreExceptId(nombre, id)) {
            throw new IllegalArgumentException("Ya existe una categoría con ese nombre");
        }

        boolean ok = dao.update(c);
        if (!ok) throw new IllegalArgumentException("Categoría no encontrada");
    }

    public void eliminar(int id) {
        Categoria actual = dao.findById(id);
        if (actual == null) throw new IllegalArgumentException("Categoría no encontrada");

        boolean ok = dao.delete(id);
        if (!ok) throw new IllegalArgumentException("Categoría no encontrada");
    }

    private void validar(Categoria c) {
        if (c == null) throw new IllegalArgumentException("Body requerido");
        if (c.getNombre() == null || c.getNombre().trim().isEmpty())
            throw new IllegalArgumentException("nombre es obligatorio");
        if (c.getNombre().trim().length() > 50)
            throw new IllegalArgumentException("nombre máximo 50 caracteres");

        if (c.getDescripcion() != null && c.getDescripcion().length() > 200)
            throw new IllegalArgumentException("descripcion máximo 200 caracteres");
    }
}
