package com.cincyfoodtrucks.integration;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.cincyfoodtrucks.R;
import com.cincyfoodtrucks.dao.INetworkTruck;
import com.cincyfoodtrucks.dao.IReviewServiceSQLite;
import com.cincyfoodtrucks.dao.ITruckServiceSQLite;
import com.cincyfoodtrucks.dao.NetworkTruckDAO;
import com.cincyfoodtrucks.dao.ReviewServiceSQLiteStub;
import com.cincyfoodtrucks.dao.TruckServiceSQLiteStub;
import com.cincyfoodtrucks.dto.Review;
import com.cincyfoodtrucks.dto.TruckOwner;

/**
 * Class to handle truck integration
 * @author Smiley
 *
 */
public class TruckIntegration implements ITruckIntegration  {
	SharedPreferences preferences;
	ITruckServiceSQLite truckServiceSQLite;
	IReviewServiceSQLite reviewServiceSQLite;
	INetworkTruck networkTruck;
	Context context;
	/**
	 * Constructor for the class. 
	 * @param context The context calling the integration class. 
	 */
	public TruckIntegration(Context context)
	{
		this.context =context;
		networkTruck = new NetworkTruckDAO();
		reviewServiceSQLite = new ReviewServiceSQLiteStub(context);
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
		truckServiceSQLite = new TruckServiceSQLiteStub(context);
	}
	

@Override
public String getHumanReadableTruckHours(TruckOwner truck) {
	Calendar cal1 = Calendar.getInstance();
	cal1.setTimeInMillis(truck.getHoursAtLocation());
	SimpleDateFormat dateFormat = new SimpleDateFormat(
	                                "MM/dd hh:mm a");
	 String dateforrow = dateFormat.format(cal1.getTime());
	 return dateforrow;

}


	
	
	/* (non-Javadoc)
	 * @see com.cincyfoodtrucks.integration.ITruckIntegration#getTruckIDByTruckName(java.lang.String)
	 */
	@Override
	public int getTruckIDByTruckName(String truckName) throws Exception
	{
		TruckOwner truck = truckServiceSQLite.getTruckByName(truckName);
		return truck.getTruckID();
		
	}
	
	
	/* (non-Javadoc)
	 * @see com.cincyfoodtrucks.integration.ITruckIntegration#getTruckUsingSharedPrefs()
	 */
	@Override
	public TruckOwner getTruckUsingSharedPrefs() throws Exception
	{
		
		String truckName = preferences.getString("current_truck_name", "");
		TruckOwner truck = truckServiceSQLite.getTruckByName(truckName);
		return truck;
		
	}
	
