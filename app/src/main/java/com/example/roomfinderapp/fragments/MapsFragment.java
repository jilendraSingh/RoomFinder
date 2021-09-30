package com.example.roomfinderapp.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.roomfinderapp.R;
import com.example.roomfinderapp.service_classes.LocationClass;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;



public class MapsFragment extends Fragment {

    double userLat = 0.0;
    double userLong = 0.0;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {


        @Override
        public void onMapReady(final GoogleMap googleMap) {

            LocationClass.LocationResult locationResult = new LocationClass.LocationResult() {
                @Override
                public void gotLocation(Location location) {
                    //Got the location!
                    userLat = location.getLatitude();
                    userLong = location.getLongitude();


                    if(userLat!=0.0){
                        LatLng sydney = new LatLng(userLat, userLong);
                       // LatLng sydney = new LatLng(28.6127, 77.2773);
                        googleMap.addMarker(new MarkerOptions().position(sydney).title("current location"));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            googleMap.setMyLocationEnabled(true);
                        }
                    }
                }
            };
            LocationClass myLocation = new LocationClass();
            myLocation.getLocation(getContext(), locationResult);


        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}