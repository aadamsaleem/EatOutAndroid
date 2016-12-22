package com.aadamsaleem.eatout.LoggedIn.Home;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.aadamsaleem.eatout.R;
import com.aadamsaleem.eatout.client.CompletionInterface;
import com.aadamsaleem.eatout.client.EventManager;
import com.aadamsaleem.eatout.util.PrefUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecommendationVoting extends Activity {
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private Button submitRecommendation;
    private String eventIDString;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String eventID = getIntent().getStringExtra("EVENT_ID");
        if (eventID == null)
            throw new IllegalArgumentException("incorrect event id");
        eventIDString = eventID;
        mContext = getApplicationContext();

        setContentView(R.layout.activity_recommendation_voting);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        getListData();
        LinearLayoutManager manager = new LinearLayoutManager(RecommendationVoting.this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(manager);

        submitRecommendation = (Button)findViewById(R.id.submitRecommendation);
        setUpListeners();
    }
    private void setUpListeners() {

        submitRecommendation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventManager.updateVoteCount(getApplicationContext(), prepareUpgradeJson(), new CompletionInterface() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        Log.d("@@@@@", "update vote List" + result.toString());
                    }
                    @Override
                    public void onFailure() {
                        Log.d("***********", "getListData failure");
                    }
                });
            }
        });
    }


    private List<Row> selectRows (List<Row> allRows) {
        List<Row> result = new ArrayList<>();
        for (Row row : allRows) {
            if (row.isSelected())
                result.add(row);
        }
        return result;
    }

    private void getListData() {
        final List<Row> recoList = new ArrayList<>();
        Log.d("***********", "getListData called()");
        EventManager.getRecommendationList(getApplicationContext(), prepareListRequestJson(), new CompletionInterface() {
            @Override
            public void onSuccess(JSONObject result) {
                Log.d("***********", "recommendation List" + result.toString());
                try {
//                    {"STATUS":601,"RECOMMENDATIONS":[{"RESTAURANT_NAME":"Kathy's Dumplings","RESTAURANT_ID":"kathys-dumplings-brooklyn","VOTE_COUNT":0},{"RESTAURANT_NAME":"Longbow Pub & Pantry","RESTAURANT_ID":"longbow-pub-and-pantry-brooklyn","VOTE_COUNT":0},{"RESTAURANT_NAME":"Tanoreen","RESTAURANT_ID":"tanoreen-brooklyn-2","VOTE_COUNT":0},{"RESTAURANT_NAME":"Brooklyn Beet Company","RESTAURANT_ID":"brooklyn-beet-company-brooklyn","VOTE_COUNT":0},{"RESTAURANT_NAME":"Mussels & More","RESTAURANT_ID":"mussels-and-more-brooklyn","VOTE_COUNT":0},{"RESTAURANT_NAME":"MyThai Cafe","RESTAURANT_ID":"mythai-cafe-brooklyn","VOTE_COUNT":0},{"RESTAURANT_NAME":"Top Thai","RESTAURANT_ID":"top-thai-brooklyn","VOTE_COUNT":0},{"RESTAURANT_NAME":"New York Koshary","RESTAURANT_ID":"new-york-koshary-brooklyn-3","VOTE_COUNT":0},{"RESTAURANT_NAME":"Schnitzel Haus","RESTAURANT_ID":"schnitzel-haus-brooklyn","VOTE_COUNT":0},{"RESTAURANT_NAME":"Somethingreek","RESTAURANT_ID":"somethingreek-brooklyn","VOTE_COUNT":0}]}
                    JSONArray recommendationArray = result.getJSONArray("RECOMMENDATIONS");

                    for (int i = 0; i < recommendationArray.length(); i++) {
                        JSONObject recommendationData = recommendationArray.getJSONObject(i);
                       recoList.add(new Row(recommendationData.getString("RESTAURANT_NAME"), recommendationData.getString("VOTE_COUNT"), recommendationData.getString("RESTAURANT_ID")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                mAdapter = new RecyclerViewAdapter(recoList, mContext, RecommendationVoting.this);
                mRecyclerView.setAdapter(mAdapter);
            }
            @Override
            public void onFailure() {
                Log.d("***********", "getListData failure");
            }
        });

    }



    private JSONObject prepareUpgradeJson() {
        List<Row> allRows = mAdapter.getAllRows();
        List<Row> selectedRows = selectRows(allRows);
        JSONObject json = new JSONObject();
        try {
            json.put("USER_TOKEN", PrefUtils.getCurrentUser(getApplicationContext()).getToken());
            JSONArray array = new JSONArray();
            for (Row selectedRow : selectedRows) {
                array.put(selectedRow.getRestaurantID());
            }
            json.put("VOTES", array);
            json.put("EVENT_ID", eventIDString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public JSONObject prepareListRequestJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("USER_TOKEN", PrefUtils.getCurrentUser(getApplicationContext()).getToken());
            json.put("EVENT_ID", eventIDString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    return json;
    }
}