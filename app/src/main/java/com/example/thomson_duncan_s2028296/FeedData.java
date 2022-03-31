package com.example.thomson_duncan_s2028296;

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
