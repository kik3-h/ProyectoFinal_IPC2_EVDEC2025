package com.vaqueras.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    
    //private static Connection conn;
    private final String URL =
            "jdbc:mysql://localhost:3306/db_vaqueras?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private final String USER = "root";
    private final String PASSWORD = "Kik3Xela1980rose";

    public Connection conectar() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establecer la conexión
            System.out.println("Conexión exitosa");
            return DriverManager.getConnection(URL, USER, PASSWORD);
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al conectar a la base de datos: " + e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver MySQL no encontrado: " + e.getMessage(), e);
        }
    }

    public void desconectar(Connection c) {
        if (c != null) {
            try {
                c.close();
                System.out.println("Conexión cerrada");
            } catch (SQLException e) {
                System.err.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
    }
}