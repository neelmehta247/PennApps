package com.example.rahul.pennapps.Helpers;

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

public class Event {
    public String description;
    public String locationName;
    public long time;
    public LatLng loc;
    public String title;
    public String imageUrl;
    public String eventId;
    public String email;

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
            event.email = json.getString("email");
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
