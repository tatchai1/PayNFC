package com.paynfc;


import java.net.MalformedURLException;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponseCallback;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View.OnClickListener;



import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends Activity {
 
 	private MobileServiceClient mClient;
 	private MobileServiceTable<Userinfo> mUserinfo;
 	private ProgressBar mProgressBar;
 	String status = "out";
 	
 	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);
        
        /*** Create to Mobile Service ***/
       final AlertDialog.Builder adb = new AlertDialog.Builder(this);
        try {
        	mClient = new MobileServiceClient(
        			 "https://paynfcapp.azure-mobile.net/",
        			    "qfPJJhnOaXVIQdbHhOZbHrPSDdHpTu13",
        		      this).withFilter(new ProgressFilter());

			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
			AlertDialog ad = adb.create();
    		ad.setMessage("Please connect internet!");
    		ad.show();
		}
        
        /*** Create getTable ***/
        mUserinfo = mClient.getTable(Userinfo.class);
        
        final EditText txtUsername = (EditText)findViewById(R.id.regisusername); 
        final EditText txtPassword = (EditText)findViewById(R.id.regispassword); 
        final EditText txtEmail = (EditText)findViewById(R.id.regisemail); 
        final EditText txtTel = (EditText)findViewById(R.id.regisphone); 
		
        //final AlertDialog.Builder adb = new AlertDialog.Builder(this);
        final int balance = 200;
        // btnSave
        final Button btnRegister = (Button) findViewById(R.id.btregister);
        final Button btncancle = (Button) findViewById(R.id.btcancle);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar5);
		mProgressBar.setVisibility(ProgressBar.GONE);
        // Perform action on click
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if (mClient == null) {
            		AlertDialog ad = adb.create();
            		ad.setMessage("Cannot connect to Windows Azure Mobile Service!");
            		ad.show();
            	}
            	else
            	{
                	/*** Item for Insert ***/
            		Userinfo item = new Userinfo();
                	item.setUsername(txtUsername.getText().toString());
                	item.setPassword(txtPassword.getText().toString());
            		item.setTel(txtTel.getText().toString());
            		item.setEmail(txtEmail.getText().toString());
            		item.setBalance(balance);
            		item.setStatus(status);
            		// Insert the new item
            		mUserinfo.insert(item, new TableOperationCallback<Userinfo>() {

            			public void onCompleted(Userinfo entity, Exception exception, ServiceFilterResponse response) {
            				
            				if (exception == null) {
                        		AlertDialog ad = adb.create();
                        		ad.setMessage("Register Data Successfully.");
                        		ad.show();  
                        		Intent newActivity = new Intent(Register.this,Login.class);
            					startActivity(newActivity);
            					finishAffinity();
                        		
            				} else {
                           		AlertDialog ad = adb.create();
                        		ad.setMessage("Please connect to internet!");
                        		ad.show();
            				}
            				
            			}
            			
            		});
            		
            	}
            	
            }	
        });
        btncancle.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		Intent home = new Intent(Register.this, Login.class);
        		startActivity(home);
        	}
        });
        
        
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
    

}