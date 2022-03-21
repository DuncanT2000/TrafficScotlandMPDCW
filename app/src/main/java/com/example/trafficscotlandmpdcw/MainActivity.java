package com.example.trafficscotlandmpdcw;

import static android.content.ContentValues.TAG;

import static java.time.temporal.ChronoUnit.DAYS;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.app.DatePickerDialog;
import android.app.Dialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trafficscotlandmpdcw.Fragments.HomeFragment;
import com.example.trafficscotlandmpdcw.Fragments.HomeOptionsFragment;
import com.example.trafficscotlandmpdcw.Fragments.JourneyFragment;
import com.example.trafficscotlandmpdcw.Fragments.SearchFragment;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{

    public static FeedData feedData = new FeedData();
    public static FeedData JourneyData = new FeedData();

    public static View pageFragment;
    private LinearLayout itemLayout;
    private ArrayList<Item> alist;
    private ArrayList<Item> currentincidents;
    private ArrayList<Item> roadworksincidents;
    private ArrayList<Item> AllFeedInfo = new ArrayList<Item>();
    private ArrayList<Item> FilteredFeedInfo = new ArrayList<Item>();
    private ArrayList<Item> journeyItems = new ArrayList<Item>();
    private String result = "";
    private String url1="";
    private Button datePickerBtn;

    private LocalDate selectedDate;
    private TextView setDateTV;
    private Spinner type_spinner;

    private PopupMenu popupMenu;
    private Toolbar mytoolBar;


    // Traffic Scotland Planned Roadworks XML link

    private String[] urlArray = {"https://trafficscotland.org/rss/feeds/plannedroadworks.aspx",
            "https://trafficscotland.org/rss/feeds/currentincidents.aspx",
            "https://trafficscotland.org/rss/feeds/roadworks.aspx"};

    private Fragment fr1;
    private Fragment frFeed;
    private Fragment frHome;

    private Fragment fr;



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("MyTag","in onCreate");


        mytoolBar =  findViewById(R.id.my_tool_bar);
        setSupportActionBar(mytoolBar);


        pageFragment = findViewById(R.id.pageFragment);

        JourneyData.setFeedInfoArray(journeyItems);


        frFeed = new FragmentFeedData();


        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        HomeFragment homeFrag = new HomeFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("feedData", feedData.getFeedInfoArray());
        homeFrag.setArguments(bundle);

        transaction.replace(R.id.pageFragment, homeFrag);
        transaction.commit();


        Log.d(TAG, "onCreate: "+ feedData.getFeedInfoArray().size());

        startProgress();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }


    public void startProgress()
    {

        new Thread(new Task(urlArray)).start();

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

                                if (desDetails[2] != null){

                                    Pattern workPattern = Pattern.compile("(?<=Works:)(.*)(?=Traffic Management:)");
                                    Matcher workmatcher = workPattern.matcher(desDetails[2]);
                                    if (workmatcher.find())
                                    {
                                        alist.get(alist.size() - 1).setWork(workmatcher.group(1));
                                    }

                                    Pattern trafficPatternNoDiver = Pattern.compile("(?<=Traffic Management:)(.*)");
                                    Pattern trafficPattern = Pattern.compile("(?<=Traffic Management:)(.*)(?=Diversion Information:)");
                                    Matcher Trafficmatcher = trafficPattern.matcher(desDetails[2]);
                                    Matcher TrafficmatcherNoDiver = trafficPatternNoDiver.matcher(desDetails[2]);
                                    if (Trafficmatcher.find())
                                    {
                                        alist.get(alist.size() - 1).setTrafficManagement(Trafficmatcher.group(1));
                                    }

                                    if (TrafficmatcherNoDiver.find()){
                                        alist.get(alist.size() - 1).setTrafficManagement(TrafficmatcherNoDiver.group(1));
                                    }

                                    Pattern diversionPattern = Pattern.compile("(?<=Diversion Information:)(.*)");
                                    Matcher diversionmatcher = diversionPattern.matcher(desDetails[2]);
                                    if (diversionmatcher.find())
                                    {
                                        alist.get(alist.size() - 1).setDiversionInformation(diversionmatcher.group(1));
                                    }


                                }


                            }

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

                Log.d(TAG, "hasWork: " + (alist.get(i).getWork() != null));

                if (alist.get(i).getWork() == null){
                    alist.remove(i);
                }

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
                Log.d(TAG, "parseData: " + currentincidents.get(i).getDescription());
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


                            for (int i = 0; i < desDetails.length; i++) {

                                if (desDetails.length == 2){
                                    if (desDetails[0] != null && i == 0){

                                        String[] startDate = desDetails[i].split("Start Date: ");
                                        String [] sdate = startDate[1].split("[,-]+");

                                        LocalDate date1 = LocalDate.parse(sdate[1], formatter);

                                        roadworksincidents.get(roadworksincidents.size() - 1).setStartDate(date1.toString());

                                    }
                                    if (desDetails[1] != null && i == 1){

                                        String[] endDate = desDetails[i].split("End Date: ");

                                        String [] sdate = endDate[1].split("[,-]+");

                                        LocalDate date1 = LocalDate.parse(sdate[1], formatter);

                                        roadworksincidents.get(roadworksincidents.size() - 1).setEndDate(date1.toString());

                                    }
                                }
                                if (desDetails.length == 3){
                                    if (desDetails[0] != null && i == 0){

                                        String[] startDate = desDetails[i].split("Start Date: ");
                                        String [] sdate = startDate[1].split("[,-]+");

                                        LocalDate date1 = LocalDate.parse(sdate[1], formatter);

                                        roadworksincidents.get(roadworksincidents.size() - 1).setStartDate(date1.toString());

                                    }
                                    if (desDetails[1] != null && i == 1){


                                        String[] endDate = desDetails[i].split("End Date: ");

                                        String [] sdate = endDate[1].split("[,-]+");

                                        LocalDate date1 = LocalDate.parse(sdate[1], formatter);

                                        roadworksincidents.get(roadworksincidents.size() - 1).setEndDate(date1.toString());
                                    }
                                    if (desDetails[2] != null){


                                        String[] DelayInfo = desDetails[2].split("Delay Information: ");

                                        String DelayInfoStr = DelayInfo[1];


                                        roadworksincidents.get(roadworksincidents.size() - 1).setDelayInformation(DelayInfoStr);
                                    }
                                }

                            }

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

                if (roadworksincidents.get(i).getDelayInformation() == null){
                    roadworksincidents.get(i).setDelayInformation("No reported delay.");
                }
                roadworksincidents.get(i).setItemType("roadworks");
            }

            AllFeedInfo.addAll(roadworksincidents);

        }



    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.nav_home){

            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();


            HomeFragment homeFrag = new HomeFragment();

            feedData.setFeedInfoArray(AllFeedInfo);

            Bundle bundle = new Bundle();
            bundle.putSerializable("feedData", feedData.getFeedInfoArray());
            homeFrag.setArguments(bundle);

            transaction.replace(R.id.pageFragment, homeFrag);
            transaction.commit();


        }

        if (item.getItemId() == R.id.nav_search){
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();

            SearchFragment sf = new SearchFragment();

            feedData.setFeedInfoArray(AllFeedInfo);

            Bundle bundle = new Bundle();
            bundle.putSerializable("feedData", feedData.getFeedInfoArray());
            sf.setArguments(bundle);

            transaction.replace(R.id.pageFragment, sf);
            transaction.commit();
        }

        if (item.getItemId() == R.id.nav_journey){

            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();

            transaction.replace(R.id.pageFragment, new JourneyFragment());
            transaction.commit();

        }


        return true;
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
                result = "";

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
                    Log.d(TAG, "run: " + result);
                    parseData(result,"current");
                }
                else if (i == 2){
                    parseData(result,"roadworks");
                }


            }

            MainActivity.this.runOnUiThread(new Runnable()
            {
                public void run() {

                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();


                    HomeFragment homeFrag = new HomeFragment();

                    feedData.setFeedInfoArray(AllFeedInfo);

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("feedData", feedData.getFeedInfoArray());
                    homeFrag.setArguments(bundle);

                    transaction.replace(R.id.pageFragment, homeFrag);
                    transaction.commit();



                }
            });


        }

    }




}