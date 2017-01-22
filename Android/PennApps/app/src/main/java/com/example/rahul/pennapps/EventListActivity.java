package com.example.rahul.pennapps;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rahul.pennapps.Helpers.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by neel on 22/01/17 at 4:11 AM.
 */

public class EventListActivity extends AppCompatActivity {
    ArrayList<Event> events = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.element_list_view);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        String url;
        if (getIntent().getIntExtra("type", 1) == 1)
            url = "http://pennapps-nrbs.herokuapp.com/events/interested/";
        else
            url = "http://pennapps-nrbs.herokuapp.com/events/organizing/";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println(response);
                    JSONObject resp = new JSONObject(response);
                    JSONArray list = resp.getJSONArray("data");

                    for (int i = 0; i < list.length(); i++) {
                        events.add(Event.fromJSON(list.getJSONObject(i)));
                    }

                    recyclerView.setAdapter(new EventListAdapter(EventListActivity.this, events));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("session_token", getSessionToken());
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private String getSessionToken() {
        SharedPreferences sharedPref = getSharedPreferences("token_prefs", Context.MODE_PRIVATE);
        String sessionToken = sharedPref.getString("session_token", null);

        System.out.println(sessionToken);

        return sessionToken;
    }
}
