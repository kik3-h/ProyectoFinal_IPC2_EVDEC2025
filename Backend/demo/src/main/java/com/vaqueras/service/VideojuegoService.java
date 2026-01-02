package com.vaqueras.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.vaqueras.config.DatabaseConfig;
import com.vaqueras.dao.CategoriaDAO;
import com.vaqueras.dao.JuegoCategoriaDAO;
import com.vaqueras.dao.MultimediaDAO;
import com.vaqueras.dao.UsuarioEmpresaDAO;
import com.vaqueras.dao.VideojuegoDAO;
import com.vaqueras.dao.VideojuegoReadDAO;
import com.vaqueras.model.VideojuegoCreateRequest;
import com.vaqueras.model.VideojuegoDetailDTO;
import com.vaqueras.model.VideojuegoPublicDTO;
import com.vaqueras.model.VideojuegoUpdateRequest;

public class VideojuegoService {
    
    private final DatabaseConfig db = new DatabaseConfig();

    private final UsuarioEmpresaDAO usuarioEmpresaDAO = new UsuarioEmpresaDAO();
    private final VideojuegoDAO videojuegoDAO = new VideojuegoDAO();
    private final JuegoCategoriaDAO juegoCategoriaDAO = new JuegoCategoriaDAO();
    private final MultimediaDAO multimediaDAO = new MultimediaDAO();
    private final VideojuegoReadDAO readDAO = new VideojuegoReadDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

    public List<VideojuegoPublicDTO> listarPublico() {
        return videojuegoDAO.listPublicActivos();
    }

    public VideojuegoDetailDTO detallePublico(int idVideojuego) {
        VideojuegoDetailDTO base = videojuegoDAO.getBaseDetail(idVideojuego);
        if (base == null) return null;

        base.setCategorias(readDAO.findCategoriasByVideojuego(idVideojuego));
        base.setMultimedia(multimediaDAO.findByVideojuego(idVideojuego));
        return base;
    }

