package com.cryptoull.motam_sdriver;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;

public class Main2Activity extends AppCompatActivity implements OnMapReadyCallback {


    private Button mLogoffBtn;

    private FirebaseAuth mAuth;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference userLocation = database.getReference("userLocation");
    private DatabaseReference locationUsers = database.getReference("locationUsers");
    private DatabaseReference ejemplos = database.getReference("ejemplos");

    private Ubicacion ubi;


    String mensaje1;
    String direccion = "";
    private Location location;
    private TextView textLat;
    private TextView textLon;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        /*
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

*/

        miUbicacion();

        mAuth = (FirebaseAuth) Comunicador.getObjeto();


        textLat = (TextView) findViewById(R.id.latitude);
        textLon = (TextView) findViewById(R.id.longitude);



        //updatePosition();

        mLogoffBtn = (Button) findViewById(R.id.logoff);




        textLat.setText(String.valueOf(location.getLatitude()));
        textLon.setText(String.valueOf(location.getLongitude()));


        long taim = System.currentTimeMillis();
       // Timestamp timestamp = new Timestamp(taim);

        ubi = new Ubicacion(mAuth.getUid(),location.getLatitude(),location.getLongitude(), taim);


        //userLocation.child(mAuth.getUid()).setValue(ubi);

        locationUsers.child(String.valueOf(taim)).setValue(ubi);


        //ejemplos.push(String.valueOf(taim));

        mLogoffBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();

                Intent intent = new Intent(Main2Activity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        LatLng currentPosition = new LatLng(ubi.getLat(), ubi.getLong());
        googleMap.addMarker(new MarkerOptions().position(currentPosition)
                .title("I'm Here"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentPosition));
    }

    //actualizar la ubicacion
    private void ActualizarUbicacion(Location location) {
        if (location != null) {
            ubi.setLat(location.getLatitude());
            ubi.setLon(location.getLongitude());
            ubi.setTimestamp(System.currentTimeMillis());
            //AgregarMarcador(lat, lng);

        }
    }

    public void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }

    }

    //control del gps
    LocationListener locListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {


            NumberFormat nf = NumberFormat.getInstance();

            nf.setMaximumFractionDigits(4);

            //String st=nf.format(x);
//lo vuelves a convertir a double
            //double dou = Double.valueOf(st);


            ActualizarUbicacion(location);
            //userLocation.child(mAuth.getUid()).setValue(ubi);

            textLat.setText(String.valueOf(nf.format(location.getLatitude())));
            textLon.setText(String.valueOf(nf.format(location.getLongitude())));
            locationUsers.child(ubi.getTimeStringStamp()).setValue(ubi);
            //setLocation(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {
            mensaje1 = ("GPS Activado");
            Mensaje();
        }

        @Override
        public void onProviderDisabled(String s) {
            mensaje1 = ("GPS Desactivado");
            locationStart();
            Mensaje();
        }
    };


    private static int PETICION_PERMISO_LOCALIZACION = 101;

    private void miUbicacion() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PETICION_PERMISO_LOCALIZACION);
            return;
        } else {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            //ActualizarUbicacion(location);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1200,0,locListener);
        }

    }


    public void Mensaje() {
        Toast toast = Toast.makeText(this, mensaje1, Toast.LENGTH_LONG);
        //toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }

}
