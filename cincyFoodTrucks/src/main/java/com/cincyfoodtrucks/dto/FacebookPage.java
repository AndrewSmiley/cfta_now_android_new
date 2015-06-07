package com.cincyfoodtrucks.dto;

public class FacebookPage {

	
	private int id;
	private String pageName;
	private int truckID;
	public String getPageName() {
		return pageName;
	}
	public void setPageName(String pageName) {
		this.pageName = pageName;
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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
