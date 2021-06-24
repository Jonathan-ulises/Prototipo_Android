package com.its_omar.prototipo.model;

public class Bitacora {

    private String modulo;
    private String accion;
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
