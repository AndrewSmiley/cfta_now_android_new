package com.cincyfoodtrucks.integration;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.Date;

import android.content.Context;
import android.content.IntentSender;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.cincyfoodtrucks.R;
import com.cincyfoodtrucks.dto.TruckOwner;
import com.cincyfoodtrucks.ui.MainActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

public class MapsIntegration implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener, IMapsIntegration {
	//private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	
	private GoogleMap mMap;
	  Button btnDismiss ;
	
	ITruckIntegration truckIntegrator;
	IUserIntegration userIntegrator;
	LocationManager locationManager;
	String provider;
	
	Location myLocation;
	LatLng currentLatLng;
	Context context;
	
		public MapsIntegration(Context context, LocationManager lm)
	{
		
		
		locationManager = lm;
		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);
		this.context = context;
		myLocation = locationManager.getLastKnownLocation(provider);
		
		
		
		
		userIntegrator = new UserIntegration(context);
		
		truckIntegrator = new TruckIntegration(context);
		}
		
		
		/* (non-Javadoc)
		 * @see com.cincyfoodtrucks.integration.IMapsIntegration#distanceToPoint(com.google.android.gms.maps.model.LatLng)
		 */
	@Override
	public String distanceToPoint(LatLng endP) {
		if(myLocation != null)
		{
			
		Location l = new Location(provider);
		
		
		
		
		//set the logitude and latitude ofteh user
		l.setLatitude(endP.latitude); 
		l.setLongitude(endP.longitude);
		
		//calculate the distance to the user
		BigDecimal a = new BigDecimal(myLocation.distanceTo(l)/1600);
		BigDecimal b = a.setScale(1, RoundingMode.UP); // => BigDecimal("1.23")
		return b.toString() + " Miles";
		}else
		{
			return context.getResources().getString(R.string.strDistanceUnavailable);
		}
	}
	
	@Override
	public boolean currentLocationAcquired() {
		//just determine if the current location has been established or not
		boolean result = (myLocation == null) ? false : true;
		return result;
	}


	@SuppressWarnings("unused")
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		// TODO Auto-generated method stub
		
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
	public double numericDistanceToPoint(LatLng endP) {
		if(myLocation != null)
		{
		Location l = new Location(provider);
		
		
		
		
		//set the logitude and latitude ofteh user
		l.setLatitude(endP.latitude); 
		l.setLongitude(endP.longitude);
		
		//calculate the distance to the user
		BigDecimal a = new BigDecimal(myLocation.distanceTo(l)/1600);
		BigDecimal b = a.setScale(1, RoundingMode.UP); // => BigDecimal("1.23")
		return Double.parseDouble(b.toString());
		
	}
		else
		{ 
			return 0;
		}
	}


}
