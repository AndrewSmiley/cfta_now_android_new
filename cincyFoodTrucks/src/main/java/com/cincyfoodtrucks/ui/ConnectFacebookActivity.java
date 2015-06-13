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

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookSdk;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

public class ConnectFacebookActivity extends BaseActionMenuActivity {
	CallbackManager callbackManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
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
											try {
												finish();
												String jsonresult = String.valueOf(json);
												System.out.println("JSON Result" + jsonresult);

												String str_email = json.getString("email");
												String str_id = json.getString("id");
												String str_firstname = json.getString("first_name");
												String str_lastname = json.getString("last_name");
												Toast.makeText(getApplicationContext(), "Login success: \nFirstName: "+str_firstname+"\nLastName: "+str_lastname,Toast.LENGTH_LONG).show();
												finish();
												//so if we're logged in..
											} catch (JSONException e) {
												e.printStackTrace();
											}
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
		super.onActivityResult(requestCode, resultCode, data);

		callbackManager.onActivityResult(requestCode, resultCode, data);
	}

}
