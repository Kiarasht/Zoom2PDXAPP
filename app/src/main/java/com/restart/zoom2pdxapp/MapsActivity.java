package com.restart.zoom2pdxapp;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private int total = 0;
    CircleOptions circleOptions;
    Circle myCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapClickListener(this);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(45.5236111, -122.675), 13));
    }

    @Override
    public void onMapClick(LatLng latLng) {
        ++total;

        if (total == 3) {
            mMap.clear();
            total = 1;
        }
        circleOptions = new CircleOptions()
                .center(latLng)
                .radius(1000)
                .strokeColor(Color.BLUE)
                .fillColor(0x40ff0000)
                .strokeWidth(5);

        myCircle = mMap.addCircle(circleOptions);
    }
}
