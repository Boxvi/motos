package com.boxvisoft.motos.model;

public class Motos {

    private String idColeccion;

    private String color;
    private String moto;
    private String nombre;
    private String placa;
    private String responsable;
    private String sticker;
    private String telefono;

    public Motos() {
    }

    public Motos(String color, String moto, String nombre, String placa, String responsable, String sticker, String telefono) {
        this.color = color;
        this.moto = moto;
        this.nombre = nombre;
        this.placa = placa;
        this.responsable = responsable;
        this.sticker = sticker;
        this.telefono = telefono;
    }

    public Motos(String idColeccion, String color, String moto, String nombre, String placa, String responsable, String sticker, String telefono) {
        this.idColeccion = idColeccion;
        this.color = color;
        this.moto = moto;
        this.nombre = nombre;
        this.placa = placa;
        this.responsable = responsable;
        this.sticker = sticker;
        this.telefono = telefono;
    }

    public String getIdColeccion() {
        return idColeccion;
    }

    public void setIdColeccion(String idColeccion) {
        this.idColeccion = idColeccion;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMoto() {
        return moto;
    }

    public void setMoto(String moto) {
        this.moto = moto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getSticker() {
        return sticker;
    }

    public void setSticker(String sticker) {
        this.sticker = sticker;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
