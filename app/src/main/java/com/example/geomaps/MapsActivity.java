package com.example.geomaps;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    FrameLayout map;
    GoogleMap gMap;
    SearchView searchView;
    Marker marker;
    FusedLocationProviderClient fusedClient;
    Location currentLocation;
    private static final int REQUEST_CODE = 101; // Predefined


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Link XML files to the java
        map = findViewById(R.id.map);
        searchView = findViewById(R.id.search);
        searchView.clearFocus();

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync((OnMapReadyCallback) this);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString().trim();
                if (location == null) {
                    Toast.makeText(MapsActivity.this, "Please enter location!", Toast.LENGTH_SHORT).show();
                } else {
                    Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                    try {
                        List<Address> addressList = geocoder.getFromLocationName(location, 1);
                        if (marker != null) {
                            marker.remove();
                        }
                        LatLng latLng = new LatLng(addressList.get(0).getLatitude(), addressList.get(0).getLongitude());
                        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(location);
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
                        gMap.animateCamera(cameraUpdate);
                        marker = gMap.addMarker(markerOptions);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.gMap = googleMap;
        LatLng latLng = new LatLng(45.5019, -73.5674);
        this.gMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        this.gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
        this.gMap.addMarker(new MarkerOptions().position(latLng).title("Your Current Location"));
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}