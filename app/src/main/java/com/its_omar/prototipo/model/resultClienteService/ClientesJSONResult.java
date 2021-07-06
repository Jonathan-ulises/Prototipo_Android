package com.its_omar.prototipo.model.resultClienteService;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Modelo para generar la estructura de una respuesta en formato JSON
 */
public class ClientesJSONResult {

    private boolean ok;

    @SerializedName("resultado")
    @Expose
    private List<Resultado> resultado;

    public List<Resultado> getResultado() {
        return resultado;
    }

    public boolean isOk() {
        return ok;
    }
}
