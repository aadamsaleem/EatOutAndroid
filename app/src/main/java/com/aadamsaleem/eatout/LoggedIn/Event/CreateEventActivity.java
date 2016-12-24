package com.aadamsaleem.eatout.LoggedIn.Event;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.aadamsaleem.eatout.CustomViews.CustomEditText.CustomEditText;
import com.aadamsaleem.eatout.CustomViews.CustomEditText.CustomFriendEditText;
import com.aadamsaleem.eatout.LoggedIn.Voting.RecommendationVotingActivity;
import com.aadamsaleem.eatout.R;
import com.aadamsaleem.eatout.client.CompletionInterface;
import com.aadamsaleem.eatout.client.EventManager;
import com.aadamsaleem.eatout.client.UserManager;
import com.aadamsaleem.eatout.models.Friend;
import com.aadamsaleem.eatout.util.PrefUtils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

public class CreateEventActivity extends AppCompatActivity {

    CustomEditText locationCustomEditText;
    CustomFriendEditText participantCustomEditText;
    Switch type;
    TextView switchTV, timeTV;
    LinearLayout timeLayout;
    TagFlowLayout mFlowLayout;
    ArrayList<String> mVals;
    Button nextButton;
    EditText name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        getViewIDs();
        setViews();
    }

    public void getViewIDs() {
        participantCustomEditText = (CustomFriendEditText) findViewById(R.id.participants);
        locationCustomEditText = (CustomEditText) findViewById(R.id.location);
        type = (Switch) findViewById(R.id.switch_show);
        switchTV = (TextView) findViewById(R.id.switch_show_tv);
        timeLayout = (LinearLayout) findViewById(R.id.timeLayout);
        timeTV = (TextView) findViewById(R.id.time_tv);
        mFlowLayout = (TagFlowLayout) findViewById(R.id.id_flowlayout);
        name = (EditText) findViewById(R.id.name);
        nextButton = (Button) findViewById(R.id.next_button);

    }

    public void setViews() {

        getCuisines();
        getFriends();

        locationCustomEditText.setTokenLimit(1);
        getNeighbourhood();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventManager.createEvent(getApplicationContext(), prepareEventJson(), new CompletionInterface() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        Intent i = new Intent(CreateEventActivity.this, RecommendationVotingActivity.class);
                        try {
                            i.putExtra("EVENT_ID", result.getJSONObject("EVENT").getString("EVENT_ID"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        startActivity(i);
                        finish();

                    }

                    @Override
                    public void onFailure() {

                    }
                });
            }
        });

        type.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    switchTV.setText("PRIVATE");

                } else {
                    switchTV.setText("CHECKIN");
                }
            }
        });

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = 1 + c.get(Calendar.MONTH);
        int day = c.get(Calendar.DATE);
        int hourOfDay = c.get(Calendar.HOUR);
        int minute = c.get(Calendar.MINUTE);
        String minuteString = null;
        if(minute<10)
            minuteString = "0"+minute;
        else
        minuteString = ""+minute;
        final String date = year + "-" + month + "-" + day;
        if (hourOfDay < 13)
            timeTV.setText(date + " " + hourOfDay + ":" + minuteString + " AM");
        else
            timeTV.setText(hourOfDay - 12 + ":" + minute + " PM");

        timeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(CreateEventActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                String minuteString = null;
                                if(minute<10)
                                    minuteString = "0"+minute;
                                else
                                    minuteString = ""+minute;

                                if (hourOfDay < 13)
                                    timeTV.setText(date + " " + hourOfDay + ":" + minuteString + " AM");
                                else
                                    timeTV.setText(date + " " + (hourOfDay - 12) + ":" + minuteString + " PM");
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();

            }
        });

    }

    public void getFriends() {

        final ArrayList<Friend> friends = new ArrayList<Friend>();
        UserManager.getUserFriends(getApplicationContext(), new CompletionInterface() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    JSONArray friendArray = result.getJSONArray("FRIENDS");

                    for (int i = 0; i < friendArray.length(); i++) {
                        JSONObject friendObject = friendArray.getJSONObject(i);
                        Friend friend = new Friend();
                        friend.setUSER_NAME(friendObject.getString("USER_NAME"));
                        friend.setEMAIL_ID(friendObject.getString("EMAIL_ID"));
                        friend.setUSER_ID(friendObject.getString("USER_ID"));
                        friends.add(friend);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ArrayAdapter adapter = new ArrayAdapter<Friend>(getApplicationContext(), R.layout.simple_list_item, friends);
                participantCustomEditText.setAdapter(adapter);

            }

            @Override
            public void onFailure() {

            }
        });


    }

    public void getNeighbourhood() {

        final ArrayList<String> neighbourhoods = new ArrayList<String>();
        EventManager.getNeighbourhood(getApplicationContext(), new CompletionInterface() {
            @Override
            public void onSuccess(JSONObject result) {

                try {
                    JSONArray neighbourhoodArray = result.getJSONArray("NEIGHBORHOODS");

                    for (int i = 0; i < neighbourhoodArray.length(); i++) {
                        JSONObject neighbourhoodObject = neighbourhoodArray.getJSONObject(i);
                        String name = neighbourhoodObject.getString("name");
                        neighbourhoods.add(name);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_item, neighbourhoods);
                locationCustomEditText.setAdapter(adapter);

            }

            @Override
            public void onFailure() {

            }
        });

    }

    public void getCuisines() {
        UserManager.getCuisines(getApplicationContext(), new CompletionInterface() {
            @Override
            public void onSuccess(JSONObject result) {
                try {

                    JSONArray cuisineArrayJSON = result.getJSONArray("LIST_OF_CUISINES");
                    mVals = new ArrayList<>();
                    for (int i = 0; i < cuisineArrayJSON.length(); i++) {

                        mVals.add(cuisineArrayJSON.getJSONObject(i).getString("name"));
                        mFlowLayout.setAdapter(new TagAdapter<String>(mVals) {
                            @Override
                            public View getView(FlowLayout parent, int position, String s) {

                                TextView tv = (TextView) getLayoutInflater().inflate(R.layout.cuisine_tv,
                                        mFlowLayout, false);
                                tv.setText(s);
                                return tv;
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure() {

                Toast.makeText(getApplicationContext(), "Something Went Wrong!", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }


    public JSONObject prepareEventJson() {
        JSONObject json = new JSONObject();
        {
            try {
                json.put("USER_TOKEN", PrefUtils.getCurrentUser(getApplicationContext()).getToken());

                JSONObject eventDetails = new JSONObject();
                eventDetails.put("NAME", name.getText());
                eventDetails.put("TYPE", switchTV.getText());
                eventDetails.put("DATETIME", timeTV.getText());

                JSONArray cuisineArray = new JSONArray();
                Set<Integer> selectedCuisines = mFlowLayout.getSelectedList();
                for (Integer i : selectedCuisines) {
                    cuisineArray.put(mVals.get(i));

                }

                eventDetails.put("PREFERENCES", cuisineArray);

                JSONArray friends = new JSONArray();
                List<Friend> friendList = participantCustomEditText.getObjects();
                for (Friend f : friendList) {
                    friends.put(f.getUSER_ID());

                }

                eventDetails.put("PARTICIPANTS", friends);

                eventDetails.put("LOCATION", locationCustomEditText.getObjects().get(0));

                json.put("EVENT_DETAILS", eventDetails);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return json;
    }
}
