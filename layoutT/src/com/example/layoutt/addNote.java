package com.example.layoutt;

import android.app.Activity;
//import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;

public class addNote extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addnote);

		final EditText t1 = (EditText) findViewById(R.id.titleo);
		final EditText t2 = (EditText) findViewById(R.id.conto);
		Button b1 = (Button) findViewById(R.id.b2);
		Button b2 = (Button) findViewById(R.id.b3);
		
		
		b1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				MainActivity.getDb().insertNote(t1.getText().toString(), t2.getText().toString());
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
