package com.parqueo.model;

import java.time.LocalDateTime;

public class Pago {

    private int id;
    private Usuario usuario;
    private Vehiculo vehiculo;

    private double valor;

    private LocalDateTime fechaPago;
    private LocalDateTime fechaVencimiento;

    private String estado;

    public Pago() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public void setFechaVencimiento(LocalDateTime fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDateTime fechaPago) {

        this.fechaPago = fechaPago;

        this.fechaVencimiento = fechaPago.plusMonths(1);

        this.estado = "ACTIVO";
    }

    public LocalDateTime getFechaVencimiento() {
        return fechaVencimiento;
    }

    public String getEstado() {
        return estado;
    }

    public boolean estaVigente() {

        return LocalDateTime.now().isBefore(fechaVencimiento);
    }

    // -----------------------------
    // CONSTRUIR OBJETO PAGO
    // -----------------------------
   
    @Override
    public String toString() {
        return "----- PAGO -----\n" +
                "Usuario: " + usuario.getNombreCompleto() + "\n" +
                "Vehiculo: " + vehiculo.getPlaca() + "\n" +
                "Valor: $" + valor + "\n" +
                "Fecha Pago: " + fechaPago + "\n" +
                "Fecha Vencimiento: " + fechaVencimiento + "\n" +
                "Estado: " + estado + "\n";
    }
}