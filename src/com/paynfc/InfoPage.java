package com.paynfc;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponseCallback;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.widget.ProgressBar;

public class InfoPage extends Fragment implements
CreateNdefMessageCallback, OnNdefPushCompleteCallback {
	public InfoPage(){}
	public TextView tname, tbalance;
	
	private MobileServiceClient mClient;
	private MobileServiceTable<Userinfo> mUserinfo;
	private AlertDialog.Builder adb;
	Context thiscontext;
	SessionManager session;
	
	private NfcAdapter mNfcAdapter;
	private ProgressBar mProgressBar;
	private LinearLayout ll;
	private FragmentActivity fa;
	String tuser,encrytuser;
	Encryption enc;
	
	SharedPreferences preferences;
	private String key = "xcab43sx";
	
	private static final String MY_PREFS = "my_prefs";
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
	//	fa = (FragmentActivity) super.getActivity();
	    thiscontext = container.getContext();
	    session = new SessionManager(thiscontext);
	    try {
			mClient = new MobileServiceClient(
					"https://paynfcapp.azure-mobile.net/",
					"qfPJJhnOaXVIQdbHhOZbHrPSDdHpTu13",thiscontext).withFilter(new ProgressFilter());
			//mUserinfo = mClient.getTable(Userinfo.class);

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    /*** Create getTable ***/
	    mUserinfo = mClient.getTable(Userinfo.class);
	    
        return inflater.inflate(R.layout.info_page, container, false);
    }
	
	
	    public void onViewCreated(View view, Bundle savedInstanceState){
	        super.onViewCreated(view, savedInstanceState);
	               
	        // initialise your views
	        tname = (TextView) view.findViewById(R.id.name);
	        tbalance = (TextView) view.findViewById(R.id.balance);
	        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
			mProgressBar.setVisibility(ProgressBar.GONE);

			mNfcAdapter = NfcAdapter.getDefaultAdapter(getActivity());     
	        mNfcAdapter.setNdefPushMessageCallback(this, getActivity());
	        mNfcAdapter.setOnNdefPushCompleteCallback(this, getActivity());
	        
	   //     checkLogin();
	        
	       /* preferences = this.getActivity().getSharedPreferences(MY_PREFS,  
			         Context.MODE_PRIVATE);*/
	        
	        //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
	     //   String username = preferences.getString("username", null);
	        HashMap<String, String> user = session.getUserDetails();
	         
	        // name
	        String username = user.get(SessionManager.KEY_NAME);
	        
	        //  String username = "a";
		    refreshItemsFromTable(username);
	    }
	    private class ProgressFilter implements ServiceFilter {
			
			@Override
			public void handleRequest(ServiceFilterRequest request, NextServiceFilterCallback nextServiceFilterCallback,
					final ServiceFilterResponseCallback responseCallback) {
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if (mProgressBar != null) mProgressBar.setVisibility(ProgressBar.VISIBLE);
					}
				});
				
				nextServiceFilterCallback.onNext(request, new ServiceFilterResponseCallback() {
					
					@Override
					public void onResponse(ServiceFilterResponse response, Exception exception) {
						getActivity().runOnUiThread(new Runnable() {

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

		@Override
		public void onNdefPushComplete(NfcEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	    private void refreshItemsFromTable(String username) {

			if (mClient == null) {
				AlertDialog ad = adb.create();
				ad.setMessage("Cannot connect to Windows Azure Mobile Service!");
				ad.show();
			} else 
				
				mUserinfo.where().field("username").eq(username).execute(new TableQueryCallback<Userinfo>() {
					public void onCompleted(List<Userinfo> result, int count, Exception exception, 
							ServiceFilterResponse response) {
						
						if (exception == null) {
							if(result.size() > 0)
							{
								
								String username = null;
								int balance = 0;
								String id = null;
								Userinfo item = result.get(0);
								username = item.getUsername();
								balance = item.getBalance();
						    	
								tname.setText(String.valueOf(username));
								tbalance.setText(String.valueOf(balance));
								id = item.getId();
								tuser= id;
//								encrytuser = new Encryption(id).encrypt(tuser);
								try {
									enc = new Encryption(key);
								//	enc.encrypt(tuser);
									encrytuser = enc.encrypt(tuser);
									session.createLoginSession(username, encrytuser);
									
									
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								//	Log.i(tag, "masage");
								}
								balance = item.getBalance();
							}

						} else {	
								HashMap<String, String> user = session.getUserDetails();
								tname.setText(user.get(SessionManager.KEY_NAME));
								Toast.makeText(thiscontext,	
    				        	"Cannot Update Data." +
    				        	"Please connect to internet",
    				        	Toast.LENGTH_SHORT).show();		
						
						}
					}
				});

			
		}
		
	

		public NdefMessage createNdefMessage(NfcEvent arg0) {
			// TODO Auto-generated method stub
		//	String text = "aaa";
		
		//	String encrytuser2 = 
			HashMap<String, String> user = session.getUserDetails();
			String text = user.get(SessionManager.KEY_PASSWORD);
	        // "Beam Time: " + time.format("%H:%M:%S"));
			NdefMessage msg = new NdefMessage(NdefRecord.createMime(
	         "application/com.example.android.beam", text.getBytes()));
			return msg;
		}
	
}	    

/*@Override
public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    // Your code here
   
	try {
		mClient = new MobileServiceClient(
				"https://paynfcapp.azure-mobile.net/",
				"qfPJJhnOaXVIQdbHhOZbHrPSDdHpTu13", 
			    getActivity().getApplicationContext()).withFilter(new InfoPage());
		mUserinfo = mClient.getTable(Userinfo.class);

	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	//*** Create getTable ***//*
	// mUserinfo = mClient.getTable(Userinfo.class);

}

    

}*/
