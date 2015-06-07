package com.cincyfoodtrucks.dao;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Base64;

import com.cincyfoodtrucks.dto.MenuItemImage;
import com.cincyfoodtrucks.utilities.ImageUtil;

/**
 * Class to Handle the SQLite DB actions related to menu item images 
 * @author Andrew Smiley
 *
 */
public class MenuItemImageServiceSQLite extends SQLiteOpenHelper {
	public static final String SQLITE_MENU_ITEM_IMAGE_TABLE = "Menu_Item_Images";
	public static final String COL_IMAGE_ID = "Image_ID";
	public static final String COL_IMAGE_CONTENT = "Image_Content"; 
	public static final String COL_MENU_ITEM_ID = "Menu_Item_ID";
	public static final String SELECT_LAST_IMAGE_ITEM = "SELECT * FROM "+SQLITE_MENU_ITEM_IMAGE_TABLE+ " ORDER BY "+COL_IMAGE_ID+" DESC LIMIT 1";
	
	/**
	 * Constructor from the super class
	 * @param context The activity calling the MenuServiceSQLiteStub class
	 */
	public MenuItemImageServiceSQLite(Context context)
	{
		super(context, SQLITE_MENU_ITEM_IMAGE_TABLE, null, 1);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		//the on create method 
			db.execSQL("CREATE TABLE "+SQLITE_MENU_ITEM_IMAGE_TABLE+" ("+COL_IMAGE_ID+" INTEGER PRIMARY KEY,  "+COL_IMAGE_CONTENT+" BLOB, "+COL_MENU_ITEM_ID+" INTEGER)");
		
	}
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Method to get a menu item image from the DB
	 * @param menuItemID the id of the menu item we wish to get the  
	 * @return
	 */
	public MenuItemImage getMenuItemImage(int menuItemID)
	{
		MenuItemImage image = new MenuItemImage();
		
		return image;
		
	}
	
	/**
	 * Method to add all the images to the database from the remote server
	 * @param images the list of images we wish to add from the remote server 
	 */
	public void addImagesToDB(List<MenuItemImage> images) throws Exception
	{
		
		for(int i = 0; i<images.size(); i++)
		{
			ContentValues content = new ContentValues();
			MenuItemImage image = images.get(i);
			content.put(COL_IMAGE_ID, image.getImageID());
			
			content.put(COL_IMAGE_CONTENT,ImageUtil.convertBlobToString(image.getBinaryData()));
			content.put(COL_MENU_ITEM_ID, image.getMenuItemID());
			getWritableDatabase().replace(SQLITE_MENU_ITEM_IMAGE_TABLE, null, content);
		}

		
	}
	
	/**
	 * Method to save an image locally to the db if the network connection is unavailable 
	 * @param image the image we wish to save 
	 * @throws Exception Throws an exception if an error occurs 
	 */
	public void offlineAddImageToDB(MenuItemImage image) throws Exception
	{
		ContentValues content = new ContentValues();
		content.put(COL_IMAGE_ID, image.getImageID());
		content.put(COL_IMAGE_CONTENT,ImageUtil.convertBlobToString(image.getBinaryData()));
		content.put(COL_MENU_ITEM_ID, image.getMenuItemID());
		getWritableDatabase().replace(SQLITE_MENU_ITEM_IMAGE_TABLE, null, content);
	
		
	}
	

	
	
	
	public int getLastImageID()
	{
		String query = SELECT_LAST_IMAGE_ITEM;
		Cursor cursor = getReadableDatabase().rawQuery(query, null);
		MenuItemImage	image = new MenuItemImage();
		//iterate over the result 
		if(cursor.getCount()>0)
		{
			//move to the first row of the results
			cursor.moveToFirst();
			while(!cursor.isAfterLast())
			{
			
				
				image.setImageID(cursor.getInt(0));
				image.setImageContent(ImageUtil.convertStringToBitmap(cursor.getString(1)));
				image.setMenuItemID(cursor.getInt(2));
				
				cursor.moveToNext();
				
			}
			cursor.close();
		
	}
		return image.getImageID();
	}
	

}
	

