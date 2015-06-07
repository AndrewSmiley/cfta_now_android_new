package com.cincyfoodtrucks.dao;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;

import com.cincyfoodtrucks.dto.TruckOwner;

public class NetworkTruckDAO implements INetworkTruck {
	INetworkDAO networkDAO;

	public NetworkTruckDAO() {
		networkDAO = new NetworkDAO();

	}

	@Override
	public ArrayList<TruckOwner> getAllTrucks() throws ConnectException,
			IOException {

		// Create the uri. Confirmed this uri works.
		String uri = "http://cincyfoodtruckapp.com/mobile_request_handler.php?action=get_all_trucks";
		String response = networkDAO.sendHTTPGetRequest(uri);

		// Split the rows into array
		String[] lines = response.split("\r\n");

		ArrayList<TruckOwner> trucks = new ArrayList<TruckOwner>();

		// Loop over each line
		for (String line : lines) {
			// Split each line of the results into a new string array and create
			// a TruckOwner object
			// from the items.
			String[] truckData = line.split(";");

			// Make sure the data is correct. Account for trucks that don't have a location
			if (truckData.length == 2) {

				TruckOwner thisTruck = new TruckOwner();
				thisTruck.setTruckID(Integer.parseInt(truckData[0]));
				thisTruck.setTruckName(truckData[1]);
				
				trucks.add(thisTruck);
				//this else if clause is to account for trucks that are online
			}else if(truckData.length > 3)
			{	TruckOwner thisTruck = new TruckOwner();
				thisTruck.setTruckID(Integer.parseInt(truckData[0]));
				thisTruck.setTruckName(truckData[1]);
				thisTruck.setLongitude(truckData[2]);
				thisTruck.setLatitude(truckData[3]);
				thisTruck.setHoursAtLocation(Long.parseLong(truckData[4]));
				trucks.add(thisTruck);
			}
		}

		return trucks;
	}

	@Override
	public ArrayList<TruckOwner> getTruckByName(String truck)
			throws IOException, Exception {
		String uri = "http://cincyfoodtruckapp.com/mobile_request_handler.php?action=get_truck_by_name&truck_name="
				+ truck;
		String response = networkDAO.sendHTTPGetRequest(uri);

		String[] lines = response.split("\r\n");

		ArrayList<TruckOwner> trucks = new ArrayList<TruckOwner>();

		// Loop over each line
		for (String line : lines) {
			// Split each line of the results into a new string array and create
			// a TruckOwner object
			// from the items.
			String[] truckData = line.split(";");

			// Make sure the data is correct.
			if (truckData.length > 3) {
				
				TruckOwner thisTruck = new TruckOwner();
				thisTruck.setTruckID(Integer.parseInt(truckData[0]));
				thisTruck.setTruckName(truckData[1]);
				thisTruck.setLongitude(truckData[2]);
				thisTruck.setLatitude(truckData[3]);
				thisTruck.setHoursAtLocation(Long.parseLong(truckData[4]));
				trucks.add(thisTruck);
			}
		}

		return trucks;

	}

	
	
	@Override
	public void updateTimeAtLocation(String hours,  int truckID) throws Exception {
		
		String uri  = "http://cincyfoodtruckapp.com/mobile_request_handler.php?action=update_time_at_location&hours_at_location="+hours+"&truck_id="+truckID;
		String response =  networkDAO.sendHTTPGetRequest(uri);
		if(response.contains("ERROR"))
		{
			//determine if an error was thrown from the remote data source
			throw new Exception();
		
		}

	}



	@Override
	public void updateLocation(String longitude, String latitude, int truckID)
			throws Exception {
		String uri = "http://cincyfoodtruckapp.com/mobile_request_handler.php?action=update_location&longitude="+longitude+"&latitude="+latitude+"&truck_id="+truckID;
		String response =  networkDAO.sendHTTPGetRequest(uri);
		if(response.contains("ERROR"))
		{
			//determine if an error was thrown from the remote data source
			throw new Exception();
		
		}
		
	}

}
