package com.cincyfoodtrucks.dto;

import java.sql.Blob;

import android.graphics.Bitmap;
/**
 * This is a DTO class to represent the menu item images table
 * @author Smiley
 *
 */
public class MenuItemImage {
	//the id of the image
	private int imageID;
	//the content of the image
	private Bitmap imageContent;
	//the associated menu item id
	private int menuItemID;
	
	//we'll use this to store the sql blob data of the image 
	private Blob binaryData;

	/**
	 * @return the imageContent
	 */
	public Bitmap getImageContent() {
		return imageContent;
	}
	/**
	 * @param imageContent the imageContent to set
	 */
	public void setImageContent(Bitmap imageContent) {
		this.imageContent = imageContent;
	}
	/**
	 * @return the menuItemID
	 */
	public int getMenuItemID() {
		return menuItemID;
	}
	/**
	 * @param menuItemID the menuItemID to set
	 */
	public void setMenuItemID(int menuItemID) {
		this.menuItemID = menuItemID;
	}
	/**
	 * @return the imageID
	 */
	public int getImageID() {
		return imageID;
	}
	/**
	 * @param imageID the imageID to set
	 */
	public void setImageID(int imageID) {
		this.imageID = imageID;
	}
	public Blob getBinaryData() {
		return binaryData;
	}
	public void setBinaryData(Blob binaryData) {
		this.binaryData = binaryData;
	}
	
	
	

}
