package com.example.layoutt;

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
	public Note(int id,String t,String c){	
		setT(t);
		setC(c);
		setId(id);
		
	}
	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		Note note = (Note) o ;
		return ((this.getId()==note.getId()));
	}

}
