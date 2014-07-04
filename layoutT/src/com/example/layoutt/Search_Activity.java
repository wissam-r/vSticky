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
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class Search_Activity extends Activity {

	ListView list ;
	listAdap mla = null ;
	static String searchT = "" ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search) ;
		list = (ListView) findViewById(R.id.listViewS) ;
		Button back = (Button) findViewById(R.id.backs) ;
		SearchView search = (SearchView) findViewById(R.id.note_pad_search_sv);
		list.setOnItemClickListener(listItemL);
		list.setOnItemLongClickListener(listItemLongL);
		back.setOnClickListener(backL);
		search.setOnQueryTextListener(search_l) ;
	
		
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
			// TODO Auto-generated method stub
			Intent intent = new Intent(Search_Activity.this, ViewNote.class);
			Note note = (Note) mla.getItem(arg2);
			intent.putExtra("id", note.getId());
			startActivity(intent);
		}


	};

	OnClickListener backL = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			finish() ;
		}
	};
	
	OnQueryTextListener search_l =  new OnQueryTextListener() {
	
				@Override
				public boolean onQueryTextSubmit(String arg0) {
					searchT= arg0 ;
					mla = new listAdap(Search_Activity.this, 0);
					list.setAdapter(mla);
					Cursor cursor = Notepad.getDb().getNote(arg0);
					drawNotes(cursor) ;
					return false;
				}
	
				@Override
				public boolean onQueryTextChange(String arg0) {
					return false;
				}
			};

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);		
		menu.setHeaderTitle("Menu");
		getMenuInflater().inflate(R.menu.submenu, menu);



	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
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
			Cursor cursor = Notepad.getDb().getNote(searchT);
			drawNotes(cursor);
			Toast.makeText(getApplicationContext(), "Deleted",
					Toast.LENGTH_LONG).show();
			return (true) ;
		case R.id.menu_edit:
			Intent intent = new Intent(Search_Activity.this, NoteEditer.class);
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
		Cursor cursor = Notepad.getDb().getNote(searchT);
		drawNotes(cursor);
	};
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.menu_remove:
			Intent intent = new Intent(Search_Activity.this,Delete_Activity.class);
			startActivity(intent) ;
			return true ;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
