package com.its_omar.prototipo.model.bodyJSONCliente;

public class BodyJSONCliente {

    private int idEstatusVisita;
    private double latitudReal;
    private double longitudReal;
    private String fotoCasa;
    private String comentario;
    private String firma;
    private int idCliente;


    public BodyJSONCliente() {
    }

    public void setIdEstatusVisita(int idEstatusVisita) {
        this.idEstatusVisita = idEstatusVisita;
    }

    public void setLatitudReal(double latitudReal) {
        this.latitudReal = latitudReal;
    }

    public void setLongitudReal(double longitudReal) {
        this.longitudReal = longitudReal;
    }

    public void setFotoCasa(String fotoCasa) {
        this.fotoCasa = fotoCasa;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }
}
