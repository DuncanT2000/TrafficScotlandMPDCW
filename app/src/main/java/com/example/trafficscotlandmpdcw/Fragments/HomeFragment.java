package com.example.trafficscotlandmpdcw.Fragments;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.trafficscotlandmpdcw.FragmentFeedData;
import com.example.trafficscotlandmpdcw.Item;
import com.example.trafficscotlandmpdcw.ItemActivity;
import com.example.trafficscotlandmpdcw.LoadingFragment;
import com.example.trafficscotlandmpdcw.R;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private static final String TAG = "Home Fragment";
    public static View contentFragment;
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

        contentFragment = view.findViewById(R.id.contentFragment);

        Log.d(TAG, "onCreateView: " + items);

            FragmentFeedData frFeedFrag = new FragmentFeedData();

            Bundle bundle = new Bundle();
            bundle.putSerializable("feedData", items);
            frFeedFrag.setArguments(bundle);

            if (items.size() == 0){
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.topPageFrag,new HomeOptionsFragment());
                transaction.replace(R.id.contentFragment,new LoadingFragment());
                transaction.commit();
            }else{
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.topPageFrag,new HomeOptionsFragment());
                transaction.replace(R.id.contentFragment,frFeedFrag);
                transaction.commit();
            }



        return view;
    }

}