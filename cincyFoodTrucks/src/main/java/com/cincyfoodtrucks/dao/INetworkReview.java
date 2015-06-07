package com.cincyfoodtrucks.dao;

import java.util.ArrayList;

import com.cincyfoodtrucks.dto.Review;

/**
 * Interface to outline methods to get review information back from the remote
 * datasource
 * 
 * @author Andrew Smiley
 * 
 */
public interface INetworkReview {

	/**
	 * Method to get all reviews from across the network
	 * 
	 * @return ArrayList<Review>
	 * @throws Exception > all the reviews in the database
	 * @throws throws Exception Exception
	 *             Throws an exception if an error occurs
	 */
	public ArrayList<Review> getAllReviews() throws Exception;

	/**
	 * Method to get all reviews of a particular truck
	 * 
	 * @param truckName
	 *            The id of the truck we wish to fetch reviews for
	 * @return ArrayList<Review> All of the reviews for a particular truck
	 * @throws Exception
	 *             Throws and exception if an error occurs
	 */
	public ArrayList<Review> getReviewsByTruckID(String truckID)
			throws Exception;

	/**
	 * Method to write a new review to the remote database
	 * 
	 * @param review
	 * @throws Exception
	 */
	public void createReview(Review review) throws Exception;

}
