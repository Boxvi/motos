package com.boxvisoft.motos.controller;

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


}
