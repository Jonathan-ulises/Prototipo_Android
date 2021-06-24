package com.its_omar.prototipo.model;

import java.util.Objects;

public class Cliente_por_visitar {

    private int idCliente;
    private String nombre;
    private String aPaterno;
    private String aMaterno;
    private int estatusVisita;

    public Cliente_por_visitar(int idCliente, String nombre, String aPaterno, String aMaterno, int estatusVisita) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.aPaterno = aPaterno;
        this.aMaterno = aMaterno;
        this.estatusVisita = estatusVisita;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public String getaPaterno() {
        return aPaterno;
    }

    public String getaMaterno() {
        return aMaterno;
    }

    public int getEstatusVisita() {
        return estatusVisita;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente_por_visitar that = (Cliente_por_visitar) o;
        return idCliente == that.idCliente &&
                estatusVisita == that.estatusVisita &&
                nombre.equals(that.nombre) &&
                aPaterno.equals(that.aPaterno) &&
                aMaterno.equals(that.aMaterno);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCliente, nombre, aPaterno, aMaterno, estatusVisita);
    }
}
