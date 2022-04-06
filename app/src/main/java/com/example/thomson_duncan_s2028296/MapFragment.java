/*
 * Name; Duncan THomson
 * Student Number: s2028296
 * */

package com.example.thomson_duncan_s2028296;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trafficscotlandmpdcw.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        String latStr = getArguments().getString("itemLat");

        String longStr = getArguments().getString("itemLong");

        Double latDouble = Double.parseDouble(latStr);
        Double longDouble = Double.parseDouble(longStr);

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);

        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                MarkerOptions markerOptions = new MarkerOptions();
                LatLng latLng1 = new LatLng(latDouble,longDouble);
                markerOptions.position(latLng1);
                markerOptions.title(latLng1.latitude+ " : " +latLng1.longitude);

                googleMap.clear();

                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        latLng1, 10
                ));

                googleMap.addMarker(markerOptions);
            }
        });

        return view;
    }
}