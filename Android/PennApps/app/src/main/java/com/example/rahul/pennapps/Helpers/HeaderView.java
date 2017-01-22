package com.example.rahul.pennapps.Helpers;

/**
 * Created by neel on 22/01/17 at 1:17 AM.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.rahul.pennapps.R;

public class HeaderView extends LinearLayout {
    TextView title;

    public HeaderView(Context context) {
        super(context);
    }

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        title = (TextView) findViewById(R.id.header_view_title);
    }

    public void bindTo(String title) {
        hideOrSetText(this.title, title);
    }

    private void hideOrSetText(TextView tv, String text) {
        if (text == null || text.equals(""))
            tv.setVisibility(GONE);
        else
            tv.setText(text);
    }

    public void setScaleXTitle(float scaleXTitle) {
        title.setScaleX(scaleXTitle);
        title.setPivotX(0);
    }

    public void setScaleYTitle(float scaleYTitle) {
        title.setScaleY(scaleYTitle);
        title.setPivotY(30);
    }
}