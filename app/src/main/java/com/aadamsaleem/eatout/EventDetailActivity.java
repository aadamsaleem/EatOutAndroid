package com.aadamsaleem.eatout;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aadamsaleem.eatout.client.CompletionInterface;
import com.aadamsaleem.eatout.client.EventManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class EventDetailActivity extends AppCompatActivity {


    private TextView nameTV, dateTimeTV, eventTypeTV, participantsTV, preferencesTV, locationTV;
    private Button vote;

    String eventID = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        eventID = getIntent().getStringExtra("EVENT_ID");

        getViewIDs();

        setViews();
    }


    public  void getViewIDs(){
        nameTV = (TextView) findViewById(R.id.name_tv);
        dateTimeTV = (TextView) findViewById(R.id.date_tv);
        eventTypeTV = (TextView) findViewById(R.id.event_tv);
        participantsTV = (TextView) findViewById(R.id.participants_tv);
        preferencesTV = (TextView) findViewById(R.id.preferneces_tv);
        locationTV = (TextView) findViewById(R.id.location_tv);

        vote = (Button) findViewById(R.id.vote_button);
    }

    public void setViews(){


        JSONObject json = new JSONObject();
        try {
            json.put("EVENT_ID", eventID);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        EventManager.getEventDetails(getApplicationContext(), json, new CompletionInterface() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    JSONObject eventDetails = result.getJSONObject("EVENT_DETAILS");

                    nameTV.setText(eventDetails.getString("EVENT_NAME"));
                    dateTimeTV.setText(eventDetails.getString("EVENT_DATETIME"));
                    eventTypeTV.setText(eventDetails.getString("EVENT_TYPE"));
                    participantsTV.setText(eventDetails.getString("EVENT_PARTICIPANTS"));
                    preferencesTV.setText(eventDetails.getString("EVENT_PREFERENCES"));
                    locationTV.setText(eventDetails.getString("EVENT_LOCATION_TEXT"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure() {

            }
        });

    }
}