    public int crear(int idUserEmpresa, VideojuegoCreateRequest req) {
        validarCreate(req);

        Integer idEmpresa = usuarioEmpresaDAO.findFirstEmpresaIdByUser(idUserEmpresa);
        if (idEmpresa == null) {
            throw new IllegalArgumentException("El usuario EMPRESA no está asociado a ninguna empresa");
        }

        // Validar categorías (si vienen)
        if (req.getCategoriaIds() != null) {
            for (Integer idCat : req.getCategoriaIds()) {
                if (idCat == null || categoriaDAO.findById(idCat) == null) {
                    throw new IllegalArgumentException("Categoría inválida: " + idCat);
                }
            }
        }

        Connection conn = null;
        try {
            conn = db.conectar();
            conn.setAutoCommit(false);

            int idVideojuego = videojuegoDAO.insert(
                    conn,
                    idEmpresa,
                    req.getTitulo().trim(),
                    req.getDescripcion(),
                    req.getPrecio(),
                    req.getRecursosMinimos(),
                    req.getFechaLanzamiento(),
                    req.getClasificacionEdad(),
                    req.getEdadMinima(),
                    req.getEstado()
            );

            juegoCategoriaDAO.replaceCategorias(conn, idVideojuego, req.getCategoriaIds());
            multimediaDAO.replaceMultimedia(conn, idVideojuego, req.getPortadaUrl().trim(), req.getGaleriaUrls());

            conn.commit();
            return idVideojuego;

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ignored) {}
            }
            throw new RuntimeException("Error creando videojuego: " + e.getMessage(), e);

        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ignored) {}
            }
        }
    }

    public void actualizar(int idUserEmpresa, int idVideojuego, VideojuegoUpdateRequest req) {
        if (req == null) throw new IllegalArgumentException("Body requerido");

        Integer idEmpresa = usuarioEmpresaDAO.findFirstEmpresaIdByUser(idUserEmpresa);
        if (idEmpresa == null) throw new IllegalArgumentException("Usuario EMPRESA no asociado a empresa");

        if (!videojuegoDAO.belongsToEmpresa(idVideojuego, idEmpresa)) {
            throw new SecurityException("No autorizado: el juego no pertenece a tu empresa");
        }

        // Validar categorías si vienen
        if (req.getCategoriaIds() != null) {
            for (Integer idCat : req.getCategoriaIds()) {
                if (idCat == null || categoriaDAO.findById(idCat) == null) {
                    throw new IllegalArgumentException("Categoría inválida: " + idCat);
                }
            }
        }

        Connection conn = null;
        try {
            conn = db.conectar();
            conn.setAutoCommit(false);

            String clasif = (req.getClasificacionEdad() != null) ? req.getClasificacionEdad().trim() : null;
            if (clasif != null && !(clasif.equals("E") || clasif.equals("T") || clasif.equals("M"))) {
                throw new IllegalArgumentException("clasificacionEdad debe ser E, T o M");
            }

            // Validar Estado (ACTIVO / SUSPENDIDO)
            String estado = (req.getEstado() != null) ? req.getEstado().trim() : null;
            if (estado != null && !(estado.equals("ACTIVO") || estado.equals("SUSPENDIDO"))) {
                throw new IllegalArgumentException("El estado debe ser ACTIVO o SUSPENDIDO");
            }

            boolean ok = videojuegoDAO.update(
                    conn,
                    idVideojuego,
                    idEmpresa,
                    blankToNull(req.getTitulo()),
                    req.getDescripcion(),
                    req.getPrecio(),
                    req.getRecursosMinimos(),
                    req.getFechaLanzamiento(),
                    clasif,
                    req.getEdadMinima(),
                    req.getEstado()
            );

            if (!ok && req.getCategoriaIds() == null && req.getPortadaUrl() == null && req.getGaleriaUrls() == null) {
                throw new IllegalArgumentException("No hay campos para actualizar");
            }

            if (req.getCategoriaIds() != null) {
                juegoCategoriaDAO.replaceCategorias(conn, idVideojuego, req.getCategoriaIds());
            }

            // Si viene portadaUrl o galeriaUrls => reemplazamos multimedia completa
            if (req.getPortadaUrl() != null || req.getGaleriaUrls() != null) {
                String portada = (req.getPortadaUrl() != null) ? req.getPortadaUrl().trim() : null;
                if (portada == null || portada.isEmpty()) {
                    throw new IllegalArgumentException("portadaUrl es obligatoria al actualizar multimedia");
                }
                multimediaDAO.replaceMultimedia(conn, idVideojuego, portada, req.getGaleriaUrls());
            }

            conn.commit();

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ignored) {}
            }
            throw new RuntimeException("Error actualizando videojuego: " + e.getMessage(), e);

        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ignored) {}
            }
        }
    }

    public void suspender(int idUserEmpresa, int idVideojuego) {
        Integer idEmpresa = usuarioEmpresaDAO.findFirstEmpresaIdByUser(idUserEmpresa);
        if (idEmpresa == null) throw new IllegalArgumentException("Usuario EMPRESA no asociado a empresa");

        if (!videojuegoDAO.belongsToEmpresa(idVideojuego, idEmpresa)) {
            throw new SecurityException("No autorizado: el juego no pertenece a tu empresa");
        }

        Connection conn = null;
        try {
            conn = db.conectar();
            conn.setAutoCommit(false);

            boolean ok = videojuegoDAO.suspend(conn, idVideojuego, idEmpresa);
            if (!ok) throw new IllegalArgumentException("Videojuego no encontrado");

            conn.commit();

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ignored) {}
            }
            throw new RuntimeException("Error suspendiendo videojuego: " + e.getMessage(), e);

        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ignored) {}
            }
        }
    }

    private void validarCreate(VideojuegoCreateRequest req) {
        if (req == null) throw new IllegalArgumentException("Body requerido");
        if (req.getTitulo() == null || req.getTitulo().trim().isEmpty()) throw new IllegalArgumentException("titulo requerido");
        if (req.getTitulo().length() > 150) throw new IllegalArgumentException("titulo máximo 150");
        if (req.getPrecio() < 0) throw new IllegalArgumentException("precio debe ser >= 0");

        if (req.getClasificacionEdad() == null) throw new IllegalArgumentException("clasificacionEdad requerida");
        String c = req.getClasificacionEdad().trim();
        if (!(c.equals("E") || c.equals("T") || c.equals("M"))) throw new IllegalArgumentException("clasificacionEdad debe ser E, T o M");
        req.setClasificacionEdad(c);

        if (req.getEdadMinima() <= 0) throw new IllegalArgumentException("edadMinima debe ser > 0");

        //  VALIDACIÓN DE ESTADO en mi jeugo al crearse
        if (req.getEstado() == null || req.getEstado().trim().isEmpty()) {
            throw new IllegalArgumentException("estado requerido");
        }
        String estado = req.getEstado().trim();
        if (!(estado.equals("ACTIVO") || estado.equals("SUSPENDIDO"))) {
            throw new IllegalArgumentException("El estado debe ser ACTIVO o SUSPENDIDO");
        }

        if (req.getPortadaUrl() == null || req.getPortadaUrl().trim().isEmpty())
            throw new IllegalArgumentException("portadaUrl requerida");
        if (req.getPortadaUrl().length() > 255) throw new IllegalArgumentException("portadaUrl máximo 255");
    }

    private String blankToNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
