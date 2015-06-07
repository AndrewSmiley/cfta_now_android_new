package com.cincyfoodtrucks.dao;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;

import com.cincyfoodtrucks.dto.TruckMenuItem;

/**
 * Interface to outline handling CRUD operations over the network
 * 
 * @author pridemai
 * 
 */
public interface INetworkMenu {

	/**
	 * Method to get all menu items
	 * 
	 * @return ArrayList<MenuItem> a collection of the menu items that are
	 *         retrieved
	 * @throws IOException
	 * @throws ConnectException
	 */
	public ArrayList<TruckMenuItem> getAllItems() throws ConnectException,
			IOException;

	/**
	 * Method to update a menu item
	 * 
	 * @param item
	 *            the item to update
	 * @throws Exception
	 *             Exception is thrown if there is an issue communicating over
	 *             the network or writing to the database
	 */
	public void updateMenuItem(TruckMenuItem item) throws Exception;

	/**
	 * Method to add a menu item to the database
	 * 
	 * @param item
	 *            The item to add
	 * @throws Exception
	 *             Exception is thrown if there is an issue communicating over
	 *             the network or writing to the database
	 */
	public boolean addMenuItem(TruckMenuItem item) throws Exception;

	/**
	 * Method to delete a menu item from the database
	 * 
	 * @param item
	 *            The item to delete from the menu
	 * @throws IOException 
	 * @throws ConnectException 
	 * @throws Exception
	 *             Exception is thrown if there is an issue communicating over
	 *             the network or writing to the database
	 */
	public boolean deleteMenuItem(TruckMenuItem item) throws ConnectException, IOException;

}
