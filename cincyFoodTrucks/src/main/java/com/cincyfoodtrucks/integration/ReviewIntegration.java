package com.cincyfoodtrucks.integration;

import java.net.UnknownHostException;
import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.cincyfoodtrucks.dao.INetworkReview;
import com.cincyfoodtrucks.dao.IReviewServiceSQLite;
import com.cincyfoodtrucks.dao.ITruckServiceSQLite;
import com.cincyfoodtrucks.dao.NetworkReviewDAO;
import com.cincyfoodtrucks.dao.ReviewServiceSQLiteStub;
import com.cincyfoodtrucks.dao.TruckServiceSQLiteStub;
import com.cincyfoodtrucks.dto.Review;
import com.cincyfoodtrucks.dto.TruckOwner;

public class ReviewIntegration implements IReviewIntegration {
	SharedPreferences preferences;
	ITruckServiceSQLite truckServiceSQLite;
	IReviewServiceSQLite reviewServiceSQLite;
	INetworkReview networkReview;
	Context context;
	ITruckIntegration truckIntegration;

	/**
	 * Constructor for the class.
	 * 
	 * @param context
	 *            The context calling the integration class.
	 */
	public ReviewIntegration(Context context) {
		reviewServiceSQLite = new ReviewServiceSQLiteStub(context);
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
		truckServiceSQLite = new TruckServiceSQLiteStub(context);
		networkReview = new NetworkReviewDAO();
		truckIntegration = new TruckIntegration(context);
		this.context = context;
	}

	
	/* (non-Javadoc)
	 * @see com.cincyfoodtrucks.integration.IReviewIntegration#createReviewOnlineOffline(com.cincyfoodtrucks.dto.Review)
	 */
	@Override
	public void createReviewOnlineOffline(Review review) throws InterruptedException {
		SendReviewData srd = new SendReviewData(context, review);
		Thread t = new Thread(srd);
		t.start();
		t.join();
	}

	/* (non-Javadoc)
	 * @see com.cincyfoodtrucks.integration.IReviewIntegration#executeGetReviewData(android.content.Context)
	 */
	@Override
	public void executeGetReviewData(Context context)
			throws InterruptedException {
		GetReviewData grd = new GetReviewData(context);
		Thread t = new Thread(grd);
		t.start();

		t.join();

	}
	 
	/* (non-Javadoc)
	 * @see com.cincyfoodtrucks.integration.IReviewIntegration#getReviewsBySharedPrefs()
	 */
	@Override
	public ArrayList<Review> getReviewsBySharedPrefs() throws Exception
	{
		TruckOwner truckUsingSharedPrefs = truckIntegration.getTruckUsingSharedPrefs();
		ArrayList<Review> reviews = reviewServiceSQLite.getReviewsByTruckID(truckUsingSharedPrefs.getTruckID());
		return reviews;
		
		
	}

	/**
	 * Private class to get the review data back from the remote sever
	 * 
	 * @author Smiley
	 * 
	 */
	private class GetReviewData implements Runnable {
		Context context;
		NetworkReviewDAO networkReviewAccessor;
		IReviewServiceSQLite reviewServiceSQLite;

		private GetReviewData(Context context) {
			this.context = context;
		}

		@Override
		public void run() {
			
			//lets get the old reviews first
			networkReviewAccessor = new NetworkReviewDAO();
			

			reviewServiceSQLite = new ReviewServiceSQLiteStub(context);

			try {
				ArrayList<Review> reviews = networkReviewAccessor
						.getAllReviews();
				//if no reviews were returned, don't try to get the offline reviews
				if (reviews.isEmpty()) {
					throw new Exception();
				}
				// try to insert trucks into the database
				reviewServiceSQLite.addReviews(reviews);

				//get any reviews written offline
				ArrayList<Review> offlineReviews = reviewServiceSQLite.getOfflineReviews();
				//if it's not empty..
				if(!offlineReviews.isEmpty()){
					
					//loop over each review
					for (Review review : offlineReviews) {
						
						//attempt to create it online
						try {
							networkReviewAccessor.createReview(review);
						} catch (Exception e) {
							//don't worry about it here. they data is still stored locally
							e.printStackTrace();
						}
						
						//if successful, delete the old record
						reviewServiceSQLite.deleteOfflineReview(review.getReviewNumber());
					}
					
				}
			}catch(UnknownHostException he){
			
				he.printStackTrace();
			} catch (Exception ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}

		}

	}
	
	/**
	 * Private inner class to create new thread and send network review data/update local data
	 * @author Andrew Smiley
	 *
	 */
	private class SendReviewData implements Runnable {
		Context context;
		Review review;
		INetworkReview networkReviewAccessor;
		IReviewServiceSQLite sqlReviewSevice;
		
		
		
		private SendReviewData(Context context, Review review){
			this.context = context;
			this.review = review;
			this.networkReviewAccessor = new NetworkReviewDAO();
			this.sqlReviewSevice = new ReviewServiceSQLiteStub(this.context);
			
		}
		
		
		@Override
		public void run(){
			try {
				

				
				//attempt to create the new review
 				networkReviewAccessor.createReview(review);
				
				
			}catch(Exception e){
				//just write the review offline if the network call fails
				try {
					sqlReviewSevice.addReview(review);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
			
			try{
					
				
			
					//get the reviews back from the remote server
					ArrayList<Review> reviews = networkReviewAccessor.getAllReviews();
					
					if(reviews.isEmpty())
					{
						throw new Exception();
					}
				
				//add the data back into the local DB
				sqlReviewSevice.addReviews(reviews);
				
			
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			
			
			
		}
		
	}
}
