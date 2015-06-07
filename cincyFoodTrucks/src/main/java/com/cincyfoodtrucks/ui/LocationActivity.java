package com.cincyfoodtrucks.ui;

import java.security.NoSuchAlgorithmException;

import com.cincyfoodtrucks.R;
import com.cincyfoodtrucks.dao.IUserServiceSQLite;
import com.cincyfoodtrucks.dao.UserServiceSQLite;
import com.cincyfoodtrucks.integration.IUserIntegration;
import com.cincyfoodtrucks.integration.UserIntegration;

import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LocationActivity extends BaseActionMenuActivity {
	EditText username;
	EditText password;
	IUserServiceSQLite sqlUserService;
	IUserIntegration userIntegration;
	String provider;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		userIntegration = new UserIntegration(this);
		sqlUserService = new UserServiceSQLite(this);
		
		try {
			
			//get the user data
			userIntegration.executeGetUserData();
		} catch (InterruptedException e) {
			Toast.makeText(this, getResources().getString(R.string.strGenericError), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		//if they get to the login we want to clear the username and password from the shared preferences. 
		userIntegration.clearUsernameAndPasswordFromSharedPrefs();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
	}
	
	 /**
     * This method is invoked when the Log In button is clicked, because
     * we set onLogInClicked as the value of the OnClick attribute of the
     * Submit Button.
     * @param v
     */
    public void onLogInClicked(View v) 
    {
    	//get the truck username and password
    			username = (EditText)findViewById(R.id.txtUsername);
    			password = (EditText)findViewById(R.id.txtReviewWriteDescription);
        String thisUsername = username.getText().toString();
        String thisPassword;
		try {
			thisPassword = sqlUserService.encryptPassword(password.getText().toString());
		} catch (NoSuchAlgorithmException e) {
			Toast.makeText(this, getResources().getString(R.string.strGenericError), Toast.LENGTH_SHORT).show();
			e.printStackTrace();
			thisPassword = "";
		}
    	
		//handle cases for login
    	if(userIntegration.isLoggedIn(thisUsername, thisPassword )){
    	
    		//add the username and password to Shared preferences
    		userIntegration.updateUsernameAndPasswordInPrefs(thisUsername, password.getText().toString());
    		//redirect to the menu
    		Intent menuIntent = new Intent(this, MenuActivity.class);
    		startActivity(menuIntent);
    	
    	}else{
    		Toast.makeText(this, getResources().getString(R.string.strBadUsernameOrPassword), Toast.LENGTH_LONG).show();
    	}
    	
    }

    /***
     * Some stuff for a dialog to post to facebook
     */
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
            case DialogInterface.BUTTON_POSITIVE:
                //Yes button clicked
                break;

            case DialogInterface.BUTTON_NEGATIVE:
                //No button clicked
                break;
            }
        }
    };
    
    
    
}
