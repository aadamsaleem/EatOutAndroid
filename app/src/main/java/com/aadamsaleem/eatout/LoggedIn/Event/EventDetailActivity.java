package com.aadamsaleem.eatout.LoggedIn.Event;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.aadamsaleem.eatout.CustomViews.ReviewView.ReviewEventActivity;
import com.aadamsaleem.eatout.LoggedIn.Voting.RecommendationVotingActivity;
import com.aadamsaleem.eatout.R;
import com.aadamsaleem.eatout.client.CompletionInterface;
import com.aadamsaleem.eatout.client.EventManager;
import com.aadamsaleem.eatout.util.PrefUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class EventDetailActivity extends AppCompatActivity {


    private TextView nameTV, dateTimeTV, eventTypeTV, participantsTV, preferencesTV, locationTV, restaurantTV;
    private Button vote,endEvent;
    private boolean isFinished;
    private ScrollView topScrollView;
    ProgressDialog progressDialog;

    String eventID = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        eventID = getIntent().getStringExtra("EVENT_ID");

        progressDialog = new ProgressDialog(EventDetailActivity.this, R.style.MyTheme);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.show();

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
        endEvent = (Button) findViewById(R.id.event_close_button);

        topScrollView = (ScrollView) findViewById(R.id.scrollViewTop);
    }

    public void setViews(){


        vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EventDetailActivity.this, RecommendationVotingActivity.class);
                i.putExtra("EVENT_ID", eventID);
                startActivity(i);
                finish();
            }
        });

        endEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"You are awesome!",Toast.LENGTH_SHORT).show();
                Intent gotoReviewPage = new Intent(EventDetailActivity.this, ReviewEventActivity.class);
                gotoReviewPage.putExtra("navigateFrom","horizontal");
                startActivity(gotoReviewPage);


                //TODO: finish this
//                EventManager.signalEndOfEvent(getApplicationContext(), prepareEndEventJson(), new CompletionInterface() {
//                    @Override
//                    public void onSuccess(JSONObject result) {
//                       finish();
//                    }
//                    @Override
//                    public void onFailure() {
//                        Toast.makeText(getApplicationContext(), "something went wrong, please try again !", Toast.LENGTH_SHORT).show();
//                    }
//                });
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
                    boolean isOwner = false;
                    if(!eventDetails.getString("EVENT_OWNER_TOKEN").equals( PrefUtils.getCurrentUser(getApplicationContext()).getToken())) {
                        endEvent.setVisibility(View.GONE);
                        isOwner = true;
                    }

                    String resturant = eventDetails.getString("EVENT_RESTAURENTSELECT");
                    if(!resturant.equals("None"))
                        restaurantTV.setText(resturant);

                    isFinished = eventDetails.getBoolean("EVENT_ISFINISHED");

                    if(!isOwner && isFinished){
                        vote.setText("REVIEW EVENT");
                        vote.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getApplicationContext(), "Coming Soon!", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    topScrollView.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure() {
                progressDialog.dismiss();

            }
        });
    }
        private JSONObject prepareEndEventJson() {
            JSONObject json = new JSONObject();
            try {
                json.put("USER_TOKEN", PrefUtils.getCurrentUser(getApplicationContext()).getToken());
                json.put("EVENT_ID", eventID);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return json;

}
}
