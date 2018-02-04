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

    private TextView textViewDist1, textViewDist2, textViewDist3;

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

    private final static int codigo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        textViewDist1 = (TextView) findViewById(R.id.txtDist1);
        textViewDist2 = (TextView) findViewById(R.id.txtDist2);
        textViewDist3 = (TextView) findViewById(R.id.txtDist3);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mLocMgr = (LocationManager) getSystemService(LOCATION_SERVICE);

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

        //Localizaciones de los tres tesoros.
        tesoro1 = new LatLng(42.237246,-8.714106);
        tesoro2 = new LatLng(42.237032,-8.714410);
        tesoro3 = new LatLng(42.23746,-8.715424);

        intent = this.getIntent();

        Button btnQR = (Button) findViewById(R.id.btnQR);
        btnQR.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                Intent lectorQR = new Intent(getApplicationContext(), QRActivity.class);
                if(intent.getExtras() != null){
                    lectorQR.putExtra("tes1", intent.getExtras().getBoolean("tes1"));
                    lectorQR.putExtra("tes2", intent.getExtras().getBoolean("tes2"));
                    lectorQR.putExtra("tes3", intent.getExtras().getBoolean("tes3"));
                }else{
                    lectorQR.putExtra("tes1",false);
                    lectorQR.putExtra("tes2",false);
                    lectorQR.putExtra("tes3",false);
                }
                startActivity(lectorQR);

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

            textViewDist1.setText("");
            textViewDist2.setText("");
            textViewDist3.setText("");

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


            textViewDist1.setText("Distancia al tesoro 1: "+ location.distanceTo(tes1));
            textViewDist2.setText("Distancia al tesoro 2: "+ location.distanceTo(tes2));
            textViewDist3.setText("Distancia al tesoro 3: "+ location.distanceTo(tes3));

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
        mMap.addCircle(new CircleOptions().center(danielCastelao).radius(300).strokeColor(Color.BLACK));

        //Añadimos tres marcas para las ubicaciones de los tesoros.
        mMap.addMarker(new MarkerOptions().position(tesoro1).title("Primer tesoro"));

        mMap.addMarker(new MarkerOptions().position(tesoro2).title("Segundo tesoro"));

        mMap.addMarker(new MarkerOptions().position(tesoro3).title("Tercer tesoro"));


    }
}

