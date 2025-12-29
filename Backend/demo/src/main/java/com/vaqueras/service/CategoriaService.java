package com.vaqueras.service;

import java.util.List;

import com.vaqueras.dao.CategoriaDAO;
import com.vaqueras.model.Categoria;

public class CategoriaService {
    
    private final CategoriaDAO dao = new CategoriaDAO();

    // LISTAR 
    public List<Categoria> listarPublico() {
        return dao.findAll();
    }

    public Categoria get(int id) {
        return dao.findById(id);
    }

    // CREAR
    public int crear(Categoria c) {
        validar(c);

        // Limpieza de datos
        c.setNombre(c.getNombre().trim());
        if (c.getDescripcion() != null) {
            c.setDescripcion(c.getDescripcion().trim());
        }

        // Llamamos a insert() y dejamos que el DAO maneje 
        // la excepción de duplicados internamente.
        return dao.insert(c); 
    }

    //  ACTUALIZAR
    public void actualizar(int id, Categoria c) {
        validar(c);

        // Verificar que exista el ID antes de intentar nada
        if (dao.findById(id) == null) {
            throw new IllegalArgumentException("Categoría no encontrada");
        }

        // Preparar objeto
        c.setIdCategoria(id);
        c.setNombre(c.getNombre().trim());
        if (c.getDescripcion() != null) {
            c.setDescripcion(c.getDescripcion().trim());
        }

        //Llamamos a update(). Si el nombre está duplicado, 
        // el DAO lanzará la excepción automáticamente.
        boolean ok = dao.update(c);
        
        if (!ok) throw new IllegalArgumentException("No se pudo actualizar la categoría");
    }

    // ELIMINAR
    public void eliminar(int id) {
        if (dao.findById(id) == null) {
            throw new IllegalArgumentException("Categoría no encontrada");
        }

        boolean ok = dao.delete(id);
        if (!ok) throw new IllegalArgumentException("No se pudo eliminar la categoría");
    }

    // VALIDACIONES BÁSICAS
    private void validar(Categoria c) {
        if (c == null) throw new IllegalArgumentException("El cuerpo de la solicitud es requerido");
        
        if (c.getNombre() == null || c.getNombre().trim().isEmpty())
            throw new IllegalArgumentException("El nombre es obligatorio");
            
        if (c.getNombre().trim().length() > 50)
            throw new IllegalArgumentException("El nombre no puede exceder 50 caracteres");

        if (c.getDescripcion() != null && c.getDescripcion().length() > 200)
            throw new IllegalArgumentException("La descripción no puede exceder 200 caracteres");
    }
}