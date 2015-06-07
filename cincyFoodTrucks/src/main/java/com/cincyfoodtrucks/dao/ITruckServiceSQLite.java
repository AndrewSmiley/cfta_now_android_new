package com.cincyfoodtrucks.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cincyfoodtrucks.dto.Review;
import com.cincyfoodtrucks.dto.TruckOwner;

public interface ITruckServiceSQLite {

	
	/**
	 * Method to write
	 * 
	 * @throws Exception
	 *             Throws exception if there is a DB Error
	 */
	public void addRow(TruckOwner truckOwner)
			throws Exception;

	/**
	 * Method to write multiple rows to the sqllite database. A good example
	 * would be when storing values from AWS to the local DB.
	 * 
	 * @param trucks
	 *            A list of objects we wish to write

	 * @throws Exception
	 */
	public void addRows(List<TruckOwner> trucks)
			throws Exception;


	/**
	 * Method to retrieve a single row of a database
	 * 
	 * @param truckName
	 *            The name of the truck we wish to retrieve
	 * @param db
	 *            The database we wish to connect to
	 * @throws Exception
	 *             Throws exception to handle a db error
	 * @return TruckOwner the truck found
	 */
	public TruckOwner getTruckByName(String truckName) throws Exception;

	/**
	 * Method to get all of the rows from a particular database
	 * 

	 * @return ArrayList<ArrayList<Object>> An Array list of each arraylist
	 *         representing an individual row
	 * @throws Exception
	 *             Throws exception to handle a db error
	 */
	public ArrayList<TruckOwner> getAllRows() throws Exception;
	
	
	/**
	 * Method to update a truck location if they are not connected to the network
	 * @param truckowner the truck we need to update the offline location of
	 * @throws Exception
	 */
	public void updateTruckLocationOffline(TruckOwner truck) throws Exception;
	
	/**
	 * Method to get all the truck locations that have a timestamp (i.e have been saved offline)
	 * @return
	 */
	public ArrayList<TruckOwner> getOfflineTrucks();
	
	/**
	 * Method to get a truck using the truck id
	 * @param truckID the id of the truck
	 * @return TruckOwner the truck found
	 */
	public TruckOwner getTruckByTruckID(int truckID);

	/**
	 * Method to get trucks back out of the db and store them in a hashmap
	 * @return HashMap<String, TruckOwner> The hashmap containing the integer key, which is the truck ID and a truck object 
	 */
	public HashMap<Integer, TruckOwner> getTrucksAsHashMap();
	
}

	