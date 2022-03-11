package com.example.trafficscotlandmpdcw;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity implements OnClickListener
{

    private LinearLayout itemLayout;
    private TextView rawDataDisplay;
    private ArrayList<Item> alist;
    private ArrayList<Item> currentincidents;
    private ArrayList<Item> roadworksincidents;
    private ArrayList<Item> AllFeedInfo;
    private String result = "";
    private String url1="";
    // Traffic Scotland Planned Roadworks XML link
    private String urlSourceplanned = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    private String urlSourceCurrent = "https://trafficscotland.org/rss/feeds/currentincidents.aspx";
    private String urlSourceroadworks = "https://trafficscotland.org/rss/feeds/roadworks.aspx";

    private Fragment fr1;
    private Fragment frFeed;

    private Fragment fr;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("MyTag","in onCreate");
        // Set up the raw links to the graphical components
        itemLayout = (LinearLayout) findViewById(R.id.ItemLayout);
        rawDataDisplay = (TextView)findViewById(R.id.rawDataDisplay);
        Log.e("MyTag","after startButton");


        fr1 = new FragmentOne();
        frFeed = new FragmentFeedData();

        fr = fr1;

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment,fr);
        transaction.commit();

        startProgress();

    }

    public void startProgress()
    {
        // Run network access on a separate thread;
        new Thread(new Task(urlSourceplanned)).start();

    } //

    @Override
    public void onClick(View v)
    {

    }




    public void parseData(String dataToParse, String feedType) {


        if (feedType == "planned") {
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

                            String[] desDetails = temp.split("</br>");

                            Log.d("desDetails", "parseData: " + desDetails.toString());


                            alist.get(alist.size() - 1).setDescription(temp);

                        } else if (xpp.getName().equalsIgnoreCase("link")) {
                            // Now just get the associated text
                            String temp = xpp.nextText();
                            // Do something with text
                            if (alist.size() == 0) {
                                return;
                            }
                            alist.get(alist.size() - 1).setLink(temp);
                        } else if (xpp.getName().equalsIgnoreCase("georss:point")) {
                            // Now just get the associated text
                            String temp = xpp.nextText();
                            // Do something with text
                            if (alist.size() == 0) {
                                return;
                            }
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


        }



    }

    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
    private class Task implements Runnable
    {
        private String url;

        public Task(String aurl)
        {
            url = aurl;
        }
        @Override
        public void run()
        {

            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";


            Log.e("MyTag","in run");

            try
            {
                Log.e("MyTag","in try");
                aurl = new URL(url);
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


            if (url == "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx"){
                parseData(result,"planned");
            }else if (url == "https://trafficscotland.org/rss/feeds/currentincidents.aspx"){
                parseData(result,"current");
            }
            else if (url == "https://trafficscotland.org/rss/feeds/roadworks.aspx"){
                parseData(result,"roadworks");
            }






            MainActivity.this.runOnUiThread(new Runnable()
            {
                public void run() {
                    Log.d("UI thread", "I am the UI thread");

                    rawDataDisplay.setText(alist.size() + " Items have been found!");

                    FragmentManager manager = getFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("feedData", alist);
                    frFeed.setArguments(bundle);
                    transaction.replace(R.id.fragment,frFeed);
                    transaction.commit();



                }
            });
        }

    }






}