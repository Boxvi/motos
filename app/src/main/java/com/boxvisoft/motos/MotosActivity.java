package com.boxvisoft.motos;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.boxvisoft.motos.adapter.ListaMotosAdapter;
import com.boxvisoft.motos.controller.MotosController;
import com.boxvisoft.motos.model.Motos;

import java.util.ArrayList;
import java.util.List;

public class MotosActivity extends AppCompatActivity {

    private MotosController motosController;
    private ListaMotosAdapter listaMotosAdapter;
    private RecyclerView registrosMotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motos);

        starApp();

        informacion();

    }

    private void starApp() {
        registrosMotos = findViewById(R.id.registrosMotos);

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

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }

}