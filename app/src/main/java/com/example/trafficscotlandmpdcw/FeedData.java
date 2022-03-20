package com.example.trafficscotlandmpdcw;

import java.util.ArrayList;

public class FeedData {

    private ArrayList<Item> FeedInfoArray = new ArrayList<Item>();

    public ArrayList<Item> getFeedInfoArray() {
        return FeedInfoArray;
    }

    public void setFeedInfoArray(ArrayList<Item> feedInfoArray) {
        FeedInfoArray = feedInfoArray;
    }
}
