package com.vaqueras.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.vaqueras.config.DatabaseConfig;
import com.vaqueras.dao.CarteraDAO;
import com.vaqueras.dao.RecargaCarteraDAO;
import com.vaqueras.model.CarteraDTO;
import com.vaqueras.model.RecargaDTO;

public class CarteraService {
    
    private static final BigDecimal MAX_RECARGA = new BigDecimal("1000000.00");

    private final DatabaseConfig db = new DatabaseConfig();
    private final CarteraDAO carteraDAO = new CarteraDAO();
    private final RecargaCarteraDAO recargaDAO = new RecargaCarteraDAO();

    public CarteraDTO getCartera(int idUser) {
        try (Connection conn = db.conectar()) {
            conn.setAutoCommit(false);
            carteraDAO.ensureExists(conn, idUser);
            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Error asegurando cartera: " + e.getMessage(), e);
        }

        CarteraDTO c = carteraDAO.findByUser(idUser);
        if (c == null) throw new RuntimeException("Cartera no encontrada (inconsistencia)");
        return c;
    }

    public CarteraDTO recargar(int idUser, BigDecimal monto) {
        validarMonto(monto);

        Connection conn = null;
        try {
            conn = db.conectar();
            conn.setAutoCommit(false);

            carteraDAO.ensureExists(conn, idUser);
            recargaDAO.insert(conn, idUser, monto);
            carteraDAO.addSaldo(conn, idUser, monto);

            conn.commit();
            return carteraDAO.findByUser(idUser);

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ignored) {}
            }
            throw new RuntimeException("Error recargando cartera: " + e.getMessage(), e);

        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ignored) {}
            }
        }
    }

    public List<RecargaDTO> historial(int idUser, int limit) {
        return recargaDAO.listByUser(idUser, limit);
    }

    private void validarMonto(BigDecimal monto) {
        if (monto == null) throw new IllegalArgumentException("monto requerido");
        if (monto.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("monto debe ser > 0");
        if (monto.compareTo(MAX_RECARGA) > 0) throw new IllegalArgumentException("monto excede el límite lógico permitido");
    }
}
