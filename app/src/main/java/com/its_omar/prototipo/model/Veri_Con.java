package com.its_omar.prototipo.model;

public class Veri_Con {

    private boolean ok;
    private String mensaje;

    public Veri_Con(boolean ok, String mensaje) {
        this.ok = ok;
        this.mensaje = mensaje;
    }

    public boolean isOk() {
        return ok;
    }

    public String getMensaje() {
        return mensaje;
    }
}
