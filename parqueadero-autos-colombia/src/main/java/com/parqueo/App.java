package com.parqueo;

import java.util.List;
import java.util.Scanner;

import com.parqueo.controller.ParqueaderoController;
import com.parqueo.model.Celda;
import com.parqueo.model.Novedad;
import com.parqueo.model.RegistroParqueo;
import com.parqueo.model.Usuario;
import com.parqueo.model.Vehiculo;
import com.parqueo.model.Enum.TipoCelda;
import com.parqueo.model.Enum.TipoVehiculo;

public class App {

    static Scanner sc = new Scanner(System.in);
    static ParqueaderoController controller = new ParqueaderoController();

    public static void main(String[] args) {

        int opcion = -1;

        while (opcion != 0) {

            System.out.println("\n==============================");
            System.out.println("   SISTEMA PARQUEADERO");
            System.out.println("==============================");
            System.out.println("1. Usuarios");
            System.out.println("2. Vehiculos");
            System.out.println("3. Celdas");
            System.out.println("4. Controlador");
            System.out.println("0. Salir");

            opcion = leerOpcion(0,4);

            switch (opcion) {

                case 1 -> menuUsuarios();
                case 2 -> menuVehiculos();
                case 3 -> menuCeldas();
                case 4 -> menuControlador();

            }
        }

        System.out.println("Sistema cerrado.");
    }


    public static int leerEntero(String mensaje) {

        while (true) {

            try {

                System.out.print(mensaje);
                return Integer.parseInt(sc.nextLine());

            } catch (NumberFormatException e) {

                System.out.println(" Debe ingresar un número válido.");

            }

        }

    }

    public static int leerOpcion(int min, int max) {

        while (true) {

            int opcion = leerEntero("Seleccione una opcion: ");

            if (opcion >= min && opcion <= max) {
                return opcion;
            }

            System.out.println(" Opcion fuera del rango.");

        }
    }

    public static String leerTexto(String mensaje) {

        while (true) {

            System.out.print(mensaje);
            String texto = sc.nextLine().trim();

            if (!texto.isEmpty()) {
                return texto;
            }

            System.out.println("El campo no puede estar vacío.");

        }
    }

    public static TipoVehiculo leerTipoVehiculo() {

        while (true) {

            try {

                System.out.print("Tipo (CARRO/MOTO): ");
                return TipoVehiculo.valueOf(sc.nextLine().toUpperCase());

            } catch (Exception e) {

                System.out.println(" Tipo inválido. Escriba CARRO o MOTO.");

            }

        }
    }

    public static TipoCelda leerTipoCelda() {

        while (true) {

            try {

                System.out.print("Tipo celda (CARRO/MOTO): ");
                return TipoCelda.valueOf(sc.nextLine().toUpperCase());

            } catch (Exception e) {

                System.out.println("Tipo inválido.");

            }

        }
    }

  // MENU USUARIOS


    public static void menuUsuarios() {

        System.out.println("\n--- USUARIOS ---");
        System.out.println("1. Registrar");
        System.out.println("2. Actualizar");

        int op = leerOpcion(1,2);

        switch (op) {

            case 1 -> {

                Usuario u = new Usuario();

                u.setIdentificacion(leerTexto("Identificacion: "));
                u.setNombreCompleto(leerTexto("Nombre: "));
                u.setTelefono(leerTexto("Telefono: "));
                u.setEmail(leerTexto("Email: "));
                u.setDireccion(leerTexto("Direccion: "));

                if (u.registrar())
                    System.out.println(" Usuario registrado");
                else
                    System.out.println("Error registrando");

            }

            case 2 -> {

                Usuario u = new Usuario();

                u.setId(leerEntero("ID usuario: "));
                u.setNombreCompleto(leerTexto("Nombre: "));
                
                u.setTelefono(leerTexto("Nuevo telefono: "));
                u.setEmail(leerTexto("Nuevo email: "));
                u.setDireccion(leerTexto("Direccion: "));
                if (u.actualizar())
                    System.out.println("✅ Usuario actualizado");
                else
                    System.out.println("❌ Error actualizando");

            }

        }

    }

