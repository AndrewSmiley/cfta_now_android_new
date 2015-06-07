package com.cincyfoodtrucks.integration;

import com.cincyfoodtrucks.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceIntegration {
	Context context;
	public PreferenceIntegration(Context context)
	{
		this.context = context;
	}

	
	
	/**
	 * Method to get the value of 'Enable Navigation' from SharedPreferences
	 * @return boolean the value found 
	 */
public boolean getEnabledNavigationValue()
{
	SharedPreferences myPreference=PreferenceManager.getDefaultSharedPreferences(context);
	return myPreference.getBoolean(context.getResources().getString(R.string.strEnableNavigation), false); 
}
}
