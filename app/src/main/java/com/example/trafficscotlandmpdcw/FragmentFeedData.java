package com.example.trafficscotlandmpdcw;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

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