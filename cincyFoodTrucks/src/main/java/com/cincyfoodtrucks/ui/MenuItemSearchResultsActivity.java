package com.cincyfoodtrucks.ui;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.cincyfoodtrucks.R;
import com.cincyfoodtrucks.dao.ITruckServiceSQLite;
import com.cincyfoodtrucks.dao.TruckServiceSQLiteStub;
import com.cincyfoodtrucks.dto.TruckMenuItem;
import com.cincyfoodtrucks.dto.TruckOwner;
import com.cincyfoodtrucks.integration.IMapsIntegration;
import com.cincyfoodtrucks.integration.IMenuIntegration;
import com.cincyfoodtrucks.integration.ITruckIntegration;
import com.cincyfoodtrucks.integration.MapsIntegration;
import com.cincyfoodtrucks.integration.MenuIntegration;
import com.cincyfoodtrucks.integration.TruckIntegration;
import com.cincyfoodtrucks.ui.LocationListActivity.TruckListItem;
import com.google.android.gms.maps.model.LatLng;

public class MenuItemSearchResultsActivity extends BaseActionMenuActivity {
	private PopupMenu popupMenu;
    //final cases to handle truck options
	//view directions
	private final static int ONE = 1;
	
	//view Truck menu
    private final static int TWO = 2;
    //view Truck reviews
    private final static int THREE = 3;
    private final static int FOUR = 4;
    
    SearchResultListView resultList;
    
	//List view adapter
    ListView lv;
	IMenuIntegration menuIntegrator;
	ArrayList<MenuResultItem> menuResultItems;
	ITruckServiceSQLite truckServiceSQLite;
	IMapsIntegration mapsIntegrator;
	LocationManager locationManager;
	ITruckIntegration truckIntegrator;
	String provider;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_item_search_results);
		
		lv = (ListView) findViewById(R.id.menuItemSearchResultsList);
		truckIntegrator = new TruckIntegration(this);
		truckServiceSQLite = new TruckServiceSQLiteStub(this);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		HashMap<Integer, TruckOwner> trucks = truckServiceSQLite.getTrucksAsHashMap();
		menuIntegrator = new MenuIntegration(this);
		menuResultItems = new ArrayList<MenuResultItem>();
		mapsIntegrator = new MapsIntegration(this,locationManager);
		
		//let's try to get the truck data back from the remote server
		try {
			menuIntegrator.executeGetTruckMenuTask();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(this, getResources().getString(R.string.strGenericError), Toast.LENGTH_LONG).show();
		}
		
		//let's play with some looping here
		ArrayList<TruckMenuItem> items = menuIntegrator.searchForFoods(menuIntegrator.getMenuItemSearchCriteriaFromSharedPrefs());
		//let the user know if there are no items
		if(items.size() == 0)
		{
			Toast.makeText(this, getResources().getString(R.string.strNoItemsFound), Toast.LENGTH_LONG).show();
			
		}
		
		for (TruckMenuItem truckMenuItem : items) {
			//lets get the data we need here
			//get the truck from the hashmap
			TruckOwner truck = trucks.get(truckMenuItem.getTruckID());
			String truckDistance = "";
			
			//calculate distance to the truck
			
			
			//make sure that the truck has a current location. Handle Appropriately
			if(truck.getLatitude() == null  || truck.getLongitude() == null || truck.getLatitude().equals("") ||truck.getLongitude().equals("") )
			{
				truckDistance = getResources().getString(R.string.strDistanceUnavailable);
			}else{
				truckDistance = mapsIntegrator.distanceToPoint(new LatLng(Double.parseDouble(truck.getLatitude()), Double.parseDouble(truck.getLongitude())));
			}
			
			//make sure that the truck has a current location. Handle Appropriately
			if(truck.getLatitude() == null  || truck.getLongitude() == null || truck.getLatitude().equals("") ||truck.getLongitude().equals("") )
			{
				//create new MenuResultItem
				//if the truck doesn't have a current location, just set them to empty strings
				MenuResultItem item = new MenuResultItem(truckMenuItem.getTitle(),
					truckMenuItem.getDescription(), truckMenuItem.getPrice().toString(), truck.getTruckName(), truckDistance, 
					"", "");
				
				//Add it to the arraylist
				menuResultItems.add(item);

			}else{
				//create new MenuResultItem
				MenuResultItem item = new MenuResultItem(truckMenuItem.getTitle(),
					truckMenuItem.getDescription(), truckMenuItem.getPrice().toString(), truck.getTruckName(), truckDistance, 
					truck.getLatitude().toString(), truck.getLongitude().toString());
				
				//Add it to the arraylist
				menuResultItems.add(item);
			}
		
			
		}
		resultList = new SearchResultListView(this, menuResultItems);
		lv.setAdapter(resultList);
	
	}

	

	/**
	 * Inner class we're going to use to help display the menu search result information
	 * @author Smiley
	 *
	 */
	private class MenuResultItem{
			String itemTitle;
			String itemDesc;
			String itemPrice;
			String truckName;
			String truckDistance;
			String latitude;
			String longitude;
			
			private MenuResultItem(String itemTitle, String itemDesc, String itemPrice, String truckName, String truckDistance, String latitude, String longitude) {
				this.itemTitle = itemTitle;
				this.itemDesc = itemDesc;
				this.itemPrice = itemPrice;
				this.truckName = truckName;
				this.truckDistance = truckDistance;
				this.longitude = longitude;
				this.latitude = latitude;
				
			}
	}
	
	
	
	
private class SearchResultListView extends BaseAdapter{
	
		
		LayoutInflater inflater;
		ArrayList<MenuResultItem> menuItems;
		Context context;
		
	    public SearchResultListView(Context context, ArrayList<MenuResultItem> menuItems) {  
	        super();
			this.context= context;
	        this.menuItems = menuItems;
	        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    }
	    
	    @Override  
	    public int getCount() {  
	        // TODO Auto-generated method stub  
	        return menuItems.size();  
	    }  
	  
	    @Override  
	    public Object getItem(int position) {  
	        // TODO Auto-generated method stub  
	        return null;  
	    }  
	  
	    @Override  
	    public long getItemId(int position) {  
	        // TODO Auto-generated method stub  
	        return 0;  
	    }
	      
	    @Override  
	    public View getView(final int position, View convertView, ViewGroup parent) {  
	        // TODO Auto-generated method stub  
	    	
	    	Criteria criteria = new Criteria();
			String provider = locationManager.getBestProvider(criteria, false);
			

			final Location myLocation;
			
			
			myLocation = locationManager.getLastKnownLocation(provider);
		

			
	    	MenuResultItem item  = menuItems.get(position);
	    	View vi=convertView;
	        
	        if(convertView==null)
	            vi = inflater.inflate(R.layout.menu_search_result_row, null);
	        
	        //find the views
	        final TextView txtMenuItemTitle = (TextView) vi.findViewById(R.id.txtSearchResultMenuItemTitle);
	        TextView  txtMenuItemDesc = (TextView) vi.findViewById(R.id.txtSearchResultMenuItemDesc);
	        TextView txtMenuItemPrice = (TextView) vi.findViewById(R.id.txtSearchResultMenuItemPrice);
	        TextView txtMenuItemTruckName = (TextView) vi.findViewById(R.id.txtSearchResultMenuItemTruckName);
	        TextView txtMenuItemTruckDistance = (TextView) vi.findViewById(R.id.txtSearchResultMenuItemTruckDistance);
	        
	        //set the view text
	        txtMenuItemTitle.setText(item.itemTitle);
	        txtMenuItemDesc.setText(item.itemDesc);
	        txtMenuItemPrice.setText("$"+item.itemPrice);
	        txtMenuItemTruckName.setText(Html.fromHtml("<b>Sold By:</b> "+item.truckName));
	        txtMenuItemTruckDistance.setText(Html.fromHtml("<b>Distance:</b> "+item.truckDistance));
	        
	        
	        
	       
			final String truckName = item.truckName;
			final String truckLat = item.latitude;
			final String truckLon = item.longitude;
			final View anchor = txtMenuItemTitle;
	          vi.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					//create the popup menu
					 popupMenu = new PopupMenu(context, anchor);
					 
					 if(myLocation != null){
						 
		             popupMenu.getMenu().add(Menu.NONE, ONE, Menu.NONE, getResources().getString(R.string.strGetDirections));
		             
					 }
		             popupMenu.getMenu().add(Menu.NONE, TWO, Menu.NONE, getResources().getString(R.string.strViewTruckMenu));
		             popupMenu.getMenu().add(Menu.NONE, THREE, Menu.NONE,  getResources().getString(R.string.strViewTruckReviews));
		             popupMenu.getMenu().add(Menu.NONE, FOUR, Menu.NONE,  getResources().getString(R.string.strCreateReview));
		           popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
					
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						
						//set the truck name in the shared preferences so we can build the activities later
						truckIntegrator.updateTruckInSharedPrefs(truckName);
						switch (item.getItemId()) {
						//get directions to the truck
					       case ONE:
					    	   //TextView thisTxt = arg0;
					    	   //Just get directions to the truck. 
					    	   if(myLocation != null)
					    	   {
								 Uri location = Uri.parse("http://maps.google.com/maps?saddr="+myLocation.getLatitude()+","+myLocation.getLongitude()+"&daddr="+truckLat+","+truckLon);
								 //Toast.makeText(this, marker.getTitle(), Toast.LENGTH_SHORT).show();
								// marker.getPosition();
								 Intent navigation = new Intent(Intent.ACTION_VIEW, location); 
								 startActivity(navigation);
					    	   }
					              break;
					              
					              //create intent to view truck menu
					       case TWO:
					    	  
					    	   Intent truckMenuIntent = new Intent(context, TruckMenuActivity.class);
					    	   startActivity(truckMenuIntent);
					            
					              break;
					              
					              //create intent to view truck reviews
					       case THREE:
					             // tv.setText("THREE");
					              Intent viewTruckReviews = new Intent(context, ViewTruckReviewsActivity.class);
					              startActivity(viewTruckReviews);
					              
					      break;
					       case FOUR:
					    	   	//Point them to the page to create the review 
					              Intent createReview = new Intent(context, RateActivity.class);
					              startActivity(createReview);
					              
					      
					       }
					   
						
					       return true;
					}
				});
		             
		             //show the pop-up menu
					popupMenu.show();
				}
				
			});
	        return vi;  
	    }
	
	  
	 
	}

	

}
