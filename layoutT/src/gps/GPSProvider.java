package gps;

import android.app.Activity;
import android.content.Context;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class GPSProvider implements LocationListener , GpsStatus.Listener{

	private Double longitude = null ;
	private Double latitude = null ;
	public Double getLatitude() {
		return latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	
	public static boolean isGPS_ConToSatil() {
		return GPS_ConToSatil;
	}
	public static boolean isGPS_on() {
		return GPS_on;
	}
	private static boolean GPS_ConToSatil  ;
	private static boolean GPS_on ;
	private LocationManager locationManager;
	private Location myLocationGPS , myLocationNetwork ;
	Activity activity = null ;
	TextView textView ;


	public GPSProvider(Activity activity , TextView textView) {
		// TODO Auto-generated constructor stub

		this.activity =  activity ;
		this.textView = textView ;
		/********** get Gps location service LocationManager object ***********/
		locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

		//		try{
		//			myLocationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		//		}
		//		catch(NullPointerException ex){
		//			Toast.makeText(activity.getBaseContext(), "NO NETWORK", Toast.LENGTH_LONG).show() ;
		//		}

		locationManager.addGpsStatusListener(this);
		GPS_on = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ;
		try{
			myLocationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		}
		catch(NullPointerException ex){
			Toast.makeText(activity.getBaseContext(), "NO Place", Toast.LENGTH_LONG).show() ;
		}


		/* CAL METHOD requestLocationUpdates */

		// Parameters :
		//   First(provider)    :  the name of the provider with which to register 
		//   Second(minTime)    :  the minimum time interval for notifications, 
		//                         in milliseconds. This field is only used as a hint 
		//                         to conserve power, and actual time between location 
		//                         updates may be greater or lesser than this value. 
		//   Third(minDistance) :  the minimum distance interval for notifications, in meters 
		//   Fourth(listener)   :  a {#link LocationListener} whose onLocationChanged(Location) 
		//                         method will be called for each location update 


		locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER,
				1000,   // 1 sec
				1, this);
		//		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000,
		//				1, this);

		/********* After registration onLocationChanged method  ********/
		/********* called periodically after each 1 sec ***********/

	}
	/************* Called after each 3 sec **********/
	@Override
	public void onLocationChanged(Location location) {

		String str = "Latitude: "+location.getLatitude()+" Longitude: "+location.getLongitude();
		latitude = location.getLatitude() ;
		longitude = location.getLongitude() ;
		Toast.makeText(activity.getBaseContext(), "Place : "+str, Toast.LENGTH_LONG).show();
		textView.setText("GPS Conected") ;
	}

	@Override
	public void onProviderDisabled(String provider) {

		/******** Called when User off Gps *********/

		Toast.makeText(activity.getBaseContext(), provider +" off ", Toast.LENGTH_LONG).show();
		textView.setText("GPS OFF") ;
		GPS_ConToSatil = false ;

	}

	@Override
	public void onProviderEnabled(String provider) {

		/******** Called when User on Gps  *********/
		Toast.makeText(activity.getBaseContext(), "Gps turned on ", Toast.LENGTH_LONG).show();
		textView.setText("GPS is ON Plz wait") ;
		GPS_ConToSatil = false ;
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onGpsStatusChanged(int arg0) {
		// TODO Auto-generated method stub
		switch (arg0) 
		{
		case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
			break;
		case GpsStatus.GPS_EVENT_FIRST_FIX:   // this means you  found Place Co-ordinates 
			GPS_ConToSatil =true ;
			break;
		case GpsStatus.GPS_EVENT_STARTED:
			break;
		case GpsStatus.GPS_EVENT_STOPPED:
			textView.setText("GPS OFF") ;
			break;
		} 
	}

}

