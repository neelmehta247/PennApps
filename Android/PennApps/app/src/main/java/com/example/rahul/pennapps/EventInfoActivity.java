package com.example.rahul.pennapps;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rahul.pennapps.Helpers.Event;
import com.example.rahul.pennapps.Helpers.HeaderView;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by neel on 22/01/17 at 1:08 AM.
 */

public class EventInfoActivity extends AppCompatActivity {
    private ImageView header;
    private HeaderView toolbarHeaderView, floatHeaderView;
    private boolean isHideToolbarView;
    private FloatingActionButton postFAB;
    private AppBarLayout appBarLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewevents_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        setSupportActionBar(toolbar);

        ((CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar)).setTitle(" ");
        header = (ImageView) findViewById(R.id.header);

        toolbarHeaderView = (HeaderView) findViewById(R.id.toolbar_header_view);
        floatHeaderView = (HeaderView) findViewById(R.id.float_header_view);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        postFAB = (FloatingActionButton) findViewById(R.id.postFAB);

        final Event event = getIntent().getParcelableExtra("event");

        floatHeaderView.bindTo(event.title);
        toolbarHeaderView.bindTo(event.title);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int maxScroll = appBarLayout.getTotalScrollRange();
                float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;

                if (percentage == 1f && isHideToolbarView) {
                    toolbarHeaderView.setVisibility(View.VISIBLE);
                    isHideToolbarView = !isHideToolbarView;

                } else if (percentage < 1f && !isHideToolbarView) {
                    toolbarHeaderView.setVisibility(View.GONE);
                    isHideToolbarView = !isHideToolbarView;
                }
            }
        });

        postFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://pennapps-nrbs.herokuapp.com/events/" + event.eventId + "interest/add/";

                StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String resp) {
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Login Error", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("session-token", getSessionToken());

                        return params;
                    }
                };

                RequestQueue queue = Volley.newRequestQueue(EventInfoActivity.this);
                queue.add(req);
            }
        });

        Picasso.with(this).load(event.imageUrl).into(header);

        ((TextView) findViewById(R.id.description_text)).setText(event.description);
        ((TextView) findViewById(R.id.location_text)).setText(event.locationName);
        ((TextView) findViewById(R.id.time_text)).setText(new Date(event.time).toString());
    }

    private String getSessionToken() {
        SharedPreferences sharedPref = getSharedPreferences("token_prefs", Context.MODE_PRIVATE);
        String sessionToken = sharedPref.getString("session_token", null);

        return sessionToken;
    }
}
