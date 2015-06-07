package com.cincyfoodtrucks.ui;

import java.util.ArrayList;

import com.cincyfoodtrucks.R;
import com.cincyfoodtrucks.dto.TruckMenuItem;
import com.cincyfoodtrucks.integration.IMenuIntegration;
import com.cincyfoodtrucks.integration.MenuIntegration;
import com.cincyfoodtrucks.ui.LocationListActivity.TruckListItem;
import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class TruckMenuActivity extends BaseActionMenuActivity {
	ListView lv;
	IMenuIntegration menuIntegrator;
	ArrayList<MenuListItem> menuListItems;
	ArrayList<TruckMenuItem> itemsArrayList;
	MenuListView adapter;
	String provider;
	
	/**
	 * Class to hold the truck menu items for the list adapter 
	 * @author Smiley
	 *
	 */
	 class MenuListItem{
		TruckMenuItem item;
		private MenuListItem(TruckMenuItem newitem)
		{
			this.item = newitem;
		}
	}
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.cincyfoodtrucks.R.layout.actvity_truck_menu);
		menuIntegrator = new MenuIntegration(this);
		menuListItems = new ArrayList<MenuListItem>();
	
		
		try {
			//try to get the new data from the server
			menuIntegrator.executeGetTruckMenuTask();
			
			//get the items from the sqlite server
			itemsArrayList = menuIntegrator.getTruckMenuItemsBySharedPreferences(); 
			
			//let the user know the truck has not created any items
			if(itemsArrayList.size() == 0)
			{
				Toast.makeText(this, getResources().getText(R.string.strThisTruckHasNoItems), Toast.LENGTH_LONG).show();
			}
			for (TruckMenuItem menuItem : itemsArrayList) {
				menuListItems.add(new MenuListItem(menuItem));
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(this, getResources().getString(R.string.strGenericError), Toast.LENGTH_SHORT).show();
		}
		
		//set the adapter and list of items
		lv = (ListView)findViewById(R.id.truckMenuListListView);
		adapter = new MenuListView(this, menuListItems);
		lv.setAdapter(adapter);
	}



	
	
	/**
	 * Private inner ArrayAdapter classs to show the menu data for a particular truck
	 * @author Smiley
	 *
	 */
private class MenuListView extends BaseAdapter{

	
	LayoutInflater inflater;
	ArrayList<MenuListItem> items;
	Context context;
	
    public MenuListView(Context context, ArrayList<MenuListItem> items) {  
        super();
		this.context= context;
        this.items = items;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    @Override  
    public int getCount() {  
        // TODO Auto-generated method stub  
        return items.size();  
    }  
  
    @Override  
    public Object getItem(int position) {  
        // TODO Auto-generated method stub  
        return null;  
    }  
  
    @Override  
    public long getItemId(int position) {  
        // TODO Auto-generated method stub  
        return 0;  
    }
      
    @Override  
    public View getView(final int position, View convertView, ViewGroup parent) {  
        // TODO Auto-generated method stub  
    	
    	MenuListItem item  = items.get(position);
    	View vi=convertView;
        
        if(convertView==null)
            vi = inflater.inflate(R.layout.menu_list_row, null);
        final TextView txtTitle = (TextView) vi.findViewById(R.id.txtMenuItemTitle);
        TextView txtDesc = (TextView) vi.findViewById(R.id.txtMenuItemDesc);
        TextView txtPrice = (TextView) vi.findViewById(R.id.txtMenuItemPrice);
        
        txtTitle.setText(item.item.getTitle());
        txtDesc.setText(item.item.getDescription());
        txtPrice.setText("$"+item.item.getPrice());
        
       
		
        return vi;  
    }

  
 
}
	
	
	
}