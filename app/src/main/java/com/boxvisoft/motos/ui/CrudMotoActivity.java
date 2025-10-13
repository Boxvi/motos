package com.boxvisoft.motos.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.boxvisoft.motos.MotosActivity;
import com.boxvisoft.motos.R;
import com.boxvisoft.motos.controller.MotosController;
import com.boxvisoft.motos.controller.Persona;
import com.boxvisoft.motos.model.Motos;
import com.google.android.material.textfield.TextInputLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CrudMotoActivity extends AppCompatActivity {

    private Button btnCrear, btnModificar, btnEliminar;
    private TextView txtSticker, txtPlaca, txtMoto, txtColor, txtTelefono ;
    private Motos motos;
    private MotosController motosController;

    private AutoCompleteTextView txtNombres, txtTrabajadorDe;

//    private List<String> listaNombres;
//    private ArrayAdapter<String> adapter;
//    private List<Persona.PersonaMotos> personasList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_moto);

        starApp();

        informacion();
        cargarNombres();
    }



    private void starApp() {
        btnCrear = findViewById(R.id.btnCrear);
        btnModificar = findViewById(R.id.btnModificar);
        btnEliminar = findViewById(R.id.btnEliminar);

        txtSticker = findViewById(R.id.txtSticker);
        txtNombres = findViewById(R.id.txtNombres);
        txtPlaca = findViewById(R.id.txtPlaca);
        txtMoto = findViewById(R.id.txtMoto);
        txtColor = findViewById(R.id.txtColor);
        txtTelefono = findViewById(R.id.txtTelefono);
        txtTrabajadorDe = findViewById(R.id.txtTrabajadorDe);


    }

    private void informacion() {


        btnCrear.setVisibility(View.VISIBLE);
        btnModificar.setVisibility(View.GONE);
        btnEliminar.setVisibility(View.GONE);

        btnCrear.setOnClickListener(v -> crearMoto());

        motosController = new MotosController();


    }

    private void crearMoto() {

        motos = new Motos(txtColor.getText().toString(),
                txtMoto.getText().toString(),
                txtNombres.getText().toString(),
                txtPlaca.getText().toString(),
                txtTrabajadorDe.getText().toString(),
                txtSticker.getText().toString(),
                txtTelefono.getText().toString());

        Log.d("MotoObjeto", motos.toString());

        motosController.addMoto(motos, new MotosController.OnMotoAddedListener() {

            @Override
            public void onMotoAdded(Motos moto) {
                Toast.makeText(CrudMotoActivity.this, "Moto Creada", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(CrudMotoActivity.this, MotosActivity.class);
                startActivity(intent);
                finish();

            }

            @Override
            public void onError(Exception e) {

            }
        });

    }

    private void cargarNombres() {

        motosController.getAllPersonasWithCount(new MotosController.OnPersonasLoadedListener() {


            @Override
            public void onPersonasLoaded(List<Persona.PersonaMotos> personas) {

                List<String> nombres = new ArrayList<>();

                for(Persona.PersonaMotos persona : personas){
                    nombres.add(persona.getNombre());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        CrudMotoActivity.this,
                        android.R.layout.simple_dropdown_item_1line,
                        nombres);


                txtNombres.setAdapter(adapter);
                txtTrabajadorDe.setAdapter(adapter);



            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(CrudMotoActivity.this, "Error cargando nombres", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MotosActivity.class);
        startActivity(intent);
        finish();

    }
}