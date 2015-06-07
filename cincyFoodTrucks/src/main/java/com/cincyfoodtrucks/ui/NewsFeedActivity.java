package com.cincyfoodtrucks.ui;

import java.util.ArrayList;

import android.content.Context;
import android.location.Criteria;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cincyfoodtrucks.R;
import com.cincyfoodtrucks.dao.FacebookPageSQLite;
import com.cincyfoodtrucks.dao.IFacebookPageSQLite;
import com.cincyfoodtrucks.dao.ITruckServiceSQLite;
import com.cincyfoodtrucks.dao.TruckServiceSQLiteStub;
import com.cincyfoodtrucks.dto.FacebookPage;
import com.cincyfoodtrucks.integration.FacebookPageIntegration;
import com.cincyfoodtrucks.utilities.NewsFeedItem;
import com.facebook.FacebookSdk;

public class NewsFeedActivity extends BaseActionMenuActivity {
	NewsFeedAdapter adapter;
	private IFacebookPageSQLite facebookPageSQLite;
	private ArrayList<NewsFeedItem> newsFeedItems;
	ListView lv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		FacebookSdk.sdkInitialize(getApplicationContext());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newsfeed);
		lv = (ListView) findViewById(R.id.newsFeedList);
		newsFeedItems = new ArrayList<NewsFeedItem>();
		facebookPageSQLite = new FacebookPageSQLite(getApplicationContext());
		
		FacebookPageIntegration fbIntegration = new FacebookPageIntegration(getApplicationContext());
		//get the facebook data
		try {
			fbIntegration.getFacebookData();
		} catch (InterruptedException e) {
			Toast.makeText(getApplicationContext(), "An error occurred fetching facbook data", Toast.LENGTH_LONG).show();
		}
		
		for (FacebookPage page : facebookPageSQLite.getAllFacebookPages()){
			try {
//				IFacebookPageSQLite facebookPageSQLite = new FacebookPageSQLite(getApplicationContext());
				ITruckServiceSQLite truckServiceSQLite = new TruckServiceSQLiteStub(getApplicationContext());
				ArrayList<NewsFeedItem>tmp =  fbIntegration.getFacebookPosts(page, truckServiceSQLite.getTruckByTruckID(page.getTruckID()));
				if(tmp.size() > 0){
					for(NewsFeedItem item : tmp){
						newsFeedItems.add(item);
					}
				}
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
				
			}
			
			adapter = new NewsFeedAdapter(newsFeedItems, getApplicationContext());
			lv.setAdapter(adapter);
//			/* make the API call */
//			new Request(
//			    session,
//			    "/me",
//			    null,
//			    HttpMethod.GET,
//			    new Request.Callback() {
//			        public void onCompleted(Response response) {
//			            /* handle the result */
//			        }
//			    }
//			).executeAsync();
		}
		
		
//		Toast.makeText(getApplicationContext(), "Test!", Toast.LENGTH_LONG).show();
		
		
//		thoughtsText = (EditText)findViewById(R.id.commentBox);
	}


	private class NewsFeedAdapter extends BaseAdapter{

		LayoutInflater inflater;
		ArrayList<NewsFeedItem> items;
		Context context;
		public NewsFeedAdapter(ArrayList<NewsFeedItem> i, Context c){
			super();
			this.context = c;
			this.items = i;
			this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return items.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View arg1, ViewGroup arg2) {
			NewsFeedItem item = items.get(position);
			View vi=arg1;
			if(arg1 == null){
				 vi = inflater.inflate(R.layout.newsfeed_row, null);
				 TextView txtTruckName=(TextView) vi.findViewById(R.id.txtNewsFeedTruckName);
				 TextView txtContext = (TextView) vi.findViewById(R.id.newsFeedPostContent);
				 txtTruckName.setText(item.getTruck().getTruckName());
				 txtContext.setText(item.getContent());
			}
			
			// TODO Auto-generated method stub
			return vi;
		}
		
	}
	
}
