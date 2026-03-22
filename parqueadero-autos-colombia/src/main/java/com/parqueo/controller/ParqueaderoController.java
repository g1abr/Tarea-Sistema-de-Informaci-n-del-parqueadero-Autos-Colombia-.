package com.parqueo.controller;

import java.time.LocalDateTime;
import java.util.List;

import com.parqueo.model.Celda;
import com.parqueo.model.Novedad;
import com.parqueo.model.RegistroParqueo;
import com.parqueo.model.Vehiculo;

import com.parqueo.model.Enum.TipoCelda;
import com.parqueo.model.Enum.TipoVehiculo;

import com.parqueo.repository.CeldaRepository;

import com.parqueo.repository.RegistroRepository;
import com.parqueo.repository.VehiculoRepository;

public class ParqueaderoController {

    private VehiculoRepository repositorioVehiculos;
    private RegistroRepository repositorioRegistros;
    private CeldaRepository repositorioCeldas;

    public ParqueaderoController() {

        repositorioVehiculos = new VehiculoRepository();
        repositorioRegistros = new RegistroRepository();
        repositorioCeldas = new CeldaRepository();
    }

    // ----------------------------------------
    // PROCESAR ENTRADA
    // ----------------------------------------
    public boolean procesarEntrada(String placa) {

        try {

            Vehiculo vehiculo = repositorioVehiculos.buscarPorPlaca(placa);

            if (vehiculo == null) {
                return false;
            }

            TipoVehiculo tipoVehiculo = vehiculo.getTipo();

            TipoCelda tipoCelda = TipoCelda.valueOf(tipoVehiculo.name());

            List<Celda> disponibles = repositorioCeldas.buscarDisponibles(tipoCelda);

            if (disponibles.isEmpty()) {
                return false;
            }

            Celda celda = disponibles.get(0);

            RegistroParqueo registro = new RegistroParqueo();
            registro.setVehiculo(vehiculo);
            registro.setCelda(celda);
            registro.setFechaHoraEntrada(LocalDateTime.now());

            repositorioRegistros.registrarEntrada(registro);

            celda.ocupar();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ----------------------------------------
    // PROCESAR SALIDA
    // ----------------------------------------

    public boolean procesarSalida(String placa) {

        try {

            Vehiculo vehiculo = repositorioVehiculos.buscarPorPlaca(placa);

            if (vehiculo == null) {
                return false;
            }

            List<RegistroParqueo> historial = vehiculo.obtenerHistorial();

            if (historial.isEmpty()) {
                return false;
            }

            RegistroParqueo registro = historial.get(0);

            registro.setFechaHoraSalida(LocalDateTime.now());

            boolean salida = registro.registrarSalida();

            if (salida && registro.getCelda() != null) {
                registro.getCelda().liberar();
            }

            return salida;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    // ----------------------------------------
    // CONSULTAR VEHICULOS DENTRO
    // ----------------------------------------

    public List<RegistroParqueo> consultarVehiculosDentro() {

        try {
            return repositorioRegistros.listarActivos();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ----------------------------------------
    // BUSCAR CELDA DISPONIBLE
    // ----------------------------------------

    public Celda buscarCeldaDisponible(TipoVehiculo tipoVehiculo) {

        try {

            TipoCelda tipoCelda = TipoCelda.valueOf(tipoVehiculo.name());

            List<Celda> disponibles = repositorioCeldas.buscarDisponibles(tipoCelda);

            if (disponibles.isEmpty()) {
                return null;
            }

            return disponibles.get(0);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ----------------------------------------
    // REGISTRAR NOVEDAD
    // ----------------------------------------

    public Novedad registrarNovedad(String placa, String descripcion) {

        try {

            Vehiculo vehiculo = repositorioVehiculos.buscarPorPlaca(placa);

            if (vehiculo == null) {
                return null;
            }

            List<RegistroParqueo> historial = vehiculo.obtenerHistorial();

            if (historial.isEmpty()) {
                return null;
            }

            RegistroParqueo registro = historial.get(0);

            Novedad novedad = new Novedad(registro);

            // 🔥 ESTO TE FALTABA
            novedad.setDescripcion(descripcion);
            novedad.setFechaHora(LocalDateTime.now());

            if (novedad.guardar()) {
                return novedad;
            }

            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    // ----------------------------------------
    // CONSULTAR HISTORIAL
    // ----------------------------------------

    public List<RegistroParqueo> consultarHistorial(String placa) {

        try {

            Vehiculo vehiculo = repositorioVehiculos.buscarPorPlaca(placa);

            if (vehiculo == null) {
                return null;
            }

            return vehiculo.obtenerHistorial();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}