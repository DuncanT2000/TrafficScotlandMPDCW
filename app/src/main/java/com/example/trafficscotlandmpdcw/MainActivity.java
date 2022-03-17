package com.example.trafficscotlandmpdcw;

import static android.content.ContentValues.TAG;

import static java.time.temporal.ChronoUnit.DAYS;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnClickListener, DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener
{

    private LinearLayout itemLayout;
    private ArrayList<Item> alist;
    private ArrayList<Item> currentincidents;
    private ArrayList<Item> roadworksincidents;
    private ArrayList<Item> AllFeedInfo = new ArrayList<Item>();
    private ArrayList<Item> FilteredFeedInfo = new ArrayList<Item>();
    private String result = "";
    private String url1="";
    private Button datePickerBtn;
    private Button searchBtn;
    private Button journeyBtn;
    private LocalDate selectedDate;
    private TextView setDateTV;
    private Spinner type_spinner;

    // Traffic Scotland Planned Roadworks XML link

    private String[] urlArray = {"https://trafficscotland.org/rss/feeds/plannedroadworks.aspx",
            "https://trafficscotland.org/rss/feeds/currentincidents.aspx",
            "https://trafficscotland.org/rss/feeds/roadworks.aspx"};

    private Fragment fr1;
    private Fragment frFeed;

    private Fragment fr;



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("MyTag","in onCreate");
        // Set up the raw links to the graphical components
        //itemLayout = (LinearLayout) findViewById(R.id.ItemLayout);
        Log.e("MyTag","after startButton");







        datePickerBtn = findViewById(R.id.datepickerBTN);
        searchBtn = findViewById(R.id.searchBtn);
        journeyBtn = findViewById(R.id.journeyMap);

        setDateTV = findViewById(R.id.setDateTV);
        type_spinner = findViewById(R.id.type_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.itemType, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type_spinner.setAdapter(adapter);

        type_spinner.setOnItemSelectedListener(this);

        datePickerBtn.setOnClickListener(this);
        searchBtn.setOnClickListener(this);
        journeyBtn.setOnClickListener(this);


        selectedDate = LocalDate.now();

        setDateTV.setText(selectedDate.toString());



        fr1 = new FragmentOne();
        frFeed = new FragmentFeedData();

        fr = fr1;

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment,fr);
        transaction.commit();


        Log.d(TAG, "onCreate: "+ AllFeedInfo.size());

        startProgress();


    }

    public void startProgress()
    {

        new Thread(new Task(urlArray)).start();

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

        if (v == searchBtn){
            Log.d(TAG, "onClick: User is looking to search");
        }else if (v == journeyBtn) {
            Log.d(TAG, "onClick: User is looking to open Journey Map");
            }
        else if (v == datePickerBtn) {
            Log.d(TAG, "onClick: User is looking to open set a new date");
            showDatePickerDialog();
        }

    }


    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
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
            startFilterByDate("planned");
        }
        if (type_spinner.getSelectedItemId() == 1){
            startFilterByDate("current");
        }
        if (type_spinner.getSelectedItemId() == 2){
            startFilterByDate("roadworks");
        }


}

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void parseData(String dataToParse, String type) {

        if (type == "planned"){
            alist = new ArrayList<Item>();
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(new StringReader(dataToParse));
                int eventType = xpp.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    // Found a start tag
                    if (eventType == XmlPullParser.START_TAG) {

                        if (xpp.getName().equalsIgnoreCase("channel")) {
                            Item aItem = new Item();
                            alist.add(aItem);
                        } else if (xpp.getName().equalsIgnoreCase("item")) {
                            Item aItem = new Item();
                            alist.add(aItem);
                        } else if (xpp.getName().equalsIgnoreCase("title")) {
                            // Now just get the associated text
                            String temp = xpp.nextText();
                            // Do something with text
                            if (alist.size() == 0) {
                                return;
                            }
                            alist.get(alist.size() - 1).setTitle(temp);

                        } else if (xpp.getName().equalsIgnoreCase("description")) {
                            // Now just get the associated text
                            String temp = xpp.nextText();
                            // Do something with text
                            if (alist.size() == 0) {
                                return;
                            }

                            String[] desDetails = temp.split("<br />");

                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(" d MMMM yyyy ", Locale.ENGLISH);

                            for (int i = 0; i < desDetails.length -1; i++) {
                                switch (i){
                                    case 0:
                                        String[] startDate = desDetails[i].split("Start Date: ");
                                        String [] sdate = startDate[1].split("[,-]+");


                                        LocalDate date1 = LocalDate.parse(sdate[1], formatter);
                                        alist.get(alist.size() - 1).setStartDate(date1.toString());
                                        break;
                                    case 1:
                                        String[] endDate = desDetails[i].split("End Date: ");
                                        String [] enddatenew = endDate[1].split("[,-]+");

                                        LocalDate date2 = LocalDate.parse(enddatenew[1], formatter);
                                        alist.get(alist.size() - 1).setEndDate(date2.toString());
                                        break;
                                }

                            }

                            alist.get(alist.size() - 1).setDescription(temp);

                        } else if (xpp.getName().equalsIgnoreCase("link")) {

                            String temp = xpp.nextText();

                            if (alist.size() == 0) {
                                return;
                            }
                            alist.get(alist.size() - 1).setLink(temp);
                        } else if (xpp.getName().equalsIgnoreCase("point")) {

                            String temp = xpp.nextText();


                            if (alist.size() == 0) {
                                return;
                            }

                            String[] geoData = temp.split(" ");
                            alist.get(alist.size() - 1).setLatStr(geoData[0]);
                            alist.get(alist.size() - 1).setLongStr(geoData[1]);
                            alist.get(alist.size() - 1).setGeorss(temp);
                        } else if (xpp.getName().equalsIgnoreCase("pubDate")) {
                            // Now just get the associated text
                            String temp = xpp.nextText();
                            // Do something with text
                            if (alist.size() == 0) {
                                return;
                            }
                            alist.get(alist.size() - 1).setPubDate(temp);
                        }

                    }


                    // Get the next event

                    eventType = xpp.next();

                } // End of while
            } catch (XmlPullParserException ae1) {
                Log.e("MyTag", "Parsing error" + ae1.toString());
            } catch (IOException ae1) {
                Log.e("MyTag", "IO error during parsing");
            }


            alist.remove(0);

            for (int i = 0; i < alist.size(); i++) {
                alist.get(i).setItemType("planned");
            }

            AllFeedInfo.addAll(alist);

        }
        else if (type == "current"){
            currentincidents = new ArrayList<Item>();
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(new StringReader(dataToParse));
                int eventType = xpp.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    // Found a start tag
                    if (eventType == XmlPullParser.START_TAG) {

                        if (xpp.getName().equalsIgnoreCase("channel")) {
                            Item aItem = new Item();
                            currentincidents.add(aItem);
                        } else if (xpp.getName().equalsIgnoreCase("item")) {
                            Item aItem = new Item();
                            currentincidents.add(aItem);
                        } else if (xpp.getName().equalsIgnoreCase("title")) {
                            // Now just get the associated text
                            String temp = xpp.nextText();
                            // Do something with text
                            if (currentincidents.size() == 0) {
                                return;
                            }
                            currentincidents.get(currentincidents.size() - 1).setTitle(temp);

                        } else if (xpp.getName().equalsIgnoreCase("description")) {
                            // Now just get the associated text
                            String temp = xpp.nextText();
                            // Do something with text
                            if (currentincidents.size() == 0) {
                                return;
                            }

                            String[] desDetails = temp.split("<br />");


                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(" d MMMM yyyy ", Locale.ENGLISH);

                            for (int i = 0; i < desDetails.length -1; i++) {
                                switch (i){
                                    case 0:
                                        String[] startDate = desDetails[i].split("Start Date: ");
                                        String [] sdate = startDate[1].split("[,-]+");


                                        LocalDate date1 = LocalDate.parse(sdate[1], formatter);
                                        currentincidents.get(currentincidents.size() - 1).setStartDate(date1.toString());
                                        break;
                                    case 1:
                                        String[] endDate = desDetails[i].split("End Date: ");
                                        String [] enddatenew = endDate[1].split("[,-]+");

                                        LocalDate date2 = LocalDate.parse(enddatenew[1], formatter);
                                        currentincidents.get(currentincidents.size() - 1).setEndDate(date2.toString());
                                        break;
                                }

                            }

                            currentincidents.get(currentincidents.size() - 1).setDescription(temp);

                        } else if (xpp.getName().equalsIgnoreCase("link")) {
                            // Now just get the associated text
                            String temp = xpp.nextText();
                            // Do something with text
                            if (currentincidents.size() == 0) {
                                return;
                            }
                            currentincidents.get(currentincidents.size() - 1).setLink(temp);
                        } else if (xpp.getName().equalsIgnoreCase("point")) {
                            // Now just get the associated text
                            String temp = xpp.nextText();
                            // Do something with text

                            if (currentincidents.size() == 0) {
                                return;
                            }

                            String[] geoData = temp.split(" ");
                            currentincidents.get(currentincidents.size() - 1).setLatStr(geoData[0]);
                            currentincidents.get(currentincidents.size() - 1).setLongStr(geoData[1]);
                            currentincidents.get(currentincidents.size() - 1).setGeorss(temp);
                        } else if (xpp.getName().equalsIgnoreCase("pubDate")) {
                            // Now just get the associated text
                            String temp = xpp.nextText();
                            // Do something with text
                            if (currentincidents.size() == 0) {
                                return;
                            }
                            currentincidents.get(currentincidents.size() - 1).setPubDate(temp);
                        }

                    }


                    // Get the next event

                    eventType = xpp.next();

                } // End of while
            } catch (XmlPullParserException ae1) {
                Log.e("MyTag", "Parsing error" + ae1.toString());
            } catch (IOException ae1) {
                Log.e("MyTag", "IO error during parsing");
            }


            currentincidents.remove(0);

            for (int i = 0; i < currentincidents.size(); i++) {
                currentincidents.get(i).setItemType("current");
            }

            AllFeedInfo.addAll(currentincidents);

        }
        else if (type == "roadworks"){
            roadworksincidents = new ArrayList<Item>();
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(new StringReader(dataToParse));
                int eventType = xpp.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    // Found a start tag
                    if (eventType == XmlPullParser.START_TAG) {

                        if (xpp.getName().equalsIgnoreCase("channel")) {
                            Item aItem = new Item();
                            roadworksincidents.add(aItem);
                        } else if (xpp.getName().equalsIgnoreCase("item")) {
                            Item aItem = new Item();
                            roadworksincidents.add(aItem);
                        } else if (xpp.getName().equalsIgnoreCase("title")) {
                            // Now just get the associated text
                            String temp = xpp.nextText();
                            // Do something with text
                            if (roadworksincidents.size() == 0) {
                                return;
                            }
                            roadworksincidents.get(roadworksincidents.size() - 1).setTitle(temp);

                        } else if (xpp.getName().equalsIgnoreCase("description")) {
                            // Now just get the associated text
                            String temp = xpp.nextText();
                            // Do something with text
                            if (roadworksincidents.size() == 0) {
                                return;
                            }

                            String[] desDetails = temp.split("<br />");

                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(" d MMMM yyyy ", Locale.ENGLISH);

                            for (int i = 0; i < desDetails.length -1; i++) {
                                switch (i){
                                    case 0:
                                        String[] startDate = desDetails[i].split("Start Date: ");
                                        String [] sdate = startDate[1].split("[,-]+");


                                        LocalDate date1 = LocalDate.parse(sdate[1], formatter);
                                        roadworksincidents.get(roadworksincidents.size() - 1).setStartDate(date1.toString());
                                        break;
                                    case 1:
                                        String[] endDate = desDetails[i].split("End Date: ");
                                        String [] enddatenew = endDate[1].split("[,-]+");

                                        LocalDate date2 = LocalDate.parse(enddatenew[1], formatter);
                                        roadworksincidents.get(roadworksincidents.size() - 1).setEndDate(date2.toString());
                                        break;
                                }

                            }

                            roadworksincidents.get(roadworksincidents.size() - 1).setDescription(temp);

                        } else if (xpp.getName().equalsIgnoreCase("link")) {
                            // Now just get the associated text
                            String temp = xpp.nextText();
                            // Do something with text
                            if (roadworksincidents.size() == 0) {
                                return;
                            }
                            roadworksincidents.get(roadworksincidents.size() - 1).setLink(temp);
                        } else if (xpp.getName().equalsIgnoreCase("point")) {
                            // Now just get the associated text
                            String temp = xpp.nextText();
                            // Do something with text

                            if (roadworksincidents.size() == 0) {
                                return;
                            }

                            String[] geoData = temp.split(" ");
                            roadworksincidents.get(roadworksincidents.size() - 1).setLatStr(geoData[0]);
                            roadworksincidents.get(roadworksincidents.size() - 1).setLongStr(geoData[1]);
                            roadworksincidents.get(roadworksincidents.size() - 1).setGeorss(temp);
                        } else if (xpp.getName().equalsIgnoreCase("pubDate")) {
                            // Now just get the associated text
                            String temp = xpp.nextText();
                            // Do something with text
                            if (roadworksincidents.size() == 0) {
                                return;
                            }
                            roadworksincidents.get(roadworksincidents.size() - 1).setPubDate(temp);
                        }

                    }


                    // Get the next event

                    eventType = xpp.next();

                } // End of while
            } catch (XmlPullParserException ae1) {
                Log.e("MyTag", "Parsing error" + ae1.toString());
            } catch (IOException ae1) {
                Log.e("MyTag", "IO error during parsing");
            }


            roadworksincidents.remove(0);

            for (int i = 0; i < roadworksincidents.size(); i++) {
                roadworksincidents.get(i).setItemType("roadworks");
            }

            AllFeedInfo.addAll(roadworksincidents);

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


    private class Task implements Runnable
    {
        private String[] url;
        private String feedType;

        public Task(String[] urlArray) {
            url = urlArray;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void run()
        {
            Log.d("TAG", "run: "+ url[0]);

            for (int i = 0; i < url.length; i++) {

                URL aurl;
                URLConnection yc;
                BufferedReader in = null;
                String inputLine = "";

                Log.e("MyTag","in run");

                try
                {
                    Log.e("MyTag","in try");
                    aurl = new URL(url[i]);
                    yc = aurl.openConnection();
                    in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                    Log.e("MyTag","after ready");
                    //
                    // Now read the data. Make sure that there are no specific hedrs
                    // in the data file that you need to ignore.
                    // The useful data that you need is in each of the item entries
                    //

                    while ((inputLine = in.readLine()) != null)
                    {
                        result = result + inputLine;

                    }

                    in.close();

                }
                catch (IOException ae)
                {
                    Log.e("MyTag", "ioexception in run");
                }

                Log.d("TAG", "Got Data: " + result);

                if(i == 0){
                    parseData(result,"planned");
                }
                else if (i == 1){
                    parseData(result,"current");
                }
                else if (i == 2){
                    parseData(result,"roadworks");
                }


            }

            MainActivity.this.runOnUiThread(new Runnable()
            {
                public void run() {

                    FragmentManager manager = getFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("feedData", AllFeedInfo);
                    frFeed.setArguments(bundle);
                    transaction.replace(R.id.fragment,frFeed);
                    transaction.commit();



                }
            });


        }

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
                LocalDate pickedLDate = LocalDate.parse(setDateTV.getText().toString());
                LocalDate startLDate = LocalDate.parse(AllFeedInfo.get(i).getStartDate());
                LocalDate endLDate = LocalDate.parse(AllFeedInfo.get(i).getEndDate());

                Date pickedDate = java.sql.Date.valueOf(pickedLDate.toString());
                Date startDate = java.sql.Date.valueOf(startLDate.toString());
                Date endDate = java.sql.Date.valueOf(endLDate.toString());




                if (AllFeedInfo.get(i).getItemType() == feedType && (pickedDate.equals(startDate) ||  pickedDate.equals(endDate) || pickedDate.after(startDate) && pickedDate.before(endDate))){
                    FilteredFeedInfo.add(AllFeedInfo.get(i));
                }
            }


            MainActivity.this.runOnUiThread(new Runnable()
            {
                public void run() {

                    Log.d(TAG, "run UI THREAD:  Current Running UI Thread for Filtering" );
                    Log.d(TAG, "run UI THREAD:  Item Filtered: " + FilteredFeedInfo.size());
                    FragmentManager manager = getFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();

                    frFeed = new FragmentFeedData();

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("feedData", FilteredFeedInfo);
                    frFeed.setArguments(bundle);
                    transaction.replace(R.id.fragment,frFeed);
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


            MainActivity.this.runOnUiThread(new Runnable()
            {
                public void run() {

                    Log.d(TAG, "run UI THREAD:  Current Running UI Thread for Filtering" );
                    Log.d(TAG, "run UI THREAD:  Item Filtered: " + FilteredFeedInfo.size());
                    FragmentManager manager = getFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();

                    frFeed = new FragmentFeedData();

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("feedData", FilteredFeedInfo);
                    frFeed.setArguments(bundle);
                    transaction.replace(R.id.fragment,frFeed);
                    transaction.commit();



                }
            });


        }

    }





}