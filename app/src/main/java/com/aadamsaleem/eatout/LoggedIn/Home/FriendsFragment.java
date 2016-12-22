package com.aadamsaleem.eatout.LoggedIn.Home;

/**
 * Created by aadamsaleem on 11/16/16.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.aadamsaleem.eatout.CustomViews.CustomListView.CustomListViewAdapter;
import com.aadamsaleem.eatout.R;
import com.aadamsaleem.eatout.client.CompletionInterface;
import com.aadamsaleem.eatout.client.EventManager;
import com.aadamsaleem.eatout.models.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

        final ListView yourListView = (ListView) rootView.findViewById(R.id.listView);

        final ArrayList<Event> items = new ArrayList<>();


        EventManager.getPublicEvents(getContext(), new CompletionInterface() {
            @Override
            public void onSuccess(JSONObject result) {
                try {

                    Log.e("aaaa","aaaa");
                    JSONArray personalEvents = result.getJSONArray("PERSONAL_EVENTS");
                    for (int i = 0; i < personalEvents.length(); i++) {
                        JSONObject json = personalEvents.getJSONObject(i);
                        Event event = new Event();
                        event.setEventID(json.getString("EVENT_ID"));
                        event.setName("NAME");
                        //event.setName(json.getString("EVENT_NAME"));
                        event.setDate("DATE");
                        //event.setDate(json.getString("EVENT_DATE"));
                        event.setParticipants(json.getString("EVENT_PARTICIPANTS"));
                        items.add(event);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                CustomListViewAdapter listadapter = new CustomListViewAdapter(getActivity(), android.R.layout.simple_list_item_1, items);
                yourListView.setAdapter(listadapter);
            }

            @Override
            public void onFailure() {
                Log.e("aaaa","bbbb");

            }
        });


        return rootView;
    }
}
