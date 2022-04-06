package com.example.thomson_duncan_s2028296.Fragments;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;

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
import androidx.fragment.app.Fragment;

import com.example.thomson_duncan_s2028296.MainActivity;
import com.example.thomson_duncan_s2028296.FragmentFeedData;
import com.example.thomson_duncan_s2028296.Item;
import com.example.trafficscotlandmpdcw.R;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class HomeOptionsFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener

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


            AllFeedInfo = MainActivity.feedData.getFeedInfoArray();

            Log.d(TAG, "AllFeedInfo Size: " + AllFeedInfo.size());

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

        if (AllFeedInfo.size()> 0){
            new Thread(new FilterData(type)).start();
        }
    }

    public void startFilterByDate( String type)
    {
        Log.d(TAG, "startFilterByDate: "+ type);
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

        Log.d(TAG, "onDateSet: " + selectedDate);

        setDateTV.setText(selectedDate.toString());

        if (type_spinner.getSelectedItemId() == 0){
            startFilterByDate("all");
        }

        if (type_spinner.getSelectedItemId() == 1){
            startFilterByDate("planned");
        }
        if (type_spinner.getSelectedItemId() == 2){
            startFilterByDate("current");
        }
        if (type_spinner.getSelectedItemId() == 3){
            startFilterByDate("roadworks");
        }


    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        if (i == 0){
            startFilter("all");
        }

        if (i == 1){
            startFilter("planned");
        }
        if (i == 2){
            startFilter("current");
        }
        if (i == 3){
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



            FilteredFeedInfo = new ArrayList<Item>();
            if (feedType == "all"){
                FilteredFeedInfo = MainActivity.feedData.getFeedInfoArray();
            }else{
                for (int i = 0; i < MainActivity.feedData.getFeedInfoArray().size(); i++) {
                    LocalDate pickedLDate = selectedDate;


                    if (AllFeedInfo.get(i).getItemType() == "current" && feedType == "current"){
                        FilteredFeedInfo.add(AllFeedInfo.get(i));
                    }else{
                        if (AllFeedInfo.get(i).getItemType() == feedType && AllFeedInfo.get(i).getStartDate() != null &&  AllFeedInfo.get(i).getEndDate() != null){
                            LocalDate startLDate = LocalDate.parse(AllFeedInfo.get(i).getStartDate());
                            LocalDate endLDate = LocalDate.parse(AllFeedInfo.get(i).getEndDate());

                            Date pickedDate = java.sql.Date.valueOf(pickedLDate.toString());
                            Date startDate = java.sql.Date.valueOf(startLDate.toString());
                            Date endDate = java.sql.Date.valueOf(endLDate.toString());

                            if (pickedDate.equals(startDate) || pickedDate.after(startDate) && pickedDate.before(endDate) || pickedDate.equals(endDate)){
                                FilteredFeedInfo.add(AllFeedInfo.get(i));
                            }




                        }

                    }


                }

            }



            getActivity().runOnUiThread(new Runnable()
            {
                public void run() {

                    Log.d(TAG, "run UI THREAD:  Current Running UI Thread for Filtering by Type" );
                    Log.d(TAG, "run UI THREAD:  Item Filtered: " + FilteredFeedInfo.size());

                    Log.d(TAG, "run: " + FilteredFeedInfo.size());

                    androidx.fragment.app.FragmentManager manager = getFragmentManager();
                    androidx.fragment.app.FragmentTransaction transaction = manager.beginTransaction();

                    FragmentFeedData fragmentFeedData = new FragmentFeedData();

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("feedData", FilteredFeedInfo);
                    fragmentFeedData.setArguments(bundle);
                    transaction.replace(HomeFragment.contentFragment.getId(),fragmentFeedData);
                    transaction.commit();




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

            FilteredFeedInfo = new ArrayList<Item>();


            if (feedType == "all"){


                for (int i = 0; i < MainActivity.feedData.getFeedInfoArray().size(); i++) {
                    LocalDate pickedLDate = selectedDate;



                    if ( AllFeedInfo.get(i).getStartDate() != null &&  AllFeedInfo.get(i).getEndDate() != null){
                        LocalDate startLDate = LocalDate.parse(AllFeedInfo.get(i).getStartDate());
                        LocalDate endLDate = LocalDate.parse(AllFeedInfo.get(i).getEndDate());

                        Date pickedDate = java.sql.Date.valueOf(pickedLDate.toString());
                        Date startDate = java.sql.Date.valueOf(startLDate.toString());
                        Date endDate = java.sql.Date.valueOf(endLDate.toString());

                        if (pickedDate.equals(startDate) || pickedDate.after(startDate) && pickedDate.before(endDate) || pickedDate.equals(endDate)){
                            FilteredFeedInfo.add(AllFeedInfo.get(i));
                        }




                    }


                }


            }else{
                for (int i = 0; i < MainActivity.feedData.getFeedInfoArray().size(); i++) {
                    LocalDate pickedLDate = selectedDate;


                    if (AllFeedInfo.get(i).getItemType() == "current" && feedType == "current"){
                        FilteredFeedInfo.add(AllFeedInfo.get(i));
                    }


                    if (AllFeedInfo.get(i).getItemType() == feedType && AllFeedInfo.get(i).getStartDate() != null &&  AllFeedInfo.get(i).getEndDate() != null){
                        LocalDate startLDate = LocalDate.parse(AllFeedInfo.get(i).getStartDate());
                        LocalDate endLDate = LocalDate.parse(AllFeedInfo.get(i).getEndDate());

                        Date pickedDate = java.sql.Date.valueOf(pickedLDate.toString());
                        Date startDate = java.sql.Date.valueOf(startLDate.toString());
                        Date endDate = java.sql.Date.valueOf(endLDate.toString());

                        if (pickedDate.equals(startDate) || pickedDate.after(startDate) && pickedDate.before(endDate) || pickedDate.equals(endDate)){
                            FilteredFeedInfo.add(AllFeedInfo.get(i));
                        }




                    }


                }

                Log.d(TAG, "run: " + FilteredFeedInfo.size());
            }



            getActivity().runOnUiThread(new Runnable()
            {
                public void run() {

                    if (FilteredFeedInfo !=null){

                        Log.d(TAG, "run UI THREAD:  Current Running UI Thread for Filtering by Date" );
                        Log.d(TAG, "run UI THREAD:  Item Filtered: " + FilteredFeedInfo.size());

                        Log.d(TAG, "run: " + FilteredFeedInfo.size());

                        androidx.fragment.app.FragmentManager manager = getFragmentManager();
                        androidx.fragment.app.FragmentTransaction transaction = manager.beginTransaction();

                        FragmentFeedData fragmentFeedData = new FragmentFeedData();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("feedData", FilteredFeedInfo);
                        fragmentFeedData.setArguments(bundle);
                        transaction.replace(HomeFragment.contentFragment.getId(),fragmentFeedData);
                        transaction.commit();


                    }






                }
            });


        }

    }





}