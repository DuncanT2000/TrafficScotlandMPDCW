package com.example.trafficscotlandmpdcw.Fragments;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.trafficscotlandmpdcw.Item;
import com.example.trafficscotlandmpdcw.R;

import java.util.ArrayList;


public class SearchFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "SearchFrag";
    View view;
    ArrayList<Item> items;
    Button searchBtn;
    EditText searchTerm;

    public SearchFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);


        items = getArguments().getParcelableArrayList("feedData");

        searchBtn = view.findViewById(R.id.searchBtn);
        searchTerm = view.findViewById(R.id.searchTerm);

        Log.d(TAG, "onCreateView: " + items);

        searchBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view == searchBtn){

            String searchTermStr = searchTerm.getText().toString();



        }
    }
}