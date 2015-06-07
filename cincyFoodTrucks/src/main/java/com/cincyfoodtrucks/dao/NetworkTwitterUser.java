package com.cincyfoodtrucks.dao;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;

import com.cincyfoodtrucks.dto.FacebookPage;
import com.cincyfoodtrucks.dto.TwitterUser;

public class NetworkTwitterUser implements INetworkTwitterUser {
	INetworkDAO networkDAO;
	public NetworkTwitterUser(){
		networkDAO = new NetworkDAO();
	}
	@Override
	public boolean addNewTwitterUser(TwitterUser twitterUser) {
		String uri = "http://cincyfoodtruckapp.com/mobile_request_handler.php?action=add_twitter_user&TwitterUsername="+twitterUser.getTwitterUsername()+"&TruckID="+twitterUser.getTruckID();
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
	public ArrayList<TwitterUser> getAllTwitterUsers() {
				//create arraylist to hold items
				ArrayList<TwitterUser> twitterUsers = new ArrayList<TwitterUser>();
				//create the uri
				String uri = "http://cincyfoodtruckapp.com/mobile_request_handler.php?action=get_all_facebook_pages";
				String response;
				try {
					response = networkDAO.sendHTTPGetRequest(uri);
				} catch (ConnectException e) {
					// TODO Auto-generated catch block
					return new ArrayList<TwitterUser>();
				} catch (IOException e) {
					return new ArrayList<TwitterUser>();
					
				}
				//loop over the responses
				String[] lines = response.split("\r\n");

				for (String line : lines) {
					String[] menuInfo = line.split(";");
					TwitterUser twitterUser = new TwitterUser();
					twitterUser.setId(Integer.parseInt(menuInfo[0]));
					twitterUser.setTruckID(Integer.parseInt(menuInfo[2]));
					twitterUser.setTwitterUsername(menuInfo[1]);
					twitterUsers.add(twitterUser);
				}

				return twitterUsers;
	}

}