	/* (non-Javadoc)
	 * @see com.cincyfoodtrucks.integration.ITruckIntegration#createRatingOnlineOffline(com.cincyfoodtrucks.dto.Review)
	 */
	@Override
	public void createRatingOnlineOffline(Review review)
	{
		
	}
	
	
	@Override
	public void updateTruckInSharedPrefs(String truckName) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(context.getResources().getString(R.string.strCurrentTruckPrefName), truckName);
		editor.apply();
		
		
	}

	
	/* (non-Javadoc)
	 * @see com.cincyfoodtrucks.integration.ITruckIntegration#getTrucksWithLocations()
	 */
	@Override
	public ArrayList<TruckOwner> getTrucksWithLocations() throws Exception
	{
		Date now =new Date();
    	//create timestamp from the time they will be leaving
    	@SuppressWarnings("deprecation")
		@Deprecated
    	Timestamp nowTimestamp = new Timestamp(now.getYear(), now.getMonth(), now.getDate(), now.getHours(), now.getMinutes(), 0, 0);
    
		ArrayList<TruckOwner> trucksUnpurged = truckServiceSQLite.getAllRows();
		ArrayList<TruckOwner> trucks = new ArrayList<TruckOwner>();
		for (TruckOwner truckOwner : trucksUnpurged) {
			
			//make sure there's locations
			if(truckOwner.getLatitude() != null && truckOwner.getLongitude() != null)
			{
				//make sure the time hasn't expired
				Timestamp truckTs = new Timestamp(truckOwner.getHoursAtLocation());
				if(!truckTs.before(nowTimestamp))
				{
				trucks.add(truckOwner);
				}
				
			}
		}
		return trucks;
	}

	
	@Override
	public void executeGetTruckDataAsyncTask(Context context) throws InterruptedException{
		GetTruckData gtd = new GetTruckData(context);
		Thread t = new Thread(gtd);
		t.setName("GetTruckData");
		t.start();
		
        //pause the UI thread to load the truck data
        t.join();
        
       
        
	}
	
	@Override
	public TruckOwner getTruckByTruckID(int truckID) {
		return truckServiceSQLite.getTruckByTruckID(truckID);
	}

	
	
	@Override
	public Date convertTimestampToDate(long timestamp) {
		Date date = new Date(timestamp);
		return date;
	}
	
	
	@Override
	public void executeSendTruckDataAsyncTask(Context context, String longitude,
			String latitude, String timestamp, int truckID) {

		//really? std bro?
		SendTruckData std = new SendTruckData(context, longitude, latitude, timestamp, truckID);
		
		Thread t = new Thread(std);
		t.setName("SendTruckData");
		t.start();
		
	}

	
	
	@Override
	public void updateLocation(String longitude, String latitude, int truckID) {
		try {
			networkTruck.updateLocation(longitude, latitude, truckID);
		} catch (Exception e) {
			Toast.makeText(context, context.getResources().getString(R.string.strErrorUpdatingLocation), Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		
	}


	@Override
	public void updateTimeAtLocation(String timestamp, int truckID) {
		try {
			networkTruck.updateTimeAtLocation(timestamp, truckID);
		} catch (Exception e) {
		Toast.makeText(context, context.getResources().getString(R.string.strErrorUpdatingLocation), Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}
	
	@Override
	public int calculateAverageRating(TruckOwner truck) throws Exception {
		ArrayList<Review> reviewsByTruckID = reviewServiceSQLite.getReviewsByTruckID(truck.getTruckID());
		
		int totalRate = 0;
		for (int i = 0; i < reviewsByTruckID.size(); i++) {
			totalRate += reviewsByTruckID.get(i).getRating();
			
		}
		int averageRate = totalRate / reviewsByTruckID.size();
		return averageRate;
	}


	
/**
 * Threading class to retrieve truck data from the remote server
 * @author Andrew Smiley
 *
 */
private class GetTruckData implements Runnable {
    
	//Create vars for the new class
	NetworkTruckDAO truckNetworkAccessor;
	TruckServiceSQLiteStub truckLocalAccessor;
	Context context;
	public GetTruckData(Context newContext)
	{
	context = newContext;
	

	   truckNetworkAccessor = new NetworkTruckDAO();
	   truckLocalAccessor = new TruckServiceSQLiteStub(context);
	}
       

@Override
public void run() {
	try {
		
		
		ArrayList<TruckOwner> trucks =  truckNetworkAccessor.getAllTrucks();
	  
	   if(trucks.isEmpty())
       {
    	//Don't raise toast
			throw new Exception();
       }
	   
	   //try to insert the trucks into the database
	 //get the offline trucks
   	ArrayList<TruckOwner> offlineTrucks = truckServiceSQLite.getOfflineTrucks();
   	if(!offlineTrucks.isEmpty())
   	{
   		//try to update the truck locations
   		for (TruckOwner truckOwner : offlineTrucks) {
				updateLocation(truckOwner.getLongitude(), truckOwner.getLatitude(), truckOwner.getTruckID());
				updateTimeAtLocation(String.valueOf(truckOwner.getHoursAtLocation()), truckOwner.getTruckID());
				
			}
   	}
   	
   		//if everything has worked, we add the new data
		   truckLocalAccessor.addRows(trucks);
	
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		
		
	}
       
       
   }
	


}

/**
 * Threading class to send  truck data back up to the remote server and pull the updated data back down
 * @author Andrew Smiley
 *
 */
private class SendTruckData implements Runnable {
    /** The system calls this to perform work in a worker thread and
     * delivers it the parameters given to AsyncTask.execute() */
	NetworkTruckDAO truckNetworkAccessor;
	TruckServiceSQLiteStub truckLocalAccessor;
	Context context;
	String longitude, latitude, timestamp;
	int truckID;
	
	//create constructor 
	public SendTruckData(Context newContext, String longitude, String latitude, String timestamp, int truckID)
	{
	this.longitude = longitude;
	this.latitude = latitude;
	this.timestamp = timestamp;
	this.truckID = truckID;
	
		
	this.context = newContext;
	}
	@Override
	public void run() {
		
		
		   truckNetworkAccessor = new NetworkTruckDAO();
		   truckLocalAccessor = new TruckServiceSQLiteStub(context);
		   ArrayList<TruckOwner> trucks;
	    try {
	    	
	    	
	 	   updateLocation(longitude, latitude, truckID);
	 	   updateTimeAtLocation(timestamp, truckID);
	 	  trucks = truckNetworkAccessor.getAllTrucks();
			
	
		   if(trucks.isEmpty())
	       {
	    	throw new Exception();
	    	}
		   
		   //try to update the truck data
		   truckLocalAccessor.addRows(trucks);
		 
			
		} catch (Exception e) {
			
			e.printStackTrace();
			//save it locally if the network call fails
			TruckOwner truck = truckServiceSQLite.getTruckByTruckID(truckID);
			truck.setLongitude(longitude);
			truck.setLatitude(latitude);
			truck.setHoursAtLocation(Long.parseLong(timestamp));
			try {
				truckServiceSQLite.addRow(truck);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
		}
		
	
	 
       
       
   }



}




}