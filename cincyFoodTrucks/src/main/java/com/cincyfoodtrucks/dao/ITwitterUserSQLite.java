package com.cincyfoodtrucks.dao;

import java.util.ArrayList;

import com.cincyfoodtrucks.dto.TwitterUser;
public interface ITwitterUserSQLite {
public ArrayList<TwitterUser> getAllTwitterUsers();
public void addTwitterUser(TwitterUser user);
}
