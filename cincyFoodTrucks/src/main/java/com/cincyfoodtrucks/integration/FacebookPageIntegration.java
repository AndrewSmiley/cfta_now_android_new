package com.cincyfoodtrucks.integration;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.cincyfoodtrucks.R;
import com.cincyfoodtrucks.dao.FacebookPageSQLite;
import com.cincyfoodtrucks.dao.NetworkFacebookPage;
import com.cincyfoodtrucks.dto.FacebookPage;
import com.cincyfoodtrucks.dto.FacebookPageContainer;
import com.cincyfoodtrucks.dto.TruckOwner;
import com.cincyfoodtrucks.utilities.NewsFeedItem;
import com.facebook.AccessToken;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.android.gms.maps.model.LatLng;
//import com.facebook.Request;
//import com.facebook.Response;
//import com.facebook.model.GraphObject;


public class FacebookPageIntegration {
    Context context;
    SharedPreferences preferences;

    public FacebookPageIntegration(Context _context) {
        context = _context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);

    }

    public void setPreference(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getPreference(String prefence) {
        SharedPreferences.Editor editor = preferences.edit();
//		editor.putString(key, value);
        return preferences.getString(prefence, "");

    }


    public void getFacebookData() throws InterruptedException {
        GetFacebookData fbDataFetcher = new GetFacebookData(context);
        Thread t = new Thread(fbDataFetcher);
        t.start();
        t.join();
    }

    public boolean addFacebookPage(FacebookPage page) throws InterruptedException {
        SendFacebookData sendFacebookData = new SendFacebookData(context);
        sendFacebookData.setPage(page);
        Thread t = new Thread(sendFacebookData);
        t.start();
        t.join();
        return true;
    }


    public ArrayList<NewsFeedItem> getFacebookPosts(FacebookPage page, TruckOwner truck) throws InterruptedException {
        GetFacebookPosts gfp = new GetFacebookPosts(page, truck);
        Thread t = new Thread(gfp);
        t.start();
        t.join();
        return gfp.getItems();

    }

    public ArrayList<FacebookPageContainer> getFacebookPages(String accessToken) throws InterruptedException {
        GetFacebookPages gfp = new GetFacebookPages(accessToken);
        Thread t = new Thread(gfp);
        t.start();
        t.join();
        return gfp.pages;

    }

    public boolean postToFacebook(String message, AccessToken token) throws InterruptedException {
        PostToFacebookPage pfp = new PostToFacebookPage(context, message, token);
        Thread t = new Thread(pfp);
        t.start();
        t.join();
        return pfp.success;
    }

    public String createUpdateMessage(Timestamp ts, String truckName, LatLng ltLng){
//        __block NSString *someString = [NSString stringWithFormat:@"%@ will be at http://maps.google.com/maps?q=%@,%@ until %@! #CFTANow",
// [truck truckName], [truck latitude], [truck longitude], [dateFormatter stringFromDate:truckHours]];
        TruckIntegration truckIntegrator = new TruckIntegration(context);

        return truckName+" will be at http://maps.google.com/maps?q="+ltLng.latitude+","+ltLng.longitude+" until "+truckIntegrator.getHumanReadableTruckHours(ts.getTime())+"! #CFTANow";
    }

    private class GetFacebookData implements Runnable {
        NetworkFacebookPage networkFacebookPage;
        Context context;
        FacebookPageSQLite facebookPageSQLite;

        public GetFacebookData(Context c) {
            context = c;
        }

        @Override
        public void run() {
            networkFacebookPage = new NetworkFacebookPage();
            facebookPageSQLite = new FacebookPageSQLite(context);
            ArrayList<FacebookPage> pages = networkFacebookPage.getAllFacebookPages();
            if (pages.size() >= 1) {
                for (FacebookPage page : pages) {
                    facebookPageSQLite.addFacebookPage(page);
                }

            }

        }

    }


    private class SendFacebookData implements Runnable {
        NetworkFacebookPage networkFacebookPage;
        Context context;
        FacebookPageSQLite facebookPageSQLite;
        FacebookPage page;

        /**
         * @return the page
         */
        public FacebookPage getPage() {
            return page;
        }

        /**
         * @param page the page to set
         */
        public void setPage(FacebookPage page) {
            this.page = page;
        }

        public SendFacebookData(Context c) {
            context = c;
        }

        @Override
        public void run() {
            networkFacebookPage = new NetworkFacebookPage();
            facebookPageSQLite = new FacebookPageSQLite(context);
            //not entirely sure how we want to handle these right here..
            //probably want to send them back to the UI at some point
            networkFacebookPage.addFacebookPage(page);


        }
    }

    private class GetFacebookPosts implements Runnable {
        private FacebookPage page;
        private TruckOwner truck;
        private ArrayList<NewsFeedItem> items;

        public GetFacebookPosts(FacebookPage fb, TruckOwner t) {
            page = fb;
            truck = t;
            items = new ArrayList<NewsFeedItem>();
        }

        public ArrayList<NewsFeedItem> getItems() {
            return this.items;
        }

        @Override
        public void run() {
            Bundle params = new Bundle();
            params.putString("access_token", "696538993808289|0ac350c45a50e74f943f0e7df6a10c12");

            final String requestId = "/" + page.getPageName() + "/posts";

            GraphRequest request = new GraphRequest(null, requestId, params, HttpMethod.GET, new GraphRequest.Callback() {
                public void onCompleted(GraphResponse response) {
//			  		GraphObject graphObject = response.getGraphObject();
                    FacebookRequestError error = response.getError();
                    if (error != null) {
//                        Log.e("Error", error.getErrorMessage());
                    } else {
                        //object.asMap();
                        JSONObject values = response.getJSONObject();
                        JSONArray json = null;
                        try {
                            json = (JSONArray) values.get("data");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        for (int i = 0; i < json.length(); i++) {
                            JSONObject jsonObj;
                            try {
                                jsonObj = json.getJSONObject(i);
                                NewsFeedItem item = new NewsFeedItem();
                                item.setTruck(truck);
                                item.setContent((String) jsonObj.get("message"));
                                String tmpUpdateTimeStr = (String) jsonObj.get("updated_time");
//								String updateTimeStr = tmpUpdateTimeStr.replaceAll("T", " ").replaceAll("+", " +");
                                String updateTimeStr = tmpUpdateTimeStr.replace("T", " ").replace("+", " +");
                                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ZZZZ");
                                item.setDate(formatter.parse(updateTimeStr));
                                items.add(item);
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        }

                        String myString = "";
                    }


                }
            });

//	    Request.executeAndWait(request);
            GraphRequest.executeAndWait(request);

        }
    }

    private class GetFacebookPages implements Runnable {
        private String accessToken;
        public ArrayList<FacebookPageContainer> pages;

        public GetFacebookPages(String _accesToken) {
            pages = new ArrayList<>();
            accessToken = _accesToken;

        }

        @Override
        public void run() {
            Bundle params = new Bundle();
            params.putString("access_token", accessToken);
            params.putString("fields", "id");
            GraphRequest request = new GraphRequest(null, "me", params, HttpMethod.GET, new GraphRequest.Callback() {
                public void onCompleted(GraphResponse response) {
                    FacebookRequestError error = response.getError();
                    if (error != null) {
                        Log.e("Error", error.getErrorMessage());
                    } else {
                        JSONObject values = response.getJSONObject();
                        try {
                            //so I have to get the ID first
                            String id = values.getString("id");
                            Bundle p = new Bundle();
                            p.putString("access_token", accessToken);
                            //yay nest the graph requests
                            //once we get the id we can get their pages
                            GraphRequest pagesRequest = new GraphRequest(null, "/" + id + "/accounts", p, HttpMethod.GET, new GraphRequest.Callback() {
                                public void onCompleted(GraphResponse response) {
                                    FacebookRequestError error = response.getError();
                                    if (error != null) {
                                        Log.e("Error", error.getErrorMessage());
                                    } else {
                                        //object.asMap();
                                        JSONObject values = response.getJSONObject();
                                        JSONArray array = null;
                                        try {
                                            array = values.getJSONArray("data");
                                            for (int i = 0; i < array.length(); i++) {
                                                //consolidation *high pitched voice* motha fucka!
                                                pages.add(new FacebookPageContainer(array.getJSONObject(i).getString("name"), array.getJSONObject(i).getString("access_token"), array.getJSONObject(i).getString("id")));
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }
                            });
                            GraphRequest.executeAndWait(pagesRequest);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            });
            GraphRequest.executeAndWait(request);
        }
    }


    private class PostToFacebookPage implements Runnable {
        private Context context;
        private boolean success;
        private String message;
        AccessToken motherFucker;

        public PostToFacebookPage(Context c, String m, AccessToken _motherFucker) {
            context = c;
            success = false;
            message = m;
            motherFucker=_motherFucker;
        }

        @Override
        public void run() {

            Bundle params = new Bundle();

            params.putString("message", message);
            final String requestId = "/" + getPreference(context.getResources().getString(R.string.facebook_page_name)) + "/feed";
            GraphRequest request = new GraphRequest(motherFucker, requestId, params, HttpMethod.POST, new GraphRequest.Callback() {
                public void onCompleted(GraphResponse response) {
                    FacebookRequestError error = response.getError();
                    if (error != null) {
                        Log.e("Error", error.getErrorMessage());

                    } else {
                        success = true;
                    }


                }
            });

//	    Request.executeAndWait(request);
            GraphRequest.executeAndWait(request);
        }
    }
}
