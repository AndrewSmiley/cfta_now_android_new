package com.cincyfoodtrucks.dao;

import java.util.ArrayList;
import java.util.List;

import com.cincyfoodtrucks.dto.TruckMenuItem;



public interface IMenuServiceSQLite {

	
	/**
	 * Method to write a new menu item to the database if it cannot be saved online
	 * @param menu The POJO object we wish to add
	 * @throws Exception Throws exception if there is a DB Error
	 */
	public  void addMenuItem(TruckMenuItem menu)
			throws Exception;

	/**
	 * Method to write multiple rows to the sqllite database. A good example would be when storing values from AWS to the
	 * local DB. 
	 * @param menus A list of objects we wish to write  
	 * @param db The databse we are writing to 
	 * @throws Exception
	 */
	public  void addRows(List<TruckMenuItem> menus)
			throws Exception;

	
	/**
	 * Method to get all of the rows from a particular database
	 * @param db The database we wish to connect to
	 * @return List<MenuItem> a list containing all menu items
	 * @throws Exception Throws exception to handle a db error
	 */
	public  ArrayList<TruckMenuItem> getAllMenuItems()
			throws Exception;

	/**
	 * Method to update a particular food
	 * @param foodName The name of the food to update
	 * @throws Exception Throws an exception to handle a databse error
	 */
	public  void updateFood(String foodName)
			throws Exception;

	
	/**
	 * Conceptual method to allow users to search the menus for a specific item or type of item
	 * @param criteria
	 * @return
	 */
	public ArrayList<TruckMenuItem> searchForFoods(String criteria);
	
	/**
	 * Method to get the menu items for a particular truck using its name
	 * @param truckID the name of the truck we're getting menus for 
	 * @return The menu items found for the truck
	 */
	public ArrayList<TruckMenuItem> getItemsByTruckID(int truckID);
	
	/**
	 * Method to get the last menu item id so we can input a new menu item offline
	 * @return int the last menu item id
	 */
	public int getLastMenuItemID();
	
	/**
	 * Method to get all the offline menu items that have been added
	 * @return
	 */
	public ArrayList<TruckMenuItem> getAllOfflineMenuItems();
	
}