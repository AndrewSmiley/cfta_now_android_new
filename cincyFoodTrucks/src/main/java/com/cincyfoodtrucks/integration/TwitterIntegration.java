package com.cincyfoodtrucks.integration;

import android.content.Context;
import android.content.SharedPreferences;

import com.cincyfoodtrucks.dao.INetworkDAO;
import com.cincyfoodtrucks.dao.INetworkTwitterUser;
import com.cincyfoodtrucks.dao.ITwitterUserSQLite;
import com.cincyfoodtrucks.dao.NetworkDAO;
import com.cincyfoodtrucks.dao.NetworkTwitterUser;
import com.cincyfoodtrucks.dao.TwitterUserSQLite;
import com.cincyfoodtrucks.dto.TwitterUser;
import com.cincyfoodtrucks.utilities.NewsFeedItem;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.internal.TwitterApi;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Andrew on 6/24/15.
 */
public class TwitterIntegration {
    Context context;
    SharedPreferences preferences;
    TruckIntegration truckIntegrator;
    public TwitterIntegration(Context _context){
        context = _context;
        truckIntegrator = new TruckIntegration(context);
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

    public void getTwitterData() throws InterruptedException {
        Thread t = new Thread(new GetTwitterData(context));
        t.start();
        t.join();
    }

    public boolean sendTwitterData(TwitterUser user) throws InterruptedException {
        SendTwitterData std = new SendTwitterData(context,user);
        Thread t = new Thread(std);
        t.start();
        t.join();
        return std.isResult();

    }



    public void testGetTwitterFeed() throws IOException, InterruptedException {
        String uri = "https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=AndrewSmiley937&count=2";
        TwitterRestAPI tra = new TwitterRestAPI(uri);
        Thread t = new Thread(tra);
        t.start();
        t.join();
//        INetworkDAO networkDAO = new NetworkDAO();

////        String uri  = "http://cincyfoodtruckapp.com/mobile_request_handler.php?action=update_time_at_location&hours_at_location="+hours+"&truck_id="+truckID;
//        String response =  networkDAO.sendHTTPGetRequest(uri);
//
//        String test =response;


    }


    private class TwitterRestAPI implements Runnable{
        private String uri;
        private String response;
        INetworkDAO networkDAO;

        public TwitterRestAPI() {
            this.networkDAO = new NetworkDAO();
        }

        public TwitterRestAPI(String response, String uri) {
            this.response = response;
            this.uri = uri;
            this.networkDAO = new NetworkDAO();
        }

        public TwitterRestAPI(String uri) {
            this.uri = uri;
            this.networkDAO = new NetworkDAO();
        }

        @Override
        public void run() {
            try {
//                TwitterApi
                String r =networkDAO.sendHTTPGetRequest(this.uri);
                this.response=r;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public String getResponse() {
            return response;
        }

        public void setResponse(String response) {
            this.response = response;
        }
    }
    public ArrayList<NewsFeedItem> getTwitterFeed(ArrayList<TwitterUser> twitterUsers) throws InterruptedException {
        ArrayList<NewsFeedItem> items = new ArrayList<>();

        GetTwitterFeed gtf = new GetTwitterFeed(context, twitterUsers, items);
        Thread t = new Thread(gtf);
        t.start();
//        t.join();
//        while (gtf.getTwits().size() == 0){
//            wait(500);
//        }
//
//        synchronized (items){
//            items.wait();
//        }
        t.join();
        return gtf.getTwits();
    }

    public TweetResult postTweet(String message) throws InterruptedException {
        PostTweet pt = new PostTweet(message);
        Thread t = new Thread(pt);
        t.start();
        t.join();
        return pt.getTweetResult();
    };


    private class GetTwitterData implements Runnable{
        protected Context context;
        protected GetTwitterData(Context _context){
            context=_context;
        }

        @Override
        public void run() {
            INetworkTwitterUser networkTwitterUser = new NetworkTwitterUser();
            ITwitterUserSQLite twitterUserSQLite = new TwitterUserSQLite(context);
            ArrayList<TwitterUser> results = networkTwitterUser.getAllTwitterUsers();
            if (results.size() > 0){
                for (TwitterUser user : results){
                    twitterUserSQLite.addTwitterUser(user);
                }
            }
        }
    }

    private class SendTwitterData implements Runnable{
        private Context context;
        private TwitterUser user;
        private boolean result;
        protected  SendTwitterData(Context _context, TwitterUser _user){
            setContext(_context);
            setUser(_user);
            setResult(false);
        }

        @Override
        public void run() {
            setResult(new NetworkTwitterUser().addNewTwitterUser(getUser()));
        }

        public Context getContext() {
            return context;
        }

        public void setContext(Context context) {
            this.context = context;
        }

        public TwitterUser getUser() {
            return user;
        }

        public void setUser(TwitterUser user) {
            this.user = user;
        }

        public boolean isResult() {
            return result;
        }

        public void setResult(boolean result) {
            this.result = result;
        }
    }

    private class GetTwitterFeed implements Runnable{
        private Context context;
        private  ArrayList<TwitterUser> users;
        private ArrayList<NewsFeedItem> twits;
        public ArrayList<NewsFeedItem> items;
        protected GetTwitterFeed(Context _context, ArrayList<TwitterUser> _users){
            setContext(_context);
            setUsers(_users);
            twits = new ArrayList<NewsFeedItem>();
        }

        protected GetTwitterFeed(Context _context, ArrayList<TwitterUser> _users, ArrayList<NewsFeedItem> _items){
            setContext(_context);
            setUsers(_users);
            twits = new ArrayList<NewsFeedItem>();
            items = _items;
        }
        @Override
        public void run() {
            for (int i = 0; i <  users.size(); i++) {
                final TwitterUser user = users.get(i);
                final int pos = i;
                final int size = users.size();
//                TwitterCore.getInstance().getApiClient(TwitterCore.getInstance().getSessionManager().getActiveSession())
                TwitterCore.getInstance().getApiClient(TwitterCore.getInstance().getSessionManager().getActiveSession()).getStatusesService()
                        .userTimeline(null,
                                user.getTwitterUsername(),
                                5,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                new Callback<List<Tweet>>() {
                                    @Override
                                    public void success(Result<List<Tweet>> result) {
                                        for (Tweet t : result.data) {

                                            SimpleDateFormat dateFormtter = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZ yyyy");
                                            Date d;
                                            try {
                                                d = dateFormtter.parse(t.createdAt);
                                            } catch (ParseException e) {
                                                d = new Date();
                                            }

                                            items.add(new NewsFeedItem(truckIntegrator.getTruckByTruckID(user.getTruckID()), t.text, d));
//                                            //ok so after we do this...
//                                            if (pos == size-1){
//                                                synchronized (items){
//                                                    items.notify();
//                                                }
//
//                                            }

                                        }
                                    }

                                    @Override
                                    public void failure(TwitterException exception) {
                                        android.util.Log.d("twittercommunity", "exception " + exception);
                                    }
                                });
            }

        }

        public Context getContext() {
            return context;
        }

        public void setContext(Context context) {
            this.context = context;
        }

        public ArrayList<TwitterUser> getUsers() {
            return users;
        }

        public void setUsers(ArrayList<TwitterUser> users) {
            this.users = users;
        }

        public ArrayList<NewsFeedItem> getTwits() {
            return twits;
        }

        public void setTwits(ArrayList<NewsFeedItem> twits) {
            this.twits = twits;
        }
    }


    private class PostTweet implements  Runnable{
        private String message;
        private TweetResult tweetResult;
        public PostTweet( String _message){
            setMessage(_message);
        }

        @Override
        public void run() {
            StatusesService statusesService = Twitter.getApiClient(TwitterCore.getInstance().getSessionManager().getActiveSession()).getStatusesService();

//        void update(@Field("status") String var1, @Field("in_reply_to_status_id") Long var2, @Field("possibly_sensitive") Boolean var3, @Field("lat") Double var4, @Field("long") Double var5, @Field("place_id") String var6, @Field("display_cooridnates") Boolean var7, @Field("trim_user") Boolean var8, Callback<Tweet> var9);
            statusesService.update(getMessage(), null, null, null, null, null, null, null, new Callback<Tweet>() {

                @Override
                public void success(Result<Tweet> result) {
                    setTweetResult(new TweetResult(null, true));

                }

                @Override
                public void failure(TwitterException e) {
                    setTweetResult(new TweetResult(e.getLocalizedMessage(), false));
                }
            });

        }
        public TweetResult getTweetResult() {
            return tweetResult;
        }

        public void setTweetResult(TweetResult tweetResult) {
            this.tweetResult = tweetResult;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

    }

    public class TweetResult{
        private String errorMessage;
        private boolean success;


        public TweetResult() {
        }

        public TweetResult(String errorMessage, boolean success) {
            this.errorMessage = errorMessage;
            this.success = success;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }
}
