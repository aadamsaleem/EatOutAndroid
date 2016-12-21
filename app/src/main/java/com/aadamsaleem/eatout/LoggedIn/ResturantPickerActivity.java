package com.aadamsaleem.eatout.LoggedIn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.aadamsaleem.eatout.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ResturantPickerActivity extends AppCompatActivity {

    JSONObject result;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resturant_picker);

        try {
            result = new JSONObject(getIntent().getStringExtra("RESULT"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        getViewIDs();

        final ArrayList<String> resturantNames = new ArrayList<>();
        final ArrayList<String> resturantID = new ArrayList<>();

        try {
            JSONArray resturantArray = result.getJSONObject("EVENT").getJSONArray("RECOMMENDATIONS");

            for(int i=0 ;i< resturantArray.length(); i++){
                JSONObject jsonObject = resturantArray.getJSONObject(i);
                resturantNames.add(i, jsonObject.getString("RESTAURANT_NAME"));
                resturantID.add(i, jsonObject.getString("RESTAURANT_ID"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        Log.e("aaa",""+resturantNames.size());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, resturantNames);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("id", resturantID.get(position));
                Log.e("name", resturantNames.get(position));
            }
        });

    }

    public void getViewIDs(){
        listView = (ListView) findViewById(R.id.resturantlistView);
    }
}
