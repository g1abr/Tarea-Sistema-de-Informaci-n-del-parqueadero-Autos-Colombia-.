package com.parqueo.model;

import java.util.List;

import com.parqueo.model.Enum.EstadoCelda;
import com.parqueo.model.Enum.TipoCelda;
import com.parqueo.repository.CeldaRepository;

public class Celda {
    private int id;
    private String numeroCelda;
    private TipoCelda tipoCelda;
    private EstadoCelda estado;

    public Celda() {
    }

    public Celda(String numeroCelda, TipoCelda tipoCelda) {
        this.numeroCelda = numeroCelda;
        this.tipoCelda = tipoCelda;
        this.estado = EstadoCelda.DISPONIBLE;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumeroCelda() {
        return numeroCelda;
    }

    public void setNumeroCelda(String nuemeroCelda) {
        this.numeroCelda = nuemeroCelda;
    }

    public TipoCelda getTipoCelda() {
        return tipoCelda;
    }

    public void setTipoCelda(TipoCelda tipoCelda) {
        this.tipoCelda = tipoCelda;
    }

    public EstadoCelda getEstado() {
        return estado;
    }

    public void setEstado(EstadoCelda estado) {
        this.estado = estado;
    }

    // ------------------------
    // MÉTODOS DEL SISTEMA
    // ------------------------

    public void ocupar() {

        CeldaRepository repo = new CeldaRepository();

        try {
            repo.ocupar(this);
            this.estado = EstadoCelda.OCUPADA;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void liberar() {

        CeldaRepository repo = new CeldaRepository();

        try {
            repo.liberar(this);
            this.estado = EstadoCelda.DISPONIBLE;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean estaDisponible() {
        return estado == EstadoCelda.DISPONIBLE;
    }

    public static List<Celda> buscarDisponibles(TipoCelda tipo) {

        CeldaRepository repo = new CeldaRepository();

        try {
            return repo.buscarDisponibles(tipo);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return "Celda{" +
                "numeroCelda='" + numeroCelda + '\'' +
                ", tipoCelda=" + tipoCelda +
                ", estado=" + estado +
                '}';
    }

}
