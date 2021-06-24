package com.its_omar.prototipo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Bitacora {

    @SerializedName("modulo")
    @Expose
    private String modulo;

    @SerializedName("accion")
    @Expose
    private String accion;

    @SerializedName("idEmpleado")
    @Expose
    private int fk_empleado;

    public Bitacora(String modulo, String accion, int fk_empleado) {
        this.modulo = modulo;
        this.accion = accion;
        this.fk_empleado = fk_empleado;
    }

    public String getModulo() {
        return modulo;
    }

    public String getAccion() {
        return accion;
    }

    public int getFk_empleado() {
        return fk_empleado;
    }
}
