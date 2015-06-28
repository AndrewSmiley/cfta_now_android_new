package com.cincyfoodtrucks.dto;

public class TwitterUser {
    private int id;
    private String twitterUsername;
    private int truckID;

    /*
    Constructors!
     */
    public TwitterUser(){}

    public TwitterUser(String _username, int _truckID){
        setTwitterUsername(_username);
        setTruckID(_truckID);
    }

    public TwitterUser(int _id, String _username, int _truckID){
        setId(_id);
        setTwitterUsername(_username);
        setTruckID(_truckID);
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the twitterUsername
     */
    public String getTwitterUsername() {
        return twitterUsername;
    }

    /**
     * @param twitterUsername the twitterUsername to set
     */
    public void setTwitterUsername(String twitterUsername) {
        this.twitterUsername = twitterUsername;
    }

    /**
     * @return the truckID
     */
    public int getTruckID() {
        return truckID;
    }

    /**
     * @param truckID the truckID to set
     */
    public void setTruckID(int truckID) {
        this.truckID = truckID;
    }

}
