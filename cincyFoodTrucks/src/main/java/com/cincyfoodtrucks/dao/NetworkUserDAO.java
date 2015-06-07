package com.cincyfoodtrucks.dao;

import java.net.URI;
import java.util.ArrayList;

import com.cincyfoodtrucks.dto.User;

public class NetworkUserDAO implements INetworkUser {

	INetworkDAO networkDAO;
    
    public NetworkUserDAO(){
    networkDAO = new NetworkDAO();
    }
    
    @Override
    public ArrayList<User> getAllUsers() throws Exception {
            String uri = "http://cincyfoodtruckapp.com/mobile_request_handler.php?action=get_all_users";
                            
            String response =  networkDAO.sendHTTPGetRequest(uri);
            
            // Split the rows into array
            String[] lines = response.split("\r\n");

            ArrayList<User> users = new ArrayList<User>();

            // Loop over each line
            for (String line : lines) {
                    // Split each line of the results into a new string array and create
                    // a TruckOwner object
                    // from the items.
                    String[] userData = line.split(";");

                    // Make sure the data is correct.
                    if (userData.length > 3) {

                            User thisUser = new User();
                            thisUser.setUserID(Integer.parseInt(userData[0]));
                            thisUser.setUsername(userData[1]);
                            thisUser.setPassword(userData[2]);
                            thisUser.setFirstName(userData[3]);
                            thisUser.setLastName(userData[4]);
                            thisUser.setTruckID(Integer.parseInt(userData[5]));
                    
                            //add the user to the list
                            users.add(thisUser);
            
                    }
            }
            return users;
    }
    
    

    @Override
    public void updatePassword(int userID, String newPW)
                    throws Exception {
            
            
            //create the query  
            String query = "http://cincyfoodtruckapp.com/mobile_request_handler.php?action=update_password&new_password="+newPW+"&user_id="+userID;
        	
            //account for whitespace
            URI uri = new URI(query.replace(' ', '+'));
    		
        	
            //send the request 
            String response =  networkDAO.sendHTTPGetRequest(uri.toString());
            
            //check it 
            if(response.contains("ERROR"))
            {
                    //determine if an error was thrown from the remote data source
                    throw new Exception();
            
            }
            
            // TODO Auto-generated method stub
            
    }

}