package com.cincyfoodtrucks.integration;

import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;

import com.cincyfoodtrucks.dto.Review;
import com.cincyfoodtrucks.dto.TruckOwner;

public interface ITruckIntegration {

	/**
	 * Convenience method to get a truck id by truck name
	 * @param truckName The name of the Truck we wish to get the id of 
	 * @return The id of the truck
	 * @throws Exception throws an exception if an error occurs on the Data tier
	 */
	public  int getTruckIDByTruckName(String truckName)
			throws Exception;

	/**
	 * Method to get the current truck using shared preferences
	 * @return
	 * @throws Exception
	 */
	public  TruckOwner getTruckUsingSharedPrefs() throws Exception;

	/**Method to create an offline online review
	 * 
	 * @param review
	 */
	public  void createRatingOnlineOffline(Review review);

	/**
	 * Method to get all trucks from the sqlite db and sort through the ones that don't have a current location
	 * @return The trucks that have location
	 * @throws Exception
	 */
	public  ArrayList<TruckOwner> getTrucksWithLocations()
	
	
			throws Exception;
	
	
	/**
	 * Method to execute a new thread to update a truck's data
	 * @param context The calling context
	 * @param longitude The longitude of the truck
	 * @param latitude The latitude of the truck
	 * @param timestamp The timestamp for when the truck will leave its current location
	 * @param truckID The id of the truck
	 */
	public void executeSendTruckDataAsyncTask(Context context, String longitude, String latitude, String timestamp, int truckID);
	
	/**
	 * Method to execute a new async task thread
	 * @param context
	 * @throws InterruptedException 
	 */
	public void executeGetTruckDataAsyncTask(Context context) throws InterruptedException;
	
	/**
	 * Method to update a truck's location
	 * @param longitude the longitude of the current location
	 * @param latitude the latitude of the current location
	 * @param truckID the id of the truck
	 */
	public void updateLocation(String longitude, String latitude, int truckID);
	
	/**
	 * Method to update a truck's time at a location
	 * @param timestamp the timestamp (millisecs) of the time the truck will leave
	 * @param truckID the id of the truck
	 */
	public void updateTimeAtLocation(String timestamp, int truckID);
	
	/**
	 * Convienience method to conver a timestamp to a human readable date 
	 * @param timestamp the timestamp of the truck
	 * @return Date the HR date
	 */
	public Date convertTimestampToDate(long timestamp);
	
	/**
	 * Method to update the current truck name in shared preferences
	 * @param truckName The name of the current truck
	 */
	public void updateTruckInSharedPrefs(String truckName);
	
	/**
	 * Method to calculate the average rating of a truck
	 * @param truck
	 * @return
	 * @throws Exception 
	 */
	public int calculateAverageRating(TruckOwner truck) throws Exception;
	
	/**
	 * Convenience method to get a truck owner by truck id 
	 * @param truckID The truck ID we wish to search for
	 * @return
	 */
	public TruckOwner getTruckByTruckID(int truckID);
	
	/**
	 * Method to get the human readable hours of the truck
	 * @param truck The truck we wish to get the human readable hours for
	 * @return String a string containing the converted human readable hours
	 */
	public String getHumanReadableTruckHours(TruckOwner truck);
	

}
