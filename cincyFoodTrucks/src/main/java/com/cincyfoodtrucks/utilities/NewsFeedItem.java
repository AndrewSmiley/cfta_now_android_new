package com.cincyfoodtrucks.utilities;

import java.util.Date;

import com.cincyfoodtrucks.dto.TruckOwner;
/**
 * Just a pojo class to contain the items we want to display in the newsfeed
 * @author Andrew
 *
 */
public class NewsFeedItem {
	private TruckOwner truck;
	private  String content;
	private Date date;
	public NewsFeedItem(){

	}

	public NewsFeedItem(TruckOwner _owner, String _content, Date _date){
		truck = _owner;
		content = _content;
		date = _date;
	}
	/**
	 * @return the truck
	 */
	public TruckOwner getTruck() {
		return truck;
	}
	/**
	 * @param truck the truck to set
	 */
	public void setTruck(TruckOwner truck) {
		this.truck = truck;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	
}
