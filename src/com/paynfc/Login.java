package com.paynfc;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponseCallback;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

public class Login extends Activity {
	
	protected static final SharedPreferences settings = null;
	private MobileServiceClient mClient;
	private MobileServiceTable<Userinfo> mUserinfo;
	private EditText edusername, edpassword;
	private AlertDialog.Builder adb;
	private static final String MY_PREFS = "my_prefs";
	SessionManager session;
	 
	// String emi;
	// static String ids, user1, names, tels, emails, usernames,passwords;
	// static String ticket = "";
	// String returnString, returnString2; // to store the result of MySQL query
	// after decoding JSON
	private ProgressBar mProgressBar;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_main);
		session = new SessionManager(getApplicationContext()); 
		
		edusername = (EditText) findViewById(R.id.edtUsername);
		edpassword = (EditText) findViewById(R.id.edtPassword);
		adb = new AlertDialog.Builder(this);
		
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
		mProgressBar.setVisibility(ProgressBar.GONE);
		
		try {
			mClient = new MobileServiceClient(
					"https://paynfcapp.azure-mobile.net/",
					"qfPJJhnOaXVIQdbHhOZbHrPSDdHpTu13", this).withFilter(new ProgressFilter());
			//mUserinfo = mClient.getTable(Userinfo.class);
				} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*** Create getTable ***/
		 mUserinfo = mClient.getTable(Userinfo.class);

	}
	 private class ProgressFilter implements ServiceFilter {
			
			@Override
			public void handleRequest(ServiceFilterRequest request, NextServiceFilterCallback nextServiceFilterCallback,
					final ServiceFilterResponseCallback responseCallback) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if (mProgressBar != null) mProgressBar.setVisibility(ProgressBar.VISIBLE);
					}
				});
				
				nextServiceFilterCallback.onNext(request, new ServiceFilterResponseCallback() {
					
					@Override
					public void onResponse(ServiceFilterResponse response, Exception exception) {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								if (mProgressBar != null) mProgressBar.setVisibility(ProgressBar.GONE);
							}
						});
						
						if (responseCallback != null)  responseCallback.onResponse(response, exception);
					}
				});
			}
		}


	@SuppressLint("NewApi")
	public void clickBtn(View v) {
		if (v.getId() == R.id.login) {

			if (mClient == null) {

			} else {

				mUserinfo.where().field("username")
						.eq(edusername.getText().toString()).and()
						.field("password").eq(edpassword.getText().toString())
						.execute(new TableQueryCallback<Userinfo>() {
							public void onCompleted(List<Userinfo> result,
									int count, Exception exception,
									ServiceFilterResponse response) {

								if (exception == null) {
									if (result.size() > 0) {

										String id = "";
										
										Userinfo item = result.get(0);

										/*
										SharedPreferences preferences = getSharedPreferences(MY_PREFS,  
										         Context.MODE_PRIVATE);
										SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Login.this);
										SharedPreferences.Editor editor = preferences.edit();
										editor.putString("id", item.getId());
										editor.putString("username",
												item.getUsername());
										editor.putString("email",
												item.getEmail());
										editor.putString("password",
												item.getPassword());
										editor.putString("tel", item.getTel());
										editor.putBoolean("LOGIN", true);
										editor.commit();*/
										session.createLoginSession(item.getUsername(), item.getPassword());

										Intent newActivity = new Intent(
												Login.this, MainActivity.class);
										
										/*Intent newActivity2 = new Intent(
												getApplicationContext(), InfoPage.class);
										newActivity2.putExtra("sid", id);
										startActivity(newActivity2);*/
										startActivity(newActivity);
										finishAffinity();

									} else {
										AlertDialog ad = adb.create();
										ad.setMessage("Incorrect Username and Password!");
										ad.show();
									}
								} else {
									AlertDialog ad = adb.create();
									ad.setMessage("Please connect Internet."
											/*+ exception.getCause().getMessage()*/);
									ad.show();
								}
							}
						});

			}
		}

	

	// Intent userLogin = new Intent(this,MainActivity.class );
	// startActivity(userLogin);

//	public void register(View v) {
		// TODO Auto-generated method stub
		else if (v.getId() == R.id.register) {
			Intent register = new Intent(this, Register.class);
			startActivity(register);
		}
	}
	public void onBackPressed() {
		   Intent intent = new Intent(Intent.ACTION_MAIN);
		   intent.addCategory(Intent.CATEGORY_HOME);
		   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		   startActivity(intent);
		 }
}
