package com.cincyfoodtrucks.ui;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.R.menu;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.cincyfoodtrucks.R;
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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.maps.model.LatLng;

/**
 *  Class to list trucknames/locations via list
 * @author Andrew Smiley
 *
 */
public class LocationListActivity extends BaseActionMenuActivity {
	private PopupMenu popupMenu;
    //final cases to handle truck options
	//view directions
	private final static int ONE = 1;
	
	//view Truck menu
    private final static int TWO = 2;
    //view Truck reviews
    private final static int THREE = 3;
    private final static int FOUR = 4;
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
			setContentView(com.cincyfoodtrucks.R.layout.activity_location_list);
			
			
			
			truckIntegration = new TruckIntegration(this);
			reviewIntegration = new ReviewIntegration(this);
			menuIntegration = new MenuIntegration(this);
			trucks = new ArrayList<TruckListItem>();
			//load the list
			//exv=(ExpandableListView)findViewById(R.id.truckLocationExpListView);
			 lv = (ListView) findViewById(R.id.truckListListView);
		        try {
		        	
		        	//let's try to preload the data for the trucks when this starts
		        	reviewIntegration.executeGetReviewData(this);
		        	menuIntegration.executeGetTruckMenuTask();
		        	truckIntegration.executeGetTruckDataAsyncTask(getApplicationContext());
				ArrayList<TruckOwner> trucksArry  = truckIntegration.getTrucksWithLocations();
				
				//Sort the trucks by distance
				Collections.sort(trucksArry, new Comparator<TruckOwner>() {

					@Override
					public int compare(TruckOwner lhs, TruckOwner rhs) {
						//get the distances
					
						Double lhsDistance = mapIntegrator.numericDistanceToPoint(new LatLng(Double.parseDouble(lhs.getLatitude()), Double.parseDouble(lhs.getLongitude())));
						Double rhsDistance = mapIntegrator.numericDistanceToPoint(new LatLng(Double.parseDouble(rhs.getLatitude()), Double.parseDouble(rhs.getLongitude())));
						return lhsDistance.compareTo(rhsDistance);
						
						
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
	            vi = inflater.inflate(R.layout.truck_list_row, null);
	        
	        //find the views
	        final TextView txtTitle = (TextView) vi.findViewById(R.id.txtTruckListTitle);
	        TextView txtDistance = (TextView) vi.findViewById(R.id.txtTruckListDistance);
	        TextView txtHours = (TextView) vi.findViewById(R.id.txtTruckListHours);
	        TextView txtAverageRate = (TextView)vi.findViewById(R.id.txtTruckAverageRating);
	        txtTitle.setText(truck.truck.getTruckName());
	       
	        //just some stuff to calculate average rating and distance
	        Criteria criteria = new Criteria();
	        provider = locationManager.getBestProvider(criteria, false);
			try {
				int averageRating = truckIntegration.calculateAverageRating(truck.truck);
				String strRating = averageRating+"/5";
				
				txtAverageRate.setText(getResources().getText(R.string.strAverageRating)+" "+strRating);
			} catch (Exception e) {
				txtAverageRate.setText("No Reviews");
//				Toast.makeText(getApplicationContext(), getResources().getString(R.string.strGenericError), Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}	
			
			
	        txtDistance.setText(mapIntegrator.distanceToPoint(new LatLng(Double.parseDouble(truck.truck.getLatitude()), Double.parseDouble(truck.truck.getLongitude()))));
			
			
			
			if(truck.truck.getHoursAtLocation() == 0){
				 txtHours.setText(getResources().getString(R.string.strCurrentlyClosed));
			}else{
	        txtHours.setText(getResources().getString(R.string.strTruckLeavingTime)+" "+truckIntegration.getHumanReadableTruckHours(truck.truck.getHoursAtLocation()));
			}
			final String truckName = txtTitle.getText().toString();
			
				final String truckLat = truck.truck.getLatitude().toString();
				final String truckLon = truck.truck.getLongitude().toString();
			
			final View anchor = txtTitle;
	          vi.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					//create the popup menu
					 popupMenu = new PopupMenu(context, anchor);
		             popupMenu.getMenu().add(Menu.NONE, ONE, Menu.NONE, getResources().getString(R.string.strGetDirections));
		             popupMenu.getMenu().add(Menu.NONE, TWO, Menu.NONE, getResources().getString(R.string.strViewTruckMenu));
		             popupMenu.getMenu().add(Menu.NONE, THREE, Menu.NONE,  getResources().getString(R.string.strViewTruckReviews));
		             popupMenu.getMenu().add(Menu.NONE, FOUR, Menu.NONE,  getResources().getString(R.string.strCreateReview));
		             popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
					
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						
						//set the truck name in the shared preferences so we can build the activities later
						 truckIntegration.updateTruckInSharedPrefs(truckName);
						switch (item.getItemId()) {
						//get directions to the truck
					       case ONE:
					    	   if(myLocation != null )
					    	   {
					    	   //TextView thisTxt = arg0;
					    	   //Just get directions to the truck. 
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


