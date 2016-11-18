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
import android.widget.ListView;

import com.aadamsaleem.eatout.CustomViews.CustomListViewAdapter;
import com.aadamsaleem.eatout.R;
import com.aadamsaleem.eatout.models.Event;

import java.util.ArrayList;

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

        ListView yourListView = (ListView)rootView.findViewById(R.id.listView);


        ArrayList<Event> items = new ArrayList<>();

        Event ev1 = new Event();
        Event ev2 = new Event();



        ev1.setName("Dinner");
        ev1.setVenue("Taj Mahal");
        ev1.setDate("19/11/2016");
        ev1.setTime("8:00PM");

        ev2.setName("Breakfast");
        ev2.setVenue("Shake Shack");
        ev2.setDate("18/11/2016");
        ev2.setTime("8:00AM");

        items.add(ev1);
        items.add(ev2);

        CustomListViewAdapter listadapter = new CustomListViewAdapter(getActivity(), android.R.layout.simple_list_item_1, items);
        yourListView.setAdapter( listadapter);
        return rootView;
    }
}
