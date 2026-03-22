package com.parqueo.model;

import java.time.Duration;
import java.time.LocalDateTime;

import com.parqueo.repository.RegistroRepository;

public class RegistroParqueo {

    private int id;
    private Vehiculo vehiculo;
    private Celda celda;

    private LocalDateTime fechaHoraEntrada;
    private LocalDateTime fechaHoraSalida;

    private boolean esMensualidad;

    public RegistroParqueo() {
    }

    public RegistroParqueo(Vehiculo vehiculo, Celda celda) {
        this.vehiculo = vehiculo;
        this.celda = celda;
        this.fechaHoraEntrada = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public Celda getCelda() {
        return celda;
    }

    public void setCelda(Celda celda) {
        this.celda = celda;
    }

    public LocalDateTime getFechaHoraEntrada() {
        return fechaHoraEntrada;
    }

    public void setFechaHoraEntrada(LocalDateTime fechaHoraEntrada) {
        this.fechaHoraEntrada = fechaHoraEntrada;
    }

    public LocalDateTime getFechaHoraSalida() {
        return fechaHoraSalida;
    }

    public void setFechaHoraSalida(LocalDateTime fechaHoraSalida) {
        this.fechaHoraSalida = fechaHoraSalida;
    }

    public boolean isEsMensualidad() {
        return esMensualidad;
    }

    public void setEsMensualidad(boolean esMensualidad) {
        this.esMensualidad = esMensualidad;
    }


    public boolean registrarEntrada() {

        RegistroRepository repo = new RegistroRepository();

        try {
            repo.registrarEntrada(this);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean registrarSalida() {

        RegistroRepository repo = new RegistroRepository();

        try {
            this.fechaHoraSalida = LocalDateTime.now();
            return repo.registrarSalida(this);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public long calcularTiempoEstadia() {

        if (fechaHoraSalida == null)
            return 0;

        return Duration.between(fechaHoraEntrada, fechaHoraSalida).toHours();
    }

    public double calcularValor() {

        long horas = calcularTiempoEstadia();

        if (horas == 0) {
            horas = 1;
        }

        double tarifaHora = 2000;

        return horas * tarifaHora;
    }

    public boolean estaActivo() {
        return fechaHoraSalida == null;
    }

    public void agregarNovedad(String novedad) {

        System.out.println("----- NOVEDAD REGISTRADA -----");
        System.out.println("Vehiculo: " + vehiculo.getPlaca());
        System.out.println("Celda: " + celda.getNumeroCelda());
        System.out.println("Detalle: " + novedad);
        System.out.println("Fecha: " + LocalDateTime.now());
    }

}