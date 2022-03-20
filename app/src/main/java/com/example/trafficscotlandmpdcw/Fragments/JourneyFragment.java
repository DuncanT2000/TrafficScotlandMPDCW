package com.example.trafficscotlandmpdcw.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trafficscotlandmpdcw.FragmentFeedData;
import com.example.trafficscotlandmpdcw.Item;
import com.example.trafficscotlandmpdcw.LoadingFragment;
import com.example.trafficscotlandmpdcw.MainActivity;
import com.example.trafficscotlandmpdcw.R;

import java.util.ArrayList;

public class JourneyFragment extends Fragment {

    View view;


    public JourneyFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_journey, container, false);

        ArrayList<Item> journeyArr = MainActivity.JourneyData.getFeedInfoArray();


        FragmentFeedData frFeedFrag = new FragmentFeedData();

        Bundle bundle = new Bundle();
        bundle.putSerializable("feedData", journeyArr);
        frFeedFrag.setArguments(bundle);

        if (journeyArr.size() == 0){
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.journeyListFragment,new LoadingFragment());
            transaction.commit();
        }else{
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.journeyListFragment,frFeedFrag);
            transaction.commit();
        }


        return view;
    }
}