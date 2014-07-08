package note;

import gps.Region;



public class Place {

	private int id ;
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	private String name ;

	private Region reg;

	public double getX() {
		return reg.getLoc().getLongitude();
	}

	public double getY() {
		return reg.getLoc().getLatitude();
	}

	public int getRaduis() {
		return reg.getRaduis();
	}

	public Region getRegion(){
		return reg;
	}

	public Place(int id , String name, Double x , Double y ,int raduis){
		this.id = id ;
		this.name = name ;
		reg = new Region(x, y, raduis);

	}


}
