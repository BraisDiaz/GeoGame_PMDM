package org.danielcastelao.bdiaznunez.locationtracker;

import android.content.Intent;

import android.Manifest;

import android.content.pm.PackageManager;

import android.view.View;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.ActivityCompat;

import android.support.v4.content.ContextCompat;

import android.widget.Button;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import android.widget.TextView;

public class QRActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {


    //Declaración de variables

    private static final int MY_PERMISSIONS = 1;
    private Result lastResult;
    private Intent intentoQR;
    TextView out;
    ZXingScannerView scan;

    @Override

    public void onCreate(Bundle savedInstanceState) {

        //Tenemos un layout activity_qr que contiene el scanner scan de la clase ZXingScannerView.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        scan = (ZXingScannerView) findViewById(R.id.scan);

        intentoQR = this.getIntent();

        // Pido permiso para usar la CAMARA

        // Here, thisActivity is the current activity

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)

                != PackageManager.PERMISSION_GRANTED) {


            // Should we show an explanation?

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,

                    Manifest.permission.CAMERA)) {

                // Show an expanation to the user *asynchronously* -- don't block

                // this thread waiting for the user's response! After the user

                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,

                        new String[]{Manifest.permission.CAMERA},

                        MY_PERMISSIONS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an

                // app-defined int constant. The callback method gets the

                // result of the request.

            }

        }


        //Vuelve a la ventana del mapa si pulsamos el botón de regreso.
        Button botonRegreso = (Button) findViewById(R.id.btnRegreso);
        botonRegreso.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View View) {

                Intent intent = new Intent(getApplicationContext(), LocationActivity.class);

                intent.putExtra("tes1", intentoQR.getExtras().getBoolean("tes1"));

                intent.putExtra("tes2", intentoQR.getExtras().getBoolean("tes2"));

                intent.putExtra("tes3", intentoQR.getExtras().getBoolean("tes3"));

                startActivity(intent);

                onStop();

            }

        });
    }

    @Override

    public void onResume() {

        super.onResume();

        scan.setResultHandler(this);

        scan.startCamera();

    }


    @Override

    public void onPause() {

        super.onPause();

        scan.stopCamera();

    }

    @Override

    protected void onStop() {

        super.onStop();

        finish();

    }


    @Override

    public void handleResult(Result rawResult) {

        if(rawResult!=lastResult) {

            // If you would like to resume scanning, call this method below:
            lastResult = rawResult;
            scan.resumeCameraPreview(this);

            // Instancio el Intent para mandar el resultado del QR

            Intent winner = new Intent(getApplicationContext(),Winner.class);

            winner.putExtra("LecturaQR", lastResult.getText());
            winner.putExtra("tes1", intentoQR.getExtras().getBoolean("tes1"));
            winner.putExtra("tes2", intentoQR.getExtras().getBoolean("tes2"));
            winner.putExtra("tes3", intentoQR.getExtras().getBoolean("tes3"));

            startActivity(winner);

        }

    }
}


