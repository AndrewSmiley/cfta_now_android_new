package com.cincyfoodtrucks.integration;

import java.security.NoSuchAlgorithmException;

import android.widget.Button;

import com.cincyfoodtrucks.dto.User;

public interface IUserIntegration {

	/** Method that hides buttons if not logged in, shows them if the user is logged in
	 * 
	 * @param isLoggedIn Whether the user is logged in or not
	 * @param btn the button we wish to hide/show
	 * @throws Exception 
	 * @throws NoSuchAlgorithmException 
	 */
	public  void setButtonViewByLogin(Button btn)
			throws NoSuchAlgorithmException, Exception;

	/**
	 * Method to get the username from shared preferences
	 * @return username the username from preferences
	 */
	public  String getUsernameFromSharedPreferences();

	/**
	 * Method to get an encrpted password from the shared preferences and encrypt it
	 * @return
	 * @throws Exception 
	 */
	public  String getEncryptedPasswordFromSharedPreferences() throws Exception;

	/**
	 * Method to execute a new async task thread to refresh user data
	 * @throws InterruptedException 
	 */
	public  void executeGetUserData() throws InterruptedException;
	
	/**
	 * Conveneince method to see if a user is logged in using the integration tier
	 * @param username the username of the truck owner
	 * @param password the password of the truck owner
	 * @return
	 */
	public boolean isLoggedIn(String username, String password);
	
	/**
	 * Method to update the current logged in username and password
	 * @param username the username we wish to add
	 * @param Password the password we wish to add
	 */
	public void updateUsernameAndPasswordInPrefs(String username, String password);
	
	/**
	 * Convenience method to get a user object based upon a username
	 * @param username The username we wish to search for 
	 * @return User the user found
	 */
	public User getUserFromUsername(String username) ;
	
	/**
	 * Method to get a user from the username stored in shared preferences
	 * @return User the user found
	 */
	public User getUserFromSharedPrefs();
	
	/**
	 * Method to create a new thread and send updated user data to the remote server
	 * @param user the user we wish to update
	 * @throws InterruptedException
	 */
	public void executeSendUserData(User user) throws InterruptedException;
	
	/**
	 * Method to get a user from the sqlite db 
	 * @return User the user found
	 * @throws Exception 
	 */
	public User getUserFromSharedPreferences() throws Exception;
	
	/**
	 * Convenience method to encrypt a user's password
	 * @param password
	 * @return String the encrypted string password
	 * @throws Exception 
	 */
	public String encryptPassword(String password) throws Exception;
	
	/**
	 * Method to get a plain-text password from the shared preferences
	 * @return String the password returned
	 */
	public String getPasswordFromSharedPrefs();
	
	public void clearUsernameAndPasswordFromSharedPrefs();

}