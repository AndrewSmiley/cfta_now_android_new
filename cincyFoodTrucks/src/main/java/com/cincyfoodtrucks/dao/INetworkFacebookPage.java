package com.cincyfoodtrucks.dao;

import java.util.ArrayList;

import com.cincyfoodtrucks.dto.FacebookPage;

public interface INetworkFacebookPage {
public boolean addFacebookPage(FacebookPage page);
public ArrayList<FacebookPage> getAllFacebookPages();
}
