package gps;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class NetworkProvider extends BroadcastReceiver implements LocationListener{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	private static boolean internet_con  ;
	public static boolean isInternet_con() {
		return internet_con;
	}
	private Double longitude = null ;
	private Double latitude = null ;
	public Double getLatitude() {
		return latitude;
	}
	public Double getLongitude() {
		return longitude;
	}


	private LocationManager locationManager;
	private Location  myLocationNetwork ;
	public Location getMyLocationNetwork() {
		return myLocationNetwork;
	}

	Activity activity = null ;
	TextView textView ;


	@Override
	public void onReceive(final Context context, final Intent intent) {

		String status = NetworkUtil.getConnectivityStatusString(context);
		if (status == "Wifi enabled") {
			internet_con  = true ;
		} else if (status =="Mobile data enabled") {
			internet_con  = true ;
		} else {
			internet_con=false ;
		}
//		Toast.makeText(context, status, Toast.LENGTH_LONG).show();
	}
	public NetworkProvider(){};
	public NetworkProvider (Activity activity , TextView textView) {
		// TODO Auto-generated constructor stub


		this.activity =  activity ;
		this.textView = textView ;
		/********** get Gps location service LocationManager object ***********/
		locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

		try{
			myLocationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
		catch(NullPointerException ex){
			Toast.makeText(activity.getBaseContext(), "NO NETWORK", Toast.LENGTH_LONG).show() ;
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



		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000,
				1, this);

		/********* After registration onLocationChanged method  ********/
		/********* called periodically after each 1 sec ***********/

	}
	/************* Called after each 3 sec **********/
	@Override
	public void onLocationChanged(Location location) {
		ConnectivityManager cm = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (null != activeNetwork) {
			if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
				internet_con =true;

			if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
				internet_con = true;
		} 
		else 
			internet_con = false ;

		if (internet_con){
//			String str = "Latitude: "+location.getLatitude()+" Longitude: "+location.getLongitude();

			latitude = location.getLatitude() ;
			longitude = location.getLongitude() ;

//			Toast.makeText(activity.getBaseContext(), "NETWORK : "+str, Toast.LENGTH_LONG).show();
			textView.setText("INTERNET Conected") ;
			}
		else
			textView.setText("NO INTERNET Connection") ;
	}

	@Override
	public void onProviderDisabled(String provider) {

		/******** Called when User off Gps *********/

		Toast.makeText(activity.getBaseContext(), provider +" off ", Toast.LENGTH_LONG).show();
		textView.setText("NO INTERNET Connection") ;


	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}


}
