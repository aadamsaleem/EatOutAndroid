package com.aadamsaleem.eatout.client;

import android.content.Context;

import com.aadamsaleem.eatout.Constants;
import com.aadamsaleem.eatout.models.Event;
import com.aadamsaleem.eatout.util.PrefUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

/**
 * Created by aadam on 21/12/2016.
 */

public class EventManager {
    public static void getNeighbourhood(Context context, final CompletionInterface completionInterface) {
        String url = Constants.BASE_URL + Constants.URL_GET_NEIGHBOURHOOD;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("USER_TOKEN", PrefUtils.getCurrentUser(context).getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AsyncHttpClient client = new AsyncHttpClient();
        StringEntity se = null;
        try {
            se = new StringEntity(jsonObject.toString());
        } catch (UnsupportedEncodingException e) {
            // handle exceptions properly!
        }
        se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

        client.post(context, url, se, "application/json", new AsyncHttpResponseHandler() {
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

    public static void getRestaurantDetails(Context context, JSONObject json, final CompletionInterface completionInterface) {

        String url = Constants.BASE_URL+ Constants.GET_RESTAURANT_DETAILS;

        AsyncHttpClient client = new AsyncHttpClient();
        StringEntity se = null;
        try {
            se = new StringEntity(json.toString());
        } catch (UnsupportedEncodingException e) {
            // handle exceptions properly!
        }
        se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

        client.post(context, url, se, "application/json", new AsyncHttpResponseHandler() {
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

    public static void updateVoteCount(Context context, JSONObject json, final CompletionInterface completionInterface) {

        String url = Constants.BASE_URL+ Constants.UPDATE_VOTE;

        AsyncHttpClient client = new AsyncHttpClient();
        StringEntity se = null;
        try {
            se = new StringEntity(json.toString());
        } catch (UnsupportedEncodingException e) {
            // handle exceptions properly!
        }
        se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

        client.post(context, url, se, "application/json", new AsyncHttpResponseHandler() {
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
    public static void getRecommendationList(Context context, JSONObject json, final CompletionInterface completionInterface) {
        String url = Constants.BASE_URL+ Constants.GET_RECOMMENDATION_LIST;

        AsyncHttpClient client = new AsyncHttpClient();
        StringEntity se = null;
        try {
            se = new StringEntity(json.toString());
        } catch (UnsupportedEncodingException e) {
            // handle exceptions properly!
        }
        se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

        client.post(context, url, se, "application/json", new AsyncHttpResponseHandler() {
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


    public static void createEvent(Context context, JSONObject json, final CompletionInterface completionInterface){

        String url = Constants.BASE_URL+ Constants.URL_EVENT_CREATE;

        AsyncHttpClient client = new AsyncHttpClient();
        StringEntity se = null;
        try {
            se = new StringEntity(json.toString());
        } catch (UnsupportedEncodingException e) {
            // handle exceptions properly!
        }
        se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

        client.post(context, url, se, "application/json", new AsyncHttpResponseHandler() {
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

    public static void getPublicEvents(Context context, final CompletionInterface completionInterface) {

        String url = Constants.BASE_URL + Constants.URL_GET_ALL_EVENT;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("USER_TOKEN", PrefUtils.getCurrentUser(context).getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AsyncHttpClient client = new AsyncHttpClient();
        StringEntity se = null;
        try {
            se = new StringEntity(jsonObject.toString());
        } catch (UnsupportedEncodingException e) {
            // handle exceptions properly!
        }
        se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

        client.post(context, url, se, "application/json", new AsyncHttpResponseHandler() {
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

    public static void getEventDetails(Context context, JSONObject jsonObject, final CompletionInterface completionInterface){
        String url = Constants.BASE_URL + Constants.URL_GET_EVENT_DETAIL;

        try {
            jsonObject.put("USER_TOKEN", PrefUtils.getCurrentUser(context).getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AsyncHttpClient client = new AsyncHttpClient();
        StringEntity se = null;
        try {
            se = new StringEntity(jsonObject.toString());
        } catch (UnsupportedEncodingException e) {
            // handle exceptions properly!
        }
        se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

        client.post(context, url, se, "application/json", new AsyncHttpResponseHandler() {
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

}

