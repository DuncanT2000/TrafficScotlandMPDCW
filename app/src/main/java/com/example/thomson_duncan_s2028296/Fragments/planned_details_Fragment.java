/*
 * Name; Duncan THomson
 * Student Number: s2028296
 * */

package com.example.thomson_duncan_s2028296.Fragments;

import static android.content.ContentValues.TAG;

import static java.time.temporal.ChronoUnit.DAYS;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.thomson_duncan_s2028296.Item;
import com.example.trafficscotlandmpdcw.R;

import java.time.LocalDate;


public class planned_details_Fragment extends Fragment {

    View view;

    Item item;

    TextView itemTitle;
    TextView itemDuration;
    TextView itemWorks;
    TextView itemTrafficManagement;
    TextView itemDiversionInfomation;
    TextView itemPubDate;


    public planned_details_Fragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =inflater.inflate(R.layout.fragment_planned_details_, container, false);

        item = getArguments().getParcelable("itemData");

        itemTitle = view.findViewById(R.id.item_title);
        itemDuration = view.findViewById(R.id.item_duration);
        itemWorks = view.findViewById(R.id.item_works);
        itemTrafficManagement = view.findViewById(R.id.item_traffic_management);
        itemDiversionInfomation = view.findViewById(R.id.item_diversion_information);
        itemPubDate = view.findViewById(R.id.item_PubDate);

        itemTitle.setText("Title: " +item.getTitle());

        LocalDate startDate = LocalDate.parse(item.getStartDate());
        LocalDate endDate = LocalDate.parse(item.getEndDate());


        long days = DAYS.between(startDate, endDate);

        itemDuration.setText("Duration: " + item.getStartDate() + " - "+ item.getEndDate() + " (" + days + "days)" );

        itemWorks.setText("Works: " + item.getWork());
        itemTrafficManagement.setText("Traffic Management: " + item.getTrafficManagement());

        itemDiversionInfomation.setText("Diversion Infomation: " + item.getDiversionInformation());

        itemPubDate.setText("Publication Date: " + item.getPubDate());




        return view;
    }
}