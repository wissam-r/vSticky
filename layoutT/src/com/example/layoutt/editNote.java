package com.example.layoutt;


import android.database.Cursor;
import android.os.Bundle;


public class editNote extends NoteEditer{
	int id ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		id = getIntent().getExtras().getInt("id");
		Cursor cursor = MainActivity.getDb().getNote(id);
		if (cursor == null)
			return;
		t1.setText(cursor.getString(1));
		t2.setText(cursor.getString(2));
	}
	
	@Override
	void save() {
		// TODO Auto-generated method stub
		MainActivity.getDb().updateNote(id, t1.getText().toString(), t2.getText().toString());

		
		
	}

}
