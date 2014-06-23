package com.example.layoutt;


public class addNote extends NoteEditer{

	@Override
	void save() {
		// TODO Auto-generated method stub
		MainActivity.getDb().insertNote(t1.getText().toString(), t2.getText().toString());
	}

	


}
