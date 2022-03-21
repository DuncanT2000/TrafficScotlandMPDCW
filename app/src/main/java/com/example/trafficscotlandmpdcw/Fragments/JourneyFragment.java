package com.example.trafficscotlandmpdcw.Fragments;

import static android.content.ContentValues.TAG;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trafficscotlandmpdcw.FragmentFeedData;
import com.example.trafficscotlandmpdcw.Item;
import com.example.trafficscotlandmpdcw.LoadingFragment;
import com.example.trafficscotlandmpdcw.MainActivity;
import com.example.trafficscotlandmpdcw.R;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

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