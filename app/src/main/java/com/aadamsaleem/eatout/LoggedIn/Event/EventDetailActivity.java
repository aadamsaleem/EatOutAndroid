package com.aadamsaleem.eatout.LoggedIn.Event;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aadamsaleem.eatout.LoggedIn.Voting.RecommendationVoting;
import com.aadamsaleem.eatout.R;
import com.aadamsaleem.eatout.client.CompletionInterface;
import com.aadamsaleem.eatout.client.EventManager;

import org.json.JSONException;
import org.json.JSONObject;

public class EventDetailActivity extends AppCompatActivity {


    private TextView nameTV, dateTimeTV, eventTypeTV, participantsTV, preferencesTV, locationTV, restaurantTV;
    private Button vote;
    private boolean isFinished;

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
        restaurantTV = (TextView) findViewById(R.id.resturant_tv);

        vote = (Button) findViewById(R.id.vote_button);
    }

    public void setViews(){


        vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EventDetailActivity.this, RecommendationVoting.class);
                i.putExtra("EVENT_ID", eventID);
                startActivity(i);
                finish();
            }
        });

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

                    Log.e("aaaaa",result.toString());
                    JSONObject eventDetails = result.getJSONObject("EVENT_DETAILS");

                    nameTV.setText(eventDetails.getString("EVENT_NAME"));
                    dateTimeTV.setText(eventDetails.getString("EVENT_DATETIME"));
                    eventTypeTV.setText(eventDetails.getString("EVENT_TYPE"));
                    participantsTV.setText(eventDetails.getString("EVENT_PARTICIPANT_NAMES"));
                    preferencesTV.setText(eventDetails.getString("EVENT_PREFERENCES"));
                    locationTV.setText(eventDetails.getString("EVENT_LOCATION_TEXT"));

                    String resturant = eventDetails.getString("EVENT_RESTAURENTSELECT");
                    if(!resturant.equals("None"))
                        restaurantTV.setText(resturant);

                    isFinished = eventDetails.getBoolean("EVENT_ISFINISHED");

                    if(isFinished){
                        vote.setText("REVIEW EVENT");
                        vote.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getApplicationContext(), "Coming Soon!", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

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
