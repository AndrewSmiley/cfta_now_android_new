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
import android.widget.EditText;
import android.widget.Toast;

import com.cincyfoodtrucks.R;
import com.cincyfoodtrucks.dto.User;
import com.cincyfoodtrucks.integration.IUserIntegration;
import com.cincyfoodtrucks.integration.UserIntegration;

public class ChangePasswordActivity extends BaseActionMenuActivity {

	IUserIntegration userIntegrator;
	EditText editTextCurrentPassword;
	EditText editTextNewPassword;
	EditText editTextConfirmPassword;
	String provider;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_password);
		
		userIntegrator = new UserIntegration(this);
	}

	

	
	
	/**
	 * Method to handle actions when the on submit change password is clicked
	 * @param v
	 */
	public void onChangePasswordSubmitClicked(View v)
	{
		editTextCurrentPassword = (EditText) findViewById(R.id.txtChangePasswordCurrentPassword);
		editTextNewPassword = (EditText)findViewById(R.id.txtNewPassword);
		editTextConfirmPassword = (EditText)findViewById(R.id.txtNewPasswordConfirm);
		String strPassword = editTextCurrentPassword.getText().toString();
		String strNewPassword = editTextNewPassword.getText().toString();
		String strNewPasswordConfirm = editTextConfirmPassword.getText().toString();
		
		try {
			
			//make sure the current password equals the old password
			if(userIntegrator.getPasswordFromSharedPrefs().equals(strPassword))
			{	//make sure the new passwords match
				if(strNewPassword.equals(strNewPasswordConfirm))
				{
					
					User user = userIntegrator.getUserFromUsername(userIntegrator.getUsernameFromSharedPreferences());
					user.setPassword(userIntegrator.encryptPassword(strNewPassword));
					userIntegrator.executeSendUserData(user);
					
					//update the password in the preferences
					userIntegrator.updateUsernameAndPasswordInPrefs(user.getUsername(), strNewPassword);
					
					//let them know it worked
					Toast.makeText(this, getResources().getString(R.string.strPasswordChanged), Toast.LENGTH_LONG).show();
					
					//send them to the main menu
					Intent menuIntent = new Intent(this, MenuActivity.class);
					startActivity(menuIntent);
					
					
				}else
				{
					//Let them know the passwords don't match
					Toast.makeText(this, getResources().getString(R.string.strErrorPasswordsDoNotMatch), Toast.LENGTH_LONG).show();
				}
				
				
			}else
			{
				//let them know their original password is incorrect
				Toast.makeText(this, getResources().getString(R.string.strErrorPasswordIncorrect), Toast.LENGTH_LONG).show();
			}
			
			
			
		} catch (Exception e) {
			//print stack trace and let them know about the error
			e.printStackTrace();
			Toast.makeText(this, getResources().getString(R.string.strGenericError), Toast.LENGTH_LONG).show();
		}
				
		
	}
}

