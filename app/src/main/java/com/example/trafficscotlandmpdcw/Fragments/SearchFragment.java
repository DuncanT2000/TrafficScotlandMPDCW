package com.example.trafficscotlandmpdcw.Fragments;

import static android.content.ContentValues.TAG;

import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.trafficscotlandmpdcw.FragmentFeedData;
import com.example.trafficscotlandmpdcw.Item;
import com.example.trafficscotlandmpdcw.MainActivity;
import com.example.trafficscotlandmpdcw.R;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;


public class SearchFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "SearchFrag";
    View view;
    ArrayList<Item> items;
    Button searchBtn;
    EditText searchTerm;
    ArrayList<Item> FilteredFeedInfo;

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


    public void startFilterBySearch( String type)
    {
        Log.d(TAG, "startFilterByType: "+ type);
        if (items.size()> 0){
            new Thread(new FilterDataBySearchTerm(type)).start();
        }else{
            Log.d(TAG, "startFilterBySearch: items Arr is less than 0" );
        }
    }

    @Override
    public void onClick(View view) {
        if (view == searchBtn){

            String searchTermStr = searchTerm.getText().toString();

            Log.d(TAG, "onClick: " + searchTermStr.toUpperCase());

            startFilterBySearch(searchTermStr);

        }
    }


    private class FilterDataBySearchTerm implements Runnable
    {
        private String searchTerm;

        public FilterDataBySearchTerm(String searchTerm) {
            this.searchTerm = searchTerm;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void run()
        {

            FilteredFeedInfo = new ArrayList<Item>();

            for (int i = 0; i < items.size() - 1; i++) {

                if (items.get(i).getTitle().toUpperCase().contains(searchTerm.toUpperCase())){
                    FilteredFeedInfo.add(items.get(i));
                }


            }



            getActivity().runOnUiThread(new Runnable()
            {
                public void run() {

                    if (FilteredFeedInfo !=null){
                        if (FilteredFeedInfo.size() >0){
                            Log.d(TAG, "Filtered By Search: " + FilteredFeedInfo);
                            Log.d(TAG, "run UI THREAD:  Current Running UI Thread for Filtering by Search Term" );
                            Log.d(TAG, "run UI THREAD:  Item Filtered: " + FilteredFeedInfo.size());

                            Log.d(TAG, "FilteredFeedInfo is: " + FilteredFeedInfo.size());

                            androidx.fragment.app.FragmentManager manager = getFragmentManager();
                            androidx.fragment.app.FragmentTransaction transaction = manager.beginTransaction();

                            FragmentFeedData fragmentFeedData = new FragmentFeedData();

                            Bundle bundle = new Bundle();
                            bundle.putSerializable("feedData", FilteredFeedInfo);
                            fragmentFeedData.setArguments(bundle);
                            transaction.replace(R.id.search_results_frag,fragmentFeedData);
                            transaction.commit();


                        }else{
                            Log.d(TAG, "FilteredFeedInfo is: " + FilteredFeedInfo.size());
                        }





                    }






                }
            });


        }

    }


}