package com.cincyfoodtrucks.dao;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cincyfoodtrucks.dto.FacebookPage;

public class FacebookPageSQLite extends SQLiteOpenHelper implements IFacebookPageSQLite {
	public static final String SQLITE_TABLE_NAME="FacebookPages";
	public static final String COL_ID = "id";
	public static final String COL_FACEBOOK_PAGE = "facebook_page";
	public static final String COL_TRUCK_ID = "truck_id";
	public static final String SELECT_ALL = "select * from "+SQLITE_TABLE_NAME;
	
	public FacebookPageSQLite(Context context) {
		super(context, SQLITE_TABLE_NAME, null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void addFacebookPage(FacebookPage page) {
		ContentValues content  = new ContentValues();
		content.put(COL_ID, page.getId());
		content.put(COL_FACEBOOK_PAGE, page.getPageName());
		content.put(COL_TRUCK_ID, page.getTruckID());
		

		getWritableDatabase().replace(SQLITE_TABLE_NAME, null, content);
		// TODO Auto-generated method stub
//		return false;
	}

	@Override
	public ArrayList<FacebookPage> getAllFacebookPages() {
		ArrayList<FacebookPage> items = new ArrayList<FacebookPage>();
		String query = SELECT_ALL;
		Cursor cursor = getReadableDatabase().rawQuery(query, null);
		 
		//iterate over the result 
		if(cursor.getCount()>0)
		{
			//move to the first row of the results
			cursor.moveToFirst();
			while(!cursor.isAfterLast())
			{
				FacebookPage page=new FacebookPage();
				page.setId(cursor.getInt(0));
				page.setPageName(cursor.getString(1));
				page.setTruckID(cursor.getInt(2));
				items.add(page);
				cursor.moveToNext();
				
			}
			cursor.close();
			
		
	}
		return items;
	}
	
	@Override
	public void onCreate(SQLiteDatabase arg0) {
		arg0.execSQL("CREATE TABLE "+SQLITE_TABLE_NAME+" ("+COL_ID+" INTEGER PRIMARY KEY, "+COL_FACEBOOK_PAGE+" TEXT, "+COL_TRUCK_ID+" INTEGER)");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		//i don't think we need to do anything here?? 

	}

}
