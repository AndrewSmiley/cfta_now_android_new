package com.cincyfoodtrucks.dao;

import java.util.ArrayList;

import android.database.sqlite.SQLiteDatabase;

import com.cincyfoodtrucks.dto.Review;

public interface IReviewServiceSQLite {

	
	/**
	 * Method to write
	 * 
	 * @param review
	 *            The POJO object we wish to add
	 * @throws Exception
	 *             Throws exception if there is a DB Error
	 */
	public void addReview(Review review) throws Exception;

	/**
	 * Method to write multiple rows to the sqllite database. A good example
	 * would be when storing values from AWS to the local DB.
	 * 
	 * @param reviews
	 *            A list of objects we wish to write
	 * @param db
	 *            The databse we are writing to
	 * @throws Exception
	 */
	public void addReviews(ArrayList<Review> reviews)
			throws Exception;

	/**
	 * Method to get the reviews for a particular truck id
	 * @param truckID
	 * @return
	 * @throws Exception 
	 */
	public ArrayList<Review> getReviewsByTruckID(int truckID) throws Exception;
	
	/**
	 * Method to get all of the rows from a particular database
	 * 
	 * @param db
	 *            The database we wish to connect to
	 * @return ArrayList<ArrayList<Object>> An Array list of each arraylist
	 *         representing an individual row
	 * @throws Exception
	 *             Throws exception to handle a db error
	 */
	public ArrayList<Review> getAllReviews()
			throws Exception;

	/**
	 * Method to update the rows of
	 * 
	 * @param rowID
	 * @param db
	 *            The database we wish to connect to
	 * @throws Exception
	 *             Throws an exception to handle a databse error
	 */
	public void updateRow(long rowID, SQLiteDatabase db) throws Exception;
	
	/**
	 * Method to get all the reviews that have a timestamp (i.e have been saved offline)
	 * @return
	 */
	public ArrayList<Review> getOfflineReviews();

	/**
	 * Method to get the last review number from the database
	 * @return
	 */
	public int getLastReviewNumber();
	
	/**
	 * Method to delete the old offline reviews if they are successfully added to the remote db
	 * @param reviewID the id of the review we have uploaded
	 */
	public void deleteOfflineReview(int reviewID);
	
}