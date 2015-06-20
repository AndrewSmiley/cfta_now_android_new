package com.cincyfoodtrucks.dto;

/**
 * Created by Andrew on 6/14/15.
 */
public class FacebookPageContainer {
    private String pageName;
    private String accessToken;
    private String id;

    public FacebookPageContainer(String _pageName, String _accessToken, String _id){
        setPageName(_pageName);
        setAccessToken(_accessToken);
        setId(_id);
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String toString(){
        return getPageName();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
