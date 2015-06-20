package com.cincyfoodtrucks.dao;

import java.util.ArrayList;

import com.cincyfoodtrucks.dto.FacebookPage;

public interface IFacebookPageSQLite {
	public void addFacebookPage(FacebookPage page);
	public ArrayList<FacebookPage> getAllFacebookPages();
	public boolean pageExistsForTruck(int truckID);
	
}
