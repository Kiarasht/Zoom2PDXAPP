package com.restart.zoom2pdxapp;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    double circlevalue = 0;
    double circlevalue2 = 0;
    private GoogleMap mMap;
    private int total = 0;
    CircleOptions circleOptions;
    Circle myCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
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
        if (total == 1) {
            circleOptions = new CircleOptions()
                    .center(latLng)
                    .radius(1500)
                    .strokeColor(Color.BLUE)
                    .fillColor(0x40ff0000)
                    .strokeWidth(5);
            if (circlevalue >= 3) {
                mMap.addMarker(new MarkerOptions().position(latLng)
                        .title(String.valueOf(circlevalue)).snippet("Outstanding").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            } else if (circlevalue >= 2) {
                mMap.addMarker(new MarkerOptions().position(latLng)
                        .title(String.valueOf(circlevalue)).snippet("Good").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            } else if (circlevalue >= 1) {
                mMap.addMarker(new MarkerOptions().position(latLng)
                        .title(String.valueOf(circlevalue)).snippet("Fair").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            } else if (circlevalue >= 0) {
                mMap.addMarker(new MarkerOptions().position(latLng)
                        .title(String.valueOf(circlevalue)).snippet("Bad").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            }
            myCircle = mMap.addCircle(circleOptions);
        } else if (total == 2) {
            circleOptions = new CircleOptions()
                    .center(latLng)
                    .radius(1500)
                    .strokeColor(Color.BLUE)
                    .fillColor(R.color.red_global_color)
                    .strokeWidth(5);
            if (circlevalue2 >= 3) {
                mMap.addMarker(new MarkerOptions().position(latLng)
                        .title(String.valueOf(circlevalue2)).snippet("Outstanding").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            } else if (circlevalue2 >= 2) {
                mMap.addMarker(new MarkerOptions().position(latLng)
                        .title(String.valueOf(circlevalue2)).snippet("Good").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            } else if (circlevalue2 >= 1) {
                mMap.addMarker(new MarkerOptions().position(latLng)
                        .title(String.valueOf(circlevalue2)).snippet("Fair").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            } else if (circlevalue2 >= 0) {
                mMap.addMarker(new MarkerOptions().position(latLng)
                        .title(String.valueOf(circlevalue2)).snippet("Bad").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            }
            myCircle = mMap.addCircle(circleOptions);
        }
        searchSchools(latLng);
    }

    public void searchSchools(LatLng latLng) {
        final LatLng latLngParameter = latLng;
        AsyncTask.execute(new Runnable() {
            public void run() {
                String strContent = "";
                String strContent2 = "";

                try {
                    URL urlHandle = new URL("http://api.civicapps.org/schools/near/" + latLngParameter.longitude + "," + latLngParameter.latitude);
                    URLConnection urlconnectionHandle = urlHandle.openConnection();
                    InputStream inputstreamHandle = urlconnectionHandle.getInputStream();

                    try {
                        int intRead;
                        byte[] byteBuffer = new byte[1024];

                        do {
                            intRead = inputstreamHandle.read(byteBuffer);

                            if (intRead == 0) {
                                break;

                            } else if (intRead == -1) {
                                break;

                            }

                            strContent += new String(byteBuffer, 0, intRead, "UTF-8");
                        } while (true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    inputstreamHandle.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    JSONArray results = new JSONObject(strContent).getJSONArray("results");

                    for (int i = 0; i < results.length(); i += 1) {
                        JSONObject result = results.getJSONObject(i);

                        final String school_id = String.valueOf(result.getString("SchoolID"));
                        final String school_name = String.valueOf(result.getString("SchoolName"));
                        final String phone_number = String.valueOf(result.getString("PhoneNumber"));
                        final double latParameter = Double.parseDouble(result.getString("Latitude"));
                        final double lngParameter = Double.parseDouble(result.getString("Longitude"));

                        MapsActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                mMap.addMarker(new MarkerOptions().position(new LatLng(latParameter, lngParameter))
                                        .title(school_name).snippet(phone_number));
                            }
                        });

                        try {
                            URL urlHandle = new URL("http://api.civicapps.org/schools/" + school_id);
                            URLConnection urlconnectionHandle = urlHandle.openConnection();
                            InputStream inputstreamHandle = urlconnectionHandle.getInputStream();

                            try {
                                int intRead;
                                byte[] byteBuffer = new byte[1024];

                                do {
                                    intRead = inputstreamHandle.read(byteBuffer);

                                    if (intRead == 0) {
                                        break;

                                    } else if (intRead == -1) {
                                        break;

                                    }

                                    strContent2 += new String(byteBuffer, 0, intRead, "UTF-8");
                                } while (true);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            inputstreamHandle.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            JSONObject jsonObject = new JSONObject(strContent2).getJSONObject("results").getJSONObject("SchoolInformation").getJSONObject("Dropouts");

                            final double dropout_previous = Double.parseDouble(String.valueOf(jsonObject.get("StateDropoutStudentsPctPrevYear")));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
