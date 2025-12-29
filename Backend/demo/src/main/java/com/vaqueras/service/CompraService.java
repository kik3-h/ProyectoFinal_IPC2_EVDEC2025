package com.vaqueras.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import com.vaqueras.config.DatabaseConfig;
import com.vaqueras.dao.BibliotecaDAO;
import com.vaqueras.dao.CarteraDAO;
import com.vaqueras.dao.ComisionDAO;
import com.vaqueras.dao.CompraDAO;
import com.vaqueras.dao.UsuarioDAO;
import com.vaqueras.dao.VentaDAO;
import com.vaqueras.model.CompraRequest;
import com.vaqueras.model.CompraResponse;

public class CompraService {
    
    private final DatabaseConfig db = new DatabaseConfig();

    private final CompraDAO compraDAO = new CompraDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final CarteraDAO carteraDAO = new CarteraDAO();
    private final BibliotecaDAO bibliotecaDAO = new BibliotecaDAO();
    private final ComisionDAO comisionDAO = new ComisionDAO();
    private final VentaDAO ventaDAO = new VentaDAO();

    public CompraResponse comprar(int idUser, CompraRequest req) {
        if (req == null) throw new IllegalArgumentException("Body requerido");
        if (req.getIdVideojuego() <= 0) throw new IllegalArgumentException("idVideojuego inválido");

        LocalDateTime fechaCompra = parseFechaCompra(req.getFechaCompra());

        Connection conn = null;
        try {
            conn = db.conectar();
            conn.setAutoCommit(false);

            carteraDAO.ensureExists(conn, idUser);

            CompraDAO.JuegoCompraInfo juego = compraDAO.getJuegoInfo(conn, req.getIdVideojuego());
            if (juego == null) throw new IllegalArgumentException("Videojuego no existe");

            if (!"ACTIVO".equalsIgnoreCase(juego.estado)) {
                throw new IllegalArgumentException("Videojuego no está disponible para compra");
            }

            if (bibliotecaDAO.exists(conn, idUser, req.getIdVideojuego())) {
                throw new IllegalStateException("Ya tienes este juego");
            }

            LocalDate fn = usuarioDAO.getFechaNacimiento(conn, idUser);
            if (fn == null) throw new IllegalArgumentException("No se encontró fecha de nacimiento del usuario");

            int edad = Period.between(fn, fechaCompra.toLocalDate()).getYears();
            if (edad < juego.edadMinima) {
                throw new SecurityException("Transacción Bloqueada: No cumple con la edad mínima requerida");
            }

            BigDecimal precio = juego.precio.setScale(2, RoundingMode.HALF_UP);

            boolean pudo = carteraDAO.decrementIfEnough(conn, idUser, precio);
            if (!pudo) {
                throw new IllegalArgumentException("Saldo insuficiente");
            }

            BigDecimal pctEmpresa = comisionDAO.getEmpresaComision(conn, juego.idEmpresa);
            BigDecimal porcentajeAplicado = (pctEmpresa != null) ? pctEmpresa : comisionDAO.getGlobalComision(conn);
            String tipoComision = (pctEmpresa != null) ? "ESPECIFICA" : "GLOBAL";

            BigDecimal pct = porcentajeAplicado.setScale(2, RoundingMode.HALF_UP);
            BigDecimal retencion = precio.multiply(pct).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
            BigDecimal ingresoEmpresa = precio.subtract(retencion).setScale(2, RoundingMode.HALF_UP);

            int idVenta = ventaDAO.insert(
                    conn,
                    idUser,
                    juego.idEmpresa,
                    req.getIdVideojuego(),
                    Timestamp.valueOf(fechaCompra),
                    precio,
                    retencion,
                    ingresoEmpresa,
                    tipoComision,
                    pct
            );

            bibliotecaDAO.insert(conn, idUser, req.getIdVideojuego());

            BigDecimal saldoNuevo = carteraDAO.getSaldo(conn, idUser);

            conn.commit();

            CompraResponse res = new CompraResponse();
            res.setIdVenta(idVenta);
            res.setIdVideojuego(req.getIdVideojuego());
            res.setPrecioFinal(precio);
            res.setRetencionPlataforma(retencion);
            res.setIngresoEmpresa(ingresoEmpresa);
            res.setTipoComision(tipoComision);
            res.setPorcentajeAplicado(pct);
            res.setSaldoNuevo(saldoNuevo);
            return res;

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ignored) {}
            }
            throw new RuntimeException("Error en compra: " + e.getMessage(), e);

        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ignored) {}
            }
        }
    }

    private LocalDateTime parseFechaCompra(String s) {
        if (s == null || s.isBlank()) return LocalDateTime.now();

        String t = s.trim();
        // acepta "yyyy-MM-dd"
        if (t.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return LocalDate.parse(t).atStartOfDay();
        }
        // acepta "yyyy-MM-dd HH:mm:ss"
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(t, fmt);
    }
}
