package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class HospitalLocation extends AppCompatActivity implements OnMapReadyCallback {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hospital_location);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap map) {
        Intent intent = getIntent();
        double longitude = intent.getDoubleExtra("LONGITUDE", 35.50160994009155);
        double latitude = intent.getDoubleExtra("LATITUDE", 33.89418368294438);
        String title = intent.getStringExtra("TITLE");

        LatLng hospitalLatLng = new LatLng(latitude, longitude);
        map.addMarker(new MarkerOptions().position(hospitalLatLng).title(title));

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(hospitalLatLng, 10));
    }
}
