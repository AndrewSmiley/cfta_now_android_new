package com.cincyfoodtrucks.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.cincyfoodtrucks.R;
import com.cincyfoodtrucks.dto.Review;
import com.cincyfoodtrucks.integration.IReviewIntegration;
import com.cincyfoodtrucks.integration.ITruckIntegration;
import com.cincyfoodtrucks.integration.ReviewIntegration;
import com.cincyfoodtrucks.integration.TruckIntegration;

public class RateActivity extends BaseActionMenuActivity {
EditText reviewDesc;
ITruckIntegration truckIntegration;
RatingBar truckRatingBar;
EditText reviewerName;
IReviewIntegration reviewIntegrator;
Button menuBtn;
String provider;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_rate);
		super.onCreate(savedInstanceState);
		menuBtn = (Button)findViewById(R.id.menuBtn);
		menuBtn.setVisibility(Button.INVISIBLE);
		reviewIntegrator = new ReviewIntegration(this);
		reviewDesc = (EditText)findViewById(R.id.txtReviewWriteDescription);
		reviewerName = (EditText)findViewById(R.id.txtWriteReviewerName);
		truckIntegration = new TruckIntegration(getApplicationContext());
		truckRatingBar = (RatingBar)findViewById(R.id.ratingBar1);
		try {
			//get the truck data
			truckIntegration.executeGetTruckDataAsyncTask(this);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
		Toast.makeText(this, getResources().getString(R.string.strGenericError), Toast.LENGTH_LONG).show();
		}
		
		
	}
	
	 /**
     * This method is invoked when the Submit button is clicked, because
     * we set onSubmitClicked as the value of the OnClick attribute of the
     * Submit Button.
     * @param v
     */
    public void onSubmitClicked(View v) 
    {
    	
    	try {
    		
    	//get the text review from the view
    	String reviewContent = reviewDesc.getText().toString();
    	float fltRating = truckRatingBar.getRating();
    	String name = reviewerName.getText().toString();
    	Math.round(fltRating);
    	int rating = (int) fltRating;
    	Review review = new Review();
    	review.setDescription(reviewContent);
    	review.setRating(rating);
    	review.setReviewerName(name);
    	review.setTruckID(truckIntegration.getTruckUsingSharedPrefs().getTruckID());
    	
    	reviewIntegrator.createReviewOnlineOffline(review);
    	Toast.makeText(this, getResources().getText(R.string.strReviewCreated), Toast.LENGTH_LONG).show();
		} catch (InterruptedException e) {
			Toast.makeText(this, getResources().getText(R.string.strCouldNotMakeReview), Toast.LENGTH_LONG).show();
		}
    	catch(NumberFormatException n)
    	{
    		Toast.makeText(this, getResources().getText(R.string.strCouldNotMakeReview), Toast.LENGTH_LONG).show();
    	} catch (Exception e) {
			// TODO Auto-generated catch block
    		Toast.makeText(this, getResources().getText(R.string.strCouldNotMakeReview), Toast.LENGTH_LONG).show();
		}
    	
    	// create an intent that tells us to go to the Advanced Search Activity.
        Intent submitIntent = new Intent(this, LocationListActivity.class);
        
        // start that activity.
        startActivity(submitIntent);
    
    }

    /**
     * This method is invoked when the Menu button is clicked, because
     * we set onRateClicked as the value of the OnClick attribute of the
     * Rate Button.
     * @param v
     */
    public void onMenuClicked(View v) 
    {
        // create an intent that tells us to go to the Advanced Search Activity.
        Intent rateIntent = new Intent(this, MenuActivity.class);
        
        // start that activity.
        startActivity(rateIntent);
    
    }
}
