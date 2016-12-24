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

import com.aadamsaleem.eatout.CustomViews.CustomListView.EventListViewAdapter;
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

        final ListView eventListView = (ListView) rootView.findViewById(R.id.listView);

        final ArrayList<Event> items = new ArrayList<>();


        EventManager.getPublicEvents(getContext(), new CompletionInterface() {
            @Override
            public void onSuccess(JSONObject result) {
                try {

                    Log.e("aaaaahjebdkjwna",result.toString());
                    JSONArray personalEvents = result.getJSONArray("PERSONAL_EVENTS");
                    for (int i = 0; i < personalEvents.length(); i++) {
                        JSONObject json = personalEvents.getJSONObject(i);
                        Event event = new Event();
                        event.setEventID(json.getString("EVENT_ID"));
                        event.setName(json.getString("EVENT_NAME"));
                        event.setDate(json.getString("EVENT_DATETIME"));
                        event.setLocation(json.getString("EVENT_LOCATION_TEXT"));
                        event.setParticipants(json.getString("EVENT_PARTICIPANT_NAMES"));
                        items.add(event);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                EventListViewAdapter listadapter = new EventListViewAdapter(getActivity(), android.R.layout.simple_list_item_1, items);
                eventListView.setAdapter(listadapter);
            }

            @Override
            public void onFailure() {

            }
        });


        return rootView;
    }
}
