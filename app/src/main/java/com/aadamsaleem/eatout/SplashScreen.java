package com.aadamsaleem.eatout;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.aadamsaleem.eatout.LoggedIn.MainActivity;
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

import org.json.JSONObject;

import java.io.IOException;

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

                            Log.e("response: ", response + "");
                            try {
                                user = new User();
                                user.setFacebookID(object.getString("id").toString());
                                user.setEmail(object.getString("email").toString());
                                user.setName(object.getString("name").toString());
                                user.setGender(object.getString("gender").toString());
                                PrefUtils.setCurrentUser(user, SplashScreen.this);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(SplashScreen.this, "Welcome " + user.getName(), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                            startActivity(intent);
                            finish();

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

        if(PrefUtils.getCurrentUser(SplashScreen.this) != null){
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

//        btnLogin= (TextView) findViewById(R.id.btnLogin);
//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                progressDialog = new ProgressDialog(SplashScreen.this);
//                progressDialog.setMessage("Loading...");
//                progressDialog.show();
//
//                loginButton.performClick();
//
//                loginButton.setPressed(true);
//
//                loginButton.invalidate();
//
//                loginButton.registerCallback(callbackManager, mCallBack);
//
//                loginButton.setPressed(false);
//
//                loginButton.invalidate();
//
//            }
//        });
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
    //endregion
}