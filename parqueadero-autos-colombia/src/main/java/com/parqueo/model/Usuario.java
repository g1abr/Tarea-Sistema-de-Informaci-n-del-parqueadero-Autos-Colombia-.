package com.parqueo.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.parqueo.repository.UsuarioRepository;

public class Usuario {

    private int id;
    private String identificacion;
    private String nombreCompleto;
    private String telefono;
    private String email;
    private String direccion;
    private LocalDateTime fechaRegistro;

    private List<Vehiculo> vehiculos = new ArrayList<>();

    public Usuario() {
    }

    public Usuario(String identificacion, String nombreCompleto, String telefono) {
        this.identificacion = identificacion;
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
        this.fechaRegistro = LocalDateTime.now();
    }

    // GETTERS Y SETTERS

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    // -----------------------
    // RELACIÓN CON VEHICULOS
    // -----------------------

    public void agregarVehiculo(Vehiculo vehiculo) {
        vehiculos.add(vehiculo);
    }

    public List<Vehiculo> listarVehiculos() {
        return vehiculos;
    }

    // -----------------------
    // MÉTODOS DE BASE DE DATOS
    // -----------------------

    public boolean registrar() {

        UsuarioRepository repo = new UsuarioRepository();

        try {
            repo.guardar(this);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Usuario buscarPorId(int id) {

        UsuarioRepository repo = new UsuarioRepository();

        try {
            return repo.buscarPorId(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Usuario> listar() {

        UsuarioRepository repo = new UsuarioRepository();

        try {
            return repo.listarTodos();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean actualizar() {

        UsuarioRepository repo = new UsuarioRepository();

        try {
            return repo.actualizar(this);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean eliminar(int id) {

        UsuarioRepository repo = new UsuarioRepository();

        try {
            return repo.eliminar(id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}