/*
 * Name; Duncan THomson
 * Student Number: s2028296
 * */

package com.example.thomson_duncan_s2028296.Fragments;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.thomson_duncan_s2028296.FragmentFeedData;
import com.example.thomson_duncan_s2028296.Item;
import com.example.thomson_duncan_s2028296.LoadingFragment;
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

        if (savedInstanceState!= null){

        }else{
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();

            if (items.size() == 0){
                transaction.replace(R.id.topPageFrag,new HomeOptionsFragment());
                transaction.replace(R.id.contentFragment,new LoadingFragment());
                transaction.commit();
            }else{

                FragmentFeedData fragmentFeedData = new FragmentFeedData();
                Bundle bundle = new Bundle();
                bundle.putSerializable("feedData", items);
                fragmentFeedData.setArguments(bundle);
                transaction.replace(R.id.topPageFrag,new HomeOptionsFragment());
                transaction.replace(contentFragment.getId(),fragmentFeedData);
                transaction.commit();

            }
        }


        return view;
    }

}