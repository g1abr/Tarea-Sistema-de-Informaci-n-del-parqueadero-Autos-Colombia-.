package com.parqueo.model;

import java.time.LocalDateTime;
import java.util.List;

import com.parqueo.repository.NovedadRepository;

public class Novedad {
    private int id;
    private RegistroParqueo registro;
    private String descripcion;
     private LocalDateTime fechaHora;

    
    public Novedad(){}
    public Novedad(RegistroParqueo registro){
        this.registro = registro;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public RegistroParqueo getRegistro() {
        return registro;
    }
    public void setRegistro(RegistroParqueo registro) {
        this.registro = registro;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public boolean guardar() {

        NovedadRepository repo = new NovedadRepository();

        try {
            repo.guardar(this);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Novedad> listarPorRegistro(RegistroParqueo registro) {

        NovedadRepository repo = new NovedadRepository();

        try {
            return repo.listarPorRegistro(registro.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
