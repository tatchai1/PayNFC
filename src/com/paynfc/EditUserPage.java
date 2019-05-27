package com.paynfc;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;

import com.google.android.gms.internal.bt;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponseCallback;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class EditUserPage extends Fragment {
	
	Context thiscontext;
	private MobileServiceClient mClient;
	private MobileServiceTable<Userinfo> mUserinfo;
	public TextView tedname, tedpass, tedphone ,tedmail;
	public EditUserPage(){}
	private static final String MY_PREFS = "my_prefs";
	private AlertDialog.Builder adb;
	String usenameet2;
	SessionManager session;
	private ProgressBar mProgressBar;
	@Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		   thiscontext = container.getContext();
		    
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
        return inflater.inflate(R.layout.edituser_page, container, false);   
       
    }
	  public void onViewCreated(View view, Bundle savedInstanceState){
	        super.onViewCreated(view, savedInstanceState);
	               
	        // initialise your views
	        tedname = (TextView) view.findViewById(R.id.edusername);
	        tedpass = (TextView) view.findViewById(R.id.edpassword);
	        tedphone = (TextView) view.findViewById(R.id.edphone);
	        tedmail = (TextView) view.findViewById(R.id.edemail);
	        final Button btnRegister = (Button) view.findViewById(R.id.btedok);
	        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
			mProgressBar.setVisibility(ProgressBar.GONE);
		
	     /*   SharedPreferences preferences = this.getActivity().getSharedPreferences(MY_PREFS,  
			         Context.MODE_PRIVATE);
	        String username = preferences.getString("username", "not found!");	        */
	        //  String username = "a";
			session = new SessionManager(thiscontext);
	        HashMap<String, String> user = session.getUserDetails();
	         
	        // name
	        String username = user.get(SessionManager.KEY_NAME);
	        
		    refreshItemsFromTable(username);
		   // usenameet2 = username;
		    
		    
		    btnRegister.setOnClickListener(new View.OnClickListener() {
		        public void onClick(View v) {
		        	if (mClient == null) {
		        		AlertDialog ad = adb.create();
		        		ad.setMessage("Cannot connect to Windows Azure Mobile Service!");
		        		ad.show();
		        	}
		        	else
		        	{	mUserinfo.where().field("username").eq(usenameet2).execute(new TableQueryCallback<Userinfo>() {
		    			public void onCompleted(List<Userinfo> result, int count, Exception exception, 
		    					ServiceFilterResponse response) {
		    				
		    				if (exception == null) {
		    					if(result.size() > 0)
		    					{
		            	//*** Item for Insert ***//*
		        		Userinfo item = result.get(0);
		            	item.setUsername(tedname.getText().toString());
		            	item.setPassword(tedpass.getText().toString());
		        		item.setTel(tedphone.getText().toString());
		        		item.setEmail(tedmail.getText().toString());
		        		
		        		//SharedPreferences preferences = this.getSharedPreferences(MY_PREFS,  
						//         Context.MODE_PRIVATE);
		        		//SharedPreferences preferences = this.getActivity().getSharedPreferences(MY_PREFS,  
		       		     //    Context.MODE_PRIVATE);
		        		
		        		/*SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(thiscontext);
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
		        	
		        		// Insert the new item
		        		mUserinfo.update(item, new TableOperationCallback<Userinfo>() {

		        			public void onCompleted(Userinfo entity, Exception exception, ServiceFilterResponse response) {
		        				
		        				if (exception == null) {
		                    		/*AlertDialog ad = adb.create();
		                    		ad.setMessage("Update Data Successfully.");
		                    		ad.show();  */
		        					updateDetail();
		                    	
		        				} else {
		                       		AlertDialog ad = adb.create();
		                    		ad.setMessage("Please connect to internet!");
		                    		ad.show();
		        				}
		        				
		        			}
		        			
		        		}); 
		        		//updateDetail();
		    					}}}

						private Context getActivity() {
							// TODO Auto-generated method stub
							return null;
						}
		        		
		        	
		        	
		        	
		        	})	;
		        	
		        	} 
		        	
		        	} 
		        
		    	});

		    
		    
		    
	  }
	  public void updateDetail() {
	        Intent intent = new Intent(getActivity(), Login.class);
	        startActivity(intent);
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
	  private void refreshItemsFromTable(String username) {
		  	usenameet2 = username;
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
						
								Userinfo item = result.get(0);
							
								tedname.setText(String.valueOf(item.getUsername()));
								tedpass.setText(String.valueOf(item.getPassword()));
								tedphone.setText(String.valueOf(item.getTel()));
								tedmail.setText(String.valueOf(item.getEmail()));
								
							//	id = item.getId();
							//	tuser= id;
//								encrytuser = new Encryption(id).encrypt(tuser);
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
	  
	  
	  
	  
	  
	  }