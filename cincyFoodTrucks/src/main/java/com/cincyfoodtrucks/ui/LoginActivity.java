package com.cincyfoodtrucks.ui;

import java.sql.Timestamp;
import java.util.Date;

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
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cincyfoodtrucks.R;
import com.cincyfoodtrucks.dto.TruckOwner;
import com.cincyfoodtrucks.dto.User;
import com.cincyfoodtrucks.integration.ITruckIntegration;
import com.cincyfoodtrucks.integration.IUserIntegration;
import com.cincyfoodtrucks.integration.TruckIntegration;
import com.cincyfoodtrucks.integration.UserIntegration;
//import com.facebook.Request;
//import com.facebook.Response;
//import com.facebook.Session;
//import com.facebook.SessionState;
//import com.facebook.android.AsyncFacebookRunner;
//import com.facebook.android.Facebook;
//import com.facebook.model.GraphUser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;


public class LoginActivity extends BaseActionMenuActivity  implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener{
	
	//Initialize the necessary vars
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	private TimePicker locationTime;
	LocationManager locationManager;
	TruckOwner sampleTruck;
	ITruckIntegration truckIntegrator;
	String provider;
	IUserIntegration userIntegrator;
	
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
		
		truckIntegrator = new TruckIntegration(this);
	
		
		super.onCreate(savedInstanceState);
		setContentView(com.cincyfoodtrucks.R.layout.activity_login);
	}

	

	
	 /**
     * This method is invoked when the "Put me on the map" button is clicked, because
     * we set onCheckInClicked as the value of the OnClick attribute of the
     * Submit Button.
     * @param v
     */
    public void onCheckInClicked(View v) 
    {
    	locationTime = (TimePicker)findViewById(R.id.LeavingTimePicker);
    	locationTime.clearFocus();
    	int hour  = locationTime.getCurrentHour();
    	int minute = locationTime.getCurrentMinute();
    	userIntegrator = new UserIntegration(this);
    	Location latLng = locationManager.getLastKnownLocation(provider);
    	User user = userIntegrator.getUserFromSharedPrefs();
    	
    	//get the current time
    	Date now =new Date();
    	//create timestamp from the time they will be leaving
    	@SuppressWarnings("deprecation")
		@Deprecated
    	Timestamp timestamp = new Timestamp(now.getYear(), now.getMonth(), now.getDate(), hour, minute, 0, 0);
    	
    	//Newly discovered bug- This thing needs to be in a try-catch as the app will crash if the location cannot be determined and we attempt to update location
    	try {
    		//send the data in a new thread
			truckIntegrator.executeSendTruckDataAsyncTask(this, String.valueOf(latLng.getLongitude()), String.valueOf(latLng.getLatitude()), String.valueOf(timestamp.getTime()), user.getTruckID());
			Toast.makeText(this, "Location Updated!", Toast.LENGTH_LONG).show();
	    	//send 'em back to the main page to see their updated location
	    	Intent submitIntent = new Intent(this, MainActivity.class);
	        
	        // start that activity.
	       startActivity(submitIntent);
		} catch (Exception e) {
			Toast.makeText(this, this.getString(R.string.strCannotAcquireLocation), Toast.LENGTH_LONG).show();
			
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
	
//	public void facebookLogin(){
//		// start Facebook Login
//	    Session.openActiveSession(this, true, new Session.StatusCallback() {
//
//	      // callback when session changes state
//	      @SuppressLint("ShowToast")
//		@Override
//	      public void call(Session session, SessionState state, Exception exception) {
//	        if (session.isOpened()) {
//
//	          // make request to the /me API
//	          Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
//
//	            // callback after Graph API response with user object
//	            @Override
//	            public void onCompleted(GraphUser user, Response response) {
//	              if (user != null) {
//	                Toast.makeText(getApplicationContext(), "It worked!", Toast.LENGTH_LONG).show();
//	              }
//	            }
//	          });
//	        }
//	      }
//	    });
//	}
	
	
	
//	@Override
//	  public void onActivityResult(int requestCode, int resultCode, Intent data) {
//	      super.onActivityResult(requestCode, resultCode, data);
//	      Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
////	      Toast.makeText(getApplicationContext(), "It worked!", Toast.LENGTH_LONG).show();
//	      Session session = Session.getActiveSession();
//	      if (session != null && session.isOpened()) {
//	    	 Toast.makeText(this, "Facebook Login Successful", Toast.LENGTH_SHORT).show();
//
//
//	      } else {
//	    	  //handle action if they are not logged in
//
//
//
//	      }
//	  }

}
