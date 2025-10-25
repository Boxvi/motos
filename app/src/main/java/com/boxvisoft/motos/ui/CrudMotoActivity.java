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

import androidx.appcompat.app.AlertDialog;
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
    private TextView txtSticker, txtPlaca, txtMoto, txtColor, txtTelefono, txtTitulo;
    private Motos motos;
    private MotosController motosController;

    private AutoCompleteTextView txtNombres, txtTrabajadorDe;

//    private List<String> listaNombres;
//    private ArrayAdapter<String> adapter;
//    private List<Persona.PersonaMotos> personasList;

    private AlertDialog.Builder builder;
    private Bundle bundle = new Bundle();
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_moto);
        motosController = new MotosController();
        starApp();

        informacion();
        cargarNombres();


        intent = getIntent();

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
        txtTitulo = findViewById(R.id.txtTitulo);


    }

    private void informacion() {

        bundle = getIntent().getExtras();

        txtTitulo.setText(bundle.getString("TITULO"));

        if (txtTitulo.getText().toString().equals("REGISTRO DE MOTO")) {
            btnCrear.setVisibility(View.VISIBLE);
            btnModificar.setVisibility(View.GONE);
            btnEliminar.setVisibility(View.GONE);


            btnCrear.setOnClickListener(v -> crearMoto());
        } else {

            motosController.getMotoById(bundle.getString("ID"), new MotosController.OnMotoAddedListener() {
                @Override
                public void onMotoAdded(Motos motito) {

                    txtSticker.setText(motito.getSticker());
                    txtNombres.setText(motito.getNombre());
                    txtPlaca.setText(motito.getPlaca());
                    txtMoto.setText(motito.getMoto());
                    txtColor.setText(motito.getColor());
                    txtTrabajadorDe.setText(motito.getResponsable());
                    txtTelefono.setText(motito.getTelefono());

                }

                @Override
                public void onError(Exception e) {

                }
            });

            btnCrear.setVisibility(View.GONE);
            btnModificar.setVisibility(View.VISIBLE);
            btnEliminar.setVisibility(View.VISIBLE);

            btnModificar.setOnClickListener(v -> modificarMoto(bundle.getString("ID")));
            btnEliminar.setOnClickListener(v -> eliminarMoto(bundle.getString("ID")));

        }


    }

    private void crearMoto() {

        if (txtSticker.getText().toString().isEmpty() ||
                txtNombres.getText().toString().isEmpty() ||
                txtPlaca.getText().toString().isEmpty() ||
                txtMoto.getText().toString().isEmpty() ||
                txtColor.getText().toString().isEmpty() ||
                txtTrabajadorDe.getText().toString().isEmpty()) {
            camposVacios();
        } else {

            builder = new AlertDialog.Builder(this);

            motos = new Motos(txtColor.getText().toString(),
                    txtMoto.getText().toString(),
                    txtNombres.getText().toString(),
                    txtPlaca.getText().toString(),
                    txtTrabajadorDe.getText().toString(),
                    txtSticker.getText().toString(),
                    txtTelefono.getText().toString());

            builder.setTitle("REGISTRO");
            builder.setMessage("¿Deseas CREAR esta moto con los siguiente datos? " +
                    "\nSticker: " + motos.getSticker() + "" +
                    "\nNombre: " + motos.getNombre() + "" +
                    "\nPlaca: " + motos.getPlaca() + "" +
                    "\nMoto: " + motos.getMoto() + "" +
                    "\nColor: " + motos.getColor() + "" +
                    "\nTelefono: " + motos.getTelefono() + "" +
                    "\nResponsable: " + motos.getResponsable() + "");

            builder.setPositiveButton("CREAR", (dialog, which) -> {
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
            });

            builder.setNegativeButton("No", (dialog, which) -> {
                Toast.makeText(this, "Registro cancelado", Toast.LENGTH_SHORT).show();
            });
            builder.show();
        }

    }

    private void modificarMoto(String idColeccion) {
        if (txtSticker.getText().toString().isEmpty() ||
                txtNombres.getText().toString().isEmpty() ||
                txtPlaca.getText().toString().isEmpty() ||
                txtMoto.getText().toString().isEmpty() ||
                txtColor.getText().toString().isEmpty() ||
                txtTrabajadorDe.getText().toString().isEmpty()) {
            camposVacios();
        } else {
            builder = new AlertDialog.Builder(this);

            motos = new Motos(txtColor.getText().toString(),
                    txtMoto.getText().toString(),
                    txtNombres.getText().toString(),
                    txtPlaca.getText().toString(),
                    txtTrabajadorDe.getText().toString(),
                    txtSticker.getText().toString(),
                    txtTelefono.getText().toString());

            builder.setTitle("MODIFICAR");
            builder.setMessage("¿Deseas MODIFICAR esta moto con los siguiente datos? " +
                    "\nSticker: " + motos.getSticker() + "" +
                    "\nNombre: " + motos.getNombre() + "" +
                    "\nPlaca: " + motos.getPlaca() + "" +
                    "\nMoto: " + motos.getMoto() + "" +
                    "\nColor: " + motos.getColor() + "" +
                    "\nTelefono: " + motos.getTelefono() + "" +
                    "\nResponsable: " + motos.getResponsable() + "");

            builder.setPositiveButton("MODIFICAR", (dialog, which) -> {
                motosController.updateMoto(idColeccion, motos, new MotosController.OnMotoUpdatedListener() {

                    @Override
                    public void onMotoUpdated(Motos moto) {
                        Toast.makeText(CrudMotoActivity.this, "Moto Modificada", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(CrudMotoActivity.this, MotosActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            });
            builder.setNegativeButton("No", (dialog, which) -> {
                Toast.makeText(this, "Registro cancelado", Toast.LENGTH_SHORT).show();
            });
            builder.show();


        }
        Log.d("ID", "MODIFICAR " + idColeccion);
    }

    private void eliminarMoto(String idColeccion) {
        builder = new AlertDialog.Builder(this);

        motos = new Motos();

        Log.d("ID", "ELIMINAR " + motos);


        builder.setTitle("ELIMINAR");
        builder.setMessage("¿Deseas ELIMINAR esta moto con los siguiente datos? " +
                "\nSticker: " + txtSticker.getText().toString() + "" +
                "\nNombre: " + txtNombres.getText().toString() + "" +
                "\nPlaca: " + txtPlaca.getText().toString() + "" +
                "\nMoto: " + txtMoto.getText().toString() + "" +
                "\nColor: " + txtColor.getText().toString() + "" +
                "\nTelefono: " + txtTelefono.getText().toString() + "" +
                "\nResponsable: " + txtTrabajadorDe.getText().toString() + "");

        builder.setPositiveButton("ELIMINAR", (dialog, which) -> {

            motosController.deleteMoto(idColeccion, new MotosController.OnMotoDeletedListener() {

                @Override
                public void onMotoDeleted(String id) {

                    Intent intent = new Intent(CrudMotoActivity.this, MotosActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onError(Exception e) {

                }
            });
        });

        builder.setNegativeButton("No", (dialog, which) -> {
            Toast.makeText(this, "Registro cancelado", Toast.LENGTH_SHORT).show();
        });
        builder.show();
    }


    private void cargarNombres() {

        motosController.getAllPersonasWithCount(new MotosController.OnPersonasLoadedListener() {


            @Override
            public void onPersonasLoaded(List<Persona.PersonaMotos> personas) {

                List<String> nombres = new ArrayList<>();

                for (Persona.PersonaMotos persona : personas) {
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

    private void camposVacios() {

        if (txtSticker.getText().toString().isEmpty()) {
            txtSticker.setError("Campo requerido");
            Toast.makeText(this, "El Sticker es un campo obligatorio para continuar", Toast.LENGTH_SHORT).show();
            txtSticker.requestFocus();
            return;
        }
        if (txtNombres.getText().toString().isEmpty()) {
            txtNombres.setError("Campo requerido");
            Toast.makeText(this, "Este campo es obligatorio", Toast.LENGTH_SHORT).show();
            txtNombres.requestFocus();
            return;
        }
        if (txtPlaca.getText().toString().isEmpty()) {
            txtPlaca.setError("Campo requerido");
            Toast.makeText(this, "Este campo es obligatorio", Toast.LENGTH_SHORT).show();
            txtPlaca.requestFocus();
            return;
        }
        if (txtMoto.getText().toString().isEmpty()) {
            txtMoto.setError("Campo requerido");
            Toast.makeText(this, "Este campo es obligatorio", Toast.LENGTH_SHORT).show();
            txtMoto.requestFocus();
            return;
        }
        if (txtColor.getText().toString().isEmpty()) {
            txtColor.setError("Campo requerido");
            Toast.makeText(this, "Este campo es obligatorio", Toast.LENGTH_SHORT).show();
            txtColor.requestFocus();
            return;
        }
        if (txtTrabajadorDe.getText().toString().isEmpty()) {
            txtTrabajadorDe.setError("Campo requerido");
            Toast.makeText(this, "Este campo es obligatorio", Toast.LENGTH_SHORT).show();
            txtTrabajadorDe.requestFocus();
            return;
        }
        if (txtTelefono.getText().toString().isEmpty()) {
            txtTelefono.setError("Campo requerido");
            Toast.makeText(this, "Este campo es obligatorio", Toast.LENGTH_SHORT).show();
            txtTelefono.requestFocus();
            return;
        }

    }

}