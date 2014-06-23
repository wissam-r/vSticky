package com.example.layoutt;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ViewNote extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note_view);
		Button b1  = (Button) findViewById(R.id.backv) ;
		TextView t1 = (TextView) findViewById(R.id.titlev);
		TextView t2 = (TextView) findViewById(R.id.contentv);
		TextView t3 = (TextView) findViewById(R.id.datev);
		int id = getIntent().getExtras().getInt("id");
		Cursor cursor = MainActivity.getDb().getNote(id);
		if (cursor == null)
			return;
		t1.setText(cursor.getString(1));
		t2.setText(cursor.getString(2));
		t3.setText(cursor.getString(3));
		
		b1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish() ;
				
			}
		}) ;

		
		
	}

}
