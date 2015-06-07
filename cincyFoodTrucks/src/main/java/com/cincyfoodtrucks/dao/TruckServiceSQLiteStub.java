package com.cincyfoodtrucks.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.cincyfoodtrucks.dto.TruckOwner;

/**
 * Class to handle connections to the SQLLite Database for trucks.
 * 
 * @author Andrew Smiley
 * 
 */
public class TruckServiceSQLiteStub extends SQLiteOpenHelper implements
		ITruckServiceSQLite {

	// Create constants
	public static final String SQLITE_TRUCK_DB = "Trucks";
	public static final String COl_ID = "ID";
	public static final String COL_TRUCK_NAME = "TRUCK_NAME";
	
	public static final String COL_LONGITUDE = "LONGITUDE";
	public static final String COL_LATITUDE = "LATITUDE";
	public static final String COL_HOURS_AT_LOCATION = "HOURS_AT_LOCATION";
	public static final String COL_OFFLINE_TIMESTAMP ="OFFLINE_TIMESTAMP";
	public static final String SELECT_ALL_OFFLINE_TRUCKS = "SELECT * FROM "+SQLITE_TRUCK_DB+" WHERE "+COL_OFFLINE_TIMESTAMP+" IS NOT NULL";

	public TruckServiceSQLiteStub(Context context) {
		super(context, SQLITE_TRUCK_DB, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		sqLiteDatabase.execSQL("Create Table " + SQLITE_TRUCK_DB + " ("
				+ COl_ID + " INTEGER PRIMARY KEY, " + COL_TRUCK_NAME
				+ " TEXT, "  + COL_LONGITUDE
				+ " TEXT, "+COL_LATITUDE+" TEXT, "+COL_HOURS_AT_LOCATION+" TEXT, "+COL_OFFLINE_TIMESTAMP+" TEXT);");
		

	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

	}

	

	@Override
	public void addRow(TruckOwner truckOwner)
			throws Exception {
		Date now = new Date();
		Timestamp timestamp = new Timestamp(now.getTime());
	
		ContentValues contentValues = new ContentValues();
		contentValues.put(COl_ID, truckOwner.getTruckID());
		contentValues.put(COL_TRUCK_NAME, truckOwner.getTruckName());
		
		if(truckOwner.getLongitude() != null && truckOwner.getLatitude() != null )
		{
			
			contentValues.put(COL_LONGITUDE, truckOwner.getLongitude());
			contentValues.put(COL_LATITUDE, truckOwner.getLatitude());
			contentValues.put(COL_HOURS_AT_LOCATION, truckOwner.getHoursAtLocation());
			contentValues.put(COL_OFFLINE_TIMESTAMP, timestamp.toString());
			
			}
		getWritableDatabase().replace(SQLITE_TRUCK_DB, null, contentValues);

	}

	@Override
	public void addRows(List<TruckOwner> truckOwners)
			throws Exception {
		//ok, so this is where we're adding the trucks in. 
		//first, let's go ahead and get all the trucks that are here locally. 
		List<TruckOwner> localTrucks = getAllRows();
		//we want to store the network and local IDs in arraylists
		ArrayList<Integer> localTruckIDs = new ArrayList<Integer>();
		ArrayList<Integer> networkTruckIDs = new ArrayList<Integer>();
		//so, let's loop over the local trucks and put their ids in the arraylist
		for (TruckOwner localTruck : localTrucks) {
			localTruckIDs.add(localTruck.getTruckID());
		}
		
		
		for (int i = 0; i < truckOwners.size(); i++) {
			TruckOwner truckOwner = truckOwners.get(i);
			//ok, so we want to see if there is a local truck object that is not in the network trucks
			networkTruckIDs.add(truckOwner.getTruckID());
			ContentValues contentValues = new ContentValues();
			contentValues.put(COl_ID, truckOwner.getTruckID());
			contentValues.put(COL_TRUCK_NAME, truckOwner.getTruckName());
			
			if(truckOwner.getLongitude() != null && truckOwner.getLatitude() != null){
			contentValues.put(COL_LONGITUDE, truckOwner.getLongitude());
			contentValues.put(COL_LATITUDE, truckOwner.getLatitude());
			contentValues.put(COL_HOURS_AT_LOCATION, truckOwner.getHoursAtLocation());
			}
			getWritableDatabase().replace(SQLITE_TRUCK_DB, null, contentValues);
//			}else{
//				
//				getWritableDatabase().delete(SQLITE_TRUCK_DB, whereClause, whereArgs)
//				
//				
//			}
			
		}
		
		
		//so  now that we've updated or inserted the new trucks, we now should delete the ones that don't exist anymore. 
		//loop over all the truck ids
		for (int i = 0; i < localTruckIDs.size(); i++) {
			
			if (!networkTruckIDs.contains(localTruckIDs.get(i))) {
				getWritableDatabase().delete(SQLITE_TRUCK_DB,COl_ID+"="+localTruckIDs.get(i) ,null);
				
			}else{
				continue;
			}
			
		}

	}
	
	

	@Override
	public TruckOwner getTruckByName(String truckName)
			throws Exception {
		TruckOwner truck = new TruckOwner();
		
		String sql =  "SELECT * FROM "+SQLITE_TRUCK_DB+" WHERE "+COL_TRUCK_NAME+"= \""+truckName+"\"";
		Cursor cursor = getReadableDatabase().rawQuery(sql, null);
		if(cursor.getCount() != 0)
		{
			cursor.moveToFirst();
			while(!cursor.isAfterLast())
			{
				truck.setTruckID(cursor.getInt(0));
				truck.setTruckName(cursor.getString(1));
				truck.setLongitude(cursor.getString(2));
				truck.setLatitude(cursor.getString(3));
				truck.setHoursAtLocation(cursor.getLong(4));
				
				cursor.moveToNext();
			}
			cursor.close();
		
		}
		
		return truck;
	}

	@Override
	public ArrayList<TruckOwner> getAllRows() throws Exception {
		
		ArrayList<TruckOwner> trucks = new ArrayList<TruckOwner>();
		//execute the query
		String select  = "SELECT * FROM "+SQLITE_TRUCK_DB;
		Cursor cursor = getReadableDatabase().rawQuery(select, null);
		 
		//iterate over the result 
		if(cursor.getCount()>0)
		{
			//move to the first row of the results
			cursor.moveToFirst();
			while(!cursor.isAfterLast())
			{
				TruckOwner truck = new TruckOwner();
				truck.setTruckID(cursor.getInt(0));
				truck.setTruckName(cursor.getString(1));
				Log.i("Truck data",cursor.getString(1));
				truck.setLongitude(cursor.getString(2));
				truck.setLatitude(cursor.getString(3));
				truck.setHoursAtLocation(cursor.getLong(4));
				//add to the array list
				trucks.add(truck);
				cursor.moveToNext();
				
			}
			cursor.close();
			
		}
		
		
		return trucks;
	}
	
	@Override
	public void updateTruckLocationOffline(TruckOwner truck) throws Exception
	{
		//concept here is the same as with the menus and reviews
		Date now = new Date();
		Timestamp timestamp = new Timestamp(now.getTime());
	
		ContentValues contentValues = new ContentValues();
		contentValues.put(COl_ID, truck.getTruckID());
		contentValues.put(COL_TRUCK_NAME, truck.getTruckName() );
		if(truck.getLongitude() != null && truck.getLatitude() != null ){
			contentValues.put(COL_LONGITUDE, truck.getLongitude());
			contentValues.put(COL_LATITUDE, truck.getLatitude());
			contentValues.put(COL_HOURS_AT_LOCATION, truck.getHoursAtLocation());
			contentValues.put(COL_OFFLINE_TIMESTAMP, timestamp.toString());
			}
		getWritableDatabase().replace(SQLITE_TRUCK_DB, null, contentValues);


		
	
	}

	@Override
	public ArrayList<TruckOwner> getOfflineTrucks() {
		ArrayList<TruckOwner> trucks = new ArrayList<TruckOwner>();
		//execute the query
		String select  = SELECT_ALL_OFFLINE_TRUCKS;
		Cursor cursor = getReadableDatabase().rawQuery(select, null);
		 
		//iterate over the result 
		if(cursor.getCount()>0)
		{
			//move to the first row of the results
			cursor.moveToFirst();
			while(!cursor.isAfterLast())
			{
				TruckOwner truck = new TruckOwner();
				truck.setTruckID(cursor.getInt(0));
				truck.setTruckName(cursor.getString(1));
				truck.setLongitude(cursor.getString(2));
				truck.setLatitude(cursor.getString(3));
				truck.setHoursAtLocation(cursor.getLong(4));
				//add to the array list
				trucks.add(truck);
				cursor.moveToNext();
				
			}
			cursor.close();
			
		}
		
		
		return trucks;
	}

	@Override
	public TruckOwner getTruckByTruckID(int truckID) {
TruckOwner truck = new TruckOwner();
		
		String sql =  "SELECT * FROM "+SQLITE_TRUCK_DB+" WHERE "+COl_ID+"= "+truckID;
		Cursor cursor = getReadableDatabase().rawQuery(sql, null);
		if(cursor.getCount() != 0)
		{
			cursor.moveToFirst();
			while(!cursor.isAfterLast())
			{
				truck.setTruckID(cursor.getInt(0));
				truck.setTruckName(cursor.getString(1));
				truck.setLongitude(cursor.getString(2));
				truck.setLatitude(cursor.getString(3));
				truck.setHoursAtLocation(cursor.getLong(4));
				
				cursor.moveToNext();
			}
			cursor.close();
		
		}
		
		return truck;}

	@Override
	public HashMap<Integer, TruckOwner> getTrucksAsHashMap() {
		HashMap<Integer, TruckOwner> trucks = new HashMap<Integer, TruckOwner>();		
		
		//execute the query
				String select  = "SELECT * FROM "+SQLITE_TRUCK_DB;
				Cursor cursor = getReadableDatabase().rawQuery(select, null);
				 
				//iterate over the result 
				if(cursor.getCount()>0)
				{
					//move to the first row of the results
					cursor.moveToFirst();
					while(!cursor.isAfterLast())
					{
						TruckOwner truck = new TruckOwner();
						truck.setTruckID(cursor.getInt(0));
						truck.setTruckName(cursor.getString(1));
						truck.setLongitude(cursor.getString(2));
						truck.setLatitude(cursor.getString(3));
						truck.setHoursAtLocation(cursor.getLong(4));
						//add to the hashmap
						trucks.put(truck.getTruckID(), truck);
						cursor.moveToNext();
						
					}
					cursor.close();
					
				}
				
				
		return trucks;
	}
}
