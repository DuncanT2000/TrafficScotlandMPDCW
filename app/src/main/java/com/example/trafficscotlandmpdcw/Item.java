package com.example.trafficscotlandmpdcw;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Item implements Parcelable {

    private String title;
    private String description;
    private String startDate;
    private String endDate;
    private String type;
    private String reason;
    private String link;
    private String georss;
    private String latStr;
    private String longStr;
    private String pubDate;
    private String itemType;


    protected Item(Parcel in) {
        title = in.readString();
        description = in.readString();
        startDate = in.readString();
        endDate = in.readString();
        type = in.readString();
        reason = in.readString();
        link = in.readString();
        georss = in.readString();
        latStr = in.readString();
        longStr = in.readString();
        pubDate = in.readString();
        itemType= in.readString();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public Item() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getGeorss() {
        return georss;
    }

    public void setGeorss(String georss) {
        this.georss = georss;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getLatStr() {
        return latStr;
    }

    public void setLatStr(String latStr) {
        this.latStr = latStr;
    }

    public String getLongStr() {
        return longStr;
    }

    public void setLongStr(String longStr) {
        this.longStr = longStr;
    }

    public String toString() {
        return "Title: " +this.getTitle() +
                " Description: " +
                this.getDescription() +
                " startDate: " +
                this.getStartDate() +
                " endDate: " +
                this.getEndDate() +
                " type: " +
                this.getType() +
                " reason: " +
                this.getReason() +
                "link: " + this.getLink() +
                "Georss: " + this.getGeorss()+
                "pubDate: " + this.getPubDate()+
                "Item Type: " + this.getItemType();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(startDate);
        parcel.writeString(endDate);
        parcel.writeString(type);
        parcel.writeString(reason);
        parcel.writeString(link);
        parcel.writeString(georss);
        parcel.writeString(latStr);
        parcel.writeString(longStr);
        parcel.writeString(pubDate);
        parcel.writeString(itemType);
    }
}
