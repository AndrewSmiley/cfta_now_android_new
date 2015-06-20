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
	public FacebookPage(int _id, String _pageName, int _truckID){
		id = _id;
		pageName = _pageName;
		truckID = _truckID;
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
