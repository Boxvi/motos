package com.boxvisoft.motos.controller;

import android.util.Log;

import com.boxvisoft.motos.model.Motos;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
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

    public void getMotoById(String id, OnMotoAddedListener listener) {
        db.collection(COLLECTION_NAME).document(id).get()
                .addOnSuccessListener(documentSnapshot -> {
                    Motos moto = documentSnapshot.toObject(Motos.class);
                    moto.setIdColeccion(documentSnapshot.getId());
                    listener.onMotoAdded(moto);
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


    public void updateMoto(String id, Motos moto, OnMotoUpdatedListener listener) {
        db.collection(COLLECTION_NAME).document(id)
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

    public void getPersonasConMotosAgrupadas(OnPersonasConMotosListener listener) {
        db.collection(COLLECTION_NAME).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Map<String, Persona.ResposableConMotos> personasMap = new HashMap<>();

                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        String responsable = doc.getString("responsable");
                        if (responsable != null) {
                            if (!personasMap.containsKey(responsable)) {
                                personasMap.put(responsable, new Persona.ResposableConMotos(responsable));
                            }

                            Motos moto = new Motos();
                            moto.setIdColeccion(doc.getId());
                            moto.setColor(doc.getString("color"));
                            moto.setMoto(doc.getString("moto"));
                            moto.setNombre(doc.getString("nombre"));
                            moto.setPlaca(doc.getString("placa"));
                            moto.setResponsable(doc.getString("responsable"));
                            moto.setSticker(doc.getString("sticker"));
                            moto.setTelefono(doc.getString("telefono"));

                            personasMap.get(responsable).agregarMoto(moto);
                        }
                    }

                    List<Persona.ResposableConMotos> personasList = new ArrayList<>(personasMap.values());
                    Collections.sort(personasList, (p1, p2) -> p1.getNombre().compareTo(p2.getNombre()));
                    listener.onPersonasConMotosLoaded(personasList);

                })
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

    public interface OnPersonasConMotosListener {
        void onPersonasConMotosLoaded(List<Persona.ResposableConMotos> personas);

        void onError(Exception e);
    }

}
