package com.cincyfoodtrucks.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.cincyfoodtrucks.R;
import com.cincyfoodtrucks.dto.TwitterUser;
import com.cincyfoodtrucks.integration.ITruckIntegration;
import com.cincyfoodtrucks.integration.IUserIntegration;
import com.cincyfoodtrucks.integration.TruckIntegration;
import com.cincyfoodtrucks.integration.TwitterIntegration;
import com.cincyfoodtrucks.integration.UserIntegration;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;


/**
 * Created by Andrew on 6/23/15.
 */
public class ConnectTwitterActivity extends Activity {
    //The auth client itself
    /*you can abstract this and call TwitterCore.getInstance().login()
    but basically that call is doing this one..
    */
    TwitterAuthClient client;
    IUserIntegration userIntegrator;
    TwitterIntegration twitterIntegrator;
    ITruckIntegration truckIntegrator;
    public static String TWITTER_CONNECTED_KEY = "TWITTERCONNECTED";
    public static String TWITTER_CONNECTED_VALUE="twitterConnectedValue";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userIntegrator = new UserIntegration(getApplicationContext());
        twitterIntegrator = new TwitterIntegration(getApplicationContext());
        truckIntegrator = new TruckIntegration(getApplicationContext());
        try {
            //just to check if they're logged in
            if (!userIntegrator.isLoggedIn(userIntegrator.getUsernameFromSharedPreferences(), userIntegrator.getEncryptedPasswordFromSharedPreferences())){
                Toast.makeText(getApplicationContext(), "You must be logged in to connect to Twitter", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        //instanciate our client
        client = new TwitterAuthClient();
        //make the call to login
        client.authorize(this, new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                //feedback
//                result.data.getUserName();


                try {
                    twitterIntegrator.sendTwitterData(new TwitterUser(result.data.getUserName(),userIntegrator.getUserFromUsername(userIntegrator.getUsernameFromSharedPreferences()).getTruckID()));
                    //not sure why we need this but we're going to do it anyway
                    twitterIntegrator.setPreference(getResources().getString(R.string.twitter_username), result.data.getUserName());
                    Intent resultData = new Intent();
                    resultData.putExtra(TWITTER_CONNECTED_KEY, TWITTER_CONNECTED_VALUE);
                    setResult(Activity.RESULT_OK, resultData);
                    finish();
                } catch (InterruptedException e) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.strGenericError), Toast.LENGTH_LONG).show();
                }
                //ok, so if we were successful we can do... This thing!


            }

            @Override
            public void failure(TwitterException e) {
                //feedback
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.strGenericError), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //this method call is necessary to get our callback to get called.
        client.onActivityResult(requestCode, resultCode, data);

        }
}
