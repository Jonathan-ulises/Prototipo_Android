package com.its_omar.prototipo.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    private boolean ok;
    private String mensaje;

    @SerializedName("idEmpleado")
    @Expose
    private int fk_empleado;

    public Result(boolean ok, String mensaje, int fk_empleado) {
        this.ok = ok;
        this.mensaje = mensaje;
        this.fk_empleado = fk_empleado;
    }

    public boolean isOk() {
        return ok;
    }

    public String getMensaje() {
        return mensaje;
    }

    public int getFk_empleado() { return fk_empleado; }
}
