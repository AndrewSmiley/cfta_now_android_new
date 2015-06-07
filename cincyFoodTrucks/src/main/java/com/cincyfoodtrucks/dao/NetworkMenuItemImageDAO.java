package com.cincyfoodtrucks.dao;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.cincyfoodtrucks.dto.MenuItemImage;
import com.cincyfoodtrucks.utilities.ImageUtil;

public class NetworkMenuItemImageDAO {
INetworkDAO networkDAO;

public NetworkMenuItemImageDAO(){
	networkDAO = new NetworkDAO();
}
	
public ArrayList<MenuItemImage> getAllImages() throws ConnectException, IOException
{
	ArrayList<MenuItemImage> images = new ArrayList<MenuItemImage>();
	
	//create the uri
	String uri = "http://cincyfoodtruckapp.com/mobile_request_handler.php?action=get_all_menu_item_images";
	
	
	String response = networkDAO.sendHTTPGetRequest(uri);
	
	//loop over the responses
	String[] lines = response.split("\r\n");

	for (String line : lines) {
		String[] imageData = line.split(";");

		// Make sure that the request returned correctly
		if (imageData.length  == 3) {
			
			MenuItemImage image = new MenuItemImage();
			image.setImageID(Integer.parseInt(imageData[0]));
			image.setImageContent(ImageUtil.convertStringToBitmap(imageData[1]));
			image.setMenuItemID(Integer.parseInt(imageData[2]));
			images.add(image);
			
		}

	}

	
	return images;

}


public boolean addMenuItemImage(MenuItemImage image) throws Exception {
	
	String query = "http://cincyfoodtruckapp.com/mobile_request_handler.php?action=add_new_image";//&content="+ImageUtil.bitmapToString(image.getImageContent())+"&menu_item_id="+image.getMenuItemID();
	
	//create the parameter list
	List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
	//add the key-value params
	parameters.add(new BasicNameValuePair("content", ImageUtil.bitmapToString(image.getImageContent())));
	parameters.add(new BasicNameValuePair("menu_item_id", Integer.toString(image.getMenuItemID())));
	//let's assume there will be no whitespace in the url
	///query.replace(' ', '+')
	//URI uri = new URI(query);
	//send the response
	String response = networkDAO.sendPOSTRequest(query, parameters);

	
	
	if(response.contains("ERROR"))
	{
		return false;
	}
	
	return true;
}


}
