package com.vaqueras.model;

import java.util.List;

public class ModeracionCategoriasRequest {
    
    private List<Integer> categorias;

    public ModeracionCategoriasRequest() {}

    public List<Integer> getCategorias() { return categorias; }
    public void setCategorias(List<Integer> categorias) { this.categorias = categorias; }
}
