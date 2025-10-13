package com.boxvisoft.motos;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.boxvisoft.motos.adapter.ListaMotosAdapter;
import com.boxvisoft.motos.controller.MotosController;
import com.boxvisoft.motos.model.Motos;
import com.boxvisoft.motos.ui.CrudMotoActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MotosActivity extends AppCompatActivity {

    private MotosController motosController;
    private ListaMotosAdapter listaMotosAdapter;
    private RecyclerView registrosMotos;
    private FloatingActionButton btnAddMoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motos);

        starApp();

        informacion();

    }

    private void starApp() {
        registrosMotos = findViewById(R.id.registrosMotos);
        btnAddMoto = findViewById(R.id.btnAddMoto);

        motosController = new MotosController();
    }

    private void informacion() {

        registrosMotos.setLayoutManager(new LinearLayoutManager(this));

        motosController.getAllMotos(new MotosController.OnMotosLoadedListener() {
            @Override
            public void onMotosLoaded(List<Motos> motos) {

                Log.d("MotosActivity", "Motos: " + motos.toString());

                listaMotosAdapter = new ListaMotosAdapter(new ArrayList<>(motos));
                registrosMotos.setAdapter(listaMotosAdapter);

            }

            @Override
            public void onError(Exception e) {

            }
        });


        btnAddMoto.setOnClickListener(v -> abrirCrudMoto());

    }

    private void abrirCrudMoto() {
        Intent intent = new Intent(this, CrudMotoActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }

}