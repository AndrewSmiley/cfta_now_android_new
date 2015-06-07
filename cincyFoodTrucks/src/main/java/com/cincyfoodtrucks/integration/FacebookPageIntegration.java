package com.cincyfoodtrucks.integration;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.cincyfoodtrucks.dao.FacebookPageSQLite;
import com.cincyfoodtrucks.dao.NetworkFacebookPage;
import com.cincyfoodtrucks.dto.FacebookPage;
import com.cincyfoodtrucks.dto.TruckOwner;
import com.cincyfoodtrucks.utilities.NewsFeedItem;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
//import com.facebook.Request;
//import com.facebook.Response;
//import com.facebook.model.GraphObject;


public class FacebookPageIntegration {
Context context;


public FacebookPageIntegration(Context _context){
	context = _context;
}


public void getFacebookData() throws InterruptedException{
	GetFacebookData fbDataFetcher = new GetFacebookData(context);
	Thread t = new Thread(fbDataFetcher);
	t.start();
	t.join();
}

public boolean addFacebookPage(FacebookPage page) throws InterruptedException{
	SendFacebookData sendFacebookData = new SendFacebookData(context);
	sendFacebookData.setPage(page);
	Thread t = new Thread(sendFacebookData);
	t.start();
	t.join();
	return true;
}


public ArrayList<NewsFeedItem> getFacebookPosts(FacebookPage page, TruckOwner truck) throws InterruptedException{
	GetFacebookPosts gfp = new GetFacebookPosts(page, truck);
	Thread t = new Thread(gfp);
	t.start();
	t.join();
	return gfp.getItems();
	
}

private class GetFacebookData implements Runnable{
	NetworkFacebookPage networkFacebookPage;
	Context context;
	FacebookPageSQLite facebookPageSQLite;
	public GetFacebookData(Context c){
		context = c;
	}
	
	@Override
	public void run() {
		networkFacebookPage = new NetworkFacebookPage();
		facebookPageSQLite = new FacebookPageSQLite(context);
		ArrayList<FacebookPage> pages =networkFacebookPage.getAllFacebookPages();
		if (pages.size() >= 1) {
			for (FacebookPage page : pages) {
				facebookPageSQLite.addFacebookPage(page);
			}

		}
		
	}
	
}


private class SendFacebookData implements Runnable{
	NetworkFacebookPage networkFacebookPage;
	Context context;
	FacebookPageSQLite facebookPageSQLite;
	FacebookPage page;
	
	/**
	 * @return the page
	 */
	public FacebookPage getPage() {
		return page;
	}
	/**
	 * @param page the page to set
	 */
	public void setPage(FacebookPage page) {
		this.page = page;
	}
	
	public SendFacebookData(Context c){
		context = c;
	}
	
	@Override 
	public void run(){
		networkFacebookPage = new NetworkFacebookPage();
		facebookPageSQLite = new FacebookPageSQLite(context);
		//not entirely sure how we want to handle these right here.. 
		//probably want to send them back to the UI at some point
		networkFacebookPage.addFacebookPage(page);
		
		
		
	}
}

private class GetFacebookPosts implements Runnable{
	private FacebookPage page;
	private TruckOwner truck;
	private ArrayList<NewsFeedItem> items;
	public GetFacebookPosts(FacebookPage fb, TruckOwner t){
		page = fb;
		truck = t;
		items = new ArrayList<NewsFeedItem>();
	}
	
	public ArrayList<NewsFeedItem> getItems(){
		return this.items;
	}
	@Override
	public void run(){
		Bundle params = new Bundle();
	     params.putString("access_token", "696538993808289|0ac350c45a50e74f943f0e7df6a10c12");

	 final String requestId = "/"+page.getPageName()+"/posts";
	 					
	 GraphRequest request = new GraphRequest(null, requestId, params, HttpMethod.GET, new GraphRequest.Callback() {
	      public void onCompleted(GraphResponse response) {
//			  		GraphObject graphObject = response.getGraphObject();
	                FacebookRequestError error = response.getError();
	                if(error!=null){
	                 Log.e("Error", error.getErrorMessage());
	                 }else{
	                	 //object.asMap();
						JSONObject values = response.getJSONObject();
						JSONArray json = null;
						try {
							json = (JSONArray) values.get("data");
						} catch (JSONException e) {
							e.printStackTrace();
						}

						for(int i = 0 ; i < json.length(); i++){
	                		JSONObject jsonObj;
							try {
								jsonObj = json.getJSONObject(i);
								NewsFeedItem item = new NewsFeedItem();
								item.setTruck(truck);
								item.setContent((String) jsonObj.get("message"));
								String tmpUpdateTimeStr = (String) jsonObj.get("updated_time");
//								String updateTimeStr = tmpUpdateTimeStr.replaceAll("T", " ").replaceAll("+", " +");
								String updateTimeStr = tmpUpdateTimeStr.replace("T", " ").replace("+", " +");
								DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ZZZZ");
								item.setDate(formatter.parse(updateTimeStr));
								items.add(item);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

	                	}

	                	 String myString = "";
	                 }



	            }
	        });

//	    Request.executeAndWait(request);
		GraphRequest.executeAndWait(request);
//		GraphRequest.executeAndWait(request);
//		request.executeAndWait();
	}
}
}
