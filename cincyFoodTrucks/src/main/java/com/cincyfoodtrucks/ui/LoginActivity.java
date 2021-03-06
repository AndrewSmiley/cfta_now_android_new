package com.cincyfoodtrucks.ui;

import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cincyfoodtrucks.R;
import com.cincyfoodtrucks.dto.TruckOwner;
import com.cincyfoodtrucks.dto.TwitterUser;
import com.cincyfoodtrucks.dto.User;
import com.cincyfoodtrucks.integration.FacebookPageIntegration;
import com.cincyfoodtrucks.integration.ITruckIntegration;
import com.cincyfoodtrucks.integration.IUserIntegration;
import com.cincyfoodtrucks.integration.TruckIntegration;
import com.cincyfoodtrucks.integration.TwitterIntegration;
import com.cincyfoodtrucks.integration.UserIntegration;
//import com.facebook.Request;
//import com.facebook.Response;
//import com.facebook.Session;
//import com.facebook.SessionState;
//import com.facebook.android.AsyncFacebookRunner;
//import com.facebook.android.Facebook;
//import com.facebook.model.GraphUser;
import com.cincyfoodtrucks.utilities.NewsFeedItem;
import com.facebook.AccessToken;
import com.facebook.AccessTokenSource;
import com.facebook.FacebookSdk;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.internal.fb;
import com.google.android.gms.maps.model.LatLng;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetui.UserTimeline;


public class LoginActivity extends BaseActionMenuActivity  implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener{
	
	//Initialize the necessary vars
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	private TimePicker locationTime;
	LocationManager locationManager;
	TruckOwner sampleTruck;
	ITruckIntegration truckIntegrator;
	String provider;
	IUserIntegration userIntegrator;
	CheckBox postToFacebookChkBox;
	CheckBox postToTwitterChkBox;
	FacebookPageIntegration fbIntegrator;
	Button checkInBtn;
	TwitterIntegration twitterIntegrator;
	 // Your Facebook APP ID
    private static String APP_ID = "662026153876150"; // Replace your App ID here

