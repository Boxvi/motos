package com.boxvisoft.motos.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.boxvisoft.motos.MainActivity;
import com.boxvisoft.motos.R;
import com.boxvisoft.motos.adapter.ListaPersonasAdapter;
import com.boxvisoft.motos.adapter.ResponsableMotosAdapter;
import com.boxvisoft.motos.controller.MotosController;
import com.boxvisoft.motos.controller.Persona;

import java.util.ArrayList;
import java.util.List;

public class PersonasActivity extends AppCompatActivity {

    private ListaPersonasAdapter listaPersonasAdapter;
    private MotosController motosController;
    private RecyclerView registrosPersonas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_personas);

        Log.d("APP", "PersonasActivity::onCreate");

        starApp();

//        informacion();

    }

    private void starApp() {
        registrosPersonas = findViewById(R.id.registrosPersonas);

        motosController = new MotosController();

        cargarResponsableConMotos();

    }

    private void cargarResponsableConMotos() {
        motosController.getPersonasConMotosAgrupadas(
                new MotosController.OnPersonasConMotosListener() {
                    @Override
                    public void onPersonasConMotosLoaded(List<Persona.ResposableConMotos> personas) {

                        ResponsableMotosAdapter adapter = new ResponsableMotosAdapter(personas);
                        registrosPersonas.setLayoutManager(new LinearLayoutManager(PersonasActivity.this));
                        registrosPersonas.setAdapter(adapter);

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                }


        );
    }


    private void informacion() {

        registrosPersonas.setLayoutManager(new LinearLayoutManager(this));

        motosController.getAllPersonasWithCount(new MotosController.OnPersonasLoadedListener() {

            @Override
            public void onPersonasLoaded(List<Persona.PersonaMotos> personas) {
                listaPersonasAdapter = new ListaPersonasAdapter(new ArrayList<>(personas));
                registrosPersonas.setAdapter(listaPersonasAdapter);
            }

            @Override
            public void onError(Exception e) {

            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

/*
        motosController = new MotosController();

        motosController.getAllPersonasWithCount(new MotosController.OnPersonasLoadedListener() {
            @Override
            public void onPersonasLoaded(List<Persona.PersonaMotos> personas) {
                for (Persona.PersonaMotos p : personas) {
                    Log.d("PERSONAS", p.getNombre() + " - " + p.getNumMotos());
                }

            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(PersonasActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });

//        motosController.getAllMotos(new MotosController.OnMotosLoadedListener() {
//
//            @Override
//            public void onMotosLoaded(List<Motos> motos) {
//                for (Motos m : motos) {
//                    Log.d("MOTOS", m.getNombre() + " - " + m.getPlaca());
//                }
//            }
//
//            @Override
//            public void onError(Exception e) {
//
//            }
//        });

//        Motos nuevaMoto = new Motos("rojo", "daytona", "lucas", "aaa505", "xavier", "aaa505", "0983797080");
//
//        motosController.addMoto(nuevaMoto, new MotosController.OnMotoAddedListener() {
//
//                    @Override
//                    public void onMotoAdded(Motos moto) {
//                        Log.d("MOTOS", "Moto agregada con id: " + moto.getIdColeccion());
//
//                        Toast.makeText(PersonasActivity.this, "Moto agregada con id: " + moto.getIdColeccion(), Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onError(Exception e) {
//
//                    }
//                }
//
//        );

*/