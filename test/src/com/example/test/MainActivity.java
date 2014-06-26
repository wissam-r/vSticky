package com.example.test;



import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements LocationListener  {

	
	private LocationManager locationManager;
	private Location myLocationGPS , myLocationNetwork ;
	TextView t1 ;
	TextView t2 ;

	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        

        /********** get Gps location service LocationManager object ***********/
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        
        try{
        myLocationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        t2 = (TextView) findViewById(com.example.test.R.id.testv2) ;
        t2.setText("L + "+myLocationNetwork.getLatitude()+" Lo + "+myLocationNetwork.getLongitude()) ;
        }
        catch(NullPointerException ex){
            t2.setText("NO NETWORK") ;
        }
        
        try{
        myLocationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        t1 = (TextView) findViewById(com.example.test.R.id.testv1) ;
        t1.setText("L + "+myLocationGPS.getLatitude()+" Lo + "+myLocationGPS.getLongitude()) ;
        
        }
        catch(NullPointerException ex){
            t1.setText("NO GPS") ;

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
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, this);
         
        /********* After registration onLocationChanged method  ********/
        /********* called periodically after each 1 sec ***********/
        
  
    }
    
    
    /************* Called after each 3 sec **********/
    @Override
    public void onLocationChanged(Location location) {
            
        String str = "Latitude: "+location.getLatitude()+" Longitude: "+location.getLongitude();

        Toast.makeText(getBaseContext(), "   GPS OR NETWORK "+str, Toast.LENGTH_LONG).show();
    }
 
    @Override
    public void onProviderDisabled(String provider) {
         
        /******** Called when User off Gps *********/
         
        Toast.makeText(getBaseContext(), "Gps turned off ", Toast.LENGTH_LONG).show();
    }
 
    @Override
    public void onProviderEnabled(String provider) {
         
        /******** Called when User on Gps  *********/
         
        Toast.makeText(getBaseContext(), "Gps turned on ", Toast.LENGTH_LONG).show();
    }
 
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
         
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
  
    
}
