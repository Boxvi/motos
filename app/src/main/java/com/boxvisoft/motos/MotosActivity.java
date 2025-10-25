package com.boxvisoft.motos;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;

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

public class MotosActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private MotosController motosController;
    private ListaMotosAdapter listaMotosAdapter;
    private RecyclerView registrosMotos;
    private FloatingActionButton btnAddMoto;
    private SearchView txtBuscador;

    private List<Motos> listaOriginalMotos  = new ArrayList<>();



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
        txtBuscador = findViewById(R.id.txtBuscador);




        motosController = new MotosController();

        listaMotosAdapter = new ListaMotosAdapter(new ArrayList<>(), MotosActivity.this);
        registrosMotos.setLayoutManager(new LinearLayoutManager(this));
        registrosMotos.setAdapter(listaMotosAdapter);

    }

    private void informacion() {


        motosController.getAllMotos(new MotosController.OnMotosLoadedListener() {
            @Override
            public void onMotosLoaded(List<Motos> motos) {

                Log.d("MotosActivity", "Motos cargadas: " + motos.size());

                // ⬇️⬇️⬇️ ACTUALIZAR LISTA ORIGINAL Y ADAPTADOR ⬇️⬇️⬇️
                listaOriginalMotos.clear();
                listaOriginalMotos.addAll(motos);

                // Actualizar el adaptador existente, no crear uno nuevo
                listaMotosAdapter.actualizarDatos(new ArrayList<>(motos));
            }

            @Override
            public void onError(Exception e) {

            }
        });
        btnAddMoto.setOnClickListener(v -> abrirCrudMoto());

        txtBuscador.setOnQueryTextListener(this);
    }


    private void abrirCrudMoto() {
        Intent intent = new Intent(this, CrudMotoActivity.class);
        intent.putExtra("TITULO", "REGISTRO DE MOTO");
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

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        listaMotosAdapter.filtrado(s);
        return false;
    }

}