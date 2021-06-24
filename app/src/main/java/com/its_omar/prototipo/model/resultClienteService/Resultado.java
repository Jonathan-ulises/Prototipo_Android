package com.its_omar.prototipo.model.resultClienteService;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Resultado {
    @SerializedName("j")
    @Expose
    private List<Json_list> json;

    public List<Json_list> getJson() {
        return json;
    }
}
