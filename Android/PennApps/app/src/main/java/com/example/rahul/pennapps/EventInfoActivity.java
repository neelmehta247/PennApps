package com.example.rahul.pennapps;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.example.rahul.pennapps.Helpers.Event;
import com.example.rahul.pennapps.Helpers.HeaderView;
import com.squareup.picasso.Picasso;

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

        Event event = getIntent().getParcelableExtra("event");

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

        Picasso.with(this).load(event.imageUrl).into(header);

    }
}
