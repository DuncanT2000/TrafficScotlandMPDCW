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
    private ArrayList<Item> alist;
    private ArrayList<Item> currentincidents;
    private ArrayList<Item> roadworksincidents;
    private ArrayList<Item> AllFeedInfo = new ArrayList<Item>();
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
        new Thread(new Task(urlSourceplanned, "planned")).start();
        new Thread(new Task(urlSourceroadworks,"roadworks")).start();
    } //


    @Override
    public void onClick(View v)
    {

    }

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

                            String[] desDetails = temp.split("</br>");

                            alist.get(alist.size() - 1).setDescription(temp);

                        } else if (xpp.getName().equalsIgnoreCase("link")) {
                            // Now just get the associated text
                            String temp = xpp.nextText();
                            // Do something with text
                            if (alist.size() == 0) {
                                return;
                            }
                            alist.get(alist.size() - 1).setLink(temp);
                        } else if (xpp.getName().equalsIgnoreCase("point")) {
                            // Now just get the associated text
                            String temp = xpp.nextText();
                            // Do something with text

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

                            String[] desDetails = temp.split("</br>");

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

                            String[] desDetails = temp.split("</br>");

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



    private class Task implements Runnable
    {
        private String url;
        private String feedType;

        public Task(String aurl,String FeedType)
        {
            url = aurl;
            feedType = FeedType;
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

            Log.d("TAG", "Got Data: " + result);

            if(feedType == "planned"){
                parseData(result,"planned");
            }else if (feedType == "current"){
                parseData(result,"current");
            }
            else if (feedType == "roadworks"){
                parseData(result,"roadworks");
            }




            MainActivity.this.runOnUiThread(new Runnable()
            {
                public void run() {
                    Log.d("UI thread", "I am the UI thread");

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







}