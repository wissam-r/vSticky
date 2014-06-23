package com.example.layoutt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public abstract class NoteEditer extends Activity {
	protected EditText t1 ;
	protected EditText t2 ;
	protected Button b1 ;
	protected Button b2 ;
	protected Button b3 ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note);

		t1 = (EditText) findViewById(R.id.titleo);
		t2 = (EditText) findViewById(R.id.conto);
		b1 = (Button) findViewById(R.id.b2);
		b2 = (Button) findViewById(R.id.b3);
		b3 = (Button) findViewById(R.id.share) ;


		b1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				save() ;
				Toast.makeText(getApplicationContext(), "Saved",
						Toast.LENGTH_LONG).show();
				finish();
			}
		});

		b2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				//				Intent sharingIntent = new Intent(android.content.Intent.ACTION_SYNC); 
				//			    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, t1.getText().toString());
				//			    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, t2.getText().toString());
				//			    startActivity(Intent.createChooser(sharingIntent, "Sync with"));
				finish();			
			}
		});

		b3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND); 
				sharingIntent.setType("text/plain");
				sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, t1.getText().toString());
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, t2.getText().toString());
				startActivity(Intent.createChooser(sharingIntent, "Share via"));
			}
		});
	}
	abstract void save() ;

}
