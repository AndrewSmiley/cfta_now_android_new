package com.cincyfoodtrucks.dao;

import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;

import com.cincyfoodtrucks.dto.Review;

/**
 * Class to handle communication between the application and remote data source
 * regarding reviews
 * 
 * @author Andrew Smiley
 * 
 */
public class NetworkReviewDAO implements INetworkReview {
	INetworkDAO networkDAO;

	/**
	 * Constructor for the class. Instantiates a new NetworkDAO object.
	 */
	public NetworkReviewDAO() {
		networkDAO = new NetworkDAO();
	}

	@Override
	public ArrayList<Review> getAllReviews() throws Exception {
		ArrayList<Review> reviews = new ArrayList<Review>();
		String uri = "http://cincyfoodtruckapp.com/mobile_request_handler.php?action=get_all_reviews";

		// Get the response from the website
		String response = networkDAO.sendHTTPGetRequest(uri);

		// loop over the result set and add the results to the arraylist
		String[] lines = response.split("\r\n");
		for (String line : lines) {
			String[] reviewInfo = line.split(";");
			// make sure we're getting valid data here
			if (reviewInfo.length > 3) {
				Review review = new Review();
				review.setReviewNumber(Integer.parseInt(reviewInfo[0]));
				review.setReviewerName(reviewInfo[4]);
				review.setDescription(reviewInfo[1]);
				review.setTruckID(Integer.parseInt(reviewInfo[2]));
				review.setRating(Integer.parseInt(reviewInfo[3]));
				reviews.add(review);

			}

		}
		return reviews;
	}

	@Override
	public ArrayList<Review> getReviewsByTruckID(String truckID)
			throws Exception {
		ArrayList<Review> reviews = new ArrayList<Review>();
		String uri = "http://cincyfoodtruckapp.com/mobile_request_handler.php?action=get_all_reviews&truck_id="
				+ truckID;

		// Get the response from the website
		String response = networkDAO.sendHTTPGetRequest(uri);
		String[] lines = response.split("\r\n");
		for (String line : lines) {
			String[] reviewInfo = line.split(";");
			if (reviewInfo.length > 3) {
				Review review = new Review();
				review.setReviewNumber(Integer.parseInt(reviewInfo[0]));
				review.setReviewerName(reviewInfo[1]);
				review.setDescription(reviewInfo[2]);
				review.setTruckID(Integer.parseInt(reviewInfo[3]));
				review.setRating(Integer.parseInt(reviewInfo[4]));
				reviews.add(review);

			}

		}
		return reviews;
	}

	@Override
	public void createReview(Review review) throws Exception {
		String query = "http://cincyfoodtruckapp.com/mobile_request_handler.php?action=create_review&reviewer_name="
				+ review.getReviewerName()
				+ "&description="
				+ review.getDescription()
				+ "&truck_id="
				+ review.getTruckID()
				+ "&rating=" + review.getRating();
		URI uri = new URI(query.replace(' ', '+'));
		
		String response = networkDAO.sendHTTPGetRequest(uri.toString());

		// See if the site threw an error.
		// if it did, throw an exception.
		if (response.contains("ERROR")) {
			throw new Exception();

		}
	}

}
