package com.cincyfoodtrucks.integration;


import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cincyfoodtrucks.R;
import com.cincyfoodtrucks.dao.INetworkUser;
import com.cincyfoodtrucks.dao.IUserServiceSQLite;
import com.cincyfoodtrucks.dao.NetworkUserDAO;
import com.cincyfoodtrucks.dao.UserServiceSQLite;
import com.cincyfoodtrucks.dto.User;

public class UserIntegration implements IUserIntegration {
	SharedPreferences preferences;
	IUserServiceSQLite userService;
	Context context;

	
	public UserIntegration(Context newContext)
	{
		context = newContext;
	 preferences= PreferenceManager.getDefaultSharedPreferences(newContext);
	 userService = new UserServiceSQLite(newContext);
	}	
	
	
	
	/* (non-Javadoc)
	 * @see com.cincyfoodtrucks.integration.IUserIntegration#setButtonViewByLogin(android.widget.Button)
	 */
	@Override
	public void setButtonViewByLogin(Button btn) throws NoSuchAlgorithmException, Exception
	{
		//
		
		if(isLoggedIn(getUsernameFromSharedPreferences(), getEncryptedPasswordFromSharedPreferences()))
		{
			btn.setVisibility(View.VISIBLE);
		}else
		{
			btn.setVisibility(View.GONE);
		}
		
		
		
	}
	/* (non-Javadoc)
	 * @see com.cincyfoodtrucks.integration.IUserIntegration#getUsernameFromSharedPreferences()
	 */
	@Override
	public String getUsernameFromSharedPreferences()
	{
		//get the username
		String username = preferences.getString(context.getResources().getString(R.string.strPreferencesUsername), "");
		
		return username;
		
		
	}
	
	
	/* (non-Javadoc)
	 * @see com.cincyfoodtrucks.integration.IUserIntegration#getPasswordFromSharedPreferences()
	 */
	@Override
	public String getEncryptedPasswordFromSharedPreferences() throws Exception
	{
		//get the password 
		String password = preferences.getString(context.getResources().getString(R.string.strPreferencesPassword), "");

		//encrypt 
		password = userService.encryptPassword(password);
		return password;
		
		
		
	}
	
	
	@Override
	public boolean isLoggedIn(String username, String password) {
		boolean result;
		try {
			//encrypt the password
		//	String encryptedPassword = userService.encryptPassword(password);
		
		
		
		
			//check to see if the user is logged in
			result = userService.isLoggedIn(username, password);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = false;
		}
		if(result)
		{
			return true;
			
		}else{
		
		return false;}
		
	}
	
	
	@Override
	public void updateUsernameAndPasswordInPrefs(String username, String password) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(context.getResources().getString(R.string.strPreferencesUsername), username);
		editor.putString(context.getResources().getString(R.string.strPreferencesPassword), password);
		editor.apply();
		
	}

	
	@Override
	public User getUserFromUsername(String username){
		User user;
		try {
			user = userService.getUserFromUsername(username);
		} catch (Exception e) {
			Toast.makeText(context, context.getResources().getString(R.string.strGenericError), Toast.LENGTH_SHORT).show();
			e.printStackTrace(); 
			user = new User();
		}
		return user;
	}
	
	@Override
	public String getPasswordFromSharedPrefs() {
		//get the password 
		String password = preferences.getString(context.getResources().getString(R.string.strPreferencesPassword), "");
		return password;

	}
	

	@Override
	public User getUserFromSharedPrefs() {
		User user;
		try {
			user = userService.getUserFromUsername(getUsernameFromSharedPreferences());
		} catch (Exception e) {
			Toast.makeText(context, context.getResources().getString(R.string.strGenericError), Toast.LENGTH_SHORT).show();
			e.printStackTrace(); 
			user = new User();
		}
		return user;
	}
	
	
	/* (non-Javadoc)
	 * @see com.cincyfoodtrucks.integration.IUserIntegration#executeUserAsyncTask()
	 */
	@Override
	public void executeGetUserData() throws InterruptedException{
	GetUserData gud = new GetUserData(context);
	Thread t = new Thread(gud);
	t.start();
	t.join();
	
		
	}
	
	
	@Override
	public void executeSendUserData(User user) throws InterruptedException {
		SendUserData sud = new SendUserData(context, user);
		Thread t = new Thread(sud);
		t.start();
		t.join();
		
	}
	
	public User getUserFromSharedPreferences() throws Exception{
		User user = userService.getUserFromUsername(getUsernameFromSharedPreferences());
		return user;
		
	}
	
	
	public String encryptPassword(String password) throws Exception
	{
		String encryptedPassword = userService.encryptPassword(password);
		return encryptedPassword;
	}
	
	
	private class GetUserData implements Runnable {
	       /** The system calls this to perform work in a worker thread and
	        * delivers it the parameters given to AsyncTask.execute() */
	   	INetworkUser networkUserService;
	   	IUserServiceSQLite userServiceSQLite;
	   	Context context;
	   	public GetUserData(Context newContext)
	   	{
	   	context = newContext;
	   	}
	
			@Override
			public void run() {
		   	 	   //create new instance of the  necessary data and network classes
		    	  networkUserService = new NetworkUserDAO();
		    	  userServiceSQLite = new UserServiceSQLite(context);
		          
		          try {
		        	 //try to get the data first
		        	  ArrayList<User> users =  networkUserService.getAllUsers();  
		        	
		        	 
		        	 ArrayList<User> offlineUsers = userServiceSQLite.getAllOfflineUsers();
		        			if(!offlineUsers.isEmpty())
		        			{
		        			for (User user : offlineUsers) {
		        				networkUserService.updatePassword(user.getUserID(), user.getPassword());
							}
		        			
		        			}
					
					
				
		  
		   	   if(users.isEmpty())
		          {
		   		   throw new Exception();
		       	   }
		   	   
		   	   //try to insert the trucks into the database
		   	   
		   		 
		   		userServiceSQLite.addAllUsers(users);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
					
				}
				
			}
	          
	          
	      }
	private class SendUserData implements Runnable {
	       /** The system calls this to perform work in a worker thread and
	        * delivers it the parameters given to AsyncTask.execute() */
	   	INetworkUser networkUserService;
	   	IUserServiceSQLite userServiceSQLite;
	   	Context context;
	   	User user;
	   	public SendUserData(Context newContext, User upUser)
	   	{
	   	context = newContext;
	   	user = upUser;
	   	}
	
			@Override
			public void run() {
		   	 	   //create new instance of the  necessary data and network classes
		    	  networkUserService = new NetworkUserDAO();
		    	  userServiceSQLite = new UserServiceSQLite(context);
		          
		          try {
					
		        	  //attempt to send the updated data
		        	  networkUserService.updatePassword(user.getUserID(), user.getPassword());
		        	  
		        	  //if it succeeeds, attempt to get the data from the remote server
		        	  ArrayList<User> users =  networkUserService.getAllUsers();
					
				
		  
		   	   if(users.isEmpty())
		          {
		   		   throw new Exception();
		       	   }
		   	   
		   	   //try to insert the trucks into the database
		   	   userServiceSQLite.addAllUsers(users);
		   	   
		   		 
		   		
				} catch (Exception e) {
					userServiceSQLite.updatePassword(user);
					
					
				}
				
			}
	          
	          
	      }
	@Override
	public void clearUsernameAndPasswordFromSharedPrefs() {
		Editor editor = preferences.edit();
		editor.clear();
		editor.commit();
		
	}
	
	
	

}
