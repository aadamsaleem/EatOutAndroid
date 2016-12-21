package com.aadamsaleem.eatout.CustomViews.CustomEditText;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aadamsaleem.eatout.R;
import com.aadamsaleem.eatout.models.Friend;
import com.tokenautocomplete.TokenCompleteTextView;

/**
 * Created by aadam on 21/12/2016.
 */

public class CustomFriendEditText extends TokenCompleteTextView<Friend> {
    public CustomFriendEditText(Context context) {
        super(context);
    }

    public CustomFriendEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomFriendEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected View getViewForObject(Friend object) {

        LayoutInflater l = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        TextView view = (TextView) l.inflate(R.layout.cuisine_tv, (ViewGroup) getParent(), false);
        view.setText(object.getUSER_NAME());

        return view;
    }

    @Override
    protected Friend defaultObject(String completionText) {
       Friend friend = new Friend();
        friend.setUSER_NAME(completionText);
        return friend;
    }
}
