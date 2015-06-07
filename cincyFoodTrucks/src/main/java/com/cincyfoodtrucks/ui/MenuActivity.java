package com.cincyfoodtrucks.ui;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cincyfoodtrucks.R;
import com.cincyfoodtrucks.dao.INetworkUser;
import com.cincyfoodtrucks.dao.IReviewServiceSQLite;
import com.cincyfoodtrucks.dao.IUserServiceSQLite;
import com.cincyfoodtrucks.dao.MenuServiceSQLiteStub;
import com.cincyfoodtrucks.dao.NetworkMenuDAO;
import com.cincyfoodtrucks.dao.NetworkReviewDAO;
import com.cincyfoodtrucks.dao.NetworkTruckDAO;
import com.cincyfoodtrucks.dao.NetworkUserDAO;
import com.cincyfoodtrucks.dao.ReviewServiceSQLiteStub;
import com.cincyfoodtrucks.dao.TruckServiceSQLiteStub;
import com.cincyfoodtrucks.dao.UserServiceSQLite;
import com.cincyfoodtrucks.dto.Review;
import com.cincyfoodtrucks.dto.TruckMenuItem;
import com.cincyfoodtrucks.dto.TruckOwner;
import com.cincyfoodtrucks.dto.User;
import com.cincyfoodtrucks.integration.IUserIntegration;
import com.cincyfoodtrucks.integration.MapsIntegration;
import com.cincyfoodtrucks.integration.UserIntegration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

/**
 * What the fuck is this class?????
 * I think this is that shitty navigation class
 * @author Andrew
 *
 */
public class MenuActivity extends BaseActionMenuActivity {
	IUserIntegration userIntegrator;
	LocationManager locationManager;
	String provider;
	Button locationListBtn;
	Button createNewItemBtn;
	Button updateLocationBtn;
	Button changePasswordBtn;
	 /** The view to show the ad. */
	  private AdView adView;
	  private static final String AD_UNIT_ID = "ca-app-pub-5343364852657568/6594382932";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		// Create an ad.
	    adView = new AdView(this);
	    adView.setAdSize(AdSize.BANNER);
	    adView.setAdUnitId(AD_UNIT_ID);
	    LinearLayout layout = (LinearLayout) findViewById(R.id.menuViewLinearLayout);
	    layout.addView(adView);

//Create an ad request. Check logcat output for the hashed device ID to
//get test ads on a physical device.
	    AdRequest adRequest = new AdRequest.Builder()
//	    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//	    .addTestDevice("6BCE21BFA078CD23EAFFA49FE680B8D3")
	    .build();

	    // Start loading the ad in the background.
	    adView.loadAd(adRequest);

		userIntegrator = new UserIntegration(this);
		
		//find the button we want to show conditionally
		updateLocationBtn = (Button)findViewById(R.id.postLocationBtn);
		
		Criteria criteria = new Criteria();
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		provider = locationManager.getBestProvider(criteria, false);
		locationListBtn = (Button)findViewById(R.id.locationListBtn);
		createNewItemBtn = (Button)findViewById(R.id.btnCreateNewMenuItem);
		changePasswordBtn = (Button)findViewById(R.id.changePasswordBtn);
		//handle the case if the gps is not enabled
		if(!locationManager.isProviderEnabled( provider ))
		{
			//just let them know and hide the location list button since they can still see the map
			Toast.makeText(this, getResources().getString(R.string.strGPSDisabledSomeFeaturesAreDisabled), Toast.LENGTH_LONG).show();
			locationListBtn.setEnabled(false);
		}
	
				
		
		
		
		
	try {
			boolean result = userIntegrator.isLoggedIn(userIntegrator.getUsernameFromSharedPreferences(), userIntegrator.getEncryptedPasswordFromSharedPreferences());
			//conditionally show/hide the Truck Owner Buttons
		if(result){
			updateLocationBtn.setVisibility(View.VISIBLE);
			createNewItemBtn.setVisibility(View.VISIBLE);
			changePasswordBtn.setVisibility(View.VISIBLE);
			
			}else
			{	
				createNewItemBtn.setVisibility(View.GONE);
				updateLocationBtn.setVisibility(View.GONE);
				changePasswordBtn.setVisibility(View.GONE);
			
			}
			
		} catch (Exception e) {
		
			e.printStackTrace();
			//raise a toast
			Toast.makeText(this, R.string.strGenericError, Toast.LENGTH_LONG).show();
		}
	
	
		}

	
	
	public void onLoginClicked(View v)
	{
		Intent i = new Intent(this, LocationActivity.class);
		startActivity(i);
	}
	

	 /**
     * This method is invoked when the Contact Us button is clicked, because
     * we set onSubmitClicked as the value of the OnClick attribute of the
     * Submit Button.
     * @param v
     */
    public void onContactClicked(View v) 
    {
        Intent submitIntent = new Intent(this, ContactActivity.class);
        
        // start that activity.
        startActivity(submitIntent);
    
    }
    
    public void onLocationMapClicked(View v)
    {
    	Intent mainIntent = new Intent(this, MainActivity.class);
    	startActivity(mainIntent);
    }

    /**
     * This method is invoked when the Rate Us button is clicked, because
     * we set onRateClicked as the value of the OnClick attribute of the
     * Rate Button.
     * @param v
     */
    public void onRateClicked(View v) 
    {
       /* // create an intent that tells us to go to the Advanced Search Activity.
        Intent rateIntent = new Intent(this, RateActivity.class);
        
        // start that activity.
        startActivity(rateIntent);*/
    	 //startActivity(rateIntent);
    	final String appName = "com.cincyfoodtrucks&hl=en";
    	
    	//on the rate us button, open a link to our page on the app store
    	try {
    	    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+appName)));
    	} catch (android.content.ActivityNotFoundException anfe) {
    	    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id="+appName)));
    	}

    
    
    }
    
    /**
     * Method to bring a user to the location listing view
     * @param v
     */
    public void onLocationListClicked(View v) 
    {
    	Intent locationListIntent = new Intent(this, LocationListActivity.class);
    	startActivity(locationListIntent);
    }
    
    /**
     * Method to bring a user to the update location page
     * @param v
     */
    public void onUpdateLocationClicked(View v)
    {
    	Intent locationIntent = new Intent(this, LoginActivity.class);
    	startActivity(locationIntent);
    
    }
    
    /**
     * Method to process the action when the create new menu item button is clicked
     * @param v
     */
    public void onCreateNewMenuItemClicked(View v)
    {
    	Intent newItem = new Intent(this, CreateMenuItemActivity.class);
    	startActivity(newItem);
    	
    }
    
    /**
     * Method to handle actions when the user selects the change password option
     * @param v
     */
    public void onChangePasswordClicked(View v)
    {
    	Intent cpIntent = new Intent(this, ChangePasswordActivity.class);
    	startActivity(cpIntent);
    	
    }
 
}
