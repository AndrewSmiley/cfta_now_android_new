package com.cincyfoodtrucks.dao;

import java.util.ArrayList;

import com.cincyfoodtrucks.dto.User;

public interface INetworkUser {

	/**
	 * Method to get all users back from the database
	 * @throws Exception
	 * @return List<User> A list of all user objects
	 */
	public ArrayList<User> getAllUsers() throws Exception;
	
	
	/**
	 * Method to update a truck's login password
	 * 
	 * @param userID the userID of the user whose password we are updating
	 * @param newPW their new password
	 * @throws Exception
	 *             Exception is thrown if there is an issue communicating over
	 *             the network or writing to the database
	 */

	public void updatePassword(int userID, String newPW)
			throws Exception;

}

