package com.example.trafficscotlandmpdcw.Fragments;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.trafficscotlandmpdcw.Item;
import com.example.trafficscotlandmpdcw.MainActivity;
import com.example.trafficscotlandmpdcw.R;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class JourneyOptionsFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener{


    View view;
    private Button datePickerBtn;
    private LocalDate selectedDate;
    private ArrayList<Item> AllFeedInfo;
    private ArrayList<Item> FilteredFeedInfo;
    private TextView setDateTV;

    private EditText toLocation;
    private EditText fromLocation;
    private Button viewJourneyBtn;

    public JourneyOptionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_journey_options, container, false);

        AllFeedInfo = MainActivity.feedData.getFeedInfoArray();

        datePickerBtn = view.findViewById(R.id.datepickerBTN);

        setDateTV = view.findViewById(R.id.setDateTV);

        toLocation = view.findViewById(R.id.et_to);
        fromLocation = view.findViewById(R.id.et_from);
        viewJourneyBtn = view.findViewById(R.id.viewjourneyBtn);

        datePickerBtn.setOnClickListener(this);

        viewJourneyBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v)
    {

        if (v == datePickerBtn) {
            Log.d(TAG, "onClick: User is looking to open set a new date");
            showDatePickerDialog();
        }

        if (v == viewJourneyBtn){
            String fromStr = fromLocation.getText().toString();
            String toStr = toLocation.getText().toString();



        }


    }

    private void DisplayDirections (String fromLocation, String toLocation){

    }

    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_YEAR));
        datePickerDialog.show();
    }
    public void startFilterByDate()
    {

        if (AllFeedInfo.size()> 0){
            new Thread(new FilterDataByDate()).start();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

        selectedDate = LocalDate.of(year, month + 1, day);

        setDateTV.setText(selectedDate.getYear()+ "-" + selectedDate.getMonthValue() + "-" + selectedDate.getDayOfMonth());

        startFilterByDate();

    }


    private class FilterDataByDate implements Runnable
    {


        public FilterDataByDate() {

        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void run()
        {

            FilteredFeedInfo = new ArrayList<Item>();




                for (int i = 0; i < MainActivity.feedData.getFeedInfoArray().size(); i++) {
                    LocalDate pickedLDate = selectedDate;



                    if ( AllFeedInfo.get(i).getStartDate() != null &&  AllFeedInfo.get(i).getEndDate() != null){
                        LocalDate startLDate = LocalDate.parse(AllFeedInfo.get(i).getStartDate());
                        LocalDate endLDate = LocalDate.parse(AllFeedInfo.get(i).getEndDate());

                        Date pickedDate = java.sql.Date.valueOf(pickedLDate.toString());
                        Date startDate = java.sql.Date.valueOf(startLDate.toString());
                        Date endDate = java.sql.Date.valueOf(endLDate.toString());

                        if (AllFeedInfo.get(i).getItemType() == "planned" && pickedDate.equals(startDate) || pickedDate.after(startDate) && pickedDate.before(endDate) || pickedDate.equals(endDate)){
                            FilteredFeedInfo.add(AllFeedInfo.get(i));
                        }

                    }

                }



            getActivity().runOnUiThread(new Runnable()
            {
                public void run() {

                    if (FilteredFeedInfo !=null){

                        Log.d(TAG, "run UI THREAD:  Current Running UI Thread for Filtering" );
                        Log.d(TAG, "run UI THREAD:  Item Filtered: " + FilteredFeedInfo.size());

                        Log.d(TAG, "run: " + FilteredFeedInfo.size());


                        androidx.fragment.app.FragmentManager manager = getFragmentManager();
                        androidx.fragment.app.FragmentTransaction transaction = manager.beginTransaction();

                        FullMapFragment fullMapFragment = new FullMapFragment();

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("feedData", FilteredFeedInfo);
                        fullMapFragment.setArguments(bundle);
                        transaction.replace(R.id.journey_map,fullMapFragment);
                        transaction.commit();



                    }






                }
            });


        }

    }

    }

