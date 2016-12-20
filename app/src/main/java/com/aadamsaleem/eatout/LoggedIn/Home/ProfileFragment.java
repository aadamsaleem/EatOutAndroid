package com.aadamsaleem.eatout.LoggedIn.Home;

/**
 * Created by aadamsaleem on 11/16/16.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aadamsaleem.eatout.LoggedIn.Preferences.PreferencesActivity;
import com.aadamsaleem.eatout.R;
import com.aadamsaleem.eatout.LoggedOut.SplashScreen;
import com.aadamsaleem.eatout.client.UserManager;
import com.aadamsaleem.eatout.models.User;
import com.aadamsaleem.eatout.util.PrefUtils;
import com.facebook.login.widget.LoginButton;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ProfileFragment extends Fragment {
    //Properties
    Context mContext;
    RoundedImageView profilePicture;
    TextView nameTextView, emailTextView;
    View rootView;
    Bitmap bitmap;
    User user;
    LoginButton loginButton;
    LinearLayout preferencesLayout;

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance(Context context) {
        ProfileFragment fragment = new ProfileFragment();
        fragment.mContext = context;

        return fragment;
    }

    //region Override Methods
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        user = PrefUtils.getCurrentUser(getContext());
        getViewIDs();

        setUpViews();

        return rootView;
    }
    //endregion

    //region Private Methods
    private void getViewIDs()
    {
        profilePicture = (RoundedImageView) rootView.findViewById(R.id.profile_picture);
        nameTextView = (TextView)rootView.findViewById(R.id.name);
        emailTextView = (TextView)rootView.findViewById(R.id.email);
        loginButton = (LoginButton) rootView.findViewById(R.id.login_button);
        preferencesLayout  = (LinearLayout) rootView.findViewById(R.id.preferences_Layout);

    }

    private void setUpViews(){
        getProfilePic();

        nameTextView.setText(user.getName());
        emailTextView.setText(user.getEmail());

        profilePicture.setScaleType(ImageView.ScaleType.CENTER_CROP);
        profilePicture.setOval(true);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();

            }
        });

        preferencesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, PreferencesActivity.class);
                startActivity(i);

            }
        });

    }

    private void getProfilePic(){
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                URL imageURL = null;
                try {
                    imageURL = new URL("https://graph.facebook.com/" + user.getFacebookID() + "/picture?type=large");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    bitmap  = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                profilePicture.setImageBitmap(bitmap);
            }
        }.execute();
    }


    private void logout(){
        UserManager.signOut(getActivity());

        Intent i= new Intent(getActivity(),SplashScreen.class);
        startActivity(i);
        getActivity().finish();
    }
    //endregion
}
