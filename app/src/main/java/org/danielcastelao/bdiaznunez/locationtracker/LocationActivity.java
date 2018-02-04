package org.danielcastelao.bdiaznunez.locationtracker;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class LocationActivity extends FragmentActivity implements OnMapReadyCallback{

    //Declaración de variables.

    private static final String TAG = "gpslog";
    private LocationManager mLocMgr;
    private Location posicionInicial;
    private TextView textViewGPS, textViewDist;
    private Circle circle;
    private GoogleMap mMap;

    private LatLng tesoro1;
    private LatLng tesoro2;
    private LatLng tesoro3;

    final LatLng danielCastelao = new LatLng(42.236574, -8.714311);

    private Intent intent;


    //Minimo tiempo para updates en Milisegundos
    private static final long UPDATE_DISTANCIA = (long) 10; // 10 metros

    //Minimo tiempo para updates en Milisegundos

    private static final long UPDATE_TIEMPO = 10000; // 10 sg

    public Intent intento;
    private final static int codigo = 0;

    //TextView para recibir dato desde QRActivity
    private TextView reciboDato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        textViewGPS = (TextView) findViewById(R.id.lat);
        textViewDist = (TextView) findViewById(R.id.dist);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mLocMgr = (LocationManager) getSystemService(LOCATION_SERVICE);

        String salutation = "adios";
        intento = new Intent(LocationActivity.this, QRActivity.class);
        intento.putExtra("salutation", salutation);

        //Defino el cuadro de texto que recibirá el dato.
        reciboDato = (TextView) findViewById(R.id.recibo);

        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
        mMap.setMyLocationEnabled(true);
            //Requiere permisos para Android 6.0

            Log.e(TAG, "No se tienen permisos necesarios!, se requieren.");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 225);

            return;
        } else {
            Log.i(TAG, "Permisos necesarios OK!.");
            mLocMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, UPDATE_TIEMPO, UPDATE_DISTANCIA, locListener, Looper.getMainLooper());
        }
        textViewGPS.setText("Lat " + " Long ");

        //Localizaciones de los tres tesoros.
        tesoro1 = new LatLng(42.237246,-8.714106);
        tesoro2 = new LatLng(42.237032,-8.714410);
        tesoro3 = new LatLng(42.23746,-8.715424);

        Button btnQR = (Button) findViewById(R.id.btnQR);
        btnQR.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                Intent lectorQR = new Intent(getApplicationContext(), QRActivity.class);

                //Al clickar en Lector QR inicio la segunda activity y espero un resultado devuelto.
                //Identifico la llamada con un código, en este caso 0.
                startActivityForResult(intento,codigo);

            }

        });

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */


    public LocationListener locListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            Log.i(TAG, "Lat " + location.getLatitude() + " Long " + location.getLongitude());

            textViewGPS.setText("Lat " + (float)location.getLatitude() + " Long " + (float)location.getLongitude());
            textViewDist.setText("");

            /*for(Location loc: locTesoros){

            textViewDist.setText(textViewDist.getText()+"Localizacion: " + tesoroLoc.distanceTo(loc));
            }*/


            // movemos la camara para la nueva posicion

            LatLng nuevaPosicion = new LatLng(location.getLatitude(),location.getLongitude());

            CameraPosition cameraPosition = CameraPosition.builder()

                    .target(nuevaPosicion)

                    .zoom(15)

                    .build();

            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


            Location tes1 =new Location("");

            tes1.setLatitude(tesoro1.latitude);

            tes1.setLongitude(tesoro1.longitude);


            Location tes2=new Location("");

            tes2.setLatitude(tesoro2.latitude);

            tes2.setLongitude(tesoro2.longitude);


            Location tes3=new Location("");

            tes3.setLatitude(tesoro3.latitude);

            tes3.setLongitude(tesoro3.longitude);


            Location inicio =new Location("");

            inicio.setLatitude(danielCastelao.latitude);

            inicio.setLongitude(danielCastelao.longitude);


            textViewDist.setText("Distancia: "+ location.distanceTo(tes1));

    }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.i(TAG, "onStatusChanged()");
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.i(TAG, "onProviderEnabled()");
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.i(TAG, "onProviderDisabled()");
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.addMarker(new MarkerOptions().position(danielCastelao).title("Posición inicial (Daniel Castelao)"));

        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.getUiSettings().setCompassEnabled(true);

        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // TODO: Consider calling

            //    ActivityCompat#requestPermissions

            // here to request the missing permissions, and then overriding

            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,

            //                                          int[] grantResults)

            // to handle the case where the user grants the permission. See the documentation

            // for ActivityCompat#requestPermissions for more details.

            return;

        }

        mMap.setMyLocationEnabled(true);
        //Pintamos el círculo alrededor de la marca de inicio.
        mMap.addCircle(new CircleOptions().center(danielCastelao).radius(300).strokeColor(Color.GREEN));

        //Añadimos tres marcas para las ubicaciones de los tesoros.
        mMap.addMarker(new MarkerOptions().position(tesoro1).title("Primer tesoro"));

        mMap.addMarker(new MarkerOptions().position(tesoro2).title("Segundo tesoro"));

        mMap.addMarker(new MarkerOptions().position(tesoro3).title("Tercer tesoro"));


    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        // Comprobamos si el resultado de la segunda actividad es "RESULT_OK".

        if (resultCode == RESULT_OK) {

            // Comprobamos el codigo de nuestra llamada

            if (requestCode == codigo) {

                // Recojemos el dato que viene en el Intent (se pasa por parámetro con el nombre de data)

                // Rellenamos el TextView

                reciboDato.setText(data.getExtras().getString("retorno"));

            }

        }

    }

}

