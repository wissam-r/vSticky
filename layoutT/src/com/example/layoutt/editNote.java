package com.example.layoutt;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class editNote extends Activity{




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addnote);

		final EditText t1 = (EditText) findViewById(R.id.titleo);
		final EditText t2 = (EditText) findViewById(R.id.conto);
		Button b1 = (Button) findViewById(R.id.b2);
		Button b2 = (Button) findViewById(R.id.b3);
		final int id = getIntent().getExtras().getInt("id");
		Cursor cursor = MainActivity.getDb().getNote(id);
		if (cursor == null)
			return;
		t1.setText(cursor.getString(1));
		t2.setText(cursor.getString(2));
			
		b1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				MainActivity.getDb().updateNote(id, t1.getText().toString(), t2.getText().toString());
//				MainActivity.mla.notifyDataSetChanged();
//				Cursor cursor = MainActivity.getDb().getNote(id);
//				Note note = new Note(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
//				MainActivity.mla.add(note);
				finish();
				
				
				
			}
		});
		
		b2.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						
						finish();
						
					}
				});
		
	}

}
