package com.aadamsaleem.eatout.CustomViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.aadamsaleem.eatout.R;
import com.aadamsaleem.eatout.models.Event;

import java.util.ArrayList;

/**
 * Created by aadam on 17/11/2016.
 */

public class CustomListViewAdapter extends ArrayAdapter<Event> {

    private ArrayList<Event> eventList;

    public CustomListViewAdapter(Context context, int resource) {
        super(context, resource);
    }

    public CustomListViewAdapter(Context context, int resource, ArrayList<Event> eventList) {
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

        Event p = eventList.get(position);

        if (p != null) {

            TextView name = (TextView) v.findViewById(R.id.name);
            TextView venue = (TextView) v.findViewById(R.id.venue);
            TextView date = (TextView) v.findViewById(R.id.date);
            TextView time = (TextView) v.findViewById(R.id.time);

            name.setText(p.getName());
            venue.setText(p.getVenue());
            date.setText(p.getDate());
            time.setText(p.getTime());
        }
        return v;
    }
}
