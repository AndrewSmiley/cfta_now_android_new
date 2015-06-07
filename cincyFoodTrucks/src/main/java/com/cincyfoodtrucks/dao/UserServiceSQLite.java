package com.cincyfoodtrucks.dao;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cincyfoodtrucks.dto.User;
import com.google.android.gms.internal.t;

public class UserServiceSQLite extends SQLiteOpenHelper implements
		IUserServiceSQLite {
	public static final String USER_DB = "Users";
	public static final String COL_USER_ID = "User_ID";
	public static final String COL_EMAIL = "Email";
	public static final String COL_PASSWORD = "Password";
	public static final String COL_FIRST_NAME = "First_Name";
	public static final String COL_LAST_NAME = "Last_Name";
	public static final String COL_TRUCK_ID = "Truck_ID";
	public static final String COL_OFFLINE_TIMESTAMP = "Offline_Timestamp";
	public static final String SELECT_ALL_OFFLINE_TRUCKS = "SELECT * FROM "+USER_DB+" WHERE "+COL_OFFLINE_TIMESTAMP+" IS NOT NULL";

	
	

	public UserServiceSQLite(Context context) {
		super(context, USER_DB, null, 1);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("Create Table " + USER_DB + " (" + COL_USER_ID
				+ " INTEGER PRIMARY KEY , " + COL_EMAIL + " TEXT, "
				+ COL_PASSWORD + " TEXT, " + COL_FIRST_NAME + " TEXT, "
				+ COL_LAST_NAME + " TEXT, "+COL_TRUCK_ID+" INTEGER, "+COL_OFFLINE_TIMESTAMP+" TEXT);");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
	
	
	@Override
	public void addAllUsers(ArrayList<User> users) throws Exception {
		ArrayList<User> localUsers = getAllUsers();
		ArrayList<Integer> networkUserIDs = new ArrayList<Integer>();
		ArrayList<Integer> localUserIDs = new ArrayList<Integer>();
		for (User user : localUsers) {
			localUserIDs.add(user.getUserID());
		}
		
		for (int i = 0; i < users.size(); i++) {
			networkUserIDs.add(users.get(i).getUserID());
			ContentValues contentValues = new ContentValues();
			contentValues.put(COL_USER_ID, users.get(i).getUserID());
			contentValues.put(COL_EMAIL, users.get(i).getUsername());
			contentValues.put(COL_PASSWORD, users.get(i).getPassword());
			contentValues.put(COL_FIRST_NAME, users.get(i).getFirstName());
			contentValues.put(COL_LAST_NAME, users.get(i).getLastName());
			contentValues.put(COL_TRUCK_ID,users.get(i).getTruckID());
			getWritableDatabase().replace(USER_DB, null, contentValues);
			

		}
		//delete
		for (int i = 0; i < localUserIDs.size(); i++) {
			if(!networkUserIDs.contains(localUserIDs.get(i))){
				getWritableDatabase().delete(USER_DB, COL_USER_ID+"="+localUserIDs.get(i), null);
			}
		}
		

	}

	@Override
	public boolean isLoggedIn(String username, String password)
			throws NoSuchAlgorithmException, Exception {
		boolean result = false;

		//make sure we're actually passing a username and password in
		if (username.equalsIgnoreCase("") || password.equalsIgnoreCase("")) {
			return false;
		}
		String query = "select * from " + USER_DB + " where " + COL_EMAIL
				+ " = '" + username + "' and " + COL_PASSWORD + " = '"
				+ password + "'";

		Cursor cursor = getReadableDatabase().rawQuery(query, null);
		ArrayList<User> users = this.getAllUsers();
		for (User user : users) {
			if(user.getUsername().equals(username) && user.getPassword().equals(password) ){
				result = true;
				break;
			}
		}
		
		
		return result;
//		// iterate over the result
//		if (cursor.getCount() != 1) {
//			result =  false;
//				
//					return result;
//		}else{
//				
//				result =  true;
//		
//		return result;
//		}
	}

	@Override
	public String encryptPassword(String password) throws NoSuchAlgorithmException {
		MessageDigest m;
		try {
			m = MessageDigest.getInstance("MD5");
			m.reset();
			m.update(password.getBytes());
			byte[] digest = m.digest();
			BigInteger bigInt = new BigInteger(1, digest);
			String encryptedPassword = bigInt.toString(16);
			// Now we need to zero pad it if you actually want the full 32
			// chars.
			while (encryptedPassword.length() < 32) {
				encryptedPassword = "0" + encryptedPassword;
			}
			return encryptedPassword;

		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
			return "";
		}

	}



	@Override
	public User getUserFromUsername(String username)  throws Exception{
		User user = new User();
		
		if (username.equalsIgnoreCase("") ) {
			return null;
		}
		String query = "SELECT * FROM " + USER_DB + " WHERE " + COL_EMAIL
				+ " = '" + username + "'";

		Cursor cursor = getReadableDatabase().rawQuery(query, null);
		
		
		// iterate over the result
		if (cursor.getCount() > 0) {
		
		
			cursor.moveToFirst();
			while(!cursor.isAfterLast())
			{
				user.setUserID(cursor.getInt(0));
				user.setUsername(cursor.getString(1));
				user.setPassword(cursor.getString(2));
				user.setFirstName(cursor.getString(3));
				if(cursor.getString(4) != null)
				{
					user.setLastName(cursor.getString(4));
				}
				user.setTruckID(cursor.getInt(5));
				cursor.moveToNext();
			}
			
			
			return user;
		}
		else{
			
			throw new Exception();
		}
	}

	@Override
	public void updatePassword(User user) {
		Date now = new Date();
		Timestamp timestamp = new Timestamp(now.getTime());
	
		ContentValues contentValues = new ContentValues();
		contentValues.put(COL_USER_ID, user.getUserID());
		contentValues.put(COL_EMAIL, user.getUsername());
		contentValues.put(COL_PASSWORD, user.getPassword());
		contentValues.put(COL_FIRST_NAME, user.getFirstName());
		contentValues.put(COL_LAST_NAME, user.getLastName());
		contentValues.put(COL_TRUCK_ID,user.getTruckID());
		contentValues.put(COL_OFFLINE_TIMESTAMP, timestamp.toString());
		getWritableDatabase().replace(USER_DB, null, contentValues);
		

		
	}

	@Override
	public ArrayList<User> getAllOfflineUsers() throws Exception {
		ArrayList<User> users = new ArrayList<User>();
		Cursor cursor = getWritableDatabase().rawQuery(SELECT_ALL_OFFLINE_TRUCKS, null);
		
		
		// iterate over the result
		if (cursor.getCount() >0) {
		
			cursor.moveToFirst();
			while(!cursor.isAfterLast())
			{
				
				User user = new User();
				user.setUserID(cursor.getInt(0));
				user.setUsername(cursor.getString(1));
				user.setPassword(cursor.getString(2));
				user.setFirstName(cursor.getString(3));
				if(cursor.getString(4) != null)
				{
					user.setLastName(cursor.getString(4));
				}
				user.setTruckID(cursor.getInt(5));
				users.add(user);
				cursor.moveToNext();
			}
			
			
		}else{
			
			return users;
		}
	
		return users;

}


//method to get all the local users from the DB
public ArrayList<User> getAllUsers() throws Exception{
	
	ArrayList<User> users =new ArrayList<User>();
	
	String query = "SELECT * FROM " + USER_DB;
	Cursor cursor = getReadableDatabase().rawQuery(query, null);
	
	
	// iterate over the result
	if (cursor.getCount() > 0) {
	
	
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			User user = new User();
			
			user.setUserID(cursor.getInt(0));
			user.setUsername(cursor.getString(1));
			user.setPassword(cursor.getString(2));
			user.setFirstName(cursor.getString(3));
			if(cursor.getString(4) != null)
			{
				user.setLastName(cursor.getString(4));
			}
			user.setTruckID(cursor.getInt(5));
			users.add(user);
			cursor.moveToNext();
			
		}
		
		
		return users;
	}
	else{
		return new ArrayList<User>();
		//throw new Exception();
	}
}
}