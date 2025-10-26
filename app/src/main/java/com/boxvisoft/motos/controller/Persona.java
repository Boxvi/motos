package com.boxvisoft.motos.controller;

import com.boxvisoft.motos.model.Motos;

import java.util.ArrayList;
import java.util.List;

public class Persona {

    public static class PersonaMotos {

        //        private String idColeccion;
        private String nombre;
        private int numMotos;
        private String responsable;

        public PersonaMotos() {
        }

//        public PersonaMotos(String idColeccion, String nombre, int numMotos) {
//            this.idColeccion = idColeccion;
//            this.nombre = nombre;
//            this.numMotos = numMotos;
//        }

        public PersonaMotos(String nombre, int numMotos, String responsable) {
            this.nombre = nombre;
            this.numMotos = numMotos;
            this.responsable = responsable;

        }

        public PersonaMotos(String key, Integer value) {
            this.nombre = key;
            this.numMotos = value;
        }

//        public String getIdColeccion() {
//            return idColeccion;
//        }

        public String getNombre() {
            return nombre;
        }

        public int getNumMotos() {
            return numMotos;
        }

        public String getResponsable() {
            return responsable;
        }
    }

    public static class PersonaNombres {
        private String nombre;

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }
    }

    public static class ResposableConMotos {
        private String nombre;
        private List<Motos> motos;

        public ResposableConMotos(String nombre) {
            this.nombre = nombre;
            this.motos = new ArrayList<>();
        }

        public void agregarMoto(Motos moto) {
            this.motos.add(moto);
        }

        public int getCantidadMotos() {
            return motos.size();
        }

        public String getNombre() {
            return nombre;
        }

        public List<Motos> getMotos() {
            return motos;
        }

    }

}
