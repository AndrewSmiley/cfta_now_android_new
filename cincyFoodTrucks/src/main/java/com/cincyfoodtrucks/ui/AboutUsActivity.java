package com.cincyfoodtrucks.ui;

import com.cincyfoodtrucks.R;
import com.cincyfoodtrucks.R.layout;
import com.cincyfoodtrucks.R.menu;

import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class AboutUsActivity extends BaseActionMenuActivity {
	String provider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_us);
	}

	

}
