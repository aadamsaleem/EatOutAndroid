package com.aadamsaleem.eatout.LoggedIn;

/**
 * Created by aadamsaleem on 11/16/16.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aadamsaleem.eatout.R;

public class FriendsFragment extends Fragment {

    Context mContext;
    public FriendsFragment() {
    }

    public static FriendsFragment newInstance(Context context) {
        FriendsFragment fragment = new FriendsFragment();
        fragment.mContext = context;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText("Friends Section");
        return rootView;
    }
}
