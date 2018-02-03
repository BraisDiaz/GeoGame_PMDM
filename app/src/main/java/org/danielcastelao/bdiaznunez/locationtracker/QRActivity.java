package org.danielcastelao.bdiaznunez.locationtracker;

import android.content.Intent;

import android.Manifest;

import android.content.pm.PackageManager;

import android.view.View;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.ActivityCompat;

import android.support.v4.content.ContextCompat;

import android.util.Log;

import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import android.widget.TextView;

import org.w3c.dom.Text;

public class QRActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;

    static final String TAG = "escaneoQR";

    private static final int MY_PERMISSIONS = 1;

    private Result lastResult;

    private Intent intento;

    TextView out;
    ZXingScannerView scan;

    @Override

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //scan = new ZXingScannerView(this);


        setContentView(R.layout.activity_qr);
        scan = (ZXingScannerView) findViewById(R.id.scan);

        Log.v(TAG, "Inicializando scanner");

        intento = getIntent();

        String salutation = intento.getExtras().getString("salutation");

        out = (TextView) findViewById(R.id.out);

        //out.setText(salutation);

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
    }


    public void devuelta(){

        // Metemos el dato que queremos enviar a la otra Activity

        intento.putExtra("retorno", "Hasta luego Lucas" );

        // Dejamos configurado el resultado para que la otra Activity lo pueda recojer

        // mandamos un resultCode RESULT_OK, conforme todo fue bien

        // podriamos utilizar otro tipo de resultCode para informar del proceso realizado,

        // https://developer.android.com/reference/android/app/Activity.html#RESULT_CANCELED

        setResult(RESULT_OK, intento);

        // cerramos la Activity

        finish();

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

            // Do something with the result here

            Log.v(TAG, rawResult.getText()); // Prints scan results

            Log.v(TAG, rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)



            // If you would like to resume scanning, call this method below:

            scan.resumeCameraPreview(this);

            // Instancio el Intent para mandar el resultado del QR

            Intent intento= new Intent();

            intento.putExtra("retorno", rawResult.getText() );

            setResult(RESULT_OK, intento);

            // cierro la cámara

            finish();

        }

    }

}

