package com.cincyfoodtrucks.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.cincyfoodtrucks.R;
import com.cincyfoodtrucks.dao.TruckServiceSQLiteStub;
import com.cincyfoodtrucks.dto.FacebookPage;
import com.cincyfoodtrucks.dto.FacebookPageContainer;
import com.cincyfoodtrucks.integration.FacebookPageIntegration;
import com.cincyfoodtrucks.integration.TruckIntegration;
import com.cincyfoodtrucks.integration.UserIntegration;
import com.facebook.AccessToken;

import java.util.ArrayList;

/**
 * Created by Andrew on 6/13/15.
 */
public class SelectFacebookPageActivity extends BaseActionMenuActivity {
    Spinner pages;
    ArrayList<FacebookPageContainer> pageList;
    FacebookPageIntegration fbIntegration;
    TruckIntegration truckIntegration;
    UserIntegration userIntegration;
    TruckServiceSQLiteStub truckSQLite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_facebook_page);
        pages = (Spinner) findViewById(R.id.facebookPageList);
        AccessToken token = AccessToken.getCurrentAccessToken();
        truckIntegration = new TruckIntegration(getApplicationContext());
        userIntegration = new UserIntegration(getApplicationContext());
        truckSQLite = new TruckServiceSQLiteStub(getApplicationContext());
        if (token != null) {
                fbIntegration = new FacebookPageIntegration(getApplicationContext());
            try {
                pageList = fbIntegration.getFacebookPages(token.getToken().toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        addItemsToSpinner();
    }



    public void onUpdateClicked(View v){
//        Toast.makeText(getApplicationContext(), "FUCK YOU", Toast.LENGTH_LONG).show();
        FacebookPageContainer cont = (FacebookPageContainer) pages.getSelectedItem();
        try {
            //add the data to the DB
            //let's try this shit
            fbIntegration.addFacebookPage(new FacebookPage(1, cont.getId(), userIntegration.getUserFromUsername(userIntegration.getUsernameFromSharedPreferences()).getTruckID()));
            //if it works, go get the data from the server
            fbIntegration.getFacebookData();

            //now add it to shared preferences so we can save it
            fbIntegration.setPreference(getResources().getString(R.string.facebook_page_name), cont.getPageName());
            fbIntegration.setPreference(getResources().getString(R.string.facebook_page_access_token), cont.getAccessToken());
            Intent resultData = new Intent();
            resultData.putExtra(ConnectFacebookActivity.FACEBOOK_SELECT_PAGE_KEY, ConnectFacebookActivity.FACEBOOK_SELECT_PAGE_VALUE);
            setResult(Activity.RESULT_OK, resultData);
            finish();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.strGenericError, Toast.LENGTH_LONG).show();
//            e.printStackTrace();
        }
//        fbIntegration.addFacebookPage()

    }


    public void addItemsToSpinner(){
        /*
        WHAT IS YOUR PRIMARY MALFUNCTION?
         */
        //was that seriously it?? Srsly??

        ArrayAdapter<FacebookPageContainer> dataAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.facebook_page_spinner_item, pageList);
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        pages.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pages.setAdapter(dataAdapter);

    }
}
