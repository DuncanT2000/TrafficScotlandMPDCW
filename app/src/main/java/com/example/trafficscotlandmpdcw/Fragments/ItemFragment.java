package com.example.trafficscotlandmpdcw.Fragments;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.trafficscotlandmpdcw.Item;
import com.example.trafficscotlandmpdcw.LoadingFragment;
import com.example.trafficscotlandmpdcw.MainActivity;
import com.example.trafficscotlandmpdcw.MapFragment;
import com.example.trafficscotlandmpdcw.R;

import java.util.ArrayList;


public class ItemFragment extends Fragment implements View.OnClickListener {

    View view;

    Item item;

    Fragment mapFragment;

    Button journeyBtn;

    Button closeBtn;




    public ItemFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_item, container, false);

        item = MainActivity.feedData.getFeedInfoArray().get(getArguments().getInt("itemPos", 0));


        journeyBtn = view.findViewById(R.id.addToJourneyBTN);

        closeBtn = view.findViewById(R.id.closeBtn);


        journeyBtn.setOnClickListener(this);
        closeBtn.setOnClickListener(this);

        Log.d(TAG, "onCreateView: " + item);

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        Bundle bundle = new Bundle();
        mapFragment=  new MapFragment();
        bundle.putString("itemLat", item.getLatStr());
        bundle.putString("itemLong", item.getLongStr());
        mapFragment.setArguments(bundle);

        transaction.replace(R.id.map_fragment,mapFragment);
        transaction.commit();


        return view;
    }

    @Override
    public void onClick(View view) {

        if (view == journeyBtn){

            ArrayList<Item> currentJourneyData = MainActivity.JourneyData.getFeedInfoArray();

            currentJourneyData.add(item);

            MainActivity.JourneyData.setFeedInfoArray(currentJourneyData);

            Toast.makeText(getContext(),"Roadwork has been added to your journey!", Toast.LENGTH_SHORT).show();

        }

        if (view == closeBtn){

            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();

            HomeFragment homeFrag = new HomeFragment();

            Bundle bundle = new Bundle();
            bundle.putSerializable("feedData", MainActivity.feedData.getFeedInfoArray());
            homeFrag.setArguments(bundle);

            transaction.replace(MainActivity.pageFragment.getId(), homeFrag);
            transaction.commit();


        }


    }
}