package com.parqueo.model;

import java.util.ArrayList;
import java.util.List;

import com.parqueo.model.Enum.TipoVehiculo;
import com.parqueo.repository.VehiculoRepository;

public class Vehiculo {
    private int id;
    private String placa;
    private String marca;
    private String modelo;
    private String color;
    private TipoVehiculo tipo;
    private Usuario propietario;

    public Vehiculo() {
    }

    public Vehiculo(String placa, String marca, String modelo, TipoVehiculo tipo) {
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public TipoVehiculo getTipo() {
        return tipo;
    }

    public void setTipo(TipoVehiculo tipo) {
        this.tipo = tipo;
    }

    public Usuario getPropietario() {
        return propietario;
    }

    public void setPropietario(Usuario propietario) {
        this.propietario = propietario;
    }

    // -----------------------
    // MÉTODOS DE BASE DE DATOS
    // -----------------------

    public boolean guardar() {

        VehiculoRepository repo = new VehiculoRepository();

        try {
            repo.guardar(this);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizar() {

        VehiculoRepository repo = new VehiculoRepository();

        try {
            return repo.actualizar(this);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Vehiculo buscarPorPlaca(String placa) {

        VehiculoRepository repo = new VehiculoRepository();

        try {
            return repo.buscarPorPlaca(placa);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
 public List<RegistroParqueo> obtenerHistorial() {

        VehiculoRepository repo = new VehiculoRepository();

        try {
            return repo.obtenerHistorial(this.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    @Override
    public String toString() {
        return "Vehiculo{" +
                "placa='" + placa + '\'' +
                ", marca='" + marca + '\'' +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}
