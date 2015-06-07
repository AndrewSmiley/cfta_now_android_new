package com.cincyfoodtrucks.dto;

/**
 * Class to represent user logins now 
 * @author pridemai
 *
 */
public class User {
	
	private int userID;
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private int truckID;
	private long offlineTimestamp;
	/**
	 * @return the userID
	 */
	public int getUserID() {
		return userID;
	}
	/**
	 * @param userID the userID to set
	 */
	public void setUserID(int userID) {
		this.userID = userID;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the truckID
	 */
	public int getTruckID() {
		return truckID;
	}
	/**
	 * @param truckID the truckID to set
	 */
	public void setTruckID(int truckID) {
		this.truckID = truckID;
	}
	public long getOfflineTimestamp() {
		return offlineTimestamp;
	}
	public void setOfflineTimestamp(long offlineTimestamp) {
		this.offlineTimestamp = offlineTimestamp;
	}
	
	//define properties

}
