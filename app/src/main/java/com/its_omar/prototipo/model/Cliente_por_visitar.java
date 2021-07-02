package com.its_omar.prototipo.model;

import java.util.Objects;

public class Cliente_por_visitar {

    private int idCliente;
    private String nombre;
    private String aPaterno;
    private String aMaterno;
    private int estatusVisita;
    private String ine;
    private String casa;
    private String firma;
    private double lat;
    private double lon;

    public Cliente_por_visitar() {
    }

    public Cliente_por_visitar(int idCliente, String nombre, String aPaterno, String aMaterno,
                               int estatusVisita, String ine, String casa, String firma, double lat, double lon) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.aPaterno = aPaterno;
        this.aMaterno = aMaterno;
        this.estatusVisita = estatusVisita;
        this.ine = ine;
        this.casa = casa;
        this.firma = firma;
        this.lat = lat;
        this.lon = lon;
    }

    public int getIdCliente() {
        return idCliente;
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

    public int getEstatusVisita() {
        return estatusVisita;
    }

    public String getIne() {
        return ine;
    }

    public String getCasa() {
        return casa;
    }

    public String getFirma() {
        return firma;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setaPaterno(String aPaterno) {
        this.aPaterno = aPaterno;
    }

    public void setaMaterno(String aMaterno) {
        this.aMaterno = aMaterno;
    }

    public void setEstatusVisita(int estatusVisita) {
        this.estatusVisita = estatusVisita;
    }

    public void setIne(String ine) {
        this.ine = ine;
    }

    public void setCasa(String casa) {
        this.casa = casa;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente_por_visitar that = (Cliente_por_visitar) o;
        return idCliente == that.idCliente &&
                estatusVisita == that.estatusVisita &&
                nombre.equals(that.nombre) &&
                aPaterno.equals(that.aPaterno) &&
                aMaterno.equals(that.aMaterno) &&
                ine.equals(that.ine) &&
                casa.equals(that.casa) &&
                firma.equals(that.firma) &&
                lat == that.lat &&
                lon == that.lon;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCliente, nombre, aPaterno, aMaterno, estatusVisita, ine, casa, firma, lat, lon);
    }
}
