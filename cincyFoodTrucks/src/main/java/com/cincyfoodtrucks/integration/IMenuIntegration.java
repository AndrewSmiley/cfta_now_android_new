package com.cincyfoodtrucks.integration;

import java.util.ArrayList;

import android.content.res.Resources.NotFoundException;

import com.cincyfoodtrucks.dto.MenuItemImage;
import com.cincyfoodtrucks.dto.TruckMenuItem;

public interface IMenuIntegration {

	/**
	 * Method to get all menu items for a truck using the values currently stored in shared preferences
	 * @return ArrayList<TruckMenuItem> the items the truck has added to its menu
	 * @throws NotFoundException
	 * @throws Exception
	 */
	public  ArrayList<TruckMenuItem> getTruckMenuItemsBySharedPreferences()
			throws NotFoundException, Exception;

	/**
	 * Method to execute a new async task to update datbase information
	 * @throws InterruptedException 
	 */
	public  void executeGetTruckMenuTask() throws InterruptedException;

	/**
	 * Method to send new truck menu data to the remote server
	 * @throws InterruptedException
	 */
	public void executeSendTruckMenuTask(TruckMenuItem item) throws InterruptedException;
	
	/**
	 * Convenience function to search for food items
	 * @param criteria The search criteria
	 * @return ArrayList the items found
	 */
	public ArrayList<TruckMenuItem> searchForFoods(String criteria);
	
	/**
	 * Method to update the search criteria in the shared preferences so we can search in one activity and load the results in another
	 * @param criteria The search criteria
	 */
	public void updateSearchItemInSharedPrefs(String criteria);
	
	/**
	 * Method to clear the current search criteria in the shared preferences
	 */
	public void clearSearchItemInSharedPrefs();
	
	/**
	 * Method to get the menu item search criteria from the shared preferences
	 * @return String the search criteria found
	 */
	public String getMenuItemSearchCriteriaFromSharedPrefs();
}