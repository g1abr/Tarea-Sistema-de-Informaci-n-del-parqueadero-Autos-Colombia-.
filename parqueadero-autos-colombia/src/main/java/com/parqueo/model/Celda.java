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
        this.estado = EstadoCelda.Disponible;

    }

    public Celda(String numeroCelda, TipoCelda tipoCelda) {
        this.numeroCelda = numeroCelda;
        this.tipoCelda = tipoCelda;
        this.estado = EstadoCelda.Disponible;
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


    public boolean guardar() {

        CeldaRepository repo = new CeldaRepository();

        try {
            repo.guardar(this);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizar() {

        CeldaRepository repo = new CeldaRepository();

        try {
            return repo.actualizar(this);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean eliminar(int idCelda) {

        CeldaRepository repo = new CeldaRepository();

        try {
            return repo.eliminar(idCelda);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Celda buscarPorId(int id) {

        CeldaRepository repo = new CeldaRepository();

        try {
            return repo.buscarPorId(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Celda> listarTodas() {

        CeldaRepository repo = new CeldaRepository();

        try {
            return repo.listarTodas();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Celda buscarPorNumero(String numero) {

        CeldaRepository repo = new CeldaRepository();

        try {
            return repo.buscarPorNumero(numero);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void ocupar() {
        try {
            new CeldaRepository().ocupar(this);
            this.estado = EstadoCelda.Ocupada;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void liberar() {
        try {
            new CeldaRepository().liberar(this);
            this.estado = EstadoCelda.Disponible;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean estaDisponible() {
        return estado == EstadoCelda.Disponible;
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
        return "Celda:" +
                "\nNúmero : " + numeroCelda +
                "\nTipo   : " + tipoCelda +
                "\nEstado : " + estado + 
                "\n*****************";
    }

}
