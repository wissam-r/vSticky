package note;


public class Note {
	
	String t ;
	public String getT() {
		return t;
	}
	public void setT(String t) {
		this.t = t;
	}
	String c ;
	public String getC() {
		return c;
	}
	public void setC(String c) {
		this.c = c;
	}
	int id ;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	Integer photo_ID ;
	Integer place_ID ;
	public int getPhoto_ID() {
		return photo_ID;
	}
	public void setPhoto_ID(Integer photo_ID) {
		this.photo_ID = photo_ID;
	}
	public int getPlace_ID() {
		return place_ID;
	}
	public void setPlace_ID(Integer place_ID) {
		this.place_ID = place_ID;
	}
	public int getMode_ID() {
		return mode_ID;
	}
	public void setMode_ID(Integer mode_ID) {
		this.mode_ID = mode_ID;
	}
	Integer mode_ID ;
	
	public Note(int id,String t,String c){	
		setT(t);
		setC(c);
		setId(id);
		
	}
	
	public Note(int id , String t ,String c ,  Integer photo_ID, Integer place_ID, Integer mode_ID ){
		setT(t);
		setC(c);
		setId(id);
		setMode_ID(mode_ID) ;
		setPhoto_ID(photo_ID);
		setPlace_ID(place_ID) ;
		
	}
	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		Note note = (Note) o ;
		return ((this.getId()==note.getId()));
	}

}
