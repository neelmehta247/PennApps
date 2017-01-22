package com.example.rahul.pennapps.Helpers;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by rahul on 21/01/2017.
 */

public class Event implements Parcelable {
    public String description;
    public String locationName;
    public long time;
    public LatLng loc;
    public String title;
    public String imageUrl;
    public String eventId;
    public int num_interested;
    public String email;

    private Event() {
    }

    protected Event(Parcel in) {
        description = in.readString();
        locationName = in.readString();
        time = in.readLong();
        loc = in.readParcelable(LatLng.class.getClassLoader());
        title = in.readString();
        imageUrl = in.readString();
        eventId = in.readString();
        num_interested = in.readInt();
        email = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeString(locationName);
        dest.writeLong(time);
        dest.writeParcelable(loc, flags);
        dest.writeString(title);
        dest.writeString(imageUrl);
        dest.writeString(eventId);
        dest.writeInt(num_interested);
        dest.writeString(email);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public static Event fromJSON(JSONObject json) {
        Event event = new Event();
        try {
            event.description = json.getString("description");
            event.locationName = json.getString("location_name");
            event.time = getTimeEpoch(json.getString("time"));
            event.loc = new LatLng(json.getDouble("location_latitude"),
                    json.getDouble("location_longitude"));
            event.title = json.getString("title");
            event.imageUrl = json.getString("image");
            event.eventId = json.getString("id");
            event.num_interested = json.getInt("num_interested");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return event;
    }

    private static long getTimeEpoch(String time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date postTime = new Date(0);
        try {
            postTime = simpleDateFormat.parse(time);
            postTime.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return postTime.getTime();
    }
}
