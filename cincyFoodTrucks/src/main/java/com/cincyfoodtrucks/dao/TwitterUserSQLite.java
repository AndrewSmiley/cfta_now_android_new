package com.cincyfoodtrucks.dao;

import java.util.ArrayList;

import com.cincyfoodtrucks.dto.FacebookPage;
import com.cincyfoodtrucks.dto.TwitterUser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class TwitterUserSQLite extends SQLiteOpenHelper implements
		ITwitterUserSQLite {
	public static final String SQLITE_TABLE_NAME = "TwitterUsers";
	public static final String COL_ID = "id";
	public static final String COL_TWITTER_USERNAME = "twitter_username";
	public static final String COL_TRUCK_ID = "truck_id";
	public static final String SELECT_ALL = "select * from "+SQLITE_TABLE_NAME;

	public TwitterUserSQLite(Context context) {
		super(context, SQLITE_TABLE_NAME, null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ArrayList<TwitterUser> getAllTwitterUsers() {
		ArrayList<TwitterUser> items = new ArrayList<TwitterUser>();
		String query = SELECT_ALL;
		Cursor cursor = getReadableDatabase().rawQuery(query, null);
		 
		//iterate over the result 
		if(cursor.getCount()>0)
		{
			//move to the first row of the results
			cursor.moveToFirst();
			while(!cursor.isAfterLast())
			{
				TwitterUser twitterUser = new TwitterUser();
				twitterUser.setId(cursor.getInt(0));
				twitterUser.setTruckID(cursor.getInt(2));
				twitterUser.setTwitterUsername(cursor.getString(1));
				items.add(twitterUser);
				cursor.moveToNext();
				
			}
			cursor.close();
			
		
	}
		return items;
	}

	@Override
	public void addTwitterUser(TwitterUser user) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(COL_ID, user.getId());
		contentValues.put(COL_TWITTER_USERNAME, user.getTwitterUsername());
		contentValues.put(COL_TRUCK_ID, user.getTruckID());
		getWritableDatabase().replace(SQLITE_TABLE_NAME, null, contentValues);

	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		arg0.execSQL("CREATE TABLE "+SQLITE_TABLE_NAME+" ("+COL_ID+" INTEGER PRIMARY KEY, "+COL_TWITTER_USERNAME+" TEXT, "+COL_TRUCK_ID+" INTEGER)");
		

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
