package com.example.trafficscotlandmpdcw.Fragments;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;

import android.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.trafficscotlandmpdcw.FragmentFeedData;
import com.example.trafficscotlandmpdcw.Item;
import com.example.trafficscotlandmpdcw.MainActivity;
import com.example.trafficscotlandmpdcw.R;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class HomeOptionsFragment extends Fragment  implements View.OnClickListener, DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener

{

    View view;

    private Button datePickerBtn;

    private LocalDate selectedDate;
    private TextView setDateTV;
    private Spinner type_spinner;

    private ArrayList<Item> AllFeedInfo = new ArrayList<Item>();
    private ArrayList<Item> FilteredFeedInfo =  new ArrayList<Item>();

    public HomeOptionsFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home_options, container, false);




        datePickerBtn = view.findViewById(R.id.datepickerBTN);

        setDateTV = view.findViewById(R.id.setDateTV);
        type_spinner = view.findViewById(R.id.type_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.itemType, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type_spinner.setAdapter(adapter);

        type_spinner.setOnItemSelectedListener(this);

        datePickerBtn.setOnClickListener(this);


        selectedDate = LocalDate.now();

        setDateTV.setText(selectedDate.toString());

        return view;
    }

    public void startFilter( String type)
    {
        Log.d(TAG, "startFilter: "+ type);
        if (AllFeedInfo.size()> 0){
            new Thread(new FilterData(type)).start();
        }


    }

    public void startFilterByDate(String type)
    {

        if (AllFeedInfo.size()> 0){
            new Thread(new FilterDataByDate(type)).start();
        }


    }



    @Override
    public void onClick(View v)
    {

        if (v == datePickerBtn) {
            Log.d(TAG, "onClick: User is looking to open set a new date");
            showDatePickerDialog();
        }

    }


    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_YEAR));
        datePickerDialog.show();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

        selectedDate = LocalDate.of(year, month + 1, day);

        setDateTV.setText(selectedDate.getYear()+ "-" + selectedDate.getMonthValue() + "-" + selectedDate.getDayOfMonth());

        if (type_spinner.getSelectedItemId() == 0){
            //startFilterByDate("planned");
        }
        if (type_spinner.getSelectedItemId() == 1){
            //startFilterByDate("current");
        }
        if (type_spinner.getSelectedItemId() == 2){
            //startFilterByDate("roadworks");
        }


    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {



        if (i == 0){
            startFilter("planned");
        }
        if (i == 1){
            startFilter("current");
        }
        if (i == 2){
            startFilter("roadworks");
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private class FilterData implements Runnable
    {
        private String feedType;

        public FilterData(String FeedType) {
            feedType = FeedType;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void run()
        {

            Log.d(TAG, "run:  New Thread Running");

            FilteredFeedInfo = new ArrayList<Item>();

            for (int i = 0; i < AllFeedInfo.size(); i++) {
                LocalDate pickedLDate = selectedDate;
                LocalDate startLDate = LocalDate.parse(AllFeedInfo.get(i).getStartDate());
                LocalDate endLDate = LocalDate.parse(AllFeedInfo.get(i).getEndDate());

                Date pickedDate = java.sql.Date.valueOf(pickedLDate.toString());
                Date startDate = java.sql.Date.valueOf(startLDate.toString());
                Date endDate = java.sql.Date.valueOf(endLDate.toString());




                if (AllFeedInfo.get(i).getItemType() == feedType && (pickedDate.equals(startDate) ||  pickedDate.equals(endDate) || pickedDate.after(startDate) && pickedDate.before(endDate))){
                    FilteredFeedInfo.add(AllFeedInfo.get(i));
                }
            }


            getActivity().runOnUiThread(new Runnable()
            {
                public void run() {

                    Log.d(TAG, "run UI THREAD:  Current Running UI Thread for Filtering" );
                    Log.d(TAG, "run UI THREAD:  Item Filtered: " + FilteredFeedInfo.size());
                    FragmentManager manager = getFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();





                }
            });


        }

    }

    private class FilterDataByDate implements Runnable
    {

        private String feedType;

        public FilterDataByDate(String FeedType) {
            feedType = FeedType;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void run()
        {

            Log.d(TAG, "run:  New Thread Running");
            FilteredFeedInfo = new ArrayList<Item>();


            for (int i = 0; i < AllFeedInfo.size(); i++) {
                LocalDate pickedLDate = selectedDate;
                LocalDate startLDate = LocalDate.parse(AllFeedInfo.get(i).getStartDate());
                LocalDate endLDate = LocalDate.parse(AllFeedInfo.get(i).getEndDate());

                Date pickedDate = java.sql.Date.valueOf(pickedLDate.toString());
                Date startDate = java.sql.Date.valueOf(startLDate.toString());
                Date endDate = java.sql.Date.valueOf(endLDate.toString());


                if (AllFeedInfo.get(i).getItemType() == feedType && (pickedDate.equals(startDate) ||  pickedDate.equals(endDate) || pickedDate.after(startDate) && pickedDate.before(endDate))){
                    FilteredFeedInfo.add(AllFeedInfo.get(i));
                }



            }


            getActivity().runOnUiThread(new Runnable()
            {
                public void run() {

                    Log.d(TAG, "run UI THREAD:  Current Running UI Thread for Filtering" );
                    Log.d(TAG, "run UI THREAD:  Item Filtered: " + FilteredFeedInfo.size());




                }
            });


        }

    }



}