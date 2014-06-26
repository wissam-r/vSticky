package com.example.test;

import android.app.Activity;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;


public class MyGPS extends Activity {

	// the listener to listen to the locations
	private LocationListener listener = null;
	public LocationListener getListener() {
		return listener;
	}


	// a location manager
	private LocationManager lm  = null;
	// locations instances to GPS and NETWORk
	private Location myLocationGPS , myLocationNetwork ;
	public String myLocation;
	public String getMyLocation() {
		return myLocation;
	}

	MyGPS(){
		// instantiates fields;
		Context context  = this ;
		lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		myLocationNetwork = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		myLocationGPS = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		listener = new GPSlistener();

	}
	
	
	class GPSlistener implements LocationListener {



		@Override
		public void onLocationChanged(Location arg0) {
			// TODO Auto-generated method stub
			// "location" is the RECEIVED locations and its here that you should proccess it

			// check if the incoming position has been received from GPS or network
			if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				lm.removeUpdates(this);
			} else {
				lm.removeUpdates(listener);
			}
			arg0.getLatitude();
			arg0.getLongitude();



			myLocation = "Latitude = " + arg0.getLatitude() + " Longitude = " + arg0.getLongitude();


		}

		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub
	        lm.removeUpdates(this);
	        lm.removeUpdates(listener);   

		}

		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub

		}


	}


}

