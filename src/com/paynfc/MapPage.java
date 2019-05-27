package com.paynfc;


import java.net.MalformedURLException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.MapFragment;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class MapPage extends Fragment {
	private static View view;
	GoogleMap gmap ;
	private MobileServiceClient mClient;
	private MobileServiceTable<LocationTable> mLocation;
	double longi,lati;
	Context thiscontext;
	MarkerOptions markopt;
	private Timer myTimer = new Timer();
	
	public MapPage(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		thiscontext = container.getContext();
		  try {
  			mClient = new MobileServiceClient(
  					"https://paynfcapp.azure-mobile.net/",
  					"qfPJJhnOaXVIQdbHhOZbHrPSDdHpTu13", thiscontext);
  				} catch (MalformedURLException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
  	  mLocation = mClient.getTable(LocationTable.class);
       // rootView = inflater.inflate(R.layout.map_page, container, false);
       /* ViewGroup parent = (ViewGroup) rootView.getParent();
        parent.removeView(rootView); */ 
  	  
  	
  
    if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        		startTagging();
        }
        try {
            view = inflater.inflate(R.layout.map_page, container, false);
          
        	startTagging();
          new LoadMap().execute();
        } catch (InflateException e) {
            /* map is already there, just return view as it is */
        }

       
	  
        return view;
	}
	public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
      
	
	}
	
	
	public class LoadMap extends AsyncTask<String, Integer, Location> {
		LocationManager lm;
	    LatLng p;

	    
	    @Override
	    protected void onPreExecute() {
	    //	startTagging();
	    }
	    protected Location doInBackground(String... params) {
	        gmap = ((MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map))
	                .getMap();
	        LocationManager locationManager = (LocationManager) getActivity()
	                .getSystemService(Context.LOCATION_SERVICE);
	        Criteria criteria = new Criteria();
	        String provider = locationManager.getBestProvider(criteria, true);
	        Location location = locationManager.getLastKnownLocation(provider);
	       
	        p = new LatLng(location.getLatitude(), location.getLongitude());
	        
	   
	      //  gmap.addMarker(new MarkerOptions().position(37.7750, 122.4183).icon(BitmapDescriptorFactory.fromResource(R.drawable.yourmarkericon)));
	        return location;
	    } 

	    protected void onPostExecute(Location location) {
	    	 
	    	 
	 /*  	 mLocation.where().field("nameboat").eq("Boat1").execute(new TableQueryCallback<LocationTable>() {
	        		public void onCompleted(List<LocationTable> result, int count, Exception exception, 
							ServiceFilterResponse response) {
						
						if (exception == null) {
							if(result.size() > 0)
							{  LocationTable item = result.get(0);
							
	    	 
	     	// LocationTable item = result.get(0);
	        Marker marker = gmap.addMarker(new MarkerOptions()
	        .position(new LatLng(item.getmLatitude(), item.getmLongitude()))
	        .title(item.getmNameboat())
	        .snippet("Time"));
	        
							}}}});*/
	        
	        
	        
	        try {

	            if (location != null) {

	                gmap.setMyLocationEnabled(true);
	                gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(
	    	                new LatLng(13.767167, 100.65136), 14));
	                gmap.moveCamera(CameraUpdateFactory.newLatLng(p));
	                gmap.moveCamera(CameraUpdateFactory.zoomTo(15));
	                gmap.setOnCameraChangeListener(null);
	            } else {
	                Toast.makeText(getActivity(), "No Location",
	                        Toast.LENGTH_LONG).show();
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	
	
	
	}   
	
	private void startTagging () {
		
		
		int minutes = 10000; //10 second
		myTimer = new Timer();
	    myTimer.schedule(new TimerTask() {          
	        
	    	@Override
	        public void run() {
	            timerMethod();       	
	        }
	    }, 0, minutes);
	}

	private void timerMethod(){
	
		getActivity().runOnUiThread(timer_tick);
	}
	private Runnable timer_tick = new Runnable() {
    public void run() {
    	//This method runs in the same thread as the UI.               
    	//Toast.makeText(getApplicationContext(), "Test", Toast.LENGTH_LONG).show();
    	
        new LoadTaging().execute();
    
	 
    			}
		};
	public void stopTaging () {
	if (myTimer != null){
		myTimer.cancel();
		}
	}
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		stopTaging ();
		super.onDestroyView();
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		stopTaging ();
		super.onDestroy();
	}
	public void onBackPressed() {
		stopTaging ();
	
		 }
	
	public class LoadTaging extends AsyncTask<String, Integer, Location> {
		LocationManager lm;
	    LatLng p;

	    
	    @Override
	    protected void onPreExecute() {
	    //	startTagging();
	    }
	    protected Location doInBackground(String... params) {
	        gmap = ((MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map))
	                .getMap();
	        LocationManager locationManager = (LocationManager) getActivity()
	                .getSystemService(Context.LOCATION_SERVICE);
	        Criteria criteria = new Criteria();
	        String provider = locationManager.getBestProvider(criteria, true);
	        Location location = locationManager.getLastKnownLocation(provider);
	       
	        p = new LatLng(location.getLatitude(), location.getLongitude());
	        
	   
	      //  gmap.addMarker(new MarkerOptions().position(37.7750, 122.4183).icon(BitmapDescriptorFactory.fromResource(R.drawable.yourmarkericon)));
	        return location;
	    } 

	    protected void onPostExecute(Location location) {
	    	if (mClient == null) {
				 Toast.makeText(getActivity(), "Cannot connect to Windows Azure Mobile Service!",
			                Toast.LENGTH_LONG).show();
			} else 	 
	    	 
	   	 mLocation.execute(new TableQueryCallback<LocationTable>() {
	        		public void onCompleted(List<LocationTable> result, int count, Exception exception, 
							ServiceFilterResponse response) {
						
						if (exception == null) {
							if(result.size() > 0)
							{ 	for(int i=0;i<result.size();i++){
								
								LocationTable item = result.get(i);
	
						   	 
	    	 
	     	// LocationTable item = result.get(0);
	         gmap.addMarker(new MarkerOptions()
	        .position(new LatLng(item.getmLatitude(), item.getmLongitude()))
	        .title(item.getmNameboat())
	        .snippet("Time"));
							}
							}}}});
	    	
	    	/*   Toast.makeText(getActivity(), "Test Location Time"+item.getmLongitude(),
	                   Toast.LENGTH_LONG).show();
	   	 */

	      
	    }
	
	
	
	}   
	
}