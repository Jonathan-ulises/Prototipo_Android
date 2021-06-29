package com.its_omar.prototipo.model.resultClienteService;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Resultado {
    @SerializedName("j")
    @Expose
    private Json_list json;

    public Json_list getJson() {
        return json;
    }
}
