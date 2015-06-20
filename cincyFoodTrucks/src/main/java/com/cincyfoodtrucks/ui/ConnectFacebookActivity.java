package com.cincyfoodtrucks.ui;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;

import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.cincyfoodtrucks.integration.FacebookPageIntegration;
import com.cincyfoodtrucks.integration.TruckIntegration;
import com.cincyfoodtrucks.integration.UserIntegration;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookSdk;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

public class ConnectFacebookActivity extends BaseActionMenuActivity {
	public static String FACEBOOK_SELECT_PAGE_KEY="fbselectpage";
	public static String FACEBOOK_SELECT_PAGE_VALUE="pageSelected";
	public static String FACEBOOK_LOGGED_IN_KEY="fbloggedin";
	public static String FACEBOOK_LOGGED_IN_VALUE="loggedIn";
	private TruckIntegration truckIntegrator;
	private UserIntegration userIntegrator;
	CallbackManager callbackManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		truckIntegrator = new TruckIntegration(getApplicationContext());
		userIntegrator = new UserIntegration(getApplicationContext());
		try {
			//just to check if they're logged in
			if (!userIntegrator.isLoggedIn(userIntegrator.getUsernameFromSharedPreferences(), userIntegrator.getEncryptedPasswordFromSharedPreferences())){
				Toast.makeText(getApplicationContext(), "You must be logged in to connect to Facebook", Toast.LENGTH_LONG).show();
				Intent i = new Intent(getApplicationContext(),MainActivity.class);
				startActivity(i);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		List<String> permissionNeeds= Arrays.asList("publish_actions", "manage_pages", "publish_pages");
	 	super.onCreate(savedInstanceState);
		FacebookSdk.sdkInitialize(getApplicationContext());
		LoginManager loginManager = LoginManager.getInstance();
//		String s = Settings.getApplicationSignature(getApplicationContext());
		try {
			PackageInfo info = getPackageManager().getPackageInfo(
					"com.cincyfoodtrucks",
					PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		} catch (PackageManager.NameNotFoundException e) {

		} catch (NoSuchAlgorithmException e) {

		}
		callbackManager = CallbackManager.Factory.create();
		// Set permissions
		LoginManager.getInstance().logInWithPublishPermissions(this, Arrays.asList("publish_actions", "manage_pages", "publish_pages"));

		LoginManager.getInstance().registerCallback(callbackManager,
				new FacebookCallback<LoginResult>() {
					@Override
					public void onSuccess(LoginResult loginResult) {

						System.out.println("Success");
						GraphRequest.newMeRequest(
								loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
									@Override
									public void onCompleted(JSONObject json, GraphResponse response) {
										if (response.getError() != null) {
											// handle error
											System.out.println("ERROR");
										} else {
											System.out.println("Success");

											Intent intent = new Intent(getApplicationContext(),SelectFacebookPageActivity.class);
											Toast.makeText(getApplicationContext(), "You're logged in", Toast.LENGTH_LONG).show();
											startActivityForResult(intent, 1);

//											startActivity(intent);
//											try {
//												FacebookPageIntegration fbIntegration = new FacebookPageIntegration(getApplicationContext());
//												try {
//													fbIntegration.getFacebookPages(AccessToken.getCurrentAccessToken().getToken().toString());
//												} catch (InterruptedException e) {
//													e.printStackTrace();
//												}
////												finish();
////												String jsonresult = String.valueOf(json);
////												System.out.println("JSON Result" + jsonresult);
////
////												String str_email = json.getString("email");
////												String str_id = json.getString("id");
////												String str_firstname = json.getString("first_name");
////												String str_lastname = json.getString("last_name");
////												Toast.makeText(getApplicationContext(), "Login success: \nFirstName: "+str_firstname+"\nLastName: "+str_lastname,Toast.LENGTH_LONG).show();
//
////												finish();
//												//so if we're logged in..
////											} catch (JSONException e) {
////												e.printStackTrace();
//											} catch (Exception e) {
//												e.printStackTrace();
//											}
										}
									}

								}).executeAsync();

					}

					@Override
					public void onCancel() {
//						Log.d(TAG_CANCEL,"On cancel");
//						Log("Cancelled");
					}

					@Override
					public void onError(FacebookException error) {
//						Log.d(TAG_ERROR,error.toString());
					}
				});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		//I wonder if I'll get a null pointer here: called it
		//it works! It's so beautiful!!!
		//Seriously just going to start injecting random ass qoutes into my code
		//It has no legs but still moves, how interesting...
		if ((data.getStringExtra(FACEBOOK_SELECT_PAGE_KEY) != null) ? data.getStringExtra(FACEBOOK_SELECT_PAGE_KEY).equalsIgnoreCase(FACEBOOK_SELECT_PAGE_VALUE) : false){
			Intent resultData = new Intent();
			resultData.putExtra(ConnectFacebookActivity.FACEBOOK_LOGGED_IN_KEY, ConnectFacebookActivity.FACEBOOK_LOGGED_IN_VALUE);
			setResult(Activity.RESULT_OK, resultData);
			finish();
		}else {
			super.onActivityResult(requestCode, resultCode, data);
			callbackManager.onActivityResult(requestCode, resultCode, data);
		}
	}

}
