package com.cincyfoodtrucks.integration;

import java.util.ArrayList;

import android.content.Context;

import com.cincyfoodtrucks.dto.Review;

public interface IReviewIntegration {

	public abstract void createReviewOnlineOffline(Review review)
			throws InterruptedException;

	public abstract void executeGetReviewData(Context context)
			throws InterruptedException;

	/**
	 * Method to get truck reviews using the truck name stored in shared preferences
	 * @return
	 * @throws Exception 
	 */
	public abstract ArrayList<Review> getReviewsBySharedPrefs()
			throws Exception;

}