package com.aadamsaleem.eatout.client;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.aadamsaleem.eatout.Constants;
import com.aadamsaleem.eatout.models.User;
import com.aadamsaleem.eatout.util.PrefUtils;
import com.facebook.login.LoginManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

/**
 * Created by aadam on 17/12/2016.
 */

public class UserManager {

    //region Public methods
    public static void signup(final User user, Context applicationContext, final CompletionInterface completionInterface) {

        JSONObject json = new JSONObject();
        try {
            json.put("FACEBOOK_ID", user.getFacebookID());
            json.put("USER_NAME", user.getName());
            json.put("EMAIL_ID", user.getEmail());
            json.put("GENDER", user.getGender());

        } catch (Exception e) {
            e.printStackTrace();
        }
        String url = Constants.BASE_URL + Constants.URL_SIGN_UP;

        AsyncHttpClient client = new AsyncHttpClient();
        StringEntity se = null;
        try {
            se = new StringEntity(json.toString());
        } catch (UnsupportedEncodingException e) {
            // handle exceptions properly!
        }
        se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

        client.post(applicationContext, url, se, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {

                    JSONObject resultJSON = null;
                    try {
                        resultJSON = new JSONObject(new String(responseBody));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    completionInterface.onSuccess(resultJSON);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                completionInterface.onFailure();
            }
        });


    }

    public static void signOut(Activity activity) {
        PrefUtils.clearCurrentUser(activity);
        LoginManager.getInstance().logOut();
    }

    public static void updateFBFriendList(JSONArray friendArray, final Context applicationContext, final CompletionInterface completionInterface) {

        String url = Constants.BASE_URL+ Constants.URL_FB_FRIEND_LIST_UPDATE;
        JSONObject json = new JSONObject();
        JSONArray friendJSONArray = new JSONArray();
        try {
            json.put("USER_TOKEN", PrefUtils.getCurrentUser(applicationContext).getToken());

            for (int i = 0; i < friendArray.length(); i++) {
                JSONObject friend = friendArray.getJSONObject(i);
                friendJSONArray.put(friend.getString("id"));
            }

            json.put("LIST_OF_FRIENDS",friendJSONArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        AsyncHttpClient client = new AsyncHttpClient();
        StringEntity se = null;
        try {
            se = new StringEntity(json.toString());
        } catch (UnsupportedEncodingException e) {
            // handle exceptions properly!
        }
        se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

        client.post(applicationContext, url, se, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {

                    JSONObject resultJSON = null;
                    try {
                        resultJSON = new JSONObject(new String(responseBody));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    completionInterface.onSuccess(resultJSON);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                completionInterface.onFailure();
            }
        });

    }
    //endregion

}
