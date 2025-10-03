package com.boxvisoft.motos.ui;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.boxvisoft.motos.R;

public class DiasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dias);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}


    /*
    private void validateLicense(String licenseKey) {
        LicenseManager licenseManager = new LicenseManager(this);

        licenseManager.checkLicense(licenseKey, new LicenseValidationCallback() {
            @Override
            public void onValid(License license) {

            }

            @Override
            public void onInvalid(String reason) {

            }

            @Override
            public void onError(String error) {

            }
        });


//        licenseManager.validateLicenseWithFirebase(licenseKey, new LicenseValidationCallback() {
//
//            @Override
//            public void onValid(License license) {
//                Toast.makeText(MainActivity.this, "Licencia válida", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onInvalid(String reason) {
//                Toast.makeText(MainActivity.this, "Licencia inválida: " + reason, Toast.LENGTH_LONG).show();
//                showLicenseDialog(); // Volver a pedir
//            }
//
//            @Override
//            public void onError(String error) {
//                Toast.makeText(MainActivity.this, "Error: " + error, Toast.LENGTH_LONG).show();
//                // Puedes permitir uso offline si ya tenía licencia guardada
//                if (licenseManager.hasSavedLicense()) {
//                    startApp(); // Usar licencia cacheada temporalmente
//                }
//            }
//
//        });
    }


    private void initializeFirebase() {
        try {
            if (FirebaseApp.getApps(this).isEmpty()) {
                FirebaseApp.initializeApp(this);
                Log.d("Firebase", "Firebase inicializado correctamente");
            } else {
                Log.d("Firebase", "Firebase ya estaba inicializado");
            }
        } catch (Exception e) {
            Log.e("Firebase", "Error inicializando Firebase: " + e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



}






    private void validateLicense(String licenseKey) {
        LicenseManager licenseManager = new LicenseManager(this);

        licenseManager.validateLicenseWithFirebase(licenseKey, new LicenseValidationCallback() {
            @Override
            public void onValid(License license) {
                Toast.makeText(MainActivity.this, "Licencia válida", Toast.LENGTH_SHORT).show();
              startApp();
            }

            @Override
            public void onInvalid(String reason) {
                Toast.makeText(MainActivity.this, "Licencia inválida: " + reason, Toast.LENGTH_LONG).show();
                showLicenseDialog(); // Volver a pedir
            }

            @Override
            public void onError(String error) {
                Toast.makeText(MainActivity.this, "Error: " + error, Toast.LENGTH_LONG).show();
                // Puedes permitir uso offline si ya tenía licencia guardada
                if (licenseManager.hasSavedLicense()) {
                   startApp(); // Usar licencia cacheada temporalmente
                }
            }
        });
    }



    private void startApp() {
        // Tu lógica normal de la app
        Toast.makeText(this, "App iniciada", Toast.LENGTH_SHORT).show();
    }

}




    //    private AppBarConfiguration appBarConfiguration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


//        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

//        binding.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAnchorView(R.id.fab)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.botton_nav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//        return NavigationUI.navigateUp(navController, appBarConfiguration)
//                || super.onSupportNavigateUp();

        return super.onSupportNavigateUp();
    }

*/
