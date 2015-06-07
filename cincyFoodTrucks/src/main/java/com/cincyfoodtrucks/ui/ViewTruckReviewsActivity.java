package com.cincyfoodtrucks.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cincyfoodtrucks.R;
import com.cincyfoodtrucks.dto.Review;
import com.cincyfoodtrucks.integration.IReviewIntegration;
import com.cincyfoodtrucks.integration.ReviewIntegration;

public class ViewTruckReviewsActivity extends BaseActionMenuActivity {
	ListView lv;
	ArrayList<ReviewListItem> reviewListItems;
	IReviewIntegration reviewIntegration;
	ReviewListView adapter;
	String provider;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//instanciate
		reviewIntegration = new ReviewIntegration(this);
		
		
		reviewListItems =  new ArrayList<ReviewListItem>();
		setContentView(R.layout.activity_view_truck_reviews);
		lv = (ListView)findViewById(R.id.reviewListListView);
		try {
			//try to get the remote data
			reviewIntegration.executeGetReviewData(this);
			//add each review to the arraylist of ReviewListItems
			ArrayList<Review> reviewsArray = reviewIntegration.getReviewsBySharedPrefs();
			
			//Let the user know if there are no reviews for this truck
			if(reviewsArray.size() == 0)
			{
				Toast.makeText(this, getResources().getText(R.string.strNoReviews), Toast.LENGTH_LONG).show();
			}
			
			//add them to our arraylist of reviewlist items
			for (Review review : reviewsArray) {
				reviewListItems.add(new ReviewListItem(review));
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			Toast.makeText(this, getResources().getString(R.string.strNoReviews), Toast.LENGTH_LONG).show();
		}
		adapter = new ReviewListView(this, reviewListItems);
		lv.setAdapter(adapter);
		
	}

	
	
	/**
	 * Method to process actions for when the create new review is clicked
	 */
	public void onNewReviewClicked(View v){
		Intent reviewIntent = new Intent(this, RateActivity.class);
		startActivity(reviewIntent);
		
	}
	
	/**
	 * Class to pass hold review items and pass them to the array adapter
	 * @author Andrew Smiley
	 *
	 */
	 class ReviewListItem{
		
		Review review;
		 ReviewListItem(Review newReview) {
			this.review = newReview;
		}
	}
	
	
	/**
	 * Private inner ArrayAdapter class to show the review data for a particular truck
	 * @author Andrew Smiley
	 *
	 */
private class ReviewListView extends BaseAdapter{

	
	LayoutInflater inflater;
	ArrayList<ReviewListItem> reviews;
	Context context;
	
    public ReviewListView(Context context, ArrayList<ReviewListItem> reviews) {  
        super();
		this.context= context;
        this.reviews = reviews;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    @Override  
    public int getCount() {  
        // TODO Auto-generated method stub  
        return reviews.size();  
    }  
  
    @Override  
    public Object getItem(int position) {  
        // TODO Auto-generated method stub  
        return null;  
    }  
  
    @Override  
    public long getItemId(int position) {  
        // TODO Auto-generated method stub  
        return 0;  
    }
      
    @Override  
    public View getView(final int position, View convertView, ViewGroup parent) {  
        // TODO Auto-generated method stub  
    	
    	ReviewListItem review  = reviews.get(position);
    	View vi=convertView;
        
        if(convertView==null)
            vi = inflater.inflate(R.layout.review_list_row, null);
        RatingBar ratingBar = (RatingBar)vi.findViewById(R.id.viewTruckRatingRatingBar);
        TextView txtDesc = (TextView) vi.findViewById(R.id.txtReviewDesc);
        TextView txtReviewerName = (TextView) vi.findViewById(R.id.txtReviewReviewerName);
        
        ratingBar.setNumStars(5);
        ratingBar.setRating(review.review.getRating());
        txtDesc.setText(review.review.getDescription());
        txtReviewerName.setText(review.review.getReviewerName());
        
       
		
        return vi;  
    }

  
 
}
	
}
