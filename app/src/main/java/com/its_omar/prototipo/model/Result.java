package com.its_omar.prototipo.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Modelo generico para optener respuesta en formato JSON
 */
public class Result {

    private boolean ok;
    private String mensaje;

    @SerializedName("nombrePersona")
    @Expose
    private String nomUsu;

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

    public String getNomUsu() {
        return nomUsu;
    }
}
