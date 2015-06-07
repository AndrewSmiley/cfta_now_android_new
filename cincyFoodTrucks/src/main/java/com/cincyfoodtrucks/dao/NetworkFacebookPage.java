package com.cincyfoodtrucks.dao;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.cincyfoodtrucks.dto.FacebookPage;
import com.cincyfoodtrucks.dto.TruckMenuItem;

public class NetworkFacebookPage implements INetworkFacebookPage {
	INetworkDAO networkDAO;
	public NetworkFacebookPage(){
		networkDAO = new NetworkDAO();
	}
	@Override
	public boolean addFacebookPage(FacebookPage page) {
		String uri = "http://cincyfoodtruckapp.com/mobile_request_handler.php?action=add_facebook_page&FacebookPage="+page.getPageName()+"&TruckID="+page.getTruckID();
		String response = "ERROR";
		try {
			response = networkDAO.sendHTTPGetRequest(uri);
		} catch (ConnectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(response.contains("ERROR"))
		{
			return false;
		}else{

		return true;
		}
	}

	@Override
	public ArrayList<FacebookPage> getAllFacebookPages() {
		//create arraylist to hold items
		ArrayList<FacebookPage> facebookPages = new ArrayList<FacebookPage>();

		//create the uri
		String uri = "http://cincyfoodtruckapp.com/mobile_request_handler.php?action=get_all_facebook_pages";
		
		
		String response;
		try {
			response = networkDAO.sendHTTPGetRequest(uri);
		} catch (ConnectException e) {
			// TODO Auto-generated catch block
			return new ArrayList<FacebookPage>();
		} catch (IOException e) {
			return new ArrayList<FacebookPage>();
			
		}
		
		//loop over the responses
		String[] lines = response.split("\r\n");

		for (String line : lines) {
			String[] menuInfo = line.split(";");
			FacebookPage page = new FacebookPage();
			page.setId(Integer.parseInt(menuInfo[0]));
			page.setPageName(menuInfo[1]);
			page.setTruckID(Integer.parseInt(menuInfo[2]));
			facebookPages.add(page);
		}

		return facebookPages;
	}

}
