package com.example.trafficscotlandmpdcw.Fragments;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import android.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trafficscotlandmpdcw.FragmentFeedData;
import com.example.trafficscotlandmpdcw.Item;
import com.example.trafficscotlandmpdcw.R;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private static final String TAG = "Home Fragment";
    View view;

    ArrayList<Item> items;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);

        items = getArguments().getParcelableArrayList("feedData");

        Log.d(TAG, "onCreateView: " + items);

            FragmentFeedData frFeedFrag = new FragmentFeedData();

            Bundle bundle = new Bundle();
            bundle.putSerializable("feedData", items);
            frFeedFrag.setArguments(bundle);

            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.topPageFrag,new HomeOptionsFragment());
            transaction.replace(R.id.contentFragment,frFeedFrag);
            transaction.commit();







        return view;
    }
}