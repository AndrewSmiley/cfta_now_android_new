package com.cincyfoodtrucks.dao;

import java.io.IOException;
import java.net.ConnectException;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public interface INetworkDAO {
	/**
	 * Method to send an http get request to get data from the remote site.
	 * 
	 * @param uri
	 *            The uri of the site we wish to connect to.
	 * @return String A string containing the response back from the site.
	 * @throws ConnectException
	 * @throws IOException
	 */
	public String sendHTTPGetRequest(String uri) throws ConnectException, IOException;
	
	/**
	 * Method to send an http post request to get data from the remote site
	 * @param uri
	 * @param params the key-value pairs containing the POST arguments
	 * @return String the string containing the response from the remote site
	 * @throws ConnectException
	 * @throws IOException
	 */
	public String sendPOSTRequest(String uri, List<BasicNameValuePair> params) throws ConnectException, IOException;
}
