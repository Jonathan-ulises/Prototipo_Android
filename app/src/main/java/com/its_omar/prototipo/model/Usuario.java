package com.its_omar.prototipo.model;

public class Usuario {

    private int id_empleado;
    private String nombreUsuario;
    private String nombrePersona;

    public Usuario() {
    }

    public Usuario(int id_empleado, String nombreUsuario) {
        this.id_empleado = id_empleado;
        this.nombreUsuario = nombreUsuario;
    }

    public int getId_empleado() {
        return id_empleado;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setId_empleado(int id_empleado) {
        this.id_empleado = id_empleado;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getNombrePersona() {
        return nombrePersona;
    }

    public void setNombrePersona(String nombrePersona) {
        this.nombrePersona = nombrePersona;
    }
}
