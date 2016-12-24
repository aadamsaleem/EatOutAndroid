package com.aadamsaleem.eatout.LoggedIn.Voting;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aadamsaleem.eatout.LoggedIn.Home.MainActivity;
import com.aadamsaleem.eatout.R;
import com.aadamsaleem.eatout.client.CompletionInterface;
import com.aadamsaleem.eatout.client.EventManager;
import com.aadamsaleem.eatout.models.Row;
import com.aadamsaleem.eatout.util.PrefUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecommendationVotingActivity extends Activity {
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private Button submitRecommendation;
    private String eventIDString;
    private Context mContext;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EditText enterName;



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
        enterName = (EditText) findViewById(R.id.restaurant_name);

        getListData();
        LinearLayoutManager manager = new LinearLayoutManager(RecommendationVotingActivity.this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(manager);

        submitRecommendation = (Button)findViewById(R.id.submitRecommendation);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);

        setUpListeners();
    }
    private void setUpListeners() {

        submitRecommendation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = new ProgressDialog(RecommendationVotingActivity.this, R.style.MyTheme);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCancelable(false);
                progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                progressDialog.show();

                EventManager.updateVoteCount(getApplicationContext(), prepareUpgradeJson(), new CompletionInterface() {
                    @Override
                    public void onSuccess(JSONObject result) {

                        Toast.makeText(getApplicationContext(), "Submitted!", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(RecommendationVotingActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();

                    }
                    @Override
                    public void onFailure() {
                        Log.d("***********", "getListData failure");
                    }
                });
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });
    }
    void refreshItems() {
       getListData();
    }

    void onItemsLoadComplete(List<Row> recoList) {
        mAdapter = new RecyclerViewAdapter(recoList, mContext, RecommendationVotingActivity.this);
        mRecyclerView.setAdapter(mAdapter);
        swipeRefreshLayout.setRefreshing(false);
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
        enterName.setVisibility(View.GONE);
        final ProgressDialog pd = new ProgressDialog(RecommendationVotingActivity.this,R.style.MyTheme);
        pd.setCancelable(false);
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pd.show();

        EventManager.getRecommendationList(getApplicationContext(), prepareListRequestJson(), new CompletionInterface() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    JSONArray recommendationArray = result.getJSONArray("RECOMMENDATIONS");

                    for (int i = 0; i < recommendationArray.length(); i++) {
                        JSONObject recommendationData = recommendationArray.getJSONObject(i);
                       recoList.add(new Row(recommendationData.getString("RESTAURANT_NAME"), recommendationData.getString("VOTE_COUNT"), recommendationData.getString("RESTAURANT_ID")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                pd.hide();
                enterName.setVisibility(View.VISIBLE);
                onItemsLoadComplete(recoList);
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