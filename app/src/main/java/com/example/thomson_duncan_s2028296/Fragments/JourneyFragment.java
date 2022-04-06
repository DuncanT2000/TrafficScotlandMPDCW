/*
 * Name; Duncan THomson
 * Student Number: s2028296
 * */

package com.example.thomson_duncan_s2028296.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.thomson_duncan_s2028296.Item;
import com.example.trafficscotlandmpdcw.R;

import java.util.ArrayList;

public class JourneyFragment extends Fragment {

    View view;

    Fragment journeyMap;


    public JourneyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_journey, container, false);



        androidx.fragment.app.FragmentManager manager = getFragmentManager();
        androidx.fragment.app.FragmentTransaction transaction = manager.beginTransaction();

        FullMapFragment fullMapFragment = new FullMapFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("feedData", new ArrayList<Item>());
        fullMapFragment.setArguments(bundle);
        transaction.replace(R.id.journey_map,fullMapFragment);
        transaction.replace(R.id.journeyoptions, new JourneyOptionsFragment());
        transaction.commit();



        return view;
    }




}