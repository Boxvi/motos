package com.boxvisoft.motos;

import static com.google.firebase.database.collection.BuildConfig.VERSION_CODE;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.boxvisoft.motos.controller.MotosController;
import com.boxvisoft.motos.databinding.ActivityMainBinding;
import com.boxvisoft.motos.model.License;
import com.boxvisoft.motos.model.Motos;
import com.boxvisoft.motos.ui.DiasActivity;
import com.boxvisoft.motos.ui.PersonasActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.collection.BuildConfig;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding activityMainBinding;
    private SharedPreferences sharedPreferences;
    private FirebaseFirestore db;
    private String licenseKey;

    private TextView txtCodigo;
    private Button btnBuscar;

    private FloatingActionButton btnPersonas, btnBike, btnToday;

    private MotosController motosController;

    // AKI VAS CAMBIANDO LA VERSION
    private final int VERSION = 5;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        db = FirebaseFirestore.getInstance();
        motosController = new MotosController();

        licenseKey = sharedPreferences.getString("licenseKey", null);

        if (licenseKey == null) {
            // 🚨 Si no hay key guardada, pedimos al usuario
            showLicenseDialog();
        } else {
            db.collection("licenses").document(licenseKey)
                    .get().addOnSuccessListener(documentSnapshot -> {

                        if (Objects.equals(sharedPreferences.getString("uuid", null), documentSnapshot.getString("uuid"))) {
                            checkLicense();

                            System.out.println("hgola" + documentSnapshot.getString("uuid"));

                        } else {
                            showInUseDialog();
                        }
                    });


        }


        db.collection("config").document("app_version")
                .get().
                addOnSuccessListener(document -> {
                    if (document.exists()) {
                        long remoteVersion = document.getLong("versionCode");
                        String notas = document.getString("notas");
                        String apkUrl = document.getString("apkUrl");

                        System.out.println(document.getLong("versionCode"));

                        if (remoteVersion > VERSION) {
                            mostrarDialogoActualizacion(this, notas, apkUrl);

                        }

                    }


                });
    }

    private void mostrarDialogoActualizacion(Context context, String notas, String apkUrl) {
        new AlertDialog.Builder(context)
                .setTitle("Nueva versión disponible")
                .setMessage("Notas:\n" + notas + "\n\n¿Deseas actualizar ahora?")
                .setPositiveButton("Actualizar", (dialog, which) -> descargarEInstalarAPK(context, apkUrl))
                .setNegativeButton("Más tarde", null)
                .show();
    }

    private void descargarEInstalarAPK(Context context, String apkUrl) {
        String filename = "update.apk"; // Nombre de tu archivo

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(apkUrl));
        request.setTitle("Descargando actualización...");
        request.setDescription("Espere un momento");
        // Usamos el directorio de descargas públicas
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        long downloadId = manager.enqueue(request);

        // Escuchar cuando termine la descarga
        BroadcastReceiver onComplete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context ctxt, Intent intent) {
                long completedDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

                // Verificamos que sea la descarga que iniciamos
                if (completedDownloadId == downloadId) {

                    // 1. OBTENER LA RUTA DEL ARCHIVO
                    Uri apkUri = manager.getUriForDownloadedFile(downloadId);

                    if (apkUri != null) {

                        // 2. CONVERTIR A URI SEGURA CON FILEPROVIDER (necesario desde API 24)
                        // NOTA: El FileProvider necesita un objeto File, no solo la Uri
                        // Hay que consultar el DownloadManager para obtener la ruta absoluta
                        // Una forma más simple es obtener el archivo de forma manual ya que sabemos la ruta,
                        // y luego usar FileProvider. Esto depende de cómo se configure el destino.

                        File file = new File(
                                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                                filename
                        );

                        // Asegúrate de que tu authority coincida con el de AndroidManifest
                        String authority = context.getPackageName() + ".provider";
                        Uri contentUri = FileProvider.getUriForFile(context, authority, file);


                        // 3. INICIAR LA INSTALACIÓN
                        Intent installIntent = new Intent(Intent.ACTION_INSTALL_PACKAGE); // Usar ACTION_INSTALL_PACKAGE para instalar APK
                        installIntent.setData(contentUri);
                        installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION); // MUY IMPORTANTE

                        try {
                            context.startActivity(installIntent);
                        } catch (Exception e) {
                            // Manejo de error si no se encuentra el instalador o si faltan permisos
                            e.printStackTrace();
                        }
                    }

                    // Desregistrar el receptor
                    context.unregisterReceiver(this);
                }
            }
        };

        context.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

