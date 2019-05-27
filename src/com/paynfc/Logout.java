
package com.paynfc;

import java.util.HashMap;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Logout extends Fragment {
	private static final String MY_PREFS = "my_prefs";
	Editor editor;
	SessionManager session;
	Context thiscontext;
	public Logout(){}
	
	@Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
	/*	SharedPreferences preferences = this.getActivity().getSharedPreferences(MY_PREFS,  
		         Context.MODE_PRIVATE);
		//SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Login.this);
		SharedPreferences.Editor editor = preferences.edit();
		editor.clear();
		editor.commit();
		Intent myIntent = new Intent((MainActivity)getActivity(), Login.class); 
		getActivity().startActivity(myIntent); */
		
	/*		editor.clear();
	        editor.commit();
	         
	        // After logout redirect user to Loing Activity
	        Intent i = new Intent((MainActivity)getActivity(), Login.class);
	        // Closing all the Activities
	    //    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	         
	        // Add new Flag to start new Activity
	      //  i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	          
	        // Staring Login Activity
	        getActivity().startActivity(i);*/
		//   session = new SessionManager(thiscontext);
		   session.logoutUser();
        return inflater.inflate(R.layout.login_main, container, false);   
       
    }
	
}