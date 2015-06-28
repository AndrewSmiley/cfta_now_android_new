package com.cincyfoodtrucks.ui;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cincyfoodtrucks.R;
import com.cincyfoodtrucks.dto.TruckOwner;
import com.cincyfoodtrucks.integration.IMapsIntegration;
import com.cincyfoodtrucks.integration.ITruckIntegration;
import com.cincyfoodtrucks.integration.IUserIntegration;
import com.cincyfoodtrucks.integration.MapsIntegration;
import com.cincyfoodtrucks.integration.TruckIntegration;
import com.cincyfoodtrucks.integration.UserIntegration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
//import com.newrelic.agent.android.NewRelic;

public class MainActivity extends BaseActionMenuActivity implements LocationListener,
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener,
		OnMarkerClickListener {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "HehIAkEuo6yzLBuPfygN5GQdl";
    private static final String TWITTER_SECRET = "qCFcYGMDUj7suOJIVZTfuqo8wOsm5HzcMatb1OO8R6obvTXC4w";

	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	private static final int REFRESH_INTERVAL = 20;
	private static final int MILLISECONDS_PER_SECOND = 1000;
	private GoogleMap mMap;
	Button btnDismiss;
	IMapsIntegration mapsIntegrator;
	ITruckIntegration truckIntegrator;
	IUserIntegration userIntegrator;
	LocationManager locationManager;
	String provider;
	private static final String AD_UNIT_ID = "ca-app-pub-5343364852657568/6594382932";
	 /** The view to show the ad. */
	  private AdView adView;


	Location myLocation;
	LatLng currentLatLng;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
//		NewRelic.withApplicationToken("AAde5b61e5245fd70f110e95a48961360afceb9bc5").start(this.getApplication());
		// create a location manager and criteria for gps uses
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);
		super.onCreate(savedInstanceState);
		
		// before doing anything to do with the GoogleMap, ensure that gps is
		// enabled
		if (locationManager.isProviderEnabled(provider)) {
			// obtain location for device
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, REFRESH_INTERVAL
							* MILLISECONDS_PER_SECOND, 0, this);
			myLocation = locationManager.getLastKnownLocation(provider);
			if (myLocation != null){
			// obtain latlng for the users pin on the googlemap
			currentLatLng = new LatLng(myLocation.getLatitude(),
					myLocation.getLongitude());

			mapsIntegrator = new MapsIntegration(this, locationManager);
			userIntegrator = new UserIntegration(this);

			truckIntegrator = new TruckIntegration(this);
			super.onCreate(savedInstanceState);
			TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
			Fabric.with(this, new Twitter(authConfig));
			setContentView(R.layout.activity_main);
			
			 // Create an ad.
		    adView = new AdView(this);
		    adView.setAdSize(AdSize.BANNER);
		    adView.setAdUnitId(AD_UNIT_ID);
		    LinearLayout layout = (LinearLayout) findViewById(R.id.mapViewLinearLayout);
		    layout.addView(adView);

// Create an ad request. Check logcat output for the hashed device ID to
// get test ads on a physical device.
		    AdRequest adRequest = new AdRequest.Builder()
//		    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//		    .addTestDevice("6BCE21BFA078CD23EAFFA49FE680B8D3")
		    .build();

		    // Start loading the ad in the background.
		    adView.loadAd(adRequest);
		    
		    
			Button loginButton = (Button) findViewById(R.id.btnLogin);
			try {
				if (userIntegrator.isLoggedIn(
						userIntegrator.getUsernameFromSharedPreferences(),
						userIntegrator.getEncryptedPasswordFromSharedPreferences())) {
					loginButton.setVisibility(Button.INVISIBLE);
				} else {
					loginButton.setVisibility(Button.VISIBLE);
				}

				// create progress bar for loading maps and truck data
				ProgressDialog mDialog = new ProgressDialog(this);
				mDialog.setMessage("Loading...");
				mDialog.setCancelable(false);
				mDialog.show();

				// execute new thread to get the data
				truckIntegrator.executeGetTruckDataAsyncTask(this);
				initilizeMap();

				// set pins for the truck
				setTruckPins(truckIntegrator.getTrucksWithLocations());

				// set pin for current location
				mMap.addMarker(
						new MarkerOptions()
								.position(currentLatLng)
								.title(getResources().getString(
										R.string.strCurrentLocationLbl))
								.icon(BitmapDescriptorFactory
										.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))
						.showInfoWindow();
				mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
						currentLatLng, 10));
				// once the truck data has been loaded & maps have been created,
				// cancel the loading bar.
				mDialog.cancel();

			} catch (Exception e) {
				e.printStackTrace();

			}}else{
				
				//show this if we cannot get the current location
				Toast.makeText(this, getResources().getString(R.string.strCannotDrawMaps), Toast.LENGTH_LONG).show();
			}
		} else {
//			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);
		
			userIntegrator = new UserIntegration(this);
			Button loginButton = (Button) findViewById(R.id.btnLogin);
			try {
				if (userIntegrator.isLoggedIn(
						userIntegrator.getUsernameFromSharedPreferences(),
						userIntegrator.getEncryptedPasswordFromSharedPreferences())) {
					loginButton.setVisibility(Button.INVISIBLE);
				} else {
					loginButton.setVisibility(Button.VISIBLE);
				}
			} catch (Exception e1) {
				Toast.makeText(this, getResources().getString(R.string.strGenericError), Toast.LENGTH_LONG).show();
			
			}

			//if gps is off, raise toast with new directions
			Toast.makeText(this,
					getResources().getString(R.string.strGPSNotEnabledError),
					Toast.LENGTH_LONG).show();
				
			// execute new thread to get the data
			try {
				truckIntegrator = new TruckIntegration(this);
				truckIntegrator.executeGetTruckDataAsyncTask(this);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			initilizeMap();

			// set pins for the truck
			try {
				setTruckPins(truckIntegrator.getTrucksWithLocations());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// set pin for current location

		}
//		super.onCreate(savedInstanceState);
	}

	/**
	 * Method to initilize the map
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void initilizeMap() {

		
        	mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        	mMap.setOnMarkerClickListener(this);
        	mMap.clear();
        	mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
				
				@Override
				public void onInfoWindowClick(Marker marker) {
					 //Just get directions to the truck. 
					 Uri location = Uri.parse("http://maps.google.com/maps?saddr="+currentLatLng.latitude+","+currentLatLng.longitude+"&daddr="+marker.getPosition().latitude+","+marker.getPosition().longitude);
					 //Toast.makeText(this, marker.getTitle(), Toast.LENGTH_SHORT).show();
					// marker.getPosition();
					 Intent navigation = new Intent(Intent.ACTION_VIEW, location); 
					 startActivity(navigation);
					
					
					
				}
			});
            if (mMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
        }
            
            


		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		mMap.setOnMarkerClickListener(this);
		mMap.clear();
		mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

			@Override
			public void onInfoWindowClick(Marker marker) {
				// Just get directions to the truck.
				Uri location = Uri.parse("http://maps.google.com/maps?saddr="
						+ currentLatLng.latitude + ","
						+ currentLatLng.longitude + "&daddr="
						+ marker.getPosition().latitude + ","
						+ marker.getPosition().longitude);
				// Toast.makeText(this, marker.getTitle(),
				// Toast.LENGTH_SHORT).show();
				// marker.getPosition();
				Intent navigation = new Intent(Intent.ACTION_VIEW, location);
				startActivity(navigation);
			}
		});
		if (mMap == null) {
			Toast.makeText(getApplicationContext(),
					"Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
		}

	}

	
	

	

	/**
	 * Method to set the pins in main map fragment
	 * 
	 * @param trucks
	 *            The trucks that we wish to make pins for
	 */
	public void setTruckPins(ArrayList<TruckOwner> trucks) {
		Location myLocation = locationManager.getLastKnownLocation(provider);

		if (trucks.isEmpty()) {
			Toast.makeText(this,
					getResources().getString(R.string.strNoTrucksFound),
					Toast.LENGTH_LONG).show();
		}
		for (int i = 0; i < trucks.size(); i++) {
			Location l = new Location(provider);

			TruckOwner truck = trucks.get(i);
			// get the leaving time of the truck
			Timestamp ts = new Timestamp(truck.getHoursAtLocation());
			Date date = new Date(ts.getTime());
			// get the current position of the user
			Double currentTruckLat = Double.parseDouble(truck.getLatitude());
			Double currentTruckLng = Double.parseDouble(truck.getLongitude());
			LatLng truckLatLng = new LatLng(currentTruckLat, currentTruckLng);

			// set the logitude and latitude ofteh user
			l.setLatitude(currentTruckLat);
			l.setLongitude(currentTruckLng);

			//// calculate the distance to the user
		//	BigDecimal a = new BigDecimal(myLocation.distanceTo(l) / 1600);
			//BigDecimal b = a.setScale(1, RoundingMode.UP); // =>
				String b = mapsIntegrator.distanceToPoint(truckLatLng);											// BigDecimal("1.23");
			// Create the title
			String title = truck.getTruckName();
			// create the marker
			mMap.addMarker(new MarkerOptions()
					.position(truckLatLng)
					.title(title
							+ ", "
							+ b
							+ ", "
							+ getResources().getString(
									R.string.strTruckLeavingTime) + " " + truckIntegrator.getHumanReadableTruckHours(truck.getHoursAtLocation()))
					.snippet(
							getResources().getString(R.string.strGetDirections)));
		}

	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		// show the truck info
		marker.showInfoWindow();
		return true;

	}

	/**
	 * This method is invoked when the Refresh Map button is clicked, because we
	 * set onRefreshClicked as the value of the OnClick attribute of the Submit
	 * Button.
	 * 
	 * @param v
	 */
	public void onRefreshClicked(View v) {
		//check to see if GPS is on before attempting to create a new GoogleMap
		if (locationManager.isProviderEnabled(provider) && myLocation != null) {
			
		initilizeMap();

		// set pins for the truck
		try {
			setTruckPins(truckIntegrator.getTrucksWithLocations());
			
		} catch (Exception e) {
			// Raise a toast
			Toast.makeText(this,
					this.getResources().getString(R.string.strCannotDrawMaps),
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		//add a pin for each truck
		mMap.addMarker(
				new MarkerOptions()
						.position(currentLatLng)
						.title(getResources().getString(
								R.string.strCurrentLocationLbl))
						.icon(BitmapDescriptorFactory
								.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))
				.showInfoWindow();

		// refresh the location
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				REFRESH_INTERVAL * MILLISECONDS_PER_SECOND, 0, this);
		// create new thread to get the remote data
		try {
			truckIntegrator.executeGetTruckDataAsyncTask(this);
		} catch (InterruptedException e) {
			Toast.makeText(this, "Threading Error!", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		}
		else{
			//we actually can initalize maps. We just can't reference the current location
			initilizeMap();
			Toast.makeText(this,
					getResources().getString(R.string.strGPSNotEnabledError),
					Toast.LENGTH_LONG).show();
		}
		
	}

	/**
	 * This method is invoked when the Login button is clicked, because we set
	 * onLogInClicked as the value of the OnClick attribute of the Rate Button.
	 * 
	 * @param v
	 */
	public void onLogInClicked(View v) {
		Intent locationAct = new Intent(this, LocationActivity.class);
		startActivity(locationAct);// start that activity.

	}

	@SuppressWarnings("unused")
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		if (connectionResult.hasResolution()) {
			try {
				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult(this,
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
			 * If no resolution is available, display a dialog to the user with
			 * the error.
			 */
			Toast.makeText(this, connectionResult.getErrorCode(),
					Toast.LENGTH_LONG).show();
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
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

}
