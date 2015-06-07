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

import com.cincyfoodtrucks.R;
import com.cincyfoodtrucks.integration.IMenuIntegration;
import com.cincyfoodtrucks.integration.MenuIntegration;

public class MenuItemSearchActivity extends BaseActionMenuActivity {
	
	private IMenuIntegration menuIntegrator;
	private EditText searchBar;
	String provider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_item_search);

		searchBar = (EditText)findViewById(R.id.txtMenuItemSearch);
		menuIntegrator = new MenuIntegration(this);
			}

	

/**
 * Method to process when the 'Search for foods' button is clicked
 * @param v The calling view
 */
public void onSearchForFoodsClicked(View v)
{
	//Just update the criteria here. We'll do something with it in the next Activity
	String criteria = searchBar.getText().toString();
	menuIntegrator.updateSearchItemInSharedPrefs(criteria);
	Intent searchResultIntent = new Intent(this, MenuItemSearchResultsActivity.class);
	startActivity(searchResultIntent);
	
}

}