    // =========================
    // MENU VEHICULOS
    // =========================

    public static void menuVehiculos() {

        System.out.println("\n--- VEHICULOS ---");
        System.out.println("1. Crear vehiculo");
        System.out.println("2. Buscar por placa");

        int op = leerOpcion(1,2);

        switch (op) {

            case 1 -> {

                Vehiculo v = new Vehiculo();

                v.setPlaca(leerTexto("Placa: "));
                v.setMarca(leerTexto("Marca: "));
                v.setModelo(leerTexto("Modelo: "));
                v.setColor(leerTexto("Color: "));
                v.setTipo(leerTipoVehiculo());

                if (v.guardar())
                    System.out.println("✅ Vehiculo guardado");
                else
                    System.out.println("❌ Error guardando");

            }

            case 2 -> {

                String placa = leerTexto("Placa: ");

                Vehiculo v = Vehiculo.buscarPorPlaca(placa);

                if (v != null) {

                    System.out.println("\nVehiculo encontrado");
                    System.out.println("Marca: " + v.getMarca());
                    System.out.println("Modelo: " + v.getModelo());
                    System.out.println("Color: " + v.getColor());

                } else {

                    System.out.println("❌ Vehiculo no encontrado");

                }

            }

        }

    }

    // =========================
    // MENU CELDAS
    // =========================

    public static void menuCeldas() {

        System.out.println("\n--- CELDAS ---");
        System.out.println("1. Buscar disponibles");

        int op = leerOpcion(1,1);

        if (op == 1) {

            TipoCelda tipo = leerTipoCelda();

            List<Celda> lista = Celda.buscarDisponibles(tipo);

            if (lista.isEmpty()) {

                System.out.println("No hay celdas disponibles");

            } else {

                for (Celda c : lista) {

                    System.out.println("Celda disponible: " + c.getNumeroCelda());

                }

            }

        }

    }

    // =========================
    // MENU CONTROLADOR
    // =========================

    public static void menuControlador() {

        System.out.println("\n--- CONTROLADOR ---");
        System.out.println("1. Registrar entrada");
        System.out.println("2. Registrar salida");
        System.out.println("3. Vehiculos dentro");
        System.out.println("4. Registrar novedad");
        System.out.println("5. Historial");

        int op = leerOpcion(1,5);

        switch (op) {

            case 1 -> {

                String placa = leerTexto("Placa: ");
                TipoVehiculo tipo = leerTipoVehiculo();

                if (controller.procesarEntrada(placa, tipo))
                    System.out.println("✅ Entrada registrada");
                else
                    System.out.println("❌ No se pudo registrar");

            }

            case 2 -> {

                String placa = leerTexto("Placa: ");

                if (controller.procesarSalida(placa))
                    System.out.println("✅ Salida registrada");
                else
                    System.out.println("❌ Error registrando salida");

            }

            case 3 -> {

                List<RegistroParqueo> lista = controller.consultarVehiculosDentro();

                if (lista.isEmpty()) {

                    System.out.println("No hay vehiculos en el parqueadero");

                } else {

                    for (RegistroParqueo r : lista) {

                        System.out.println(
                                "Placa: " + r.getVehiculo().getPlaca() +
                                " | Celda: " + r.getCelda().getNumeroCelda() +
                                " | Entrada: " + r.getFechaHoraEntrada()
                        );

                    }

                }

            }

            case 4 -> {

                String placa = leerTexto("Placa: ");
                String desc = leerTexto("Descripcion: ");

                Novedad n = controller.registrarNovedad(placa, desc);

                if (n != null)
                    System.out.println("✅ Novedad registrada");
                else
                    System.out.println("❌ Error");

            }

            case 5 -> {

                String placa = leerTexto("Placa: ");

                List<RegistroParqueo> historial = controller.consultarHistorial(placa);

                if (historial == null || historial.isEmpty()) {

                    System.out.println("No hay historial");

                } else {

                    for (RegistroParqueo r : historial) {

                        System.out.println(
                                "Entrada: " + r.getFechaHoraEntrada() +
                                " | Salida: " + r.getFechaHoraSalida()
                        );

                    }

                }

            }

        }

    }

}
