package com.cincyfoodtrucks.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cincyfoodtrucks.R;

public class ContactActivity extends BaseActionMenuActivity {
	//the EditText for the email
	String provider;
	
	
	//the EditText for the content
	
	EditText thoughtsText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		
		
		thoughtsText = (EditText)findViewById(R.id.commentBox);
	}



	
	 /**
     * This method is invoked when the Submit button is clicked, because
     * we set onSubmitClicked as the value of the OnClick attribute of the
     * Submit Button.
     * @param v
     */
    public void onSubmitClicked(View v) 
    {
        // create an intent that tells us to go to the Advanced Search Activity.
        //Intent submitIntent = new Intent(this, MainActivity.class);
    	
    	//some bs
    	
        // start that activity.
        //startActivity(submitIntent);
    	//get the values from the screen
    	
    	//String name = nameText.getText().toString();
    	String comment = thoughtsText.getText().toString();
    	
    	//use implicit intent to open the email client
    	Intent i = new Intent(Intent.ACTION_SEND);
    	i.setType("message/rfc822");
    	i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"admin@cincyfoodtruckapp.com"});
    	i.putExtra(Intent.EXTRA_SUBJECT, "RE: CFTA Application");
    	i.putExtra(Intent.EXTRA_TEXT   , comment);
    	try {
    	    startActivity(Intent.createChooser(i, "Send mail..."));
    	} catch (android.content.ActivityNotFoundException ex) {
    	    Toast.makeText(getApplicationContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
    	}
    	
    	//Toast.makeText(getApplicationContext(), email+name+comment, 
        //Toast.LENGTH_LONG).show();
    	
    	
    
    }

    /**
     * This method is invoked when the Rate Us button is clicked, because
     * we set onRateClicked as the value of the OnClick attribute of the
     * Rate Button.
     * @param v
     */
    public void onRateClicked(View v) 
    {
        // create an intent that tells us to go to the Advanced Search Activity.
        //Intent rateIntent = new Intent(this, RateActivity.class);
        
        // start that activity.
        //startActivity(rateIntent);
    	final String appName = "com.cincyfoodtrucks&hl=en";
    	
    	//on the rate us button, open a link to our page on the app store
    	try {
    	    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+appName)));
    	} catch (android.content.ActivityNotFoundException anfe) {
    	    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id="+appName)));
    	}

    
    }
}
