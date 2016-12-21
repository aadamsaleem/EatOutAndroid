package com.aadamsaleem.eatout.CustomViews.CustomEditText;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aadamsaleem.eatout.R;
import com.tokenautocomplete.TokenCompleteTextView;

/**
 * Created by aadam on 21/12/2016.
 */

public class CustomEditText extends TokenCompleteTextView<String> {
    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected View getViewForObject(String object) {
        LayoutInflater l = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        TextView view = (TextView) l.inflate(R.layout.cuisine_tv, (ViewGroup) getParent(), false);
        view.setText(object);

        return view;
    }

    @Override
    protected String defaultObject(String completionText) {
        return completionText;
    }
}
