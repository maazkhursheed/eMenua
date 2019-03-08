package com.attribe.waiterapp.screens;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.attribe.waiterapp.Database.DatabaseHelper;
import com.attribe.waiterapp.Database.TABLE_CATEGORIES;
import com.attribe.waiterapp.R;
import com.attribe.waiterapp.models.*;
import com.attribe.waiterapp.network.PassCodeResponse;
import com.attribe.waiterapp.network.RestClient;

import com.attribe.waiterapp.utils.DeviceInfo;
import com.attribe.waiterapp.utils.DevicePreferences;
import com.attribe.waiterapp.utils.ExceptionHanlder;
import com.google.gson.Gson;
import org.apache.http.util.ByteArrayBuffer;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sabih Ahmed on 5/18/2015.
 */
public class SplashScreen extends Activity{

    private static final String PASSCODE = "17408";
    private Handler postDelayedHandler;
	private DatabaseHelper databaseHelper;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
	private TABLE_CATEGORIES table_categories;
	ArrayList<Integer> categoryIdList;
	private Gson gson;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHanlder(this));
		setContentView(R.layout.screen_splash);
        initContents();
        DevicePreferences.getInstance().setRtlLayout(true);
        if(DevicePreferences.getInstance().getClientKey() == null ){// app is being installed on first time

            clearDbTables();

            showSetupDialog();
        }

        else{ // app has been installed earlier and data has been download

            showStartScreen();



        }

	}

    private void showStartScreen() {
        //check for Screen size
        if(DeviceInfo.getScreen()==DeviceInfo.ScreenSize.SCREEN_LARGE){
            //in case of 7 inch screen
            showMenuScreen();
        }

        if(DeviceInfo.getScreen()==DeviceInfo.ScreenSize.SCREEN_XLARGE){
            showCarouselScreen();
        }
    }

    private void getClientKey() {

        if(DevicePreferences.getInstance().isNetworkAvailable()){
            RestClient.getAdapter().getClientKey(PASSCODE, new Callback<PassCodeResponse>() {

                @Override
                public void success(PassCodeResponse passCodeResponse, Response response) {

                    String responseJson = gson.toJson(passCodeResponse);
                    PassCodeResponse responsePasscode = gson.fromJson(responseJson, PassCodeResponse.class);

                    if (responsePasscode.getStatus().equals(PassCodeResponse.RESPONSE_PASSCODE_INVALID)) {
                        //TODO: Handle invalid passcode
                    }

                    if (responsePasscode.getStatus().equals(PassCodeResponse.RESPONSE_PASSCODE_SUCCESS)) {
                        DevicePreferences.getInstance().setClientKey(responsePasscode.getApi_key());

                        //registerDevice();

                        showSetupDialog();
                        //syncData();
                    }


                }

                @Override
                public void failure(RetrofitError retrofitError) {

                }
            });
        }

        else{
            //showNoInternetDialog();
        }

    }

    private void clearDbTables() {
        databaseHelper.clearCategoryTable();
        databaseHelper.clearMenuTable();
        databaseHelper.clearImagesTable();
        databaseHelper.clearOrdersTable();
        databaseHelper.clearOrderDetailsTable();
    }

    private void registerDevice() {
        DeviceRegister deviceRegistration = new DeviceRegister(DevicePreferences.getInstance().getDeviceId(),"","");

        RestClient.getAdapter().registerDevice(deviceRegistration, new Callback<DeviceRegister.Response>() {
            @Override
            public void success(DeviceRegister.Response response, Response response2) {

                if (response.status.equals("501")) { //Device has already registered, skip registration

                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {

            }
        });

    }

    private void initContents() {
		gson = new Gson();
		databaseHelper = new DatabaseHelper(this);
		databaseHelper.getWritableDatabase();
		DevicePreferences.getInstance().init(SplashScreen.this);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

	}

	private void showMenuScreen() {
		Intent intent = new Intent(SplashScreen.this, MainActivity.class);
		startActivity(intent);
		finish();
	}

    private void showCarouselScreen(){
        Intent intent = new Intent(SplashScreen.this, CarouselScreen.class);
        startActivity(intent);
        finish();
    }

	private void getItems() {
		int categoryIdListSize = categoryIdList.size();
		ArrayList<Item> itemList = new ArrayList<Item>();
		Item receivedItem;
		for (int i = 0; i < categoryIdListSize; i++) {

			try {
				itemList = RestClient.getAdapter().getItemSync(
						categoryIdList.get(i));

			}catch(RetrofitError error){
				itemList = RestClient.getAdapter().getItemSync(
						categoryIdList.get(i));

			}

			int itemListSize = itemList.size();

			for(int x = 0; x < itemListSize; x++){
				receivedItem = itemList.get(x);

				String receivedItemJson = gson.toJson(receivedItem);

				Item item = gson.fromJson(receivedItemJson,Item.class);


				databaseHelper.addItem(item);
				List<Image> images = itemList.get(x).getImages();
				int imagesListSize = images.size();
				for (int z = 0; z < imagesListSize  ; z++) {

					//byte[] itemImage = convertToBlob(images.get(z).getUrl());
					databaseHelper.addImages(item.getId(), images.get(x).getUrl(),
							images.get(z).getCreated_at(), images.get(z).getUpdated_at());
				}
			}
				
			}
			
			
			
		Toast.makeText(SplashScreen.this, "Items added successfully into db",
				Toast.LENGTH_SHORT).show();


		showMenuScreen();

	}

	private void getCategories() {
		categoryIdList = new ArrayList<Integer>();
		ArrayList<Category> categoryArrayList;
		try {
			categoryArrayList = RestClient.getAdapter()
					.getCategoriesSync();
		}catch (RetrofitError retrofitError){
			categoryArrayList=RestClient.getAdapter().getCategoriesSync();
		}

		for (int i = 0; i < categoryArrayList.size(); i++) {
			Category receivedCategory = categoryArrayList.get(i);
			categoryIdList.add(categoryArrayList.get(i).getId());

			String receivedCategoryJson = gson.toJson(receivedCategory);


			Category category=gson.fromJson(receivedCategoryJson, Category.class);

			if(!(category.getImageUrl()== null)){
				byte[] imageBlob = convertToBlob(category.getImageUrl());


				category.setImageBlob(imageBlob);

			}
			databaseHelper.addCategory(category);

		}
		Toast.makeText(SplashScreen.this,
				"Categories added successfully into db", Toast.LENGTH_SHORT)
				.show();


	}

	private byte[] convertToBlob(String imageURL) {

		try {
			URL imageUrl=new URL(imageURL);
			URLConnection urlConnection = imageUrl.openConnection();

			InputStream inputStream = urlConnection.getInputStream();
			BufferedInputStream bis=new BufferedInputStream(inputStream);

			ByteArrayBuffer baf=new ByteArrayBuffer(500);

			int current=0;
			while((current = bis.read())!=-1){
				baf.append((byte) current);
			}

			return baf.toByteArray();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


    private void saveImageIntoFile(String imageUrl,String itemName,String imageCreateDate){
        //Image file is saved in following pattern
        //filepath = ItemName+ImageFileCreationDateTime
        try {
            URL imageURL = new URL(imageUrl);
            URLConnection connection = imageURL.openConnection();

            InputStream inputStream = new BufferedInputStream(imageURL.openStream(),10240);

            File cacheDir = getCacheDir();
            File cacheFile = new File(cacheDir,itemName + imageCreateDate);
            FileOutputStream fileOutputStream = new FileOutputStream(cacheFile);

            byte buffer []= new byte[1024];
            int dataSize;
            int loadSize = 0;

            while((dataSize = inputStream.read(buffer))!= -1){

                loadSize += dataSize;
                fileOutputStream.write(buffer,0,dataSize);

            }

            fileOutputStream.close();




        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public void showPassCodeDialog(){

		Dialog dialog = new Dialog(this);

		dialog.setTitle(R.string.pass_code_title);
		dialog.setContentView(R.layout.dialog_pass_code);

		EditText passCodeText=(EditText)dialog.findViewById(R.id.dialog_pass_code_text);
		final String passCode = passCodeText.getText().toString();
		Button verifyButton = (Button)dialog.findViewById(R.id.dialog_pass_code_button);
		verifyButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {

				RestClient.getAdapter().getClientKey(passCode, new Callback<PassCodeResponse>() {


                    @Override
                    public void success(PassCodeResponse passCodeResponse, Response response) {

                        DevicePreferences.getInstance().init(SplashScreen.this);

                        DevicePreferences.getInstance().setClientKey(passCodeResponse.toString());

                        showSetupDialog();
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {

                    }
                });
			}
		});
		dialog.show();

	}

	private void syncData(){

        showProgress(getResources().getString(R.string.getting_data));

		RestClient.getAdapter().syncData(new Callback<ArrayList<Data>>() {


            @Override
            public void success(ArrayList<Data> data, Response response) {

                for (int i = 0; i < data.size(); i++) {

                    String categoryJson = gson.toJson(data.get(i));
                    Category receivedCategory = gson.fromJson(categoryJson, Category.class);
                    if (receivedCategory.getImageUrl() != null) {

                        saveImageIntoFile(receivedCategory.getImageUrl(), receivedCategory.getName(), receivedCategory.getCreated_at());

                        //receivedCategory.setImageBlob(convertToBlob(receivedCategory.getImageUrl()));
                    }

                    databaseHelper.addCategory(receivedCategory);


                    ArrayList<Item> menus = data.get(i).getMenus();
                    for (int z = 0; z < menus.size(); z++) {

                        String itemJson = gson.toJson(menus.get(z));
                        Item receivedItem = gson.fromJson(itemJson, Item.class);


                        databaseHelper.addItem(receivedItem);
                        List<Image> images = receivedItem.getImages();
                        int imageListSize = images.size();
                        for (int x = 0; x < imageListSize; x++) {

                            databaseHelper.addImages(receivedItem.getId(), images.get(x).getUrl(), images.get(x).getCreated_at(), images.get(x).getUpdated_at());
                            saveImageIntoFile(images.get(x).getUrl(), receivedItem.getName(), images.get(x).getCreated_at());
                            //byte[] itemImage = convertToBlob(images.get(x).getUrl());
                            //databaseHelper.addImages(receivedItem.getId(),itemImage,images.get(x).getCreated_at(),images.get(x).getUpdated_at());
                        }

                    }

                }
                showStartScreen();

            }

            @Override
            public void failure(RetrofitError retrofitError) {

            }
        });
	}

    private void showProcessStatus(final String message) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToast(message);
            }
        });
    }

    private void showSetupDialog() {
		final Dialog dialog = new Dialog(SplashScreen.this);

		dialog.setTitle(R.string.title_device_registration);
		dialog.setContentView(R.layout.dialog_register_device);

        final EditText passCode=(EditText)dialog.findViewById(R.id.dialog_register_text);
		final EditText deviceNameText=(EditText) dialog.findViewById(R.id.dialog_register_deviceName);
		Button registerButton = (Button)dialog.findViewById(R.id.dialog_register_button);

		registerButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {

                String passcode = passCode.getText().toString();

				String deviceName = deviceNameText.getText().toString();
				String deviceId = DevicePreferences.getInstance().getDeviceId();

				DeviceRegister deviceRegister=new DeviceRegister(deviceId,deviceName,passcode);
                dialog.dismiss();
                showProgress(getResources().getString(R.string.registering_device));
                if(DevicePreferences.getInstance().isNetworkAvailable()){
                    RestClient.getAdapter().registerDevice(deviceRegister, new Callback<DeviceRegister.Response>() {


                        @Override
                        public void success(DeviceRegister.Response response, Response response2) {

                            hideProgress();
                            String responseJson = gson.toJson(response);
                            DeviceRegister.Response registrationResponse = gson.fromJson(responseJson,DeviceRegister.Response.class);

                            if(registrationResponse.status.equals(DeviceRegister.STATUS_SUCCESS)){
                                DevicePreferences.getInstance().setDeviceRegistrationStatus(true);

                                DevicePreferences.getInstance().setClientKey(registrationResponse.getKey());
                                syncData();
                            }

                            if(registrationResponse.status.equals(DeviceRegister.STATUS_DEVICE_ID_EXISTS)){
                                dialog.dismiss();
                                dialog.setTitle(registrationResponse.getMessage());
                                dialog.show();
                            }

                            if(registrationResponse.status.equals(DeviceRegister.STATUS_DEVICE_NAME_EXISTS)){
                                dialog.dismiss();
                                dialog.setTitle(registrationResponse.getMessage());
                                dialog.show();
                            }

                        }

                        @Override
                        public void failure(RetrofitError retrofitError) {

                        }
                    });
                }
                else{
                    DevicePreferences.getInstance().showNoConnectionDialog();
                }

			}
		});


		dialog.show();

	}

    private void showToast(String message){

        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    private void showProgress(String message){

        progressDialog=ProgressDialog.show(this,"",message,false);

    }

    private void hideProgress(){

        progressDialog.dismiss();
    }
}