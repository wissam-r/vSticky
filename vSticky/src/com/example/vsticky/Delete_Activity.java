package com.example.vsticky;

import java.util.Scanner;

import com.example.layoutt.R;


import note.Note;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class Delete_Activity extends Activity{

	ListView list ;
	static ListAdap2 mla2 = null ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.check_view);
		list = (ListView) findViewById(R.id.listView2);
		mla2 =  new ListAdap2(this, 0) ;
		Cursor cursor = Notepad.getDb().getNotesByDate(Notepad.getUser_ID()) ;
		for (int i= 0 ; i < cursor.getCount() ; i++) {
			Scanner con = new Scanner(cursor.getString(2));
			Note note =  new Note(cursor.getInt(0), cursor.getString(1),con.nextLine() );	
			mla2.add(note,cursor.getString(3),false);
			cursor.moveToNext();
			con.close() ;
		}

		list.setAdapter(mla2);

		Button remove = (Button) findViewById(R.id.removec) ;
		Button cancel = (Button) findViewById(R.id.cancelc) ;

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				actions.Buttons.done(Delete_Activity.this) ;

			}
		});

		remove.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				for (int i = 0 ; i<mla2.getChecked().size();i++){
					if (mla2.getChecked().get(i)){
						Note note  = (Note) mla2.getItem(i);
						actions.Buttons.delete(note.getId(), Notepad.getDb()) ;
					}
				}
				actions.Buttons.done(Delete_Activity.this) ;
			}
			
		}) ;

	}

}
