package com.cincyfoodtrucks.ui;

import com.cincyfoodtrucks.R;
import com.cincyfoodtrucks.integration.IUserIntegration;
import com.cincyfoodtrucks.integration.PreferenceIntegration;
import com.cincyfoodtrucks.integration.UserIntegration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.LocationManager;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class BaseActionMenuActivity extends Activity {
	String provider;
	IUserIntegration userIntegrator;
	PreferenceIntegration prefIntegration;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		prefIntegration = new PreferenceIntegration(this);
		userIntegrator = new UserIntegration(this);
		//we're going to default this to be false. If the user is logged in we set to true
		boolean result = false;
		try {
			 result = userIntegrator.isLoggedIn(userIntegrator.getUsernameFromSharedPreferences(), userIntegrator.getEncryptedPasswordFromSharedPreferences());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Inflate the menu; this adds items to the action bar if it is present.
		if(result)
		{
			//check to see if the nav is enabled
			if(prefIntegration.getEnabledNavigationValue()){
		getMenuInflater().inflate(R.menu.admin_menu, menu);
			}else
			{
				getMenuInflater().inflate(R.menu.admin_menu_without_nav, menu);
			}
		}else
		{
			//check to see if the nav is enabled 
			if(prefIntegration.getEnabledNavigationValue()){
				getMenuInflater().inflate(R.menu.main, menu);
					}else
					{
						getMenuInflater().inflate(R.menu.main_without_nav, menu);
					}
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection

		if (item.getItemId() == R.id.action_settings) {
			Intent settingsIntent = new Intent(this, SettingsActivity.class);
			startActivity(settingsIntent);
			return true;
		} else if (item.getItemId() == R.id.action_menu) {
			Intent menuIntent = new Intent(this, MenuActivity.class);

	        startActivity(menuIntent);
    	return true;
			
		}else if(item.getItemId() == R.id.action_view_map)
		{
			Intent mapIntent = new Intent(this, MainActivity.class);
			startActivity(mapIntent);

			

			return true;

		}
		else if(item.getItemId() == R.id.action_truck_list)
		{
			LocationManager portableLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			Criteria criteria = new Criteria();
			provider = portableLocationManager.getBestProvider(criteria, false);
			
			//don't give them access to this if the location is disabled
			if(portableLocationManager.isProviderEnabled(provider))
			{
			Intent truckList = new Intent(this, LocationListActivity.class);
			startActivity(truckList);
			}else
			{
				Toast.makeText(this, getResources().getText(R.string.strCannotViewActiveTrucks), Toast.LENGTH_LONG).show();
			}
			return true;
		}else if(item.getItemId() == R.id.action_contact)
		{
			Intent contact = new Intent(this, ContactActivity.class);
			startActivity(contact);
			return true;
		}else if(item.getItemId() == R.id.action_about){
			Intent aboutIntent = new Intent(this, AboutUsActivity.class);
			startActivity(aboutIntent);
			return true;
			
		}
		else if(item.getItemId() == R.id.action_search_for_foods)
		{
			Intent searchIntent = new Intent(this, MenuItemSearchActivity.class);
			startActivity(searchIntent);
			return true;
		}
		else if(item.getItemId() == R.id.action_view_all_trucks)
		{
			Intent viewAll = new Intent(this, ViewAllTrucksActivity.class);
			startActivity(viewAll);
			return true;
		}else if(item.getItemId() == R.id.action_login)
		{
			Intent locationAct = new Intent(this, LocationActivity.class);
			startActivity(locationAct);// start that activity.
			return true;
			
			//Handle case for change password
		}else if(item.getItemId() == R.id.action_change_password)
		{
			Intent changePasswordIntent = new Intent(this, ChangePasswordActivity.class);
			startActivity(changePasswordIntent);
			return true;
			//handle case for new menu item clicked
		}else if (item.getItemId() == R.id.action_new_menu_item)
		{
			Intent newItemIntent = new Intent(this, CreateMenuItemActivity.class);
			startActivity(newItemIntent);
			return true;
			
			//handle case for update_location_clicked
		}else if (item.getItemId() == R.id.action_update_location)
		{
			Intent updateLocationIntent = new Intent(this, LoginActivity.class);
			startActivity(updateLocationIntent);
			return true;

			//handle case for rate clicked
		}
		else if (item.getItemId() == R.id.action_newsfeed)
		{
			Intent updateLocationIntent = new Intent(this, NewsFeedActivity.class);
			startActivity(updateLocationIntent);
			return true;

			//handle case for rate clicked
		}else if (item.getItemId() == R.id.action_connect_facebook)
		{
//			this.startActivityForResult();
			Intent intent = new Intent(this, ConnectFacebookActivity.class);
			startActivity(intent);
			return true;

			//handle case for rate clicked
		}
		else if(item.getItemId() == R.id.action_rate_us)
		{
			final String appName = "com.cincyfoodtrucks&hl=en";
	    	
	    	//on the rate us button, open a link to our page on the app store
	    	try {
	    	    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+appName)));
	    	} catch (android.content.ActivityNotFoundException anfe) {
	    	    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id="+appName)));
	    	}

			return true;
		}
		
		
		
		
		else
		{
			  return super.onOptionsItemSelected(item);


		} 

	}

//	@Override
//	public void onA

}
