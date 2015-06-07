package com.cincyfoodtrucks.utilities;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class ImageUtil {
	
	
	/**
	 * Method to convert a String to a Bitmap
	 * @param string The string we need to convert 
	 * @return Bitmap the bitmap image we've converted
	 */
	public static Bitmap convertStringToBitmap(String string)
	{
		try{
	           byte [] encodeByte=Base64.decode(string,Base64.DEFAULT);
	           Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
	           return bitmap;
	         }catch(Exception e){
	           
	        	 e.getMessage();
	        	 
	        	 //we'll just return an empty bitmap image
	        	 int w = 150, h = 100;
	        	 Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
	        	 Bitmap bmp = Bitmap.createBitmap(w, h, conf); // this creates a MUTABLE bitmap
	        	 return bmp;
	         }
	 }


	/**
	 * Method to convert a BitMap to a string to save in the database
	 * @param bitmap the bitmap we wish to convert 
	 * @return String the converted string 
	 */
	public  static String bitmapToString(Bitmap bitmap){
        ByteArrayOutputStream baos = new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] arr = baos.toByteArray();
        String result=Base64.encodeToString(arr, Base64.DEFAULT);
        return result;
  }
	
	
	
	/**
	 * Method to convert a Blob to a String to save in the DB
	 * @param blob the blob to convert 
	 * @return String the converted String
	 * @throws Exception throws an exception if an error occurs 
	 */
	public static String convertBlobToString(Blob blob) throws Exception
	{
		//some magic to get the blob to a string
		byte[] bdata = blob.getBytes(1, (int) blob.length());
		String s = new String(bdata);
		return s;
		
	}
	
	
}
