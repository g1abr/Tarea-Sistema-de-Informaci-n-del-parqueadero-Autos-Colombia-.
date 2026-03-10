package com.parqueo.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
     private static final String URL = "jdbc:mysql://localhost:3306/parqueadero_autos_colombia";
    private static final String USER = "root";  //  usuario de MySQL
    private static final String PASSWORD = "Juneam25";  // contraseña
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    public static void testConnection() {
        try (Connection conn = getConnection()) {
            System.out.println("Conexión exitosa a la base de datos");
        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }
    }
}
