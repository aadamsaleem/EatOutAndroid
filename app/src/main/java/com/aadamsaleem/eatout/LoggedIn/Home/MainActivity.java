package com.aadamsaleem.eatout.LoggedIn.Home;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.aadamsaleem.eatout.LoggedIn.Event.CreateEventActivity;
import com.aadamsaleem.eatout.R;
import com.aadamsaleem.eatout.client.CompletionInterface;
import com.aadamsaleem.eatout.client.UserManager;
import com.aadamsaleem.eatout.models.User;
import com.aadamsaleem.eatout.util.PrefUtils;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private User user;

    //region Override Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Log.e("firebasetoken",FirebaseInstanceId.getInstance().getToken());

        user = PrefUtils.getCurrentUser(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, CreateEventActivity.class);
                startActivity(i);
            }
        });


        getFacebookFriendList(user.getFacebookID());
    }
    //endregion

    //region Private Methods
    private void getFacebookFriendList(String fbUserID) {


        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + fbUserID + "/friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {

                        JSONArray friendArray = null;
                        try {
                            friendArray = (response.getJSONObject().getJSONArray("data"));
                            UserManager.updateFBFriendList(friendArray,getApplicationContext(), new CompletionInterface() {
                                @Override
                                public void onSuccess(JSONObject result) {
                                }

                                @Override
                                public void onFailure() {

                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
        ).executeAsync();
    }
    //endregion

    //region class SectionPageAdapter
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return NearbyFragment.newInstance(getApplicationContext());
                case 1:
                    return FriendsFragment.newInstance(getApplicationContext());
                case 2:
                    return ProfileFragment.newInstance(getApplicationContext());

            }
            return null;
        }

        @Override
        public int getCount() {

            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "NEARBY";
                case 1:
                    return "EVENTS";
                case 2:
                    return "ME";
            }
            return null;
        }
    }
    //endregion
}
