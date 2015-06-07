package com.cincyfoodtrucks.dao;

import java.io.IOException;
import java.util.ArrayList;

import com.cincyfoodtrucks.dto.TruckOwner;

/**
 * Interface for retrieving truck information from the network
 * 
 * @author Andrew Smiley
 * 
 */
public interface INetworkTruck {

	/**
	 * Method to get all trucks from across the network.
	 * 
	 * @return An arraylist containing all truck objects
	 * @throws IOException
	 * @throws Exception
	 */
	public ArrayList<TruckOwner> getAllTrucks() throws IOException, Exception;

	/**
	 * Method to get an individual truck from the database by name
	 * 
	 * @param owner
	 *            The name of the truck we wish to find
	 * @return
	 * @throws Exception
	 * @throws IOException
	 */

	public ArrayList<TruckOwner> getTruckByName(String truck)
			throws IOException, Exception;

	
	/**
	 * Method to update a truck's location
	 * 
	 * @param location
	 *            The new location of the truck
	 * @throws Exception
	 *             Exception is thrown if there is an issue communicating over
	 *             the network or writing to the database
	 */

	public void updateLocation(String longitude, String latitude, int truckID) throws Exception;

	/**
	 * Method to update the number of hours a truck is at a location
	 * 
	 * @param hours
	 *            The number of hours a truck will be at a location
	 * @throws Exception
	 *             Exception is thrown if there is an issue communicating over
	 *             the network or writing to the database
	 */
	public void updateTimeAtLocation(String hours, int truckID) throws Exception;
}
	