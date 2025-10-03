package com.boxvisoft.motos;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.boxvisoft.motos.databinding.ActivityMainBinding;
import com.boxvisoft.motos.model.License;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding activityMainBinding;
    private SharedPreferences sharedPreferences;
    private FirebaseFirestore db;
    private String licenseKey;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        db = FirebaseFirestore.getInstance();

        licenseKey = sharedPreferences.getString("licenseKey", null);

        if (licenseKey == null) {
            // 🚨 Si no hay key guardada, pedimos al usuario
            showLicenseDialog();
        } else {

            db.collection("licenses").document(licenseKey)
                    .get().addOnSuccessListener(documentSnapshot -> {

                        if (Objects.equals(sharedPreferences.getString("uuid", null), documentSnapshot.getString("uuid"))) {
                            checkLicense();
                        } else {
                            showInUseDialog();
                        }
                    });


        }

    }

    private void checkLicense() {
        long expirationTime = sharedPreferences.getLong("expirationTime", 0);
        if (expirationTime == 0 || System.currentTimeMillis() >= expirationTime) {
            Toast.makeText(this, "la licencia ha expirado" + new Date(expirationTime), Toast.LENGTH_SHORT).show();
            showExpiredDialog();
        } else {
            Toast.makeText(this, "License is valid until: " + new Date(expirationTime), Toast.LENGTH_SHORT).show();
        }

    }

    private void startApp() {
        Toast.makeText(this, "App iniciada", Toast.LENGTH_SHORT).show();
    }


    private void showLicenseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Licencia Requerida");
        builder.setMessage("Ingrese su clave de licencia:");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Validar", (dialog, which) -> {
            String key = input.getText().toString().trim();
            if (!key.isEmpty()) {


                licenseKey = key;

                refreshLicenseFromFirebase();

            } else {
                Toast.makeText(this, "Debes ingresar una clave válida", Toast.LENGTH_SHORT).show();
                showLicenseDialog(); // volver a pedirla
            }
        });

        builder.setNegativeButton("Salir", (dialog, which) -> {
            finish();
        });

        builder.setCancelable(false);
        builder.show();
    }

    private void refreshLicenseFromFirebase() {
        db.collection("licenses").document(licenseKey).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {

                        License license = documentSnapshot.toObject(License.class);

                        if (!license.isActive()) {
                            String uuid = UUID.randomUUID().toString();

                            db.collection("licenses").document(licenseKey).update("active", true,
                                    "uuid", uuid);

                            license.setActive(true);
                            license.setUuid(uuid);

                            saveLicenseToPrefs(licenseKey, license);

                            Toast.makeText(this, "Licencia válida hasta: " +
                                    new Date(license.getExpirationDate().getTime()), Toast.LENGTH_LONG).show();


                        } else {

                            Toast.makeText(this, "La licencia ya se uso en otro dispostivo, por favor contacte al administrador 0996474990", Toast.LENGTH_SHORT).show();
                            showLicenseDialog();
                        }

                    } else {
                        Toast.makeText(this, "Licencia no encontrada, introduce otra clave.", Toast.LENGTH_SHORT).show();
                        showLicenseDialog();
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al verificar licencia", Toast.LENGTH_SHORT).show();
                });

    }

    private void saveLicenseToPrefs(String licenseKey, License license) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("licenseKey", licenseKey);
        editor.putBoolean("active", license.isActive());
        editor.putLong("expirationTime", license.getExpirationDate().getTime());
        editor.putString("uuid", license.getUuid());
        editor.apply();
    }


    private void showInUseDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Licencia en uso")
                .setMessage("Tu licencia ya está activa en otro dispositivo. No puedes acceder desde este móvil.")
                .setMessage("Llama al 0996474990 para obtener una nueva licencia.")
                .setPositiveButton("Aceptar", (dialog, which) -> finish())
                .setCancelable(false)
                .show();

    }

    private void showExpiredDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Licencia caducada")
                .setMessage("Tu licencia ha expirado. Por favor adquiere un nuevo plazo.")
                .setMessage("Llama al 0996474990 para obtener un nuevo plazo.")
                .setPositiveButton("Recargar", (dialog, which) -> {
                    db.collection("licenses").document(licenseKey).get().addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            License license = documentSnapshot.toObject(License.class);

                            saveLicenseToPrefs(licenseKey, license);

                            long expirationTime = sharedPreferences.getLong("expirationTime", 0);

                            if (System.currentTimeMillis() >= expirationTime) {
                                showExpiredDialog();
                            } else {
                                Toast.makeText(this, "Licencia actualizada" + new Date(license.getExpirationDate().getTime()), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                })
                .setCancelable(false)
                .show();
    }

}
