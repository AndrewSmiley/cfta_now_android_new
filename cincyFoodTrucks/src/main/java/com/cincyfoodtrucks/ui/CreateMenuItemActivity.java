package com.cincyfoodtrucks.ui;

import java.io.InputStream;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cincyfoodtrucks.R;
import com.cincyfoodtrucks.dto.MenuItemImage;
import com.cincyfoodtrucks.dto.TruckMenuItem;
import com.cincyfoodtrucks.dto.User;
import com.cincyfoodtrucks.integration.IMenuIntegration;
import com.cincyfoodtrucks.integration.ITruckIntegration;
import com.cincyfoodtrucks.integration.IUserIntegration;
import com.cincyfoodtrucks.integration.MenuIntegration;
import com.cincyfoodtrucks.integration.TruckIntegration;
import com.cincyfoodtrucks.integration.UserIntegration;

public class CreateMenuItemActivity extends BaseActionMenuActivity {
	
	
	private IMenuIntegration menuIntegrator;
	private EditText newItemTitleEditText;
	EditText newItemDescEditText;
	EditText newItemPriceEditText;
	ITruckIntegration truckIntegrator;
	Button submitButton;
	String provider;
	Uri imageURI;
	Bitmap bitmapImage;
	TextView menuItemItemLbl;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//instanciate the vars
			menuIntegrator = new MenuIntegration(this);
		truckIntegrator = new TruckIntegration(this);
		
		
		//need to do someting in case the data is offline, need to resync before the data can be uploaded? 
		submitButton = (Button)findViewById(R.id.btnCreateNewMenuItemSubmit);
		try {
			truckIntegrator.executeGetTruckDataAsyncTask(getApplicationContext());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_menu_item);
		menuItemItemLbl = (TextView)findViewById(R.id.lblMenuItemImage);
	}



	/**
	 * Method to process the action for when the sumbit new menu item is clicked
	 * @param v
	 */
	public void onSubmitNewMenuItemClicked(View v)
	{
		
		newItemTitleEditText = (EditText)findViewById(R.id.txtNewMenuItemTitle);
		newItemDescEditText = (EditText)findViewById(R.id.txtNewMenuItemDescription);
		newItemPriceEditText = (EditText)findViewById(R.id.txtNewMenuItemPrice);
	
		//get the values
		String itemTitle = newItemTitleEditText.getText().toString();
		String itemDesc = newItemDescEditText.getText().toString();
		String itemPrice = newItemPriceEditText.getText().toString();
		//Make sure all fields are filled out
		if(itemTitle.equals("") || itemDesc.equals("") || itemPrice.equals(""))
			
		{
			Toast.makeText(this, getResources().getString(R.string.strAllFieldsRequired), Toast.LENGTH_LONG).show();
		
		}else{
		//create a new item to be created
		TruckMenuItem item = new TruckMenuItem();
		item.setTitle(itemTitle);
		item.setPrice(itemPrice);
		item.setDescription(itemDesc);
		
		
		try {
			//get the truck owner using shared prefs
			
			IUserIntegration userIntegration = new UserIntegration(this);
			User user = userIntegration.getUserFromSharedPreferences();
			
//			//Here, we'll get the bitmap from the uri we provided earlier
//			 InputStream stream = getContentResolver().openInputStream(imageURI);
//	         bitmapImage = BitmapFactory.decodeStream(stream);
//	         stream.close();
//	         
//	         //instanciate the new menu item image
//	         //and set the values 
//	         MenuItemImage menuItemImage = new MenuItemImage();
//	         //all we need to set here is the image. The other information gets set in the network call method 
//	         menuItemImage.setImageContent(bitmapImage);
//	         
	         //not sure what we'll do with the bitmap image just yet
	         //do the db save stuff here
	         
			
			//set the item id
			item.setTruckID(user.getTruckID());

			//Attempt to send the truck data to the remote server
			menuIntegrator.executeSendTruckMenuTask(item);
			
			//if we get here just let them know
			Toast.makeText(this, getResources().getString(R.string.strMenuItemAdded), Toast.LENGTH_LONG).show();
			
			//bring them back to the main menu
			Intent mainIntent = new Intent(this, MenuActivity.class);
			startActivity(mainIntent);
		} catch (Exception e) {
			
			//let them know an error occured
			Toast.makeText(this, getResources().getString(R.string.strErrorAddingMenuItem), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		
		}
		
		
	}
	
	/**
	 * Method to start the intent to open the gallery to choose the menu item image 
	 * @param v
	 */
	public void onChooseExistingClick(View v)
	{
		
		Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 1);
		
	}
	
	
	/**
	 * Here, we'll override the onActivityResult method so we can get the image data. This is coming from the gallery chooser right now
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) { 

	        if (resultCode == RESULT_OK) {

	                if (requestCode == 1) {

	                        // currImageURI is the global variable I'm using to hold the content:// URI of the image
	                        imageURI = data.getData();
	                        menuItemItemLbl.setText(getResources().getString(R.string.strImageLoaded));
	                }
	        }
	}

	
	
	
	 /**
     * Method to get the full path from the uri
     * @param uri the uri we wish to get the path for
     */
    public String getPath(Uri uri) {
            // just some safety built in 
            if( uri == null ) {
                // TODO perform some logging or show user feedback
                return null;
            }
            // try to retrieve the image from the media store first
            // this will only work for images selected from gallery
            String[] projection = { MediaStore.Images.Media.DATA };
            Cursor cursor = managedQuery(uri, projection, null, null, null);
            if( cursor != null ){
                int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            }
            // this is our fallback here
            return uri.getPath();
    }

}
