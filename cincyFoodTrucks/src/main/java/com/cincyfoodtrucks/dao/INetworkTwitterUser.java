package com.cincyfoodtrucks.dao;

import java.util.ArrayList;

import com.cincyfoodtrucks.dto.TwitterUser;

public interface INetworkTwitterUser {
public boolean addNewTwitterUser(TwitterUser twitterUser);
public ArrayList<TwitterUser> getAllTwitterUsers();
}
