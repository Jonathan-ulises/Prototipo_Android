package com.its_omar.prototipo.model.resultClienteService;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClientesJSONResult {

    @SerializedName("resultado")
    @Expose
    private List<Resultado> resultado;

    public List<Resultado> getResultado() {
        return resultado;
    }
}
