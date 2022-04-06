/*
* Name; Duncan THomson
* Student Number: s2028296
* */

package com.example.thomson_duncan_s2028296.Fragments;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.thomson_duncan_s2028296.Item;
import com.example.trafficscotlandmpdcw.R;


public class current_details extends Fragment {

    private View view;
    Item item;
    TextView TitleTv;
    TextView DescriptionTv;
    TextView PubDateTv;


    public current_details() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_current_details, container, false);

        item = getArguments().getParcelable("itemData");


        TitleTv = view.findViewById(R.id.item_title);
        DescriptionTv = view.findViewById(R.id.item_description);
        PubDateTv = view.findViewById(R.id.item_PubDate);


        TitleTv.setText("Title: " +item.getTitle().toString());
        DescriptionTv.setText("Description: " +item.getDescription().toString());
        PubDateTv.setText("Publication Date: " +item.getPubDate().toString());

        return view;
    }
}