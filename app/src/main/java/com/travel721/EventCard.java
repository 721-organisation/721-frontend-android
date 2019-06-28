package com.travel721;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EventCard implements Parcelable, Serializable, Comparable<EventCard> {
    public static final Creator<EventCard> CREATOR = new Creator<EventCard>() {
        @Override
        public EventCard createFromParcel(Parcel in) {
            return new EventCard(in);
        }

        @Override
        public EventCard[] newArray(int size) {
            return new EventCard[size];
        }
    };
    private String name;
    private String eventSourceID;
    private String venueName;

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    private String latitude;
    private String longitude;
    private String imgURL;
    private String eventHyperLink;
    private String formattedDate;
    private String time;
    private String minimumAge;
    private String price;
    private Bitmap imgBitmap = null;
    private String description;
    private String sourceTag;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof EventCard) {
            EventCard eventCard = (EventCard) obj;
            return
                    this.name.equals(eventCard.name) &&
                            this.eventSourceID.equals(eventCard.eventSourceID) &&
                            this.venueName.equals(eventCard.venueName) &&
                            this.latitude.equals(eventCard.latitude) &&
                            this.longitude.equals(eventCard.longitude) &&
                            this.imgURL.equals(eventCard.imgURL) &&
                            this.eventHyperLink.equals(eventCard.eventHyperLink) &&
                            this.formattedDate.equals(eventCard.formattedDate) &&
                            this.minimumAge.equals(eventCard.minimumAge) &&
                            this.price.equals(eventCard.price) &&
                            this.description.equals(eventCard.description) &&
                            this.sourceTag.equals(eventCard.sourceTag);
        } else {
            return false;
        }
    }

    protected EventCard(Parcel in) {
        name = in.readString();
        eventSourceID = in.readString();
        venueName = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        imgURL = in.readString();
        eventHyperLink = in.readString();
        formattedDate = in.readString();
        time = in.readString();
        minimumAge = in.readString();
        price = in.readString();
        description = in.readString();
        sourceTag = in.readString();
    }

    public EventCard() {
    }

    public static EventCard unpackFromJson(JSONObject jo) throws JSONException {
        Log.v("json", jo.toString());
        // Guaranteed Field
        EventCard eventCard = new EventCard();
        eventCard.setSourceTag(checkHasAndReturnData(jo, "eventSourceTag", ""));
        eventCard.setName(checkHasAndReturnData(jo, "name", eventCard.getSourceTag()));
        eventCard.setEventSourceID(checkHasAndReturnData(jo, "eventSourceId", eventCard.getSourceTag().toLowerCase()));
        eventCard.setVenueName(checkHasAndReturnData(jo, "venueName", eventCard.getSourceTag().toLowerCase()));
        eventCard.setLatitude(checkHasAndReturnData(jo, "venueLat", eventCard.getSourceTag().toLowerCase()));
        eventCard.setLongitude(checkHasAndReturnData(jo, "venueLong", eventCard.getSourceTag().toLowerCase()));
        eventCard.setImgURL(checkHasAndReturnData(jo, "image", eventCard.getSourceTag().toLowerCase()));
        eventCard.setEventHyperLink(checkHasAndReturnData(jo, "link", eventCard.getSourceTag().toLowerCase()));
        eventCard.setFormattedDate(checkHasAndReturnData(jo, "date", eventCard.getSourceTag().toLowerCase()));
        eventCard.setTime(checkHasAndReturnData(jo, "time", eventCard.getSourceTag().toLowerCase()));
        eventCard.setMinimumAge(checkHasAndReturnData(jo, "minAge", eventCard.getSourceTag().toLowerCase()));
        eventCard.setPrice(checkHasAndReturnData(jo, "price", eventCard.getSourceTag().toLowerCase()));
        eventCard.setDescription(checkHasAndReturnData(jo, "description", eventCard.getSourceTag().toLowerCase()));
        return eventCard;
    }

    private static String checkHasAndReturnData(JSONObject jo, String prop, String eventSource) throws JSONException {
        if (jo.has(prop)) {
            String strProp = jo.getString(prop);
            if (strProp.equals("")) {
                return "No " + prop + " provided by " + eventSource;
            }
            return jo.getString(prop);
        } else {
            return "No " + prop + " provided by " + eventSource;
        }

    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(eventSourceID);
        parcel.writeString(venueName);
        parcel.writeString(latitude);
        parcel.writeString(longitude);
        parcel.writeString(imgURL);
        parcel.writeString(eventHyperLink);
        parcel.writeString(formattedDate);
        parcel.writeString(time);
        parcel.writeString(minimumAge);
        parcel.writeString(price);
        parcel.writeString(description);
        parcel.writeString(sourceTag);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEventSourceID() {
        return eventSourceID;
    }

    public void setEventSourceID(String eventSourceID) {
        this.eventSourceID = eventSourceID;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getEventHyperLink() {
        return eventHyperLink;
    }

    public void setEventHyperLink(String eventHyperLink) {
        this.eventHyperLink = eventHyperLink;
    }

    public String getDayOfWeek() {
        try {
            Date realDateOfEvent = new SimpleDateFormat("yyyy-MM-dd").parse(formattedDate);
            Date realCurrentDate = new Date();
            String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(realCurrentDate);

            if (formattedDate.equals(currentDate)) {
                return "Today";
            } else {
                return new SimpleDateFormat("EEEE").format(realDateOfEvent);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getPrettyDate() {
        try {
            if (getDayOfWeek().equals("Today")) return "Today";
            Date realDateOfEvent = new SimpleDateFormat("yyyy-MM-dd").parse(formattedDate);
            return new SimpleDateFormat("EEE, MMM d").format(realDateOfEvent);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getFormattedDate() {
        return formattedDate;
    }

    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;

    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMinimumAge() {
        return minimumAge;
    }

    public void setMinimumAge(String minimumAge) {
        this.minimumAge = minimumAge;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Bitmap getImgBitmap() {
        return imgBitmap;
    }

    public void setImgBitmap(Bitmap imgBitmap) {
        this.imgBitmap = imgBitmap;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String s) {
        this.imgURL = s;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSourceTag() {
        return sourceTag;
    }

    public void setSourceTag(String sourceTag) {
        this.sourceTag = sourceTag;
    }

    public String getLocationLatitude() {
        return latitude;
    }

    public String getLocationLongitude() {
        return longitude;
    }

    /**
     * Events are to be compared by dates
     *
     * @param eventCard
     * @return
     */
    @Override
    public int compareTo(EventCard eventCard) {
        try {
            Date dateOfThis = new SimpleDateFormat("yyyy-MM-dd").parse(formattedDate);
            Date dateOfTo = new SimpleDateFormat("yyyy-MM-dd").parse(eventCard.formattedDate);

            return dateOfThis.compareTo(dateOfTo);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }
}