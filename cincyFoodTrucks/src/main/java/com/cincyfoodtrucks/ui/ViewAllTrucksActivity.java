package com.cincyfoodtrucks.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.cincyfoodtrucks.R;
import com.cincyfoodtrucks.R.layout;
import com.cincyfoodtrucks.R.menu;
import com.cincyfoodtrucks.dao.ITruckServiceSQLite;
import com.cincyfoodtrucks.dao.TruckServiceSQLiteStub;
import com.cincyfoodtrucks.dto.TruckOwner;
import com.cincyfoodtrucks.integration.IMapsIntegration;
import com.cincyfoodtrucks.integration.IMenuIntegration;
import com.cincyfoodtrucks.integration.IReviewIntegration;
import com.cincyfoodtrucks.integration.ITruckIntegration;
import com.cincyfoodtrucks.integration.MapsIntegration;
import com.cincyfoodtrucks.integration.MenuIntegration;
import com.cincyfoodtrucks.integration.ReviewIntegration;
import com.cincyfoodtrucks.integration.TruckIntegration;
import com.cincyfoodtrucks.ui.LocationListActivity.TruckListItem;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.maps.model.LatLng;

import android.annotation.TargetApi;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class ViewAllTrucksActivity extends BaseActionMenuActivity {
	private PopupMenu popupMenu;
    //final cases to handle truck options
	//view truck menu
	private final static int ONE = 1;
	
	//view Truck reviews
    private final static int TWO = 2;
    //review a truck
    private final static int THREE = 3;
    /** The view to show the ad. */
	  private AdView adView;
	  private static final String AD_UNIT_ID = "ca-app-pub-5343364852657568/6594382932";

    
    

	ListView lv;
	ArrayList<TruckListItem> trucks;
	ITruckServiceSQLite truckServiceSQLite;
	TruckListView adapter;
	ITruckIntegration truckIntegration;
	IMapsIntegration mapIntegrator;
	LocationManager locationManager;
	Location myLocation;
	String provider;
	IReviewIntegration reviewIntegration;
	IMenuIntegration menuIntegration;
	class TruckListItem
		{
		TruckOwner truck;
		public TruckListItem(TruckOwner truck)
		{
			this.truck = truck;
		}
		
		
		  
	  }
	
	
	
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			Criteria criteria = new Criteria();
		
			truckServiceSQLite = new TruckServiceSQLiteStub(this);
			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			
		
			provider = locationManager.getBestProvider(criteria, false);
			myLocation = locationManager.getLastKnownLocation(provider);
			mapIntegrator = new MapsIntegration(this, locationManager); 
			setContentView(com.cincyfoodtrucks.R.layout.activity_view_all_trucks);

			
			truckIntegration = new TruckIntegration(this);
			reviewIntegration = new ReviewIntegration(this);
			menuIntegration = new MenuIntegration(this);
			trucks = new ArrayList<TruckListItem>();
			//load the list
			//exv=(ExpandableListView)findViewById(R.id.truckLocationExpListView);
			 lv = (ListView) findViewById(R.id.viewAllTrucksList);
		        try {
		        	
		        	//let's try to preload the data for the trucks when this starts
		        	reviewIntegration.executeGetReviewData(this);
		        	menuIntegration.executeGetTruckMenuTask();
		        	truckIntegration.executeGetTruckDataAsyncTask(getApplicationContext());
				ArrayList<TruckOwner> trucksArry  = truckServiceSQLite.getAllRows();
				
				//Sort the trucks by name
				Collections.sort(trucksArry, new Comparator<TruckOwner>() {

					@Override
					public int compare(TruckOwner lhs, TruckOwner rhs) {
						//get the distances
					
						String lhsName = lhs.getTruckName();
						String rhsName = rhs.getTruckName();
						return lhsName.compareTo(rhsName);
						
						
					}
				});
				
				//loop over the trucks and create our Truck array helpers
				for (TruckOwner newTruck : trucksArry) {
					trucks.add(new TruckListItem(newTruck));
					}
				
		        
		        } catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(this, getResources().getString(R.string.strGenericError), Toast.LENGTH_LONG).show();
				}
		       
		       
		        if(trucks.isEmpty()){
		        	Toast.makeText(this, getResources().getString(R.string.strNoTrucksFound), Toast.LENGTH_LONG).show();
		        }
		        
		        adapter = new TruckListView(this, trucks);
		        lv.setAdapter(adapter);
		}


		
		/**
			 * Method to handle the view map button click
			 * @param v
			 */
			public void onViewMapClicked(View v){
			Intent mapIntent = new Intent(this, MainActivity.class);
			startActivity(mapIntent);
			}
		
		
		/**
		 * Private inner ArrayAdapter classs to show the truck data and handle the popup menu actions
		 * @author Smiley
		 *
		 */
	private class TruckListView extends BaseAdapter{
	
		
		LayoutInflater inflater;
		ArrayList<TruckListItem> trucks;
		Context context;
		
	    public TruckListView(Context context, ArrayList<TruckListItem> trucks) {  
	        super();
			this.context= context;
	        this.trucks = trucks;
	        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    }
	    
	    @Override  
	    public int getCount() {  
	        // TODO Auto-generated method stub  
	        return trucks.size();  
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
	    	
	    	TruckListItem truck  = trucks.get(position);
	    	View vi=convertView;
	        
	        if(convertView==null)
	            vi = inflater.inflate(R.layout.all_trucks_row, null);
	        
	        //find the views
	        final TextView txtTitle = (TextView) vi.findViewById(R.id.txtAllTrucksListTitle);
	        //TextView txtAverageRate = (TextView)vi.findViewById(R.id.txtAllTrucksAverageRating);
	        txtTitle.setText(truck.truck.getTruckName());
	       
	        //just some stuff to calculate average rating and distance
	        Criteria criteria = new Criteria();
	        provider = locationManager.getBestProvider(criteria, false);
			/**try {
				int averageRating = truckIntegration.calculateAverageRating(truck.truck);
				String strRating = averageRating+"/5";
				
				txtAverageRate.setText(getResources().getText(R.string.strAverageRating)+" "+strRating);
			} catch (Exception e) {
				//set the alternative text
				txtAverageRate.setText(getResources().getString(R.string.strNoReviewsBasic));
				e.printStackTrace();
			}	*/
			
			
	      
			
			
			
			final String truckName = txtTitle.getText().toString();
			
			final View anchor = txtTitle;
	          vi.setOnClickListener(new View.OnClickListener() {
				
				@TargetApi(Build.VERSION_CODES.HONEYCOMB)
				@Override
				public void onClick(View arg0) {
					//create the popup menu
					 popupMenu = new PopupMenu(context, anchor);
		             popupMenu.getMenu().add(Menu.NONE, ONE, Menu.NONE, getResources().getString(R.string.strViewTruckMenu));
		             popupMenu.getMenu().add(Menu.NONE, TWO, Menu.NONE,  getResources().getString(R.string.strViewTruckReviews));
		             popupMenu.getMenu().add(Menu.NONE, THREE, Menu.NONE,  getResources().getString(R.string.strCreateReview));
		             popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
					
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						
						//set the truck name in the shared preferences so we can build the activities later
						 truckIntegration.updateTruckInSharedPrefs(truckName);
						switch (item.getItemId()) {
						   case ONE:
					    	  
					    	   Intent truckMenuIntent = new Intent(context, TruckMenuActivity.class);
					    	   startActivity(truckMenuIntent);
					            
					              break;
					              
					              //create intent to view truck reviews
					       case TWO:
					             // tv.setText("THREE");
					              Intent viewTruckReviews = new Intent(context, ViewTruckReviewsActivity.class);
					              startActivity(viewTruckReviews);
					              
					      break;
					       case THREE:
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


