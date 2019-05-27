package com.paynfc;


import java.net.MalformedURLException;
import java.util.List;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;

public class PayPage extends Fragment implements
CreateNdefMessageCallback, OnNdefPushCompleteCallback {
	private NfcAdapter mNfcAdapter;
	private PendingIntent mPendingIntent;
	private NdefMessage mNdefPushMessage;
	public TextView paytext;
	public EditText pay;
	public String paycost;
	private FragmentActivity fa;
	
	private MobileServiceClient mClient;
	private MobileServiceTable<Userinfo> mUserinfo;
	private AlertDialog.Builder adb;
	private static final String MY_PREFS = "my_prefs";
	private String key = "xcab43sx";
	
	Context thiscontext;
	String tuser,encrytuser;
	Encryption enc;
	
	public PayPage(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		//fa = super.getActivity();
	/*	mNfcAdapter = NfcAdapter.getDefaultAdapter(this);     
        mNfcAdapter.setNdefPushMessageCallback(this, this);
        mNfcAdapter.setOnNdefPushCompleteCallback(this, this);*/
		   thiscontext = container.getContext();
		    
		    try {
				mClient = new MobileServiceClient(
						"https://paynfcapp.azure-mobile.net/",
						"qfPJJhnOaXVIQdbHhOZbHrPSDdHpTu13",thiscontext);
				mUserinfo = mClient.getTable(Userinfo.class);

			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		    /*** Create getTable ***/
		    mUserinfo = mClient.getTable(Userinfo.class);
	
        return inflater.inflate(R.layout.pay_page, container, false);
    }
	
	  public void onViewCreated(View view, Bundle savedInstanceState){
		  
		 
		  
			mNfcAdapter = NfcAdapter.getDefaultAdapter(getActivity());     
	        mNfcAdapter.setNdefPushMessageCallback(this, getActivity());
	        mNfcAdapter.setOnNdefPushCompleteCallback(this, getActivity());
	      
	       
	        super.onViewCreated(view, savedInstanceState);
	        
	      
	        	
	        SharedPreferences preferences = this.getActivity().getSharedPreferences(MY_PREFS,  
			         Context.MODE_PRIVATE);
	        String username = preferences.getString("username", "not found!");
	     
	        refreshItemsFromTable(username);
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
			} else {
				
				mUserinfo.where().field("username").eq(username).execute(new TableQueryCallback<Userinfo>() {
					public void onCompleted(List<Userinfo> result, int count, Exception exception, 
							ServiceFilterResponse response) {
						
						if (exception == null) {
							if(result.size() > 0)
							{
								
								String username = null;
								String id = null;
								int balance = 0;
					            
								Userinfo item = result.get(0);
							//	username = item.getUsername();
								id = item.getId();
								tuser= id;
//								encrytuser = new Encryption(id).encrypt(tuser);
								try {
									enc = new Encryption(key);
								//	enc.encrypt(tuser);
									encrytuser = enc.encrypt(tuser);
									
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								//	Log.i(tag, "masage");
								}
								balance = item.getBalance();
						    	
						//		tname.setText(String.valueOf(username));
						//		tbalance.setText(String.valueOf(balance));
							}

						} else {
							AlertDialog ad = adb.create();
							ad.setMessage("Error : " + exception.getCause().getMessage());
							ad.show();
						}
					}
				});

			}
		}

	@Override
	public NdefMessage createNdefMessage(NfcEvent arg0) {
		// TODO Auto-generated method stub
	//	String text = "aaa";
	
		
		String text = encrytuser;
        // "Beam Time: " + time.format("%H:%M:%S"));
		NdefMessage msg = new NdefMessage(NdefRecord.createMime(
         "application/com.example.android.beam", text.getBytes()));
		return msg;
	}
}