//nuevo metodo para listar los videojuegos de la empresa del usuario logueado ptm
    public List<com.vaqueras.model.VideojuegoEmpresaDTO> listarMisVideojuegos(int idUserEmpresa) {
    Integer idEmpresa = usuarioEmpresaDAO.findFirstEmpresaIdByUser(idUserEmpresa);
    if (idEmpresa == null) {
        throw new IllegalArgumentException("Usuario EMPRESA no asociado a empresa");
    }
    return videojuegoDAO.listByEmpresa(idEmpresa);
}

public VideojuegoDetailDTO detalleMiVideojuego(int idUserEmpresa, int idVideojuego) {
    Integer idEmpresa = usuarioEmpresaDAO.findFirstEmpresaIdByUser(idUserEmpresa);
    if (idEmpresa == null) {
        throw new IllegalArgumentException("Usuario EMPRESA no asociado a empresa");
    }

    if (!videojuegoDAO.belongsToEmpresa(idVideojuego, idEmpresa)) {
        throw new SecurityException("No autorizado: el juego no pertenece a tu empresa");
    }

    // Reutilizamos el detalle ya armado categorías + multimedia
    VideojuegoDetailDTO d = detallePublico(idVideojuego);
    return d; // puede ser null si no existe
}
    //metodo nuevo para eliminar permanentemente un videojuego de la DB
    public void eliminarPermanente(int idUserEmpresa, int idVideojuego) {
        Integer idEmpresa = usuarioEmpresaDAO.findFirstEmpresaIdByUser(idUserEmpresa);
        if (idEmpresa == null) throw new IllegalArgumentException("Usuario EMPRESA no asociado a empresa");

        // Verificamos pertenencia
        if (!videojuegoDAO.belongsToEmpresa(idVideojuego, idEmpresa)) {
            throw new SecurityException("No autorizado: el juego no pertenece a tu empresa");
        }

        Connection conn = null;
        try {
            conn = db.conectar();
            conn.setAutoCommit(false);

            // Llamamos al DAO de borrado
            boolean ok = videojuegoDAO.deletePermanent(conn, idVideojuego, idEmpresa);
            
            if (!ok) throw new IllegalArgumentException("No se pudo eliminar el videojuego (no encontrado)");

            conn.commit();

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ignored) {}
            }
            throw new RuntimeException("Error eliminando videojuego: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ignored) {}
            }
        }
    }
}
