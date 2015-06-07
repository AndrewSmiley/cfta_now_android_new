package com.cincyfoodtrucks.dao;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.util.ArrayList;

import com.cincyfoodtrucks.dto.TruckMenuItem;

public class NetworkMenuDAO implements INetworkMenu {
	INetworkDAO networkDAO;

	public NetworkMenuDAO() {
		networkDAO = new NetworkDAO();
	}

	@Override
	public ArrayList<TruckMenuItem> getAllItems() throws ConnectException, IOException {
		
		//create arraylist to hold items
		ArrayList<TruckMenuItem> menuItems = new ArrayList<TruckMenuItem>();

		//create the uri
		String uri = "http://cincyfoodtruckapp.com/mobile_request_handler.php?action=get_all_menu_items";
		
		
		String response = networkDAO.sendHTTPGetRequest(uri);
		
		//loop over the responses
		String[] lines = response.split("\r\n");

		for (String line : lines) {
			String[] menuInfo = line.split(";");

			// Make sure that the request returned correctly
			if (menuInfo.length > 3) {
				TruckMenuItem item = new TruckMenuItem();
				item.setMenuItemID(Integer.parseInt(menuInfo[0]));
				item.setTitle(menuInfo[3]);
				item.setDescription(menuInfo[1]);
				String price = menuInfo[2];
				item.setPrice(price);
				item.setTruckID(Integer.parseInt(menuInfo[4]));
				menuItems.add(item);
			}

		}

		return menuItems;
	}

	@Override
	public void updateMenuItem(TruckMenuItem item) throws Exception {
		String uri = "http://cincyfoodtruck.com/mobile_request_handler.php?action=update_menu_item&description="+item.getDescription()+"&price="+item.getPrice()+"&title="+item.getTitle()+"&truck_id="+item.getTruckID();
		String response = networkDAO.sendHTTPGetRequest(uri);
		
		if(response.contains("ERROR"))
		{
			throw new Exception();
		}

	}

	@Override
	public boolean addMenuItem(TruckMenuItem item) throws Exception {
		String query = "http://cincyfoodtruckapp.com/mobile_request_handler.php?action=create_new_menu_item&description="+item.getDescription()+"&price="+item.getPrice()+"&title="+item.getTitle()+"&truck_id="+item.getTruckID();
		
		URI uri = new URI(query.replace(' ', '+'));
		
		String response = networkDAO.sendHTTPGetRequest(uri.toString());

		
		
		if(response.contains("ERROR"))
		{
			return false;
		}
		
		return true;
	}

	@Override
	public boolean deleteMenuItem(TruckMenuItem item) throws ConnectException, IOException {
		String uri = "http://cincyfoodtruckapp.com/mobile_request_handler.php?action=delete_menu_item&item_id="+item.getmenuItemID();
		
		String response = networkDAO.sendHTTPGetRequest(uri);
		
		if(response.contains("ERROR"))
		{
			return false;
		}
		
		return true;
	}

}
