package com.aadamsaleem.eatout.CustomViews.CustomListView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.aadamsaleem.eatout.LoggedIn.Event.EventDetailActivity;
import com.aadamsaleem.eatout.R;
import com.aadamsaleem.eatout.models.Event;

import java.util.ArrayList;

/**
 * Created by aadam on 17/11/2016.
 */

public class EventListViewAdapter extends ArrayAdapter<Event> {

    private ArrayList<Event> eventList;

    public EventListViewAdapter(Context context, int resource) {
        super(context, resource);
    }

    public EventListViewAdapter(Context context, int resource, ArrayList<Event> eventList) {
        super(context, resource, eventList);
        this.eventList = eventList;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {

            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.event_list_item, null);

        }

        final Event p = eventList.get(position);

        if (p != null) {

            TextView name = (TextView) v.findViewById(R.id.name);
            TextView participants = (TextView) v.findViewById(R.id.participants);
            TextView date = (TextView) v.findViewById(R.id.date);
            TextView location = (TextView) v.findViewById(R.id.location);

            name.setText(p.getName());
            participants.setText(p.getParticipants());
            date.setText(p.getDateTime());
            location.setText(p.getLocation());

        }

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getContext(), EventDetailActivity.class);
                i.putExtra("EVENT_ID", p.getEventID());
                getContext().startActivity(i);

            }
        });
        return v;
    }
}
