/*
 * Name; Duncan THomson
 * Student Number: s2028296
 * */

package com.example.thomson_duncan_s2028296.Fragments;

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

import com.example.thomson_duncan_s2028296.MainActivity;
import com.example.thomson_duncan_s2028296.MapFragment;
import com.example.thomson_duncan_s2028296.Item;
import com.example.trafficscotlandmpdcw.R;


public class ItemFragment extends Fragment implements View.OnClickListener {

    View view;

    Item item;

    Fragment mapFragment;

    Button closeBtn;

    Fragment itemDetailsFragment;


    public ItemFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_item, container, false);

        item = getArguments().getParcelable("itemData");



        closeBtn = view.findViewById(R.id.closeBtn);



        closeBtn.setOnClickListener(this);



        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        Bundle bundle = new Bundle();
        Bundle itemDetailsBundle = new Bundle();
        mapFragment=  new MapFragment();
        bundle.putString("itemLat", item.getLatStr());
        bundle.putString("itemLong", item.getLongStr());
        mapFragment.setArguments(bundle);

        if (item.getItemType() == "planned"){
            itemDetailsFragment = new planned_details_Fragment();
        }else if(item.getItemType() == "current"){
            itemDetailsFragment = new current_details();
        }else if(item.getItemType() == "roadworks"){
            itemDetailsFragment = new roadwork_details_Fragment();
        }
        itemDetailsBundle.putSerializable("itemData", item);
        itemDetailsFragment.setArguments(itemDetailsBundle);

        transaction.replace(R.id.map_fragment,mapFragment);
        transaction.replace(R.id.item_details_fragment,itemDetailsFragment);
        transaction.commit();


        return view;
    }

    @Override
    public void onClick(View view) {

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