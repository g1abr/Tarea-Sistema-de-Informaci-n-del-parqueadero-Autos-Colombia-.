package com.parqueo;

import java.util.List;
import java.util.Scanner;

import com.parqueo.controller.PagoController;
import com.parqueo.controller.ParqueaderoController;
import com.parqueo.model.*;
import com.parqueo.model.Enum.TipoCelda;
import com.parqueo.model.Enum.TipoVehiculo;
import com.parqueo.repository.*;

public class App {

    static Scanner sc = new Scanner(System.in);
    static ParqueaderoController controller = new ParqueaderoController();

    public static void main(String[] args) {

        int opcion;

        do {
            System.out.println("\n****************************************");
            System.out.println("   SISTEMA PARQUEADERO");
            System.out.println("******************************************");
            System.out.println("1. Usuarios");
            System.out.println("2. Vehiculos");
            System.out.println("3. Celdas");
            System.out.println("4. Parqueadero");
            System.out.println("5. Pagos");

            System.out.println("0. Salir");

            opcion = leerOpcion(0, 5);

            switch (opcion) {
                case 1 -> menuUsuarios();
                case 2 -> menuVehiculos();
                case 3 -> menuCeldas();
                case 4 -> menuControlador();
                case 5 -> menuPagos();
            }

        } while (opcion != 0);

        System.out.println("Sistema cerrado.");
    }

    // Utilidades del sistema

