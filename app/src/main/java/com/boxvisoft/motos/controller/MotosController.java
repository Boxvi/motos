package com.boxvisoft.motos.controller;

import android.util.Log;

import com.boxvisoft.motos.model.Motos;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MotosController {

    private FirebaseFirestore db;
    private static final String COLLECTION_NAME = "motos";

    public MotosController() {
        db = FirebaseFirestore.getInstance();
    }

    public void addMoto(Motos motos, OnMotoAddedListener listener) {
        db.collection(COLLECTION_NAME)
                .add(motos)
                .addOnSuccessListener(documentReference -> {
                    motos.setIdColeccion(documentReference.getId());
                    listener.onMotoAdded(motos);
                }).addOnFailureListener(listener::onError);
    }

    public void getAllMotos(OnMotosLoadedListener listener) {
        db.collection(COLLECTION_NAME).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Motos> motosList = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Motos moto = doc.toObject(Motos.class);
                        moto.setIdColeccion(doc.getId());
                        motosList.add(moto);
                    }
                    Log.d("Motito", "Motos 7w7 hshsh: " + motosList.size());
                    listener.onMotosLoaded(motosList);
                })
                .addOnFailureListener(listener::onError);
    }

    public void getMotoBySticker(String sticker, OnMotoAddedListener listener) {
        db.collection(COLLECTION_NAME).whereEqualTo("sticker", sticker).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Motos moto = new Motos();
//                    List<Motos> motosList = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        moto = doc.toObject(Motos.class);

                        moto.setIdColeccion(doc.getId());
//                        motosList.add(moto);
                    }
                    listener.onMotoAdded(moto);
                })
                .addOnFailureListener(listener::onError);
    }

    public void getAllPersonasWithCount(OnPersonasLoadedListener listener) {
        db.collection(COLLECTION_NAME).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Map<String, Integer> personasCount = new HashMap<>();

                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        String nombre = doc.getString("nombre");

                        if (nombre != null) {
                            int count = personasCount.containsKey(nombre) ? personasCount.get(nombre) : 0;
                            personasCount.put(nombre, count + 1);
                        }
                    }

                    // Convertimos el Map en una lista de objetos Persona
                    List<Persona.PersonaMotos> personasList = new ArrayList<>();
                    for (Map.Entry<String, Integer> entry : personasCount.entrySet()) {
                        personasList.add(new Persona.PersonaMotos(entry.getKey(), entry.getValue()));
                    }

                    // 🔥 Notificamos al listener con la lista de objetos
                    listener.onPersonasLoaded(personasList);

                })
                .addOnFailureListener(listener::onError);
    }

//    private void getAllNombres(On) {
//
//    }


    //        db.collection(COLLECTION_NAME).get()
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//                    Set<String> nombresSet = new HashSet<>(); //// evita duplicados
//
//                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
//                        String nombre = doc.getString("nombre");
//                        if (nombre != null) {
//                            nombresSet.add(nombre);
//                        }
//                    }
//
//                    List<String> nombresList = new ArrayList<>(nombresSet);
//
//                    Log.d("PersonasController", "Nombres: " + nombresList);
//                })
//                .addOnFailureListener(listener::onError);


    public void updateMoto(Motos moto, OnMotoUpdatedListener listener) {
        db.collection(COLLECTION_NAME).document(moto.getIdColeccion())
                .set(moto)
                .addOnSuccessListener(aVoid -> listener.onMotoUpdated(moto))
                .addOnFailureListener(listener::onError);
    }

    public void deleteMoto(String id, OnMotoDeletedListener listener) {
        db.collection(COLLECTION_NAME).document(id)
                .delete()
                .addOnSuccessListener(aVoid -> listener.onMotoDeleted(id))
                .addOnFailureListener(listener::onError);
    }


    public interface OnMotoAddedListener {
        void onMotoAdded(Motos moto);

        void onError(Exception e);
    }

    public interface OnMotosLoadedListener {
        void onMotosLoaded(List<Motos> motos);

        void onError(Exception e);
    }

    public interface OnMotoUpdatedListener {
        void onMotoUpdated(Motos moto);

        void onError(Exception e);
    }

    public interface OnMotoDeletedListener {
        void onMotoDeleted(String id);

        void onError(Exception e);
    }

//    public interface OnPersonasLoadedListener {
//        void onPersonasLoaded(Map<String, Integer> personasCount);
//
//        void onError(Exception e);
//    }

    public interface OnPersonasLoadedListener {
        void onPersonasLoaded(List<Persona.PersonaMotos> personas);

        void onError(Exception e);
    }

    private interface OnNombres {
        void onNombres(List<Persona.PersonaNombres> nombres);

        void onError(Exception e);
    }

}
