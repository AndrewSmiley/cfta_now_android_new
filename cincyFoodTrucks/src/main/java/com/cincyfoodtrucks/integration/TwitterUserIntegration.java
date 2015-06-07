package com.cincyfoodtrucks.integration;

import java.util.ArrayList;

import android.content.Context;

import com.cincyfoodtrucks.dao.INetworkTwitterUser;
import com.cincyfoodtrucks.dao.ITwitterUserSQLite;
import com.cincyfoodtrucks.dao.NetworkTwitterUser;
import com.cincyfoodtrucks.dao.TwitterUserSQLite;
import com.cincyfoodtrucks.dto.TwitterUser;

public class TwitterUserIntegration {
	private Context context;
	public TwitterUserIntegration (Context c){
		context=c;
	}
	
	public void getTwitterData() throws InterruptedException{
//		GetTwitterData getTwitterData = new GetTwitterData(context);
		Thread t = new Thread(new GetTwitterData(context));
		t.start();
		t.join();
	}
	
	public void sendTwitterData() throws InterruptedException{
		Thread t = new Thread(new SendTwitterData(context));
		t.start();
		t.join();
	}

	private class GetTwitterData implements Runnable{
		Context context;
		ITwitterUserSQLite twitterUserSQLite;
		INetworkTwitterUser networkTwitterUser;
		public GetTwitterData(Context c){
			context = c;
			twitterUserSQLite = new TwitterUserSQLite(context);
			networkTwitterUser = new NetworkTwitterUser();
		}
		
		@Override
		public void run(){
			
			ArrayList<TwitterUser> twitterUsers = networkTwitterUser.getAllTwitterUsers();
			if (twitterUsers.size() > 0) {
				for (TwitterUser user : twitterUsers) {
					twitterUserSQLite.addTwitterUser(user);
				}
				
			}
			
		}
	}
	
	
	private class SendTwitterData implements Runnable{
		Context context;
		ITwitterUserSQLite twitterUserSQLite;
		INetworkTwitterUser networkTwitterUser;
		public SendTwitterData(Context c){
			context = c;
			twitterUserSQLite = new TwitterUserSQLite(context);
			networkTwitterUser = new NetworkTwitterUser();
		}
		
		@Override
		public void run(){
			
			ArrayList<TwitterUser> twitterUsers = networkTwitterUser.getAllTwitterUsers();
			if (twitterUsers.size() > 0) {
				for (TwitterUser user : twitterUsers) {
					twitterUserSQLite.addTwitterUser(user);
				}
				
			}
			
		}
	}
}
