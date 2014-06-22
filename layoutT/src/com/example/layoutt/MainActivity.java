package com.example.layoutt;

import com.example.layoutt.NotesDbAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
//import android.text.format.DateUtils;
//import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends Activity{

	static listAdap mla = null ;
	private static NotesDbAdapter mDbHelper;
	Menu myMenu =  null ;
	ListView list ;

	public static NotesDbAdapter getDb(){
		return mDbHelper;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
		mDbHelper = new NotesDbAdapter(this);
		mDbHelper.open();
		//		mDbHelper.dropTable() ;
		//		mDbHelper.createTable() ;
		//		drawAllNotes();
		//		mDbHelper.deleteAll();
		//		mDbHelper.dropTable();
		list = (ListView) findViewById(R.id.listView);
		Button b1 = (Button) findViewById(R.id.b1);
		b1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent =  new Intent(MainActivity.this, addNote.class);
				startActivity(intent) ;

			}
		});
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, editNote.class);
				Note note = (Note) mla.getItem(arg2);
				intent.putExtra("id", note.getId());
				startActivity(intent);

			}


		});
		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				registerForContextMenu(arg0);
				openContextMenu(arg0);	
				closeContextMenu();
				return false;
			}
		});
	}
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
			mDbHelper.deleteID(note.getId());
			mla = new listAdap(this, 0);
			list.setAdapter(mla);
			drawAllNotes();
			return (true) ;
		case R.id.menu_edit:
			Intent intent = new Intent(MainActivity.this, editNote.class);
			intent.putExtra("id", note.getId());
			startActivity(intent);
			return true ;
		default:
			return super.onContextItemSelected(item);
		}
	}

	private void drawAllNotes(){
		Cursor cursor = mDbHelper.getAllNotes();
		if (cursor == null)
			return;
		int i =1 ;
		while(i <= cursor.getCount()){
			i++;
			Note note =  new Note(cursor.getInt(0), cursor.getString(1),cursor.getString(2) );	
			MainActivity.mla.add(note,cursor.getString(3));
			cursor.moveToNext();


		}



	}
	//	private void drawLast(){
	//		Cursor cursor = mDbHelper.getAllNotes();
	//		if (cursor == null)
	//				return;
	//		cursor.moveToLast();
	//		Note note =  new Note(cursor.getInt(0),cursor.getString(1), cursor.getString(2));
	//		MainActivity.mla.add(note);				
	//	}
	//	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		myMenu = menu ;
		return true;
	}
	@Override
	protected void onStart() {
		super.onStart();
		mla = new listAdap(this, 0);
		list.setAdapter(mla);
		drawAllNotes();
	};
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	@Override
	protected void onStop() {
		super.onStop();
	};
	@Override
	protected void onRestart() {
		super.onRestart();

	};
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode){
		case 1 : if (resultCode==RESULT_OK);
		}
	}
	
	//	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
	//		@Override
	//		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
	//			// TODO Auto-generated method stub
	//			return false;
	//		}
	//		@Override
	//		public void onDestroyActionMode(ActionMode mode) {
	//			// TODO Auto-generated method stub
	//		}
	//		@Override
	//		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
	//			// TODO Auto-generated method stub
	//			getMenuInflater().inflate(R.menu.main, menu);
	//			return true;
	//		}
	//		@Override
	//		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
	//			// TODO Auto-generated method stub
	//			return false;
	//		}
	//	};


}
