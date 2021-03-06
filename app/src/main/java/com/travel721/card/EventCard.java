package com.travel721.card;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

@SuppressWarnings("WeakerAccess")
@Entity
public class EventCard extends Card implements Parcelable, Serializable, Comparable<EventCard> {
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
    public String name;
    @SuppressWarnings("NullableProblems")
    @NonNull
    @PrimaryKey
    public String eventSourceID;
    public ArrayList<String> tags;
    public String venueName;
    public String latitude;
    public String longitude;
    public String imgURL;
    public String eventHyperLink;
    public String formattedDate;
    public String time;
    public String minimumAge;
    public String price;
    public String description;
    public String sourceTag;
    @Ignore
    public static final String dateFormatString = "yyyy-MM-dd";

    protected EventCard(Parcel in) {
        name = in.readString();
        eventSourceID = Objects.requireNonNull(in.readString());
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
        tags = new ArrayList<>();
        in.readList(tags, String.class.getClassLoader());

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
        eventCard.setTags(checkHasAndReturnData(jo, "tag", eventCard.getSourceTag().toLowerCase()));
        return eventCard;
    }

    private void setTags(String tag) {
        try {
            JSONArray jsonArray = new JSONArray(tag);
            ArrayList<String> listdata = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                listdata.add(jsonArray.getString(i));
            }
            this.tags = listdata;
        } catch (JSONException e) {
            this.tags = new ArrayList<>();
        }

    }

    private static String checkHasAndReturnData(JSONObject jo, String prop, String eventSource) throws JSONException {
        if (jo.has(prop)) {
            String strProp = jo.getString(prop);
            if (strProp.equals("") || strProp.toLowerCase().equals("null")) {
                return "No " + prop + " provided by " + eventSource;
            }
            return jo.getString(prop);
        } else {
            return "No " + prop + " provided by " + eventSource;
        }

    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof EventCard) {
            EventCard eventCard = (EventCard) obj;
            return this.eventSourceID.equals(eventCard.eventSourceID);
        } else {
            return false;
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
        parcel.writeList(tags);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull
    public String getEventSourceID() {
        return eventSourceID;
    }

    public void setEventSourceID(@NotNull String eventSourceID) {
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
            Date realDateOfEvent = new SimpleDateFormat(dateFormatString).parse(formattedDate);
            Date realCurrentDate = new Date();
            String currentDate = new SimpleDateFormat(dateFormatString).format(realCurrentDate);

            if (formattedDate.equals(currentDate)) {
                return "Today";
            } else {
                return new SimpleDateFormat("EEEE").format(Objects.requireNonNull(realDateOfEvent));
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getPrettyDate() {
        try {
            if (getDayOfWeek().equals("Today")) return "Today";
            Date realDateOfEvent = new SimpleDateFormat(dateFormatString).parse(formattedDate);
            return new SimpleDateFormat("EEEE dd MMM").format(Objects.requireNonNull(realDateOfEvent));
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getFormattedDate() {
        try {
            DateFormat originalFormat = new SimpleDateFormat(dateFormatString, Locale.ENGLISH);
            DateFormat targetFormat = new SimpleDateFormat("EEEE dd MMMM");
            Date date = originalFormat.parse(formattedDate);

            return targetFormat.format(Objects.requireNonNull(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return formattedDate;
        }
    }

    public String getOriginalFormatDate() {
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
            Date dateOfThis = new SimpleDateFormat(dateFormatString).parse(formattedDate);
            Date dateOfTo = new SimpleDateFormat(dateFormatString).parse(eventCard.formattedDate);

            return Objects.requireNonNull(dateOfThis).compareTo(dateOfTo);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }
}