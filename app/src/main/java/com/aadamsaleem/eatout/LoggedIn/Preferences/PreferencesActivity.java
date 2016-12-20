package com.aadamsaleem.eatout.LoggedIn.Preferences;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aadamsaleem.eatout.R;
import com.aadamsaleem.eatout.client.CompletionInterface;
import com.aadamsaleem.eatout.client.UserManager;
import com.aadamsaleem.eatout.util.PrefUtils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Set;

public class PreferencesActivity extends AppCompatActivity {

    TagFlowLayout mFlowLayout;
    ArrayList<String> mVals;
    RadioGroup priceGroup;
    RatingBar ratingbar;
    SeekBar distanceSeekBar;
    TextView seekBarDistanceTV;
    Button updatePreferences;
    Float distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);


        getViews();

        setViews();

    }

    public void getViews() {
        mFlowLayout = (TagFlowLayout) findViewById(R.id.id_flowlayout);
        priceGroup = (RadioGroup) findViewById(R.id.radioGroup1);
        ratingbar = (RatingBar) findViewById(R.id.minimum_rating);
        distanceSeekBar = (SeekBar) findViewById(R.id.distance_seekbar);
        seekBarDistanceTV = (TextView) findViewById(R.id.seekbar_distance_textView);
        updatePreferences = (Button) findViewById(R.id.save_preferences_button);
    }

    public void setViews() {


        distanceSeekBar.setProgress(1);
        distanceSeekBar.setMax(100);
        distanceSeekBar.incrementProgressBy(1);
        distanceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                distance = (float) progress / 10;
                seekBarDistanceTV.setText(distance + " miles");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

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

        updatePreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject json = new JSONObject();

                try {
                    json.put("USER_TOKEN", PrefUtils.getCurrentUser(getApplicationContext()).getToken());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONArray cuisineArray = new JSONArray();
                Set<Integer> selectedCuisines = mFlowLayout.getSelectedList();
                for (Integer i : selectedCuisines) {
                    cuisineArray.put(mVals.get(i));

                }

                JSONObject jsonPreferences = new JSONObject();
                try {
                    jsonPreferences.put("LIST_OF_PRIMARY_CUISINES", cuisineArray);

                    switch (priceGroup.getCheckedRadioButtonId()) {
                        case R.id.oneDollar:
                            jsonPreferences.put("PRICE_RANGE", 1);
                            break;
                        case R.id.twoDollar:
                            jsonPreferences.put("PRICE_RANGE", 2);
                            break;
                        case R.id.threeDollar:
                            jsonPreferences.put("PRICE_RANGE", 3);
                            break;
                        case R.id.fourDollar:
                            jsonPreferences.put("PRICE_RANGE", 4);
                            break;
                    }

                    jsonPreferences.put("MINIMUM_RATING", (int) ratingbar.getRating());

                    jsonPreferences.put("DISTANCE_RADIUS", distance.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    json.put("PREFERENCES ", jsonPreferences);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                UserManager.updatePreferences(getApplicationContext(), json, new CompletionInterface() {
                    @Override
                    public void onSuccess(JSONObject result) {

                    }

                    @Override
                    public void onFailure() {

                    }
                });
            }
        });
    }
}
