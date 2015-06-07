package com.cincyfoodtrucks.dto;

/**
 * Pojo class to reference menu items
 * 
 * @author Andrew Smiley
 * 
 */
public class TruckMenuItem {

	public TruckMenuItem() {
		// TODO Auto-generated constructor stub
	}

	private int menuItemID;
	private String description;
	private String price;
	private String title;
	private int truckID;
	private long offlineTimestamp;

	/**
	 * @return the menuID
	 */

	public int getmenuItemID() {
		return menuItemID;
	}

	/**
	 * @param menuID
	 *            the menuID to set
	 */
	public void setMenuItemID(int menuItemID) {
		this.menuItemID = menuItemID;
	}

	/**
	 * @return the description
	 */

	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the price
	 */

	public String getPrice() {
		return price;
	}

	/**
	 * @param price
	 *            the price to set
	 */
	public void setPrice(String price) {
		this.price = price;
	}

	/**
	 * @return the title
	 */

	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the truckID
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

	public long getOfflineTimestamp() {
		return offlineTimestamp;
	}

	public void setOfflineTimestamp(long offlineTimestamp) {
		this.offlineTimestamp = offlineTimestamp;
	}

}
