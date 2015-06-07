/**
 * 
 */
package com.cincyfoodtrucks.dto;

/**
 * Pojo class to represent review objects.
 * 
 * @author Andrew Smiley
 * 
 */

public class Review {

	/**
	 * 
	 */
	public Review() {
		// TODO Auto-generated constructor stub

	}

	private int reviewNumber;
	private String description;
	private int truckID;
	private int rating;
	private String reviewerName;
	private long timestamp;

	/**
	 * @return the reviewNumber
	 */

	public int getReviewNumber() {
		return reviewNumber;
	}

	/**
	 * @param reviewNumber
	 *            the reviewNumber to set
	 */
	public void setReviewNumber(int reviewNumber) {
		this.reviewNumber = reviewNumber;
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

	/**
	 * @return the rating
	 */

	public int getRating() {
		return rating;
	}

	/**
	 * @param rating
	 *            the rating to set
	 */
	public void setRating(int rating) {
		this.rating = rating;
	}

	/**
	 * @return the reviewerName
	 */

	public String getReviewerName() {
		return reviewerName;
	}

	/**
	 * @param reviewerName
	 *            the reviewerName to set
	 */
	public void setReviewerName(String reviewerName) {
		this.reviewerName = reviewerName;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

}
