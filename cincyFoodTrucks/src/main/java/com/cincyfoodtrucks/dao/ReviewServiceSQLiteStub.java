package com.cincyfoodtrucks.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cincyfoodtrucks.dto.Review;

public class ReviewServiceSQLiteStub extends SQLiteOpenHelper implements
		IReviewServiceSQLite {

	// Create Constants
	public static final String SQLITE_REVIEW_DB = "Reviews";
	public static final String COL_ID = "ID";
	public static final String COL_DESCRIPTION = "DESCRIPTION";
	public static final String COL_TRUCK_ID = "TRUCK_ID";
	public static final String COL_RATING = "RATING";
	public static final String COL_REVIEWER = "REVIEWER";
	public static final String COL_TIMESTAMP = "TIMESTAMP";
	public static final String SELECT_ALL  = "SELECT * FROM "+SQLITE_REVIEW_DB;
	public static final String SELECT_LAST_REVIEW = "SELECT * FROM "+SQLITE_REVIEW_DB+ " ORDER BY "+COL_ID+" DESC LIMIT 1";
	public static final String SELECT_OFFLINE_REVIEWS = "SELECT * FROM "+SQLITE_REVIEW_DB+" WHERE "+COL_TIMESTAMP+ " IS NOT NULL";
	public static final String DELETE_OFFLINE_REVIEW = "DELETE FROM "+SQLITE_REVIEW_DB+ " WHERE "+COL_ID+"= ";

	/**
	 * Constructor from superclass SQLiteOpenHelper
	 * 
	 * @param context
	 */
	public ReviewServiceSQLiteStub(Context context) {
		super(context, SQLITE_REVIEW_DB, null, 1);
	}

	

	@Override
	public void addReview(Review review) throws Exception {
		
		int reviewNumber = getLastReviewNumber();
		reviewNumber ++;
		Date now = new Date();
		Timestamp timestamp = new Timestamp(now.getTime());
				
		ContentValues contentValues = new ContentValues();
		contentValues.put(COL_ID, reviewNumber);
		contentValues.put(COL_DESCRIPTION, review.getDescription());
		contentValues.put(COL_RATING, review.getRating());
		contentValues.put(COL_REVIEWER, review.getReviewerName());
		contentValues.put(COL_TRUCK_ID, review.getTruckID());
		contentValues.put(COL_TIMESTAMP, timestamp.toString());
		getWritableDatabase().insert(SQLITE_REVIEW_DB, null, contentValues);
	}

	@Override
	public void addReviews(ArrayList<Review> reviews)
			throws Exception {
		
		ArrayList<Review> localReviews = getAllReviews();
		ArrayList<Integer> localReviewIDs = new ArrayList<Integer>();
		ArrayList<Integer> networkReviewIDs = new ArrayList<Integer>();
		for (Review review : localReviews) {
			localReviewIDs.add(review.getReviewNumber());
		}
		for (int i = 0; i < reviews.size(); i++) {
			Review review = reviews.get(i);
			networkReviewIDs.add(review.getReviewNumber());
			ContentValues contentValues = new ContentValues();
			contentValues.put(COL_ID, review.getReviewNumber());
			contentValues.put(COL_DESCRIPTION, review.getDescription());
			contentValues.put(COL_RATING, review.getRating());
			contentValues.put(COL_REVIEWER, review.getReviewerName());
			contentValues.put(COL_TRUCK_ID, review.getTruckID());
			getWritableDatabase().replace(SQLITE_REVIEW_DB, null, contentValues);

		}
		
		for (int i = 0; i < localReviewIDs.size(); i++) {
			if (!networkReviewIDs.contains(localReviewIDs.get(i))) {
				getWritableDatabase().delete(SQLITE_REVIEW_DB, COL_ID+"="+localReviewIDs.get(i), null);
				
			}
			
		}

	}


	

	@Override
	public ArrayList<Review> getAllReviews() throws Exception {
		ArrayList<Review> reviews = new ArrayList<Review>();
		//execute the query
		String select  = SELECT_ALL;
		Cursor cursor = getReadableDatabase().rawQuery(select, null);
		 
		//iterate over the result 
		if(cursor.getCount()>0)
		{
			//move to the first row of the results
			cursor.moveToFirst();
			while(!cursor.isAfterLast())
			{
				Review review = new Review();
				//truck.setTruckID(cursor.getInt(0));
				review.setReviewNumber(cursor.getInt(0));
				review.setDescription(cursor.getString(1));
				review.setRating(cursor.getInt(2));
				review.setReviewerName(cursor.getString(3));
				review.setTruckID(cursor.getInt(4));
				reviews.add(review);
				cursor.moveToNext();
				
			}
			cursor.close();
			
		}
		
		
		return reviews;
	}
	

	@Override
	public int getLastReviewNumber() {
		//execute the query
		Review review = new Review();
				String select  = SELECT_LAST_REVIEW;
				Cursor cursor = getReadableDatabase().rawQuery(select, null);
				 
				//iterate over the result 
				if(cursor.getCount()>0)
				{
					//move to the first row of the results
					cursor.moveToFirst();
					while(!cursor.isAfterLast())
					{

						//truck.setTruckID(cursor.getInt(0));
						review.setReviewNumber(cursor.getInt(0));
						review.setDescription(cursor.getString(1));
						review.setRating(cursor.getInt(2));
						review.setReviewerName(cursor.getString(3));
						review.setTruckID(cursor.getInt(4));
						
						cursor.moveToNext();
						
					}
					cursor.close();
					
					
				}
				
				return review.getReviewNumber();
	}



	@Override
	public ArrayList<Review> getOfflineReviews() {
		ArrayList<Review> reviews = new ArrayList<Review>();
		//execute the query
		String select  = SELECT_OFFLINE_REVIEWS;
		Cursor cursor = getReadableDatabase().rawQuery(select, null);
		 
		//iterate over the result 
		if(cursor.getCount()>0)
		{
			//move to the first row of the results
			cursor.moveToFirst();
			while(!cursor.isAfterLast())
			{
				Review review = new Review();
				
				review.setReviewNumber(cursor.getInt(0));
				review.setDescription(cursor.getString(1));
				review.setRating(cursor.getInt(2));
				review.setReviewerName(cursor.getString(3));
				review.setTruckID(cursor.getInt(4));
				reviews.add(review);
				cursor.moveToNext();
				
			}
			
		}
		
		
		return reviews;
	}

	
	


	@Override
	public ArrayList<Review> getReviewsByTruckID(int truckID) throws Exception {
		
		
		ArrayList<Review> reviews = new ArrayList<Review>();
		//create query
		String query = SELECT_ALL+" WHERE "+COL_TRUCK_ID+"= "+truckID;
		
		Cursor cursor = getReadableDatabase().rawQuery(query, null);
		//iterate over the result 
				if(cursor.getCount()>0)
				{
					//move to the first row of the results
					cursor.moveToFirst();
					while(!cursor.isAfterLast())
					{
						Review review = new Review();
						//truck.setTruckID(cursor.getInt(0));
						review.setReviewNumber(cursor.getInt(0));
						review.setDescription(cursor.getString(1));
						review.setRating(cursor.getInt(2));
						review.setReviewerName(cursor.getString(3));
						review.setTruckID(cursor.getInt(4));
						reviews.add(review);
						cursor.moveToNext();
						
					}
					cursor.close();
				}else{
					throw new Exception();
				}
				
				return reviews;
				
		
	}
	


	@Override
	public void deleteOfflineReview(int reviewID) {
		String query = DELETE_OFFLINE_REVIEW+reviewID;
		getWritableDatabase().rawQuery(query, null);
		
	}

	
	
	@Override
	public void updateRow(long rowID, SQLiteDatabase db) throws Exception {

	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		sqLiteDatabase.execSQL("Create Table " + SQLITE_REVIEW_DB + "("
				+ COL_ID + " INTEGER PRIMARY KEY, " + COL_DESCRIPTION
				+ " TEXT, " + COL_RATING + " TEXT, " + COL_REVIEWER + " TEXT, "
				+ COL_TRUCK_ID + " TEXT,"+COL_TIMESTAMP+" INTEGER);");

	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

	}






}
