/*
 * Name; Duncan THomson
 * Student Number: s2028296
 * */

package com.example.thomson_duncan_s2028296.Fragments;

import static java.time.temporal.ChronoUnit.DAYS;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.thomson_duncan_s2028296.Item;
import com.example.thomson_duncan_s2028296.MainActivity;
import com.example.trafficscotlandmpdcw.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.time.LocalDate;
import java.util.ArrayList;


public class FullMapFragment extends Fragment {


    private static final String TAG = "marker";
    ArrayList<Item> items;
    View view;


    public FullMapFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_full_map, container, false);

        items = getArguments().getParcelableArrayList("feedData");


        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.full_google_map);

        if (supportMapFragment != null){
            supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onMapReady(@NonNull GoogleMap googleMap) {

                    if (items.size() >0){
                        for (int i = 0; i < items.size() -1; i++) {
                            MarkerOptions markerOptions = new MarkerOptions();
                            LatLng latLng1 = new LatLng(Double.parseDouble(items.get(i).getLatStr()), Double.parseDouble(items.get(i).getLongStr()));
                            markerOptions.position(latLng1);
                            if (items.get(i).getStartDate() != null && items.get(i).getEndDate()  != null){
                                LocalDate startDate = LocalDate.parse(items.get(i).getStartDate());
                                LocalDate endDate = LocalDate.parse(items.get(i).getEndDate());

                                long days = DAYS.between(startDate, endDate);


                                if (days >=0 && days <=2){
                                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                }else if (days >=3 && days <=5){
                                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                                }
                                else if (days >=6){
                                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                                }else {
                                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                                }

                            }

                            markerOptions.title(i+"- "+items.get(i).getTitle());
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(Double.parseDouble(String.valueOf(56.4907)), Double.parseDouble(String.valueOf(-4.2026))), 5
                            ));



                            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(Marker marker) {


                                    FragmentManager manager = getFragmentManager();
                                    FragmentTransaction transaction = manager.beginTransaction();

                                    ItemFragment itemFragment = new ItemFragment();

                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("itemData", items.get(Integer.parseInt(marker.getTitle().split("-")[0])));
                                    itemFragment.setArguments(bundle);

                                    transaction.replace(MainActivity.pageFragment.getId(), itemFragment);
                                    transaction.commit();


                                    return false;
                                }
                            });

                            googleMap.addMarker(markerOptions);
                        }
                    }


                }

            });



        }

        return view;

    }




}