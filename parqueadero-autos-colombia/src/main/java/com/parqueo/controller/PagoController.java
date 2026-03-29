package com.parqueo.controller;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import com.parqueo.model.Pago;
import com.parqueo.model.Usuario;
import com.parqueo.model.Vehiculo;
import com.parqueo.repository.PagoRepository;
import com.parqueo.repository.VehiculoRepository;

public class PagoController {

    private PagoRepository pagoRepo;
    private VehiculoRepository vehiculoRepo;

    public PagoController() {

        pagoRepo = new PagoRepository();
        vehiculoRepo = new VehiculoRepository();
    }

    // -----------------------------
    // REGISTRAR PAGO
    // -----------------------------
    public boolean registrarPago(String placa, double valor) {

        try {

            Vehiculo vehiculo = vehiculoRepo.buscarPorPlaca(placa);

            if (vehiculo == null) {
                System.out.println("Vehículo no encontrado");
                return false;
            }

            Usuario usuario = vehiculo.getPropietario();

            Pago pago = new Pago();

            pago.setVehiculo(vehiculo);
            pago.setUsuario(usuario);
            pago.setValor(valor);
            pago.setFechaPago(LocalDateTime.now());

            return pagoRepo.registrarPago(pago);

        } catch (SQLException e) {

            System.out.println("Error al registrar pago");
            e.printStackTrace();
            return false;
        }
    }

    // -----------------------------
    // CONSULTAR PAGOS POR PLACA
    // -----------------------------
    public List<Pago> consultarPagos(String placa) {

        try {

            Vehiculo vehiculo = vehiculoRepo.buscarPorPlaca(placa);

            if (vehiculo == null) {
                return null;
            }

            return pagoRepo.listarPagosVehiculo(vehiculo.getId());

        } catch (SQLException e) {

            System.out.println("Error al consultar pagos");
            e.printStackTrace();
            return null;
        }
    }

    // -----------------------------
    // VALIDAR MENSUALIDAD ACTIVA
    // -----------------------------
    public boolean tieneMensualidadActiva(String placa) {

        try {

            Vehiculo vehiculo = vehiculoRepo.buscarPorPlaca(placa);

            if (vehiculo == null) {
                return false;
            }

            return pagoRepo.tieneMensualidadActiva(vehiculo.getId());

        } catch (Exception e) {

            System.out.println("Error validando mensualidad");
            e.printStackTrace();
            return false;
        }
    }

    // -----------------------------
    // LISTAR TODOS LOS PAGOS
    // -----------------------------
    public List<Pago> listarPagos() {

        try {

            return pagoRepo.listarTodos();

        } catch (Exception e) {

            System.out.println("Error al listar pagos");
            e.printStackTrace();
            return List.of();
        }
    }

    // -----------------------------
    // LISTAR ACTIVOS
    // -----------------------------
    public List<Pago> listarActivos() {

        try {
            return pagoRepo.listarActivos();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    // -----------------------------
    // LISTAR VENCIDOS
    // -----------------------------
    public List<Pago> listarVencidos() {

        try {
            return pagoRepo.listarVencidos();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

}