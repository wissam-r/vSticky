package com.example.layoutt;

import java.util.Scanner;

import note.Note;
import note.NoteEditer;
import note.ViewNote;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class Search_ByTag_Activity extends Activity {

	ListView list ;
	listAdap mla = null ;
	static String searchT = "" ;
	private Integer photo_ID ;
	private Integer mode_ID ;
	private Integer place_ID ;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_by_tag) ;
		
		photo_ID = getIntent().getIntExtra("photo_ID", -1) ;
		place_ID = getIntent().getIntExtra("place_ID", -1) ;
		mode_ID = getIntent().getIntExtra("mode_ID", -1) ;
		
		list = (ListView) findViewById(R.id.listViewSt) ;
		Button back = (Button) findViewById(R.id.backst) ;
		list.setOnItemClickListener(listItemL);
		list.setOnItemLongClickListener(listItemLongL);
		back.setOnClickListener(backL);
	
		
	}
	OnItemLongClickListener listItemLongL = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			registerForContextMenu(arg0);
			openContextMenu(arg0);	
			closeContextMenu();
			return false;
		}
	} ;
	OnItemClickListener listItemL = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			
			Intent intent = new Intent(Search_ByTag_Activity.this, ViewNote.class);
			Note note = (Note) mla.getItem(arg2);
			intent.putExtra("id", note.getId());
			startActivity(intent);
		}


	};

	OnClickListener backL = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			
			finish() ;
		}
	};
	


	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		
		super.onCreateContextMenu(menu, v, menuInfo);		
		menu.setHeaderTitle("Menu");
		getMenuInflater().inflate(R.menu.submenu, menu);



	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		return (applyMenuChoice(item));

	}
	public boolean applyMenuChoice(MenuItem item){
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		Note note  = (Note) mla.getItem(info.position);
		switch (item.getItemId()) {
		case R.id.menu_delete:
			buttons.Buttons.delete(note.getId(), Notepad.getDb()) ;
			mla = new listAdap(this, 0);
			list.setAdapter(mla);
			Cursor cursor = Notepad.getDb().getNoteByModePlacePhoto(mode_ID, place_ID, photo_ID);
			drawNotes(cursor);
			Toast.makeText(getApplicationContext(), "Deleted",
					Toast.LENGTH_LONG).show();
			return (true) ;
		case R.id.menu_edit:
			Intent intent = new Intent(Search_ByTag_Activity.this, NoteEditer.class);
			intent.putExtra("mode", "edit") ;
			intent.putExtra("id", note.getId());
			startActivity(intent);
			return true ;
		case R.id.menu_share :
			buttons.Buttons.share(note.getT(), note.getC(), this) ;
		default:
			return super.onContextItemSelected(item);
		}
	}

	public void drawNotes(Cursor cursor){
		TextView t1 = (TextView) findViewById(R.id.notes_number) ;
		t1.setText("Memo ("+cursor.getCount()+")");
		int i =1 ;
		while(i <= cursor.getCount()){
			Scanner con = new Scanner(cursor.getString(2));
			Note note =  new Note(cursor.getInt(0), cursor.getString(1),con.nextLine() );	
			mla.add(note,cursor.getString(3));
			cursor.moveToNext();
			con.close() ;
			i++;

		}

	}
	@Override
	protected void onStart() {
		super.onStart();
		mla = new listAdap(this, 0);
		list.setAdapter(mla);
		Cursor cursor = Notepad.getDb().getNoteByModePlacePhoto(mode_ID, place_ID, photo_ID);
		drawNotes(cursor);
	};
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_remove:
			Intent intent = new Intent(Search_ByTag_Activity.this,Delete_Activity.class);
			startActivity(intent) ;
			return true ;

		default:
			return super.onOptionsItemSelected(item);
		}
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		outState.putInt("photo_ID", photo_ID) ;
		outState.putInt("mode_ID", mode_ID) ;
		outState.putInt("place_ID", place_ID) ;
		super.onSaveInstanceState(outState);
	}
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		photo_ID = savedInstanceState.getInt("photo_ID");
		mode_ID = savedInstanceState.getInt("mode_ID") ;
		place_ID = savedInstanceState.getInt("place_ID");
	}

}