    // Instance of Facebook Class
//    private Facebook facebook;
//    private AsyncFacebookRunner mAsyncRunner;
//
    
 
 
        
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//get the time picker
		
//		facebook = new Facebook(APP_ID);
//        mAsyncRunner = new AsyncFacebookRunner(facebook);
		sampleTruck = new TruckOwner();
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);
		fbIntegrator = new FacebookPageIntegration(getApplicationContext());
		truckIntegrator = new TruckIntegration(this);
		twitterIntegrator = new TwitterIntegration(getApplicationContext());
		super.onCreate(savedInstanceState);
		setContentView(com.cincyfoodtrucks.R.layout.activity_login);
		postToFacebookChkBox = (CheckBox) findViewById(R.id.postToFacebookChkBox);
		postToTwitterChkBox = (CheckBox) findViewById(R.id.postToTwitterChkBox);
		checkInBtn = (Button) findViewById(R.id.btnLogin);
		userIntegrator = new UserIntegration(getApplicationContext());
		if (!FacebookSdk.isInitialized()) {
			FacebookSdk.sdkInitialize(getApplicationContext());
		}
	}

	

	
	 /**
     * This method is invoked when the "Put me on the map" button is clicked, because
     * we set onCheckInClicked as the value of the OnClick attribute of the
     * Submit Button.
     * @param v
     */
    public void onCheckInClicked(View v) 
    {

    	//Newly discovered bug- This thing needs to be in a try-catch as the app will crash if the location cannot be determined and we attempt to update location
    	try {
			//do some shit motha fucka!
			boolean fbPost, twitterPost;
			fbPost = postToFacebookChkBox.isChecked();
			twitterPost = postToTwitterChkBox.isChecked();
			if (twitterPost) {
				//hoping this is the right code
				if (TwitterCore.getInstance().getSessionManager().getActiveSession() == null) {
					Intent i = new Intent(this, ConnectTwitterActivity.class);
					startActivity(i);
					return;
				}

			}


			if (fbPost) {
				//ok so first, check to see if there's a facebook session or not
				//ok, so if the access token isn't null, then we're logged in
				String accessTokenStr = fbIntegrator.getPreference(getResources().getString(R.string.facebook_page_access_token));
				if (AccessToken.getCurrentAccessToken() == null || accessTokenStr == null || accessTokenStr.equalsIgnoreCase("")) {
					//if they're already logged into facebook, we can just do the work
					//otherwise send them to the login flow
					Intent i = new Intent(this, ConnectFacebookActivity.class);
					startActivityForResult(i, 1);
					return;

				}
			}
			doWork(fbPost, twitterPost);

		}catch (Exception e) {
			Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
			
		}
//    	//more test dialog stuff
//    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
//    	builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
//    	    .setNegativeButton("No", dialogClickListener).show();
//    
   
    }
    
    /**
     * Test dialog stuff for facebook integration
     */
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
        	Intent submitIntent = new Intent(getApplicationContext(), MainActivity.class);
            switch (which){
            /*
             * If they choose to post to facebook
             */
            case DialogInterface.BUTTON_POSITIVE:
            	
//        	facebookLogin();
            //No button clicked
        	Toast.makeText(getApplicationContext(), "No", Toast.LENGTH_LONG).show();
        	Toast.makeText(getApplicationContext(), "Location Updated!", Toast.LENGTH_LONG).show();
        	//send 'em back to the main page to see their updated location
        
            
            // start that activity.
           startActivity(submitIntent);
        
            	
            	
                break;

            case DialogInterface.BUTTON_NEGATIVE:
                //No button clicked
            	Toast.makeText(getApplicationContext(), "No", Toast.LENGTH_LONG).show();
            	Toast.makeText(getApplicationContext(), "Location Updated!", Toast.LENGTH_LONG).show();
            	//send 'em back to the main page to see their updated location
            	// start that activity.
               startActivity(submitIntent);
            
                break;
            }
        }
    };

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
           Toast.makeText(this, connectionResult.getErrorCode(), Toast.LENGTH_LONG).show();
        }
		
		
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}

	
	
	@Override
	  public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			if(resultCode == RESULT_OK){
				//this is where we'll nigger rig the shit for the facebook and the twitter
				if (data.getStringExtra(ConnectFacebookActivity.FACEBOOK_LOGGED_IN_KEY) != null){
					Toast.makeText(getApplicationContext(), "You're logged into Facebook!", Toast.LENGTH_LONG).show();
					checkInBtn.performClick();

//					doWork();
				}else if (data.getStringExtra(ConnectTwitterActivity.TWITTER_CONNECTED_KEY) != null){
					Toast.makeText(getApplicationContext(), "You're logged into Twitter!", Toast.LENGTH_LONG).show();
					checkInBtn.performClick();
				}
			}
			if (resultCode == RESULT_CANCELED) {
				//Write your code if there's no result
			}
		}

	  }

	public void doWork(boolean postToFB, boolean postToTwitter){
		//send the data in a new thread
		//comment this out for right now
		locationTime = (TimePicker)findViewById(R.id.LeavingTimePicker);
		locationTime.clearFocus();
		int hour  = locationTime.getCurrentHour();
		int minute = locationTime.getCurrentMinute();
		userIntegrator = new UserIntegration(this);
		Location latLng = locationManager.getLastKnownLocation(provider);
		User user = userIntegrator.getUserFromSharedPrefs();
		LatLng lng = new LatLng(latLng.getLatitude(), latLng.getLongitude());
		//get the current time
		Date now =new Date();
		//create timestamp from the time they will be leaving
		@SuppressWarnings("deprecation")
		@Deprecated
		Timestamp timestamp = new Timestamp(now.getYear(), now.getMonth(), now.getDate(), hour, minute, 0, 0);
		if (postToFB && ! postToTwitter) {
			postToFacebookPage(timestamp, truckIntegrator.getTruckByTruckID(userIntegrator.getUserFromSharedPrefs().getTruckID()), lng);
			postTruckLocation(latLng, timestamp, user);
		}
		if(postToTwitter && !postToFB){
			postTweet(fbIntegrator.createUpdateMessage(timestamp, truckIntegrator.getTruckByTruckID(userIntegrator.getUserFromSharedPrefs().getTruckID()).getTruckName(), lng), latLng, timestamp, user);

		}
		if(postToFB && postToTwitter){
			postToFacebookPage(timestamp, truckIntegrator.getTruckByTruckID(userIntegrator.getUserFromSharedPrefs().getTruckID()), lng);
			postTweet(fbIntegrator.createUpdateMessage(timestamp, truckIntegrator.getTruckByTruckID(userIntegrator.getUserFromSharedPrefs().getTruckID()).getTruckName(), lng), latLng, timestamp, user);
		}
	}


	public void postToFacebookPage(Timestamp ts, TruckOwner truck, LatLng ltLng){
		String accessTokenStr = fbIntegrator.getPreference(getResources().getString(R.string.facebook_page_access_token));
//		if (accessTokenStr == null || accessTokenStr.equalsIgnoreCase("")){
//			//if they're already logged into facebook, we can just do the work
//			//otherwise send them to the login flow
//			Intent i = new Intent(this, ConnectFacebookActivity.class);
//			startActivityForResult(i, 1);
//			return;
//		}
		AccessToken token = new AccessToken(accessTokenStr, AccessToken.getCurrentAccessToken().getApplicationId(),
				AccessToken.getCurrentAccessToken().getUserId(), Arrays.asList("publish_actions", "manage_pages", "publish_pages"), null, AccessTokenSource.FACEBOOK_APPLICATION_SERVICE,
				AccessToken.getCurrentAccessToken().getExpires(), AccessToken.getCurrentAccessToken().getLastRefresh());
		AccessToken.setCurrentAccessToken(token);


		try {

			boolean result = fbIntegrator.postToFacebook(fbIntegrator.createUpdateMessage(ts, truck.getTruckName(), ltLng), AccessToken.getCurrentAccessToken());
			//ternaries mother fuckers!
			Toast.makeText(getApplicationContext(), result?"Posted to Facebook Successfully!":"Could not post to Facebook at this time.", Toast.LENGTH_LONG).show();
		} catch (InterruptedException e) {
			Toast.makeText(getApplicationContext(), "An error occurred posting to Facebook", Toast.LENGTH_LONG).show();
//			e.printStackTrace();
		}
	}

