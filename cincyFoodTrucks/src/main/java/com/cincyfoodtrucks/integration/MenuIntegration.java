package com.cincyfoodtrucks.integration;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources.NotFoundException;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.cincyfoodtrucks.R;
import com.cincyfoodtrucks.dao.IMenuServiceSQLite;
import com.cincyfoodtrucks.dao.INetworkMenu;
import com.cincyfoodtrucks.dao.ITruckServiceSQLite;
import com.cincyfoodtrucks.dao.MenuServiceSQLiteStub;
import com.cincyfoodtrucks.dao.NetworkMenuDAO;
import com.cincyfoodtrucks.dao.NetworkMenuItemImageDAO;
import com.cincyfoodtrucks.dao.TruckServiceSQLiteStub;
import com.cincyfoodtrucks.dto.MenuItemImage;
import com.cincyfoodtrucks.dto.TruckMenuItem;
import com.cincyfoodtrucks.dto.TruckOwner;

public class MenuIntegration implements IMenuIntegration {
INetworkMenu networkMenu;
IMenuServiceSQLite sqliteMenu;
ITruckServiceSQLite sqliteTruck;
SharedPreferences prefs;
Context context;

public MenuIntegration(Context newContext)
{
	this.context = newContext;
	sqliteTruck = new TruckServiceSQLiteStub(context);
	networkMenu = new NetworkMenuDAO();
	sqliteMenu = new MenuServiceSQLiteStub(context);
	prefs = PreferenceManager.getDefaultSharedPreferences(context);
}
	

@Override
public String getMenuItemSearchCriteriaFromSharedPrefs() {
	//get the search criteria
	String criteria = prefs.getString(context.getResources().getString(R.string.strCurrentItemSearchCriteria), "");
	return criteria;
}

	

/* (non-Javadoc)
 * @see com.cincyfoodtrucks.integration.IMenuIntegration#getTruckMenuItemsBySharedPreferences()
 */
@Override
public ArrayList<TruckMenuItem> getTruckMenuItemsBySharedPreferences() throws NotFoundException, Exception
{
	String truckName = prefs.getString(context.getResources().getString(R.string.strCurrentTruckPrefName), "");
	if(truckName.contains("'"));
	{
		truckName.replace("'", "\\'");
	}
			
	TruckOwner truck  = sqliteTruck.getTruckByName(truckName);
	ArrayList<TruckMenuItem> items = sqliteMenu.getItemsByTruckID(truck.getTruckID());
	return items;

}

@Override
public ArrayList<TruckMenuItem> searchForFoods(String criteria) {
	ArrayList<TruckMenuItem> items = sqliteMenu.searchForFoods(criteria);
	return items;
}



@Override
public void updateSearchItemInSharedPrefs(String criteria) {
	SharedPreferences.Editor editor = prefs.edit();
	editor.putString(context.getResources().getString(R.string.strCurrentItemSearchCriteria), criteria);
	editor.apply();
	
	
}

@Override
public void clearSearchItemInSharedPrefs() {
	SharedPreferences.Editor editor = prefs.edit();
	editor.putString(context.getResources().getString(R.string.strCurrentItemSearchCriteria), "");
	editor.apply();
	
	
}


/* (non-Javadoc)
 * @see com.cincyfoodtrucks.integration.IMenuIntegration#executeTruckMenuAsyncTask()
 */
@Override
public void executeGetTruckMenuTask() throws InterruptedException{
	GetMenuData gmt = new GetMenuData(context);
	Thread t = new Thread(gmt);
	t.start();
	t.join();
	
}

@Override
public void executeSendTruckMenuTask(TruckMenuItem item) throws InterruptedException {
	SendMenuData stm = new SendMenuData(context, item);
	Thread t = new Thread(stm);
	t.start();
	t.join();
	
}

/**
 * Class to run menu related network calls. 
 * @author Andrew Smiley
 *
 */
private class GetMenuData implements Runnable {
    /** The system calls this to perform work in a worker thread and
     * delivers it the parameters given to AsyncTask.execute() */
	NetworkMenuDAO menuNetworkAccessor;
	MenuServiceSQLiteStub menuSQLService ;
	Context context;
	public GetMenuData(Context newContext)
	{
	context = newContext;
	}
	@Override
	public void run() {
		 menuNetworkAccessor = new NetworkMenuDAO();
		   menuSQLService = new MenuServiceSQLiteStub(context);
		  
	       try {
	    	   //attempt to get the truck menu items
				ArrayList<TruckMenuItem> menuItems =  menuNetworkAccessor.getAllItems();

		   
	 	  if(menuItems.isEmpty())
	       {
	    	//Don't raise toast. Print stack trace
				//Toast.makeText(context, "Could not connect to network", Toast.LENGTH_LONG).show();
	 		  throw new Exception();
	       }
		   
	 	  
	 	 ArrayList<TruckMenuItem> offlineMenuItems = menuSQLService.getAllOfflineMenuItems();
		   
		   //determine if there are any offline menu items to upload
		   if(!offlineMenuItems.isEmpty())
		   {
			   //if so, attempt to re-add them to the database
			   for (TruckMenuItem offlineItem : offlineMenuItems) {
				
				   try {
					   
					   //try  to add the menu item
					networkMenu.addMenuItem(offlineItem);
					
					//if it fails, no worries. The data is still stored locally. Just try again next time
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			   
		   }
	       
		   //try to insert the trucks into the database
		   menuSQLService.addRows(menuItems);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				//Toast.makeText(context, "Issues updating data", Toast.LENGTH_LONG).show();
				
			}
	       
	       
		
	}
	
    
   }


/**
 * Class to run menu related network calls. 
 * @author Andrew Smiley
 *
 */
private class SendMenuData implements Runnable {
    /** The system calls this to perform work in a worker thread and
     * delivers it the parameters given to AsyncTask.execute() */
	NetworkMenuDAO menuNetworkAccessor;
	MenuServiceSQLiteStub menuSQLService ;
	NetworkMenuItemImageDAO menuImageAccessor;
	Context context;
	TruckMenuItem item;
//	MenuItemImage image;
	public SendMenuData(Context newContext, TruckMenuItem newItem)
	{
	context = newContext;
	item = newItem;
//	image = newImage;
	}
	@Override
	public void run() {
		 menuNetworkAccessor = new NetworkMenuDAO();
		 menuImageAccessor =  new NetworkMenuItemImageDAO();
		   menuSQLService = new MenuServiceSQLiteStub(context);
		   ArrayList<TruckMenuItem> menuItems;
		   
		   
	       try {
	    	   //try to send the new menu item to the remote server
	    	   menuNetworkAccessor.addMenuItem(item);
	    	   
	    	   
				
				
	       }catch(Exception e)
	       {
	    	   
	    	   try {
	    		   //if the network call fails, attempt to save it offline
				menuSQLService.addMenuItem(item);
				
				//just handle the exception
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    	   
	       }
	       
	       //attempt to get all the new menu items
	       try{
	    	 
			menuItems = networkMenu.getAllItems();
			
			//if we're able to get the network menu items, let's get the item id from the db using the name 
			//and the truck id
			TruckMenuItem retreivedItem = menuSQLService.getItemByName(item.getTitle(), item.getTruckID());
//			image.setMenuItemID(retreivedItem.getmenuItemID());
//			menuImageAccessor.addMenuItemImage(image);
			
			
			
	       
				
				//if the call did retrieve any new items, throw an exception
	 	  if(menuItems.isEmpty())
	       {
	    	//Don't raise toast. Print stack trace
				//Toast.makeText(context, "Could not connect to network", Toast.LENGTH_LONG).show();
	 		  throw new Exception();
	       }
		   
		   //try to insert the trucks into the database
		   
			 //Try to add 
			menuSQLService.addRows(menuItems);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				//Toast.makeText(context, "Issues updating data", Toast.LENGTH_LONG).show();
				
			}
	       
	       
		
	}
	
    
   }


	
}
