package com.cincyfoodtrucks.dao;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import com.cincyfoodtrucks.dto.User;

public interface IUserServiceSQLite {

	
	
	/**
	 * Method to put all the users in the database from a network call. 
	 * @param users the users we've pulled back from the remote db
	 */
	public void addAllUsers(ArrayList<User> users) throws Exception;
	
	
	/**
	 * Method to determine if a user is logged in using their username and password
	 * @param username The username of the user
	 * @param password The password of the user
	 * @return boolean returns whether the action was successful or not 
	 * @throws NoSuchAlgorithmException, Exception 
	 */
	public boolean isLoggedIn(String username, String password) throws NoSuchAlgorithmException, Exception;
	
	/**
	 * Method to encrypt a user's password to compare against the database.Encryption is via 
	 * md5 hashing. Will cause a failure if the hashing is not md5, as passwords are stored in the database in that way 
	 * @param password The password of the user we wish to encrypt
	 * @return returns a string containing the encrypted hash
	 * @throws NoSuchAlgorithmException 
	 */
	public String encryptPassword(String password) throws NoSuchAlgorithmException;
	
	/**
	 * Method to get a user object from a username
	 * @param username The username we wish to search for
	 * @return The user found
	 * @throws Exception 
	 */
	public User getUserFromUsername(String username) throws Exception;
	
	/**
	 * Method to update a user's password locally
	 * @param user
	 */
	public void updatePassword(User user);
	
	/**
	 * Method to get all the offline user changes
	 * @return ArrayList<Users> the users who have updated their information offline
	 * @throws Exception 
	 */
	public ArrayList<User> getAllOfflineUsers() throws Exception;
	
}