    public static int leerEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Número inválido");
            }
        }
    }

    public static int leerOpcion(int min, int max) {
        while (true) {
            int op = leerEntero("Seleccione opción: ");
            if (op >= min && op <= max)
                return op;
            System.out.println("Opción fuera de rango");
        }
    }

    public static String leerTexto(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String txt = sc.nextLine().trim();
            if (!txt.isEmpty())
                return txt;
            System.out.println("Campo obligatorio");
        }
    }

    public static TipoVehiculo leerTipoVehiculo() {
        while (true) {
            try {
                System.out.print("Tipo (CARRO/MOTO): ");
                return TipoVehiculo.valueOf(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Tipo inválido");
            }
        }
    }

    public static TipoCelda leerTipoCelda() {

        while (true) {
            try {
                String input = leerTexto("Tipo celda (Carro/Moto): ").trim();

                input = input.substring(0, 1).toUpperCase() +
                        input.substring(1).toLowerCase();

                return TipoCelda.valueOf(input);

            } catch (Exception e) {
                System.out.println(" Tipo inválido. Ej: Carro o Moto");
            }
        }
    }
    // Menú usuarios

    public static void menuUsuarios() {

        int op;
        UsuarioRepository repo = new UsuarioRepository();

        do {
            System.out.println("\n----- MODULO USUARIOS -----");
            System.out.println("1. Registrar");
            System.out.println("2. Buscar");
            System.out.println("3. Listar");
            System.out.println("4. Actualizar");
            System.out.println("5. Eliminar");
            System.out.println("0. Volver");

            op = leerOpcion(0, 5);

            switch (op) {

                case 1 -> {
                    Usuario u = new Usuario();
                    u.setIdentificacion(leerTexto("Identificacion: "));
                    u.setNombreCompleto(leerTexto("Nombre: "));
                    u.setTelefono(leerTexto("Telefono: "));
                    u.setEmail(leerTexto("Email: "));
                    u.setDireccion(leerTexto("Direccion: "));
                    System.out.println(
                            u.registrar() ? "Se registro correctamente el usuario" : "Hubo un error en el registro");
                }

                case 2 -> {
                    try {
                        Usuario u = repo.buscarPorId(leerEntero("ID: "));
                        System.out.println(u != null ? u.getNombreCompleto() : "No encontrado");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                case 3 -> {
                    try {
                        repo.listarTodos().forEach(u -> System.out.println(u.getId() + " - " + u.getNombreCompleto()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                case 4 -> {
                    Usuario u = new Usuario();
                    u.setId(leerEntero("ID: "));
                    u.setNombreCompleto(leerTexto("Nombre: "));
                    u.setTelefono(leerTexto("Telefono: "));
                    u.setEmail(leerTexto("Email: "));
                    u.setDireccion(leerTexto("Direccion: "));
                    System.out.println(u.actualizar() ? "Usuario Actualizado con exito" : "Error");
                }

                case 5 -> {
                    try {
                        boolean ok = repo.eliminar(leerEntero("ID: "));
                        System.out.println(ok ? " Eliminado exitosamente " : " Error");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        } while (op != 0);
    }

    // Menú de vehículos

    public static void menuVehiculos() {

        int op;
        VehiculoRepository repo = new VehiculoRepository();

        do {
            System.out.println("\n----- MODULO VEHICULOS -----");
            System.out.println("1. Registrar");
            System.out.println("2. Buscar");
            System.out.println("3. Listar");
            System.out.println("4. Actualizar");
            System.out.println("5. Eliminar");
            System.out.println("6. Historial");
            System.out.println("0. Volver");

            op = leerOpcion(0, 6);

            switch (op) {

                case 1 -> {
                    Vehiculo v = new Vehiculo();
                    v.setPlaca(leerTexto("Placa: "));
                    v.setMarca(leerTexto("Marca: "));
                    v.setModelo(leerTexto("Modelo: "));
                    v.setColor(leerTexto("Color: "));
                    v.setTipo(leerTipoVehiculo());

                    Usuario u = new Usuario();
                    u.setId(leerEntero("ID usuario: "));
                    v.setPropietario(u);

                    System.out.println(v.guardar() ? "Vehículo guardado exitosamete " : "Hubo un error");
                }

                case 2 -> {
                    Vehiculo v = Vehiculo.buscarPorPlaca(leerTexto("Placa: "));
                    System.out.println(v != null ? v : "No encontrado");
                }

                case 3 -> {
                    try {
                        repo.listarTodos().forEach(System.out::println);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                case 4 -> {
                    Vehiculo v = new Vehiculo();
                    v.setPlaca(leerTexto("Placa: "));
                    v.setMarca(leerTexto("Marca: "));
                    v.setModelo(leerTexto("Modelo: "));
                    v.setColor(leerTexto("Color: "));
                    v.setTipo(leerTipoVehiculo());

                    Usuario u = new Usuario();
                    u.setId(leerEntero("ID usuario: "));
                    v.setPropietario(u);

                    System.out.println(v.actualizar() ? "Vehículo Actualizado " : "Error");
                }

                case 5 -> {
                    try {
                        boolean ok = repo.eliminar(leerTexto("Placa: "));
                        System.out.println(ok ? " Eliminado" : "Error");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                case 6 -> {
                    Vehiculo v = Vehiculo.buscarPorPlaca(leerTexto("Placa: "));
                    if (v != null) {
                        v.obtenerHistorial().forEach(
                                r -> System.out.println(r.getFechaHoraEntrada() + " - " + r.getFechaHoraSalida()));
                    }
                }
            }

        } while (op != 0);
    }

    // Menú de celdas

    public static void menuCeldas() {

        int op;
        CeldaRepository repo = new CeldaRepository();

        do {
            System.out.println("\n----- MODULO CELDAS -----");
            System.out.println("1. Registrar");
            System.out.println("2. Buscar");
            System.out.println("3. Listar");
            System.out.println("4. Cambiar estado");
            System.out.println("5. Eliminar");
            System.out.println("0. Volver");

            op = leerOpcion(0, 5);

            switch (op) {

                case 1 -> {
                    Celda c = new Celda(
                            leerTexto("Numero: "),
                            leerTipoCelda());

                    System.out.println(c.guardar() ? "Celda guardada exitosamente " : "Hubo un error");
                }
                case 2 -> {
                    try {
                        String numero = leerTexto("Numero: ")
                                .trim()
                                .toUpperCase();

                        if (numero.isEmpty()) {
                            System.out.println("Debe ingresar un número de celda");
                            return;
                        }
                        if (!numero.matches("[A-Z]\\d{2}")) {
                            System.out.println("Formato inválido (");
                            return;
                        }

                        Celda c = Celda.buscarPorNumero(numero);

                        if (c != null) {
                            System.out.println("\n CELDA ENCONTRADA");
                            System.out.println("**********************");
                            System.out.println(c);
                            System.out.println("*********************\n");
                        } else {
                            System.out.println(" No encontrada");
                        }

                    } catch (IllegalArgumentException e) {
                        System.out.println("Error en datos ");
                    } catch (Exception e) {
                        System.out.println(" Error inesperado");
                        e.printStackTrace();
                    }
                }

                case 3 -> {
                    try {
                        List<Celda> lista = repo.listarTodas();

                        if (lista.isEmpty()) {
                            System.out.println(" No hay celdas");
                        } else {
                            System.out.println("\n LISTA DE CELDAS");
                            System.out.println("***********************");

                            lista.forEach(System.out::println);

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                case 4 -> {
                    try {
                        int id = leerEntero("ID: ");

                        Celda c = repo.buscarPorId(id);

                        if (c == null) {
                            System.out.println(" No encontrada");
                            return;
                        }

                        System.out.println("1. OCUPAR");
                        System.out.println("2. LIBERAR");

                        int opEstado = leerEntero("Seleccione: ");

                        if (opEstado == 1) {
                            c.ocupar();
                        } else if (opEstado == 2) {
                            c.liberar();
                        }

                        System.out.println(" Estado actualizado");

                    } catch (Exception e) {
                        System.out.println(" Error al buscar la celda");
                        e.printStackTrace();
                    }
                }

                case 5 -> {
                    try {
                        boolean ok = repo.eliminar(leerEntero("ID: "));
                        System.out.println(ok ? "Eliminado" : "Error");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        } while (op != 0);
    }

    // Menú del Parqueadero
    public static void menuControlador() {

        int op;

        do {
            System.out.println("\n===== CONTROL PARQUEADERO =====");
            System.out.println("1. Registrar entrada de vehículo");
            System.out.println("2. Registrar salida de vehículo");
            System.out.println("3. Ver vehículos en parqueadero");
            System.out.println("4. Ver historial de vehículo");
            System.out.println("5. Registrar novedad");
            System.out.println("0. Volver");

            op = leerOpcion(0, 5);

            switch (op) {

                case 1 -> {
                    String placa = leerTexto("Placa: ").toUpperCase();
                    System.out.println(controller.procesarEntrada(placa)
                            ? "Entrada registrada"
                            : " Error");
                }
                case 2 -> {
                    try {
                        String placa = leerTexto("Placa: ").toUpperCase();

                        boolean ok = controller.procesarSalida(placa);

                        System.out.println(ok ? " Salida registrada"
                                : " No se pudo registrar");

                    } catch (Exception e) {
                        System.out.println("Error en salida");
                        e.printStackTrace();
                    }
                }

                case 3 -> {
                    try {
                        var lista = controller.consultarVehiculosDentro();

                        if (lista.isEmpty()) {
                            System.out.println("No hay vehículos en el parqueadero");
                        } else {
                            System.out.println("\n******** VEHÍCULOS DENTRO *******");

                            for (var r : lista) {
                                System.out.println("----------------------------");
                                System.out.println("Placa : " + r.getVehiculo().getPlaca());
                                System.out.println("Celda : " + r.getCelda().getNumeroCelda());
                                System.out.println("Entrada: " + r.getFechaHoraEntrada());
                            }

                            System.out.println("************\n");
                        }

                    } catch (Exception e) {
                        System.out.println("Error al listar");
                        e.printStackTrace();
                    }
                }

                case 4 -> {
                    try {
                        String placa = leerTexto("Placa: ").toUpperCase();

                        var lista = controller.consultarHistorial(placa);

                        if (lista.isEmpty()) {
                            System.out.println("Sin historial");
                        } else {
                            System.out.println("\n************ HISTORIAL ***********");

                            for (var r : lista) {
                                System.out.println("----------------------------");
                                System.out.println("Entrada: " + r.getFechaHoraEntrada());
                                System.out.println("Salida : " + r.getFechaHoraSalida());
                            }

                            System.out.println("***********\n");
                        }

                    } catch (Exception e) {
                        System.out.println("Error en historial");
                        e.printStackTrace();
                    }
                }

                case 5 -> {
                    try {
                        String placa = leerTexto("Placa: ").toUpperCase();
                        String desc = leerTexto("Descripción: ");

                        var novedad = controller.registrarNovedad(placa, desc);

                        System.out.println(novedad != null
                                ? "Novedad registrada"
                                : "Error al registrar");

                    } catch (Exception e) {
                        System.out.println(" Error en novedad");
                        e.printStackTrace();
                    }
                }
            }

        } while (op != 0);
    }

    // -----------------------------
    // MENÚ PAGOS
    // -----------------------------
    public static void menuPagos() {

        int op;
        PagoController controller = new PagoController();

        do {

            System.out.println("\n----- MODULO PAGOS -----");
            System.out.println("1. Registrar pago mensualidad");
            System.out.println("2. Consultar pagos por placa");
            System.out.println("3. Listar pagos activos");
            System.out.println("4. Listar pagos vencidos");
            System.out.println("5. Listar todos los pagos");
            System.out.println("0. Volver");

            op = leerOpcion(0, 5);

            switch (op) {

                // Registrar pago
                case 1 -> {

                    try {

                        String placa = leerTexto("Placa: ").toUpperCase();
                        double valor = leerEntero("Valor: ");

                        boolean ok = controller.registrarPago(placa, valor);

                        System.out.println(ok
                                ? "Pago registrado correctamente"
                                : "Error al registrar pago");

                    } catch (Exception e) {
                        System.out.println("Error al registrar pago");
                        e.printStackTrace();
                    }
                }

                // Consultar pagos por placa
                case 2 -> {

                    try {

                        String placa = leerTexto("Placa: ").toUpperCase();

                        List<Pago> lista = controller.consultarPagos(placa);

                        if (lista == null || lista.isEmpty()) {
                            System.out.println("No hay pagos");
                        } else {

                            System.out.println("\n***** HISTORIAL PAGOS *****");

                            lista.forEach(System.out::println);
                        }

                    } catch (Exception e) {
                        System.out.println("Error al consultar pagos");
                        e.printStackTrace();
                    }
                }

                // Listar activos
                case 3 -> {

                    try {

                        List<Pago> lista = controller.listarActivos();

                        if (lista.isEmpty()) {
                            System.out.println("No hay pagos activos");
                        } else {

                            System.out.println("\n***** PAGOS ACTIVOS *****");

                            lista.forEach(System.out::println);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // Listar vencidos
                case 4 -> {

                    try {

                        List<Pago> lista = controller.listarVencidos();

                        if (lista.isEmpty()) {
                            System.out.println("No hay pagos vencidos");
                        } else {

                            System.out.println("\n***** PAGOS VENCIDOS *****");

                            lista.forEach(System.out::println);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // Listar todos
                case 5 -> {

                    try {

                        List<Pago> lista = controller.listarPagos();

                        if (lista.isEmpty()) {
                            System.out.println("No hay pagos");
                        } else {

                            System.out.println("\n***** TODOS LOS PAGOS *****");

                            lista.forEach(System.out::println);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

        } while (op != 0);
    }

}