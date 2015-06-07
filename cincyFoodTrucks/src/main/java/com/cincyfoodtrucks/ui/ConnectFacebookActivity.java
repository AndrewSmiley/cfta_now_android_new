package com.cincyfoodtrucks.ui;

import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import com.facebook.FacebookSdk;
import com.facebook.FacebookException;
public class ConnectFacebookActivity extends Activity {

@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	 List<String> permissionNeeds= Arrays.asList("user_photos", "friends_photos", "email", "user_birthday", "user_friends");
	 super.onCreate(savedInstanceState);
	FacebookSdk.sdkInitialize(getApplicationContext());

}
	
}
