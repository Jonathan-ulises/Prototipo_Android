package com.its_omar.prototipo.model.geoCercaJSONRequest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Estrucuta de una peticion JSON para enviar ubicacion
 */
public class GeoCercaJSONRequest {

    @SerializedName("coordenadasLlegada")
    @Expose
    private Coordenadas coordenadasLlegada;

    @SerializedName("radio")
    @Expose
    private final int RADIO = 5;

    @SerializedName("coordenadasEmpleado")
    @Expose
    private Coordenadas coordenadasEmpleado;

    public Coordenadas getCoordenadasLlegada() {
        return coordenadasLlegada;
    }

    public int getRADIO() {
        return RADIO;
    }

    public Coordenadas getCoordenadasEmpleado() {
        return coordenadasEmpleado;
    }

    public void setCoordenadasLlegada(Coordenadas coordenadasLlegada) {
        this.coordenadasLlegada = coordenadasLlegada;
    }

    public void setCoordenadasEmpleado(Coordenadas coordenadasEmpleado) {
        this.coordenadasEmpleado = coordenadasEmpleado;
    }
}
