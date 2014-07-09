package gps;

import android.location.Location;

public class Region {
	public Region(){
		loc = null;
		raduis = -1;
	}
	public Region(Location loc,int raduis){
		setLoc(loc);
		setRaduis(raduis);
	}
	
	public Region(double longitude,double latitude,int raduis){
		Location loc = new Location("");
		loc.setLongitude(longitude);
		loc.setLatitude(latitude);
		setLoc(loc);
		setRaduis(raduis);
	}
	

	Location loc;
	public Location getLoc() {
		return loc;
	}

	public void setLoc(Location loc) {
		this.loc = loc;
	}

	int raduis;

	public int getRaduis() {
		return raduis;
	}

	public void setRaduis(int raduis) {
		this.raduis = raduis;
	}
	

	public double distance(Location loc){
		double R = 6378.137; // Radius of earth in KM
		double dLat = (loc.getLatitude() - this.loc.getLatitude()) * Math.PI / 180;
		double dLon = (loc.getLongitude() - this.loc.getLongitude()) * Math.PI / 180;
		double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
				Math.cos(this.loc.getLatitude() * Math.PI / 180) * Math.cos(loc.getLatitude() * Math.PI / 180) *
				Math.sin(dLon/2) * Math.sin(dLon/2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		double d = R * c;
		return d * 1000; // meters
	}

	public boolean isInside(Location loc){
		return distance(loc) <= this.raduis;
	}

	public boolean isInside(Region loc){
		return distance(loc.loc) <= this.raduis+loc.raduis;
	}


}
