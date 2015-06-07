package com.cincyfoodtrucks.integration;

import com.google.android.gms.maps.model.LatLng;

public interface IMapsIntegration {

	/**
	 * This is a method to calculate the distance from the user's current location
	 * to the end point (i.e the truck location)
	 * @param endP
	 * @return
	 */
	public String distanceToPoint(LatLng endP);
	
	/**
	 * Method to determine if the user's current location has been acquired
	 * @return boolean returns true if the location has been determined. False otherwise
	 */
	public boolean currentLocationAcquired(); 
	
	/**
	 * Method to determine the distance to a point and return a numeric representation of that number
	 * @param endP The endpoint we wish to calculate
	 * @return
	 */
	public double numericDistanceToPoint(LatLng endP);
		

}