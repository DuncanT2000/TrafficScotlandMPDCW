/*
 * Name; Duncan THomson
 * Student Number: s2028296
 * */

package com.example.thomson_duncan_s2028296;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trafficscotlandmpdcw.R;

import java.util.ArrayList;

public class FragmentFeedData extends Fragment

{
    ArrayList<Item> items;
    View view;

    RecyclerView rc;

    public FragmentFeedData()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        view = inflater.inflate(R.layout.fragment_feed_data, container, false);
        rc = (RecyclerView) view.findViewById(R.id.recyclerView);
        items = getArguments().getParcelableArrayList("feedData");

        setAdapter();


        return view;
    }

    private void setAdapter(){
        recyclerAdapter adapter = new recyclerAdapter(items);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        rc.setLayoutManager(layoutManager);
        rc.setItemAnimator(new DefaultItemAnimator());
        rc.setAdapter(adapter);
    }



}