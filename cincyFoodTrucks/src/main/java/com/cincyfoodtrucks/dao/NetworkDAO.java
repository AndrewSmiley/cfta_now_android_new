package com.cincyfoodtrucks.dao;

import java.io.IOException;
import java.net.ConnectException;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class NetworkDAO implements INetworkDAO {

	@Override
	public String sendHTTPGetRequest(String uri) throws ConnectException, IOException {
		// Create the return variable
		String returnStr = null;
		// Create a default http client object
		HttpClient client = new DefaultHttpClient();
		
		// create a get object with the uri
		HttpGet httpGet = new HttpGet(uri);

		// Create a response handler
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		returnStr = client.execute(httpGet, responseHandler);
		return returnStr;
	}
	
	
	public String sendPOSTRequest(String uri, List<BasicNameValuePair> params) throws ConnectException, IOException{
		
		// Create the return variable
		String returnStr = null;
				// Create a default http client object
		HttpClient client = new DefaultHttpClient();
		
		HttpPost httpPost = new HttpPost(uri);
		httpPost.setEntity(new UrlEncodedFormEntity(params));
		// Create a response handler
	 	ResponseHandler<String> responseHandler = new BasicResponseHandler();
	 	returnStr = client.execute(httpPost, responseHandler);
	 		
		return returnStr;
	}

}
