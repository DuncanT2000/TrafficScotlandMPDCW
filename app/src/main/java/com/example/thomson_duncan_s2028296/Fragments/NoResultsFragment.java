package com.example.thomson_duncan_s2028296.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.trafficscotlandmpdcw.R;


public class NoResultsFragment extends Fragment {

    View view;
    TextView message;

    public NoResultsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view =inflater.inflate(R.layout.fragment_no_results, container, false);



        message = view.findViewById(R.id.messagetv);

        message.setText("No results found for search: ");


        return view;
    }

}