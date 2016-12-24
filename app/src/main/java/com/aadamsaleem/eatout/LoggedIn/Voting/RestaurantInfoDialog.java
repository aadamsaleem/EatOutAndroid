package com.aadamsaleem.eatout.LoggedIn.Voting;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.aadamsaleem.eatout.R;
import com.aadamsaleem.eatout.client.CompletionInterface;
import com.aadamsaleem.eatout.client.EventManager;
import com.aadamsaleem.eatout.models.Row;
import com.aadamsaleem.eatout.util.PrefUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kirank on 12/22/16.
 */

public class RestaurantInfoDialog extends Dialog {
//        implements android.view.View.OnClickListener {

    private static final String DOLLAR = "$";
    public Activity activity;
    private TextView restaurantName;
    private ImageView image;
    private TextView priceTV;
    private TextView ratingTV;
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
                try {
                    JSONObject profile = result.getJSONObject("PROFILE");
                    restaurantName.setText("" + profile.getString("RESTAURANT_NAME"));
                    ratingTV.setText("" + profile.getDouble("RESTAURANT_RATING"));


                    ImageLoader imageLoader = ImageLoader.getInstance();

                    imageLoader.init(ImageLoaderConfiguration.createDefault(getContext()));

                    imageLoader.displayImage(profile.getString("RESTAURANT_PHOTO"), image);

                    double price = profile.getDouble("RESTAURANT_PRICE");
                    if (profile.has("RESTAURANT_PHONE")) {
                        contact.setText(profile.getString("RESTAURANT_PHONE"));
                        contact.setVisibility(View.VISIBLE);
                        phoneNumber.setVisibility(View.VISIBLE);
                    }
                    String priceString = "";
                    while (price > 0) {
                        priceString += DOLLAR;
                        price--;
                    }
                    priceTV.setText("" + priceString);
                    String address = profile.getString("RESTAURANT_ADDRESS");
                    ratingCountTV.setText("" + profile.getInt("RESTAURANT_REVIEWCOUNT"));

                    setAddress(address);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

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

        if (Address == null) {
            line1.setText("not available");
            line2.setText("not available");
        }
        String[] addressArray = Address.split(",");
        line1.setText(addressArray[0]);
        line2.setText(addressArray[1]);

    }
}

