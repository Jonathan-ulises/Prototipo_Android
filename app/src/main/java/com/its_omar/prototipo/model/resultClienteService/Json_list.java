package com.its_omar.prototipo.model.resultClienteService;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Json_list {

    @SerializedName("idCliente")
    @Expose
    private int id_Cliente;

    @SerializedName("idVisita")
    @Expose
    private int id_Visita;
    private String nombre;

    @SerializedName("apPaterno")
    @Expose
    private String aPaterno;

    @SerializedName("apMaterno")
    @Expose
    private String aMaterno;

    @SerializedName("INE")
    @Expose
    private String fotoINE;

    public int getId_Cliente() {
        return id_Cliente;
    }

    public int getId_Visita() {
        return id_Visita;
    }

    public String getNombre() {
        return nombre;
    }

    public String getaPaterno() {
        return aPaterno;
    }

    public String getaMaterno() {
        return aMaterno;
    }

    public String getFotoINE() {
        return fotoINE;
    }
}
