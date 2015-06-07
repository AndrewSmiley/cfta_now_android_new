/**
 * 
 */
package com.cincyfoodtrucks.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cincyfoodtrucks.dto.TruckMenuItem;
import com.cincyfoodtrucks.dto.TruckOwner;

/**
 * @author pridemai
 *
 */
public class MenuServiceSQLiteStub extends SQLiteOpenHelper implements IMenuServiceSQLite {

	public static final String SQLITE_MENU_TABLE = "Menus";
	public static final String COL_ID = "MENU_ID";
	public static final String COL_DESCRIPTION = "DESCRIPTION";
	public static final String COL_PRICE = "PRICE";
	public static final String COL_TITLE = "TITLE";
	public static final String COL_TRUCK_ID = "TRUCK_ID";
	public static final String COL_OFFLINE_TIMESTAMP = "OFFLINE_TIMESTAMP";
	public static final String SELECT_ALL = "SELECT * FROM "+SQLITE_MENU_TABLE;
	public static final String SELECT_LAST_MENU_ITEM = "SELECT * FROM "+SQLITE_MENU_TABLE+ " ORDER BY "+COL_ID+" DESC LIMIT 1";
	public static final String SELECT_OFFLINE_MENU_ITEMS = "SELECT * FROM "+SQLITE_MENU_TABLE+" WHERE "+COL_OFFLINE_TIMESTAMP+ " IS NOT NULL";
	

	
	/**
	 * Constructor from the super class
	 * @param context The activity calling the MenuServiceSQLiteStub class
	 */
	public MenuServiceSQLiteStub(Context context)
	{
		super(context, SQLITE_MENU_TABLE, null, 1);
	}

	
	/* (non-Javadoc)
	 * @see com.cincyfoodtrucks.dao.IMenuServiceSQLite#addRow(android.view.Menu, android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void addMenuItem(TruckMenuItem menu) throws Exception {
		
		//What we're going to do here is add an offline timestamp if the item is being saved offline. It was be erased the next time the user is online
		Date now = new Date();
		Timestamp timestamp = new Timestamp(now.getTime());
		//get the last known menu id to keep in line with the remote DB
		int id = getLastMenuItemID();
		//increment it by 1 
		id++;
		ContentValues content  = new ContentValues();
		content.put(COL_ID, id);
		content.put(COL_DESCRIPTION, menu.getDescription());
		content.put(COL_PRICE, menu.getPrice());
		content.put(COL_TITLE, menu.getTitle());
		content.put(COL_TRUCK_ID, menu.getTruckID());
		content.put(COL_OFFLINE_TIMESTAMP, timestamp.toString());
		getWritableDatabase().replace(SQLITE_MENU_TABLE, null, content);

	}

	/* (non-Javadoc)
	 * @see com.cincyfoodtrucks.dao.IMenuServiceSQLite#addRows(java.util.List, android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void addRows(List<TruckMenuItem> menus) throws Exception {
		ArrayList<TruckMenuItem> localMenuItems;
		ArrayList<Integer> localMenuItemIDs, networkMenuItemIDs;
		localMenuItems = getAllMenuItems();
		networkMenuItemIDs = new ArrayList<Integer>();
		localMenuItemIDs = new ArrayList<Integer>();
		
		for (TruckMenuItem truckMenuItem : localMenuItems) {
			localMenuItemIDs.add(truckMenuItem.getmenuItemID());
		}
		for(int i = 0; i<menus.size(); i++)
		{
			ContentValues content = new ContentValues();
			TruckMenuItem menu = menus.get(i);
			networkMenuItemIDs.add(menu.getmenuItemID());
			content.put(COL_ID, menu.getmenuItemID());
			content.put(COL_DESCRIPTION, menu.getDescription());
			content.put(COL_PRICE, menu.getPrice());
			content.put(COL_TITLE, menu.getTitle());
			content.put(COL_TRUCK_ID, menu.getTruckID());
			getWritableDatabase().replace(SQLITE_MENU_TABLE, null, content);
		}
		
		for (int i = 0; i < localMenuItemIDs.size(); i++) {
			if(!networkMenuItemIDs.contains(localMenuItemIDs.get(i))){
				getWritableDatabase().delete(SQLITE_MENU_TABLE, COL_ID+"="+localMenuItemIDs.get(i),null);
			}else{
				continue;
			}
			
		}

	}
	

	/* (non-Javadoc)
	 * @see com.cincyfoodtrucks.dao.IMenuServiceSQLite#getAllRows(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public ArrayList<TruckMenuItem> getAllMenuItems() throws Exception {
		ArrayList<TruckMenuItem> items = new ArrayList<TruckMenuItem>();
		String query = SELECT_ALL;
		Cursor cursor = getReadableDatabase().rawQuery(query, null);
		 
		//iterate over the result 
		if(cursor.getCount()>0)
		{
			//move to the first row of the results
			cursor.moveToFirst();
			while(!cursor.isAfterLast())
			{
				TruckMenuItem item = new TruckMenuItem();
				
				item.setMenuItemID(cursor.getInt(0));
				item.setDescription(cursor.getString(1));
				item.setPrice(cursor.getString(2));
				item.setTitle(cursor.getString(3));
				item.setTruckID(cursor.getInt(4));
				items.add(item);
				cursor.moveToNext();
				
			}
			cursor.close();
			
		
	}
		return items;
	}

	/* (non-Javadoc)
	 * @see com.cincyfoodtrucks.dao.IMenuServiceSQLite#updateRow(long, android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void updateFood(String foodName) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE "+SQLITE_MENU_TABLE+" ("+COL_ID+" INTEGER PRIMARY KEY, "+COL_DESCRIPTION+" TEXT, "+COL_PRICE+" TEXT, "+COL_TITLE+" TEXT, "+COL_TRUCK_ID+" INTEGER,"+COL_OFFLINE_TIMESTAMP+" TEXT);");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<TruckMenuItem> searchForFoods(String criteria) {
		//create the ArrayList to hold the items found
		ArrayList<TruckMenuItem> items = new ArrayList<TruckMenuItem>();
		//create the query 
		String query = "SELECT * FROM "+SQLITE_MENU_TABLE+" WHERE "+COL_TITLE+" LIKE '%"+criteria+"%' OR "+COL_DESCRIPTION+" LIKE '%"+criteria+"%'";
		Cursor cursor = getReadableDatabase().rawQuery(query, null);
		 
		//iterate over the result 
		if(cursor.getCount()>0)
		{
			//move to the first row of the results
			cursor.moveToFirst();
			while(!cursor.isAfterLast())
			{
				TruckMenuItem item = new TruckMenuItem();
				
				item.setMenuItemID(cursor.getInt(0));
				item.setDescription(cursor.getString(1));
				item.setPrice(cursor.getString(2));
				item.setTitle(cursor.getString(3));
				item.setTruckID(cursor.getInt(4));
				items.add(item);
				cursor.moveToNext();
				
			}
			cursor.close();
			
		
	}
		return items;
	}


	@Override
	public ArrayList<TruckMenuItem> getItemsByTruckID(int truckID) {
		
		ArrayList<TruckMenuItem> items = new ArrayList<TruckMenuItem>();
	
		String query = SELECT_ALL+" WHERE "+COL_TRUCK_ID+" = "+truckID;
		Cursor cursor = getReadableDatabase().rawQuery(query, null);
		 
		//iterate over the result 
		if(cursor.getCount()>0)
		{
			//move to the first row of the results
			cursor.moveToFirst();
			while(!cursor.isAfterLast())
			{
				TruckMenuItem item = new TruckMenuItem();
				
				item.setMenuItemID(cursor.getInt(0));
				item.setDescription(cursor.getString(1));
				item.setPrice(cursor.getString(2));
				item.setTitle(cursor.getString(3));
				item.setTruckID(cursor.getInt(4));
				items.add(item);
				cursor.moveToNext();
				
			}
			cursor.close();
			
		
	}
		return items;
	}


	@Override
	public int getLastMenuItemID() {
		String query = SELECT_LAST_MENU_ITEM;
		Cursor cursor = getReadableDatabase().rawQuery(query, null);
		TruckMenuItem item = new TruckMenuItem();
		//iterate over the result 
		if(cursor.getCount()>0)
		{
			//move to the first row of the results
			cursor.moveToFirst();
			while(!cursor.isAfterLast())
			{
			
				
				item.setMenuItemID(cursor.getInt(0));
				item.setDescription(cursor.getString(1));
				item.setPrice(cursor.getString(2));
				item.setTitle(cursor.getString(3));
				item.setTruckID(cursor.getInt(4));
			
				cursor.moveToNext();
				
			}
			cursor.close();
			
		
	}
		return item.getmenuItemID();
	}


	@Override
	public ArrayList<TruckMenuItem> getAllOfflineMenuItems() {
		ArrayList<TruckMenuItem> items = new ArrayList<TruckMenuItem>();
		String query = SELECT_OFFLINE_MENU_ITEMS;
		Cursor cursor = getReadableDatabase().rawQuery(query, null);
		 
		//iterate over the result 
		if(cursor.getCount()>0)
		{
			//move to the first row of the results
			cursor.moveToFirst();
			while(!cursor.isAfterLast())
			{
				TruckMenuItem item = new TruckMenuItem();
				
				item.setMenuItemID(cursor.getInt(0));
				item.setDescription(cursor.getString(1));
				item.setPrice(cursor.getString(2));
				item.setTitle(cursor.getString(3));
				item.setTruckID(cursor.getInt(4));
				items.add(item);
				cursor.moveToNext();
				
			}
			cursor.close();
			
		
	}
		return items;
	}
	/**
	 * Method to get a menu item using the truck id and string menu item name 
	 * @param itemName
	 * @param truckID
	 * @return
	 */
	public TruckMenuItem getItemByName(String itemName, int truckID)
	{
		//create the query
		String query = "Select * from "+SQLITE_MENU_TABLE+" where "+COL_TITLE+"='"+itemName+"' and "+COL_TRUCK_ID+"="+truckID;
		//execute the query
		Cursor cursor = getReadableDatabase().rawQuery(query, null);
		
		TruckMenuItem item = new TruckMenuItem();
		if(cursor.getCount()>0)
		{
			//move to the first row of the results
			cursor.moveToFirst();
			while(!cursor.isAfterLast())
			{
				
				
				item.setMenuItemID(cursor.getInt(0));
				item.setDescription(cursor.getString(1));
				item.setPrice(cursor.getString(2));
				item.setTitle(cursor.getString(3));
				item.setTruckID(cursor.getInt(4));
				
				cursor.moveToNext();
				
			}
			cursor.close();
			
		
	}
		return item;
		
		
	}
}
