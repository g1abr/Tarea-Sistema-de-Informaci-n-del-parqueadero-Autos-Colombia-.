package com.parqueo.model;

import java.time.LocalDate;

public class Usuario {
    private int id;
    private String identificacion;
    private String nombreCompleto;
    private String telefono;
    private String email;
    private String direccion;
    private LocalDate fechaRegistro; // Puedes usar LocalDateTime si prefieres

    public Usuario() {}

    public Usuario(String identificacion, String nombreCompleto, String telefono, String email, String direccion) {
        this.identificacion = identificacion;
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
    }

    // Getters y Setters (generarlos todos)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getIdentificacion() { return identificacion; }
    public void setIdentificacion(String identificacion) { this.identificacion = identificacion; }
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public LocalDate getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDate fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    @Override
    public String toString() {
        return "Usuario{" + "id=" + id + ", nombre='" + nombreCompleto + '\'' + '}';
    }
}