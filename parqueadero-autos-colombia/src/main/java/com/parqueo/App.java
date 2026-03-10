package com.parqueo;

import com.parqueo.util.ConexionBD;

/**
 * Hello world!
 *
 */
public class App {
   public static void main(String[] args) {
        System.out.println("🚗 SISTEMA DE PARQUEADERO AUTOS COLOMBIA");
        System.out.println("========================================");
        
        // Probar conexión a BD
        ConexionBD.testConnection();
        
        // Aquí irá el menú principal después
        System.out.println("\n✨ Sistema listo para desarrollar");
    }
}
