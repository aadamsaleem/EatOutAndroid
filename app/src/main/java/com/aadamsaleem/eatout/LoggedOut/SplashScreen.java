package com.aadamsaleem.eatout.LoggedOut;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.aadamsaleem.eatout.LoggedIn.Home.MainActivity;
import com.aadamsaleem.eatout.R;
import com.aadamsaleem.eatout.client.CompletionInterface;
import com.aadamsaleem.eatout.client.UserManager;
import com.aadamsaleem.eatout.models.User;
import com.aadamsaleem.eatout.util.PrefUtils;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.yqritc.scalablevideoview.ScalableType;
import com.yqritc.scalablevideoview.ScalableVideoView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SplashScreen extends Activity {
    ScalableVideoView videoHolder;
    LoginButton loginButton;
    CallbackManager callbackManager;
    User user;

    //region Facebook CallBack
    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {

                            try {
                                user = new User();
                                user.setFacebookID(object.getString("id"));
                                user.setEmail(object.getString("email"));
                                user.setName(object.getString("name"));
                                user.setGender(object.getString("gender"));


                                UserManager.signup(user, getApplicationContext(), new CompletionInterface() {
                                    @Override
                                    public void onSuccess(JSONObject result) {
                                        try {
                                            String token = result.getString("user_token");
                                            user.setToken(token);
                                            PrefUtils.setCurrentUser(user, SplashScreen.this);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        Toast.makeText(SplashScreen.this, "Welcome " + user.getName(), Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();

                                    }

                                    @Override
                                    public void onFailure() {
                                        Toast.makeText(SplashScreen.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                                        try {
                                            UserManager.signOut(SplashScreen.this);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender, birthday");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
        }

        @Override
        public void onError(FacebookException e) {
        }
    };
    //endregion

    //region Override Methods
    @Override
    public void onCreate(Bundle savedInstanceState) {

        initFacebook();

        super.onCreate(savedInstanceState);

        printKeyHash();
        if (PrefUtils.getCurrentUser(SplashScreen.this) != null) {
            Intent homeIntent = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(homeIntent);
            finish();
        }

        setContentView(R.layout.activity_splash_screen);

        getViewIDs();
        setUpBackgroundVideo();

    }

    @Override
    protected void onResume() {
        super.onResume();

        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile", "email", "user_friends");
        loginButton.registerCallback(callbackManager, mCallBack);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    //endregion

    //region Private Methods
    private void getViewIDs() {

        videoHolder = (ScalableVideoView) findViewById(R.id.splash_background_video);
        loginButton = (LoginButton) findViewById(R.id.login_button);

    }

    private void setUpBackgroundVideo() {

        try {
            videoHolder.setRawData(R.raw.splash_video);
        } catch (IOException e) {
            e.printStackTrace();
        }
        videoHolder.setLooping(true);
        videoHolder.setScalableType(ScalableType.CENTER_CROP);
        videoHolder.prepareAsync(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });

    }

    private void initFacebook() {

        FacebookSdk.sdkInitialize(getApplicationContext());

    }

    private void printKeyHash() {
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.aadamsaleem.eatout", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("KeyHash:", e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("KeyHash:", e.toString());
        }
    }
    //endregion
}