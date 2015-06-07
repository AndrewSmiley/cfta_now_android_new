package com.cincyfoodtrucks.dto;

public class TwitterUser {
private int id;
private String twitterUsername;
private int truckID;

/**
 * @return the id
 */
public int getId() {
	return id;
}
/**
 * @param id the id to set
 */
public void setId(int id) {
	this.id = id;
}
/**
 * @return the twitterUsername
 */
public String getTwitterUsername() {
	return twitterUsername;
}
/**
 * @param twitterUsername the twitterUsername to set
 */
public void setTwitterUsername(String twitterUsername) {
	this.twitterUsername = twitterUsername;
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

}