//    private void descargarEInstalarAPK(Context context, String apkUrl) {
//        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(apkUrl));
//        request.setTitle("Descargando actualización...");
//        request.setDescription("Espere un momento");
//        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "update.apk");
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//
//        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//        long downloadId = manager.enqueue(request);
//
//        // Escuchar cuando termine la descarga
//        BroadcastReceiver onComplete = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context ctxt, Intent intent) {
//                Intent installIntent = new Intent(Intent.ACTION_VIEW);
//                installIntent.setDataAndType(
//                        Uri.parse("file://" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/update.apk"),
//                        "application/vnd.android.package-archive");
//                installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(installIntent);
//                context.unregisterReceiver(this);
//            }
//        };
//
//        context.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
//    }


    private void checkLicense() {
        // ⬇️⬇️⬇️ CORREGIR: usar getLong() en lugar de getString() ⬇️⬇️⬇️
        long expirationTime = sharedPreferences.getLong("expirationTime", 0);

        if (expirationTime == 0 || System.currentTimeMillis() >= expirationTime) {
            Toast.makeText(this, "La licencia ha expirado: " + new Date(expirationTime), Toast.LENGTH_SHORT).show();
            showExpiredDialog();
        } else {
            startApp();
            Toast.makeText(this, "License is valid until: " + new Date(expirationTime), Toast.LENGTH_SHORT).show();
        }
    }

    private void startApp() {

        txtCodigo = findViewById(R.id.txtCodigo);
        btnBuscar = findViewById(R.id.btnBuscar);
//
        btnPersonas = findViewById(R.id.btnPersonas);
        btnBike = findViewById(R.id.btnBike);
        btnToday = findViewById(R.id.btnToday);

        btnPersonas.setOnClickListener(view -> abrirPersonas());
        btnBike.setOnClickListener(view -> abrirMotos());
        btnToday.setOnClickListener(view -> abrirHoy());


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        btnBuscar.setOnClickListener(view -> {
            String code = txtCodigo.getText().toString();

            if (code.isEmpty()) {
                Toast.makeText(this, "Debes ingresar un Codigo", Toast.LENGTH_SHORT).show();
            } else {

                motosController.getMotoBySticker(code, new MotosController.OnMotoAddedListener() {
                    @Override
                    public void onMotoAdded(Motos moto) {

                        Log.d("MOTOS", moto.getSticker() == null ? "null" : moto.getSticker());

                        if (moto.getSticker() != null) {
                            mostrarDialogoInformacionConOpciones(moto);

                        } else {

                            Toast.makeText(MainActivity.this, "El codigo no existe", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            }
        });

    }


    private void mostrarDialogoOpciones(Motos moto) {
        final String[] opciones = {"Cobrar", "Verificar", "Editar", "Eliminar"};

        AlertDialog.Builder builderOpciones = new AlertDialog.Builder(this);
        builderOpciones.setTitle("¿Qué desea hacer?, Seleccione una opcion para " + moto.getSticker());
        builderOpciones.setItems(opciones, (dialog, which) -> {
            switch (which) {
                case 0:
                    // Cobrar
//                    realizarCobro(moto);
                    break;
                case 1:
                    // Verificar
//                    verificarMoto(moto);
                    break;
                case 2:
                    // Editar
//                    editarMoto(moto);
                    break;
                case 3:
                    // Eliminar
//                    eliminarMoto(moto);
                    break;
            }
        });

        builderOpciones.setNegativeButton("<- Volver", (dialog, which) -> {
            // Volver a ver la información
            mostrarDialogoInformacionConOpciones(moto);
        });

        builderOpciones.setCancelable(false);
        builderOpciones.show();
    }

    private void mostrarDialogoInformacionConOpciones(Motos moto) {
        AlertDialog.Builder builderInfo = new AlertDialog.Builder(this);
        builderInfo.setTitle("INFORMACION DE " + moto.getSticker());
        builderInfo.setMessage("Nombre: " + moto.getNombre() + "\n" +
                "Moto: " + moto.getMoto() + "\n" +
                "Placa: " + moto.getPlaca() + "\n" +
                "Color: " + moto.getColor() + "\n" +
                "Telefono: " + moto.getTelefono() + "\n" +
                "Trabajador de: " + moto.getResponsable());

        builderInfo.setPositiveButton("Siguiente →", (dialog, which) -> {
            // Mostrar opciones después de confirmar la información
            mostrarDialogoOpciones(moto);
        });

        builderInfo.setNegativeButton("Cancelar", (dialog, which) -> {
            // El usuario cancela, no hace nada
            Toast.makeText(this, "Operación cancelada", Toast.LENGTH_SHORT).show();
        });

        builderInfo.setCancelable(false);
        builderInfo.show();
    }

    private void abrirPersonas() {
        Intent intent = new Intent(this, PersonasActivity.class);
        startActivity(intent);
        finish();
    }

    private void abrirHoy() {
        Toast.makeText(this, "ABRIR HOY", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(this, CrudMotoActivity.class);
//        startActivity(intent);
//        finish();
    }

    private void abrirMotos() {
//        Toast.makeText(this, "ABRIR MOTOS", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MotosActivity.class);
        startActivity(intent);
        finish();
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
