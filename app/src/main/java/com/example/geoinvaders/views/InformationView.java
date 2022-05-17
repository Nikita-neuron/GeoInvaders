package com.example.geoinvaders.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.geoinvaders.R;

public class InformationView extends LinearLayout {
    private TextView information_TextView;

    public InformationView(Context context) {
        super(context);

        initViews();
    }

    private void initViews() {
        LinearLayout mLayout = (LinearLayout)
                LayoutInflater.from(getContext()).inflate(R.layout.information_view, this);

        information_TextView = mLayout.findViewById(R.id.information_textView);
    }

    public void setText(String text) {
        information_TextView.setText(text);
    }
}
