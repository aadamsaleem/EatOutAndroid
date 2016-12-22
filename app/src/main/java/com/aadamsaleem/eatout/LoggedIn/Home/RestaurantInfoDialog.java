package com.aadamsaleem.eatout.LoggedIn.Home;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import com.aadamsaleem.eatout.R;
import com.aadamsaleem.eatout.client.CompletionInterface;
import com.aadamsaleem.eatout.client.EventManager;
import com.aadamsaleem.eatout.util.PrefUtils;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by kirank on 12/22/16.
 */

public class RestaurantInfoDialog extends Dialog {
//        implements android.view.View.OnClickListener {

    private static final String DOLLAR = "$";
    public Activity activity;
    private  TextView restaurantName;
    private  ImageView image;
    private  TextView priceTV;
    private  TextView ratingTV;
    private TextView ratingCountTV;
    private TextView line1;
    private TextView line2;
    private TextView contact;
    private String restaurantID;
    private TextView phoneNumber;

    public RestaurantInfoDialog(Activity activity, Row selectedRow) {
        super(activity);
        // TODO Auto-generated constructor stub
        this.activity = activity;
        Row selectedRow1 = selectedRow;
        restaurantID = selectedRow.getRestaurantID();
        // make an api call here
        EventManager.getRestaurantDetails(activity, prepareInfoRequestJson(), new CompletionInterface() {
            @Override
            public void onSuccess(JSONObject result) {
                Log.d("***********", "restaurant details" + result.toString());
                try {
                    JSONObject profile = result.getJSONObject("PROFILE");
                    restaurantName.setText("" + profile.getString("RESTAURANT_NAME"));
                    ratingTV.setText("" +profile.getDouble("RESTAURANT_RATING"));
//                    URL newurl = new URL(profile.getString("RESTAURANT_PHOTO"));
//                    Bitmap mIcon_val = BitmapFactory.decodeStream(newurl.openConnection() .getInputStream());
//                    image.setImageBitmap(mIcon_val);
                    double price = profile.getDouble("RESTAURANT_PRICE");
                    if(profile.has("RESTAURANT_PHONE")) {
                        contact.setText(profile.getString("RESTAURANT_PHONE"));
                        contact.setVisibility(View.VISIBLE);
                        phoneNumber.setVisibility(View.VISIBLE);
                    }
                    String priceString = "";
                    while(price > 0) {
                        priceString += DOLLAR;
                        price --;
                    }
                    priceTV.setText("" + priceString);
                    String address = profile.getString("RESTAURANT_ADDRESS");
                    ratingCountTV.setText("" + profile.getInt("RESTAURANT_REVIEWCOUNT"));

                    setAddress(address);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                {"PROFILE":{"RESTAURANT_RATING":4.5,"RESTAURANT_HOURS":[{"start":"1130","is_overnight":false,"end":"0000","day":0},{"start":"1130","is_overnight":false,"end":"0000","day":1},{"start":"1130","is_overnight":false,"end":"0000","day":2},{"start":"1130","is_overnight":false,"end":"0000","day":3},{"start":"1130","is_overnight":true,"end":"0200","day":4},{"start":"1130","is_overnight":true,"end":"0200","day":5},{"start":"1130","is_overnight":false,"end":"0000","day":6}],"RESTAURANT_ADDRESS":"95 Macdougal St, New York, NY 10012","RESTAURANT_PRICE":2,"RESTAURANT_ID":"mirch-masala-new-york","RESTAURANT_CUISINES":["Indian","Halal","Vegetarian"],"RESTAURANT_PHONE":"+12127772888","RESTAURANT_PHOTO":"https:\/\/s3-media4.fl.yelpcdn.com\/bphoto\/-pWV2wwNsHYpfWkeWnXw9Q\/o.jpg","RESTAURANT_LOCATION":[40.7293640992544,-74.0010555819702],"ITEMS_REVIEW":[],"RESTAURANT_NAME":"Mirch Masala","RESTAURANT_REVIEWCOUNT":188},"STATUS":501}
            }
            @Override
            public void onFailure() {
                Log.d("***********", "restaurant details failure");
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.restaurant_info);
        restaurantName = (TextView) findViewById(R.id.restaname);
        image = (ImageView) findViewById(R.id.imageView);
        priceTV = (TextView) findViewById(R.id.ActualPrice);
        ratingTV = (TextView) findViewById(R.id.ActualRating);
        ratingCountTV = (TextView) findViewById(R.id.ActualReviewCount);
        line1 = (TextView) findViewById(R.id.line1);
        line2 = (TextView) findViewById(R.id.line2);
        contact = (TextView) findViewById(R.id.ActualPhoneNumber);
        phoneNumber = (TextView) findViewById(R.id.phoneNumber);

    }

    public JSONObject prepareInfoRequestJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("USER_TOKEN", PrefUtils.getCurrentUser(activity).getToken());
            json.put("RESTAURANT_ID", restaurantID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public void setAddress(String Address) {

        if(Address == null) {
            line1.setText("not available");
            line2.setText("not available");
        }
        String[] addressArray = Address.split(",");
        line1.setText(addressArray[0]);
        line2.setText(addressArray[1]);

    }
}