public void postTweet(String message, final Location latLng, final Timestamp timestamp, final User user){
	StatusesService statusesService = Twitter.getApiClient(TwitterCore.getInstance().getSessionManager().getActiveSession()).getStatusesService();

//        void update(@Field("status") String var1, @Field("in_reply_to_status_id") Long var2, @Field("possibly_sensitive") Boolean var3, @Field("lat") Double var4, @Field("long") Double var5, @Field("place_id") String var6, @Field("display_cooridnates") Boolean var7, @Field("trim_user") Boolean var8, Callback<Tweet> var9);
	statusesService.update(message, null, null, null, null, null, null, null, new Callback<Tweet>() {

		@Override
		public void success(Result<Tweet> result) {
			postTruckLocation(latLng, timestamp, user);

		}

		@Override
		public void failure(TwitterException e) {
			Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
		}
	});
}

	public  void postTruckLocation(Location latLng, Timestamp timestamp, User user){
		truckIntegrator.executeSendTruckDataAsyncTask(this, String.valueOf(latLng.getLongitude()), String.valueOf(latLng.getLatitude()), String.valueOf(timestamp.getTime()), user.getTruckID());
			Toast.makeText(this, "Location Updated!", Toast.LENGTH_LONG).show();
//		send 'em back to the main page to see their updated location
	    	Intent submitIntent = new Intent(this, MainActivity.class);
	        // start that activity.
	       startActivity(submitIntent);
	}


}
