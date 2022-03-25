package com.example.trafficscotlandmpdcw.Fragments;

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

import com.example.trafficscotlandmpdcw.Item;
import com.example.trafficscotlandmpdcw.R;

import java.time.LocalDate;


public class roadwork_details_Fragment extends Fragment {

    View view;
    Item item;

    TextView itemTitle;
    TextView itemDuration;
    TextView itemDelayInformation;
    TextView itemPubDate;

    public roadwork_details_Fragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_roadwork_details_, container, false);
        item = getArguments().getParcelable("itemData");

        itemTitle = view.findViewById(R.id.item_title);
        itemDuration = view.findViewById(R.id.item_duration);
        itemDelayInformation = view.findViewById(R.id.item_delay_information);
        itemPubDate = view.findViewById(R.id.item_PubDate);

        itemTitle.setText("Title: " + item.getTitle());

        LocalDate startDate = LocalDate.parse(item.getStartDate());
        LocalDate endDate = LocalDate.parse(item.getEndDate());
        long days = DAYS.between(startDate, endDate);

        itemDuration.setText("Duration: " + item.getStartDate() + " - " + item.getEndDate() + " (" + days + "days)" );
        itemDelayInformation.setText("Delay Information: " + item.getDelayInformation());
        itemPubDate.setText("Publication Date: " + item.getPubDate());

        Log.d(TAG, "onCreateView: " +item);

        return view;
    }
}