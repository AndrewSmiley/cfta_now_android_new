package com.cincyfoodtrucks.dto;

/**
 * Pojo class to represent the trucks themselves
 * 
 * @author Andrew Smiley
 * 
 */
public class TruckOwner {

	public TruckOwner() {

	}

	// the unique id of the truck in the database
	private int truckID;

	// the name of the truck
	private String truckName;


	//The longitude of the truck in its current location
	private String longitude;
	
	//The latitude of the truck in its current location
	private String latitude;

	private long hoursAtLocation;
	private long offlineTimestamp;

	/**
	 * @return the trurckID
	 */

	public int getTruckID() {
		return truckID;
	}

	/**
	 * @param truckID
	 *            the truckID to set
	 */
	public void setTruckID(int truckID) {
		this.truckID = truckID;
	}

	/**
	 * @return the truckName
	 */

	public String getTruckName() {
		return truckName;
	}

	/**
	 * @param truckName
	 *            the truckName to set
	 */
	public void setTruckName(String truckName) {
		this.truckName = truckName;
	}


	


	/**
	 * Get the hours at a location
	 * 
	 * @return int The number of hours at a location
	 */
	public long getHoursAtLocation() {
		return hoursAtLocation;
	}

	/**
	 * Set the number of hours at a location
	 * 
	 * @param hoursAtLocation
	 *            The number of hours to be at that location
	 */
	public void setHoursAtLocation(long hoursAtLocation) {
		this.hoursAtLocation = hoursAtLocation;
	}

	/**
	 * @return the longitude
	 */
	public String getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the latitude
	 */
	public String getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public long getOfflineTimestamp() {
		return offlineTimestamp;
	}

	public void setOfflineTimestamp(long offlineTimestamp) {
		this.offlineTimestamp = offlineTimestamp;
	}

}
