package com.its_omar.prototipo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Empleado {
    @SerializedName("idEmpleado")
    @Expose
    private int idEmpleado;

    public Empleado(int id_Empleado) {
        this.idEmpleado = id_Empleado;
    }

    public int getId_Empleado() {
        return idEmpleado;
    }
}
