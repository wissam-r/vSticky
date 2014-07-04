package com.example.layoutt;


import java.util.Scanner;

import note.Note;
import note.NoteEditer;
import note.ViewNote;
import dp.NotesDbAdapter;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;

public class Notepad extends Activity{

	private static listAdap mla = null ;
	private static NotesDbAdapter mDbHelper;
	ListView list ;	
	Button sortBy_s ;
	Button overflow_b ;

	public static NotesDbAdapter getDb(){
		return mDbHelper;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note_pado);
		mDbHelper = new NotesDbAdapter(this);
		mDbHelper.open();
		//				mDbHelper.dropTable() ;
		//				mDbHelper.createTable() ;

		list = (ListView) findViewById(R.id.note_pad_noteslist);	
		Button new_note_b  = (Button) findViewById(R.id.note_pad_add);
		Button search_t_b = (Button) findViewById(R.id.note_pad_search);
		Button search_tag_b = (Button) findViewById(R.id.note_pad_search_tag);
		sortBy_s = (Button) findViewById(R.id.note_pad_s_sortby) ;
		overflow_b = (Button) findViewById(R.id.note_pad_overflow) ;


		//		String array_spinner[];
		//		array_spinner=new String[2];
		//		array_spinner[0]="Date";
		//		array_spinner[1]="Title";
		//
		//		ArrayAdapter spinnerAd = new ArrayAdapter(Notepad.this,
		//				R.layout.spinner_item, array_spinner);
		//		
		//		sortBy_s.setAdapter(spinnerAd) ;
		//		sortBy_s.setOnItemSelectedListener(sortBy_l) ;


		//	        array_spinner[2]="option 3";
		//	        array_spinner[3]="option 4";
		//	        array_spinner[4]="option 5";
		//	        final ArrayAdapter adapter = new ArrayAdapter(Notepad.this,
		//	        android.R.layout.simple_spinner_item, array_spinner);
		//	        s1.setAdapter(adapter);
		//			s1.setOnItemSelectedListener(new OnItemSelectedListener(){
		//
		//				@Override
		//				public void onItemSelected(AdapterView<?> arg0, View arg1,
		//						int arg2, long arg3) {
		//					// TODO Auto-generated method stub
		//					Toast.makeText(getBaseContext(),arg0.getItemAtPosition(arg2).toString() , Toast.LENGTH_LONG).show();
		//
		//					
		//				}
		//
		//				@Override
		//				public void onNothingSelected(AdapterView<?> arg0) {
		//					// TODO Auto-generated method stub
		//					
		//				}
		//
		//				
		//			});

		//		SearchView search_title_b = (SearchView) findViewById(R.id.note_pad_search);
		overflow_b.setOnClickListener(overflow_l) ;
		sortBy_s.setOnClickListener(sortby_l) ;
		new_note_b.setOnClickListener(new_note_b_l) ;
		search_t_b.setOnClickListener(search_t_b_l) ;
		search_tag_b.setOnClickListener(search_tag_b_l) ;


		list.setOnItemClickListener(listItemL);
		list.setOnItemLongClickListener(listItemLongL);
		//		search_title_b.setOnQueryTextListener(search_title_b_l);
		//				new OnQueryTextListener() {
		//
		//			@Override
		//			public boolean onQueryTextSubmit(String arg0) {
		//				Intent intent =  new Intent(Notepad.this,Search_Activity.class);
		//				intent.putExtra("titleS", search_title_b.getQuery().toString()) ;
		//				startActivity(intent) ;
		//				return false;
		//			}
		//
		//			@Override
		//			public boolean onQueryTextChange(String arg0) {
		//				return false;
		//			}
		//		});

	}

	OnItemClickListener listItemL = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(Notepad.this, ViewNote.class);
			Note note = (Note) mla.getItem(arg2);
			intent.putExtra("id", note.getId());
			startActivity(intent);
			//			Context context = Notepad.this ;
			////			AlertDialog.Builder builder = new AlertDialog.Builder(context)
			////            .setTitle("My title")
			////            .setMessage("Enter password");
			////			final FrameLayout frameView = new FrameLayout(context);
			////			builder.setView(frameView);
			////			final AlertDialog alertDialog = builder.create();
			////			LayoutInflater inflater = alertDialog.getLayoutInflater();
			////			View dialoglayout = inflater.inflate(R.layout.testwithmenu, frameView);
			////			alertDialog.show();
			//			AlertDialog.Builder builder = new Builder(context) ;
			//			final FrameLayout frameView = new FrameLayout(context);
			//			builder.setView(frameView);
			//			final AlertDialog alertDialog = builder.create()  ;
			//			LayoutInflater inflater = alertDialog.getLayoutInflater();
			//			View dialoglayout = inflater.inflate(R.layout.testwithmenu, frameView);
			//			final MyDialog myD = new MyDialog(Notepad.this, R.layout.testwithmenu) ;
			//			
			//			Spinner s1 = (Spinner) myD.getDialoglayout().findViewById(R.id.spinner) ;
			//			String array_spinner[];
			//			array_spinner=new String[5];
			//	        array_spinner[0]="option 1";
			//	        array_spinner[1]="option 2";
			//	        array_spinner[2]="option 3";
			//	        array_spinner[3]="option 4";
			//	        array_spinner[4]="option 5";
			//	        final ArrayAdapter adapter = new ArrayAdapter(Notepad.this,
			//	        android.R.layout.simple_spinner_item, array_spinner);
			//	        s1.setAdapter(adapter);
			//			s1.setOnItemSelectedListener(new OnItemSelectedListener(){
			//
			//				@Override
			//				public void onItemSelected(AdapterView<?> arg0, View arg1,
			//						int arg2, long arg3) {
			//					// TODO Auto-generated method stub
			//					Toast.makeText(getBaseContext(),arg0.getItemAtPosition(arg2).toString() , Toast.LENGTH_LONG).show();
			//
			//					
			//				}
			//
			//				@Override
			//				public void onNothingSelected(AdapterView<?> arg0) {
			//					// TODO Auto-generated method stub
			//					
			//				}
			//
			//				
			//			});
			//			Button b1  = (Button) myD.getDialoglayout().findViewById(R.id.canceldaa);
			//			b1.setOnClickListener(new OnClickListener() {
			//				
			//				@Override
			//				public void onClick(View arg0) {
			//					// TODO Auto-generated method stub
			//					myD.getAlertDialog().cancel() ;
			//				}
			//			}) ; 
			////			alertDialog.setContentView(R.layout.testwithmenu) ;
			//			myD.getAlertDialog().show() ;
		}
	};

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
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);		
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
			buttons.Buttons.delete(note.getId(), getDb()) ;
			mla = new listAdap(this, 0);
			list.setAdapter(mla);
			Cursor cursor = mDbHelper.getNotesByDate();
			drawNotes(cursor);
			Toast.makeText(getApplicationContext(), "Deleted",
					Toast.LENGTH_LONG).show();
			return (true) ;
		case R.id.menu_edit:
			Intent intent = new Intent(Notepad.this, NoteEditer.class);
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

	private void drawNotes(Cursor cursor){

		TextView note_number = (TextView) findViewById(R.id.note_pad_t_number) ;
		note_number.setText("Memo ("+cursor.getCount()+")");
		int i =1 ;
		while(i <= cursor.getCount()){
			Scanner con = new Scanner(cursor.getString(2));
			Note note =  new Note(cursor.getInt(0), cursor.getString(1),con.nextLine() );	
			Notepad.mla.add(note,cursor.getString(3));
			cursor.moveToNext();
			con.close() ;
			i++;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_remove:
			Intent intent = new Intent(Notepad.this,Delete_Activity.class);
			startActivity(intent) ;
			return true ;
		

		default:
			return super.onOptionsItemSelected(item);
		}
	}
	@Override
	protected void onStart() {
		super.onStart();
		mla = new listAdap(this, 0);
		list.setAdapter(mla);
		Cursor cursor = mDbHelper.getNotesByDate();
		drawNotes(cursor);
		//		if (sortBy_s.getSelectedItem().toString().equals("Date"))
		//			 cursor = mDbHelper.getNotesByDate();
		//		else
		//			 cursor = mDbHelper.getNotesByTitle();

		//		sortBy_s.setOnItemSelectedListener(sortBy_l) ;
	};
	@Override
	protected void onPause() {
		super.onPause();
	}
	@Override
	protected void onResume() {
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
	//	@Override
	//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	//		super.onActivityResult(requestCode, resultCode, data);
	//		switch(requestCode){
	//		case 1 : if (resultCode==RESULT_OK);
	//		}
	//	}

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

	OnClickListener new_note_b_l = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent =  new Intent(Notepad.this, NoteEditer.class);
			intent.putExtra("mode", "add") ;
			startActivity(intent) ;
			
//			Intent intent  = new Intent(Notepad.this,CameraPhotoCapture.class)  ;
//			startActivity(intent) ;
			
			//			PopupMenu popup = new PopupMenu(Notepad.this, new_note_b);  
			//            //Inflating the Popup using xml file  
			//            popup.getMenuInflater().inflate(R.menu.submenu, popup.getMenu());
			//            popup.show();
			
//			Button b1  = (Button) myD.getDialoglayout().findViewById(R.id.canceldaa);
//			//			b1.setOnClickListener(new OnClickListener() {
//			//				
//			//				@Override
//			//				public void onClick(View arg0) {
//			//					// TODO Auto-generated method stub
//			//					myD.getAlertDialog().cancel() ;
//			//				}
//			//			}) ; 
//			////			alertDialog.setContentView(R.layout.testwithmenu) ;
//			//			myD.getAlertDialog().show() ;

		}
	};

	OnClickListener search_t_b_l = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent =  new Intent(Notepad.this, Search_Activity.class);
			startActivity(intent) ;
		}
	};
	OnClickListener search_tag_b_l = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			MyDialog myD = new MyDialog(Notepad.this, R.layout.search_tags_layout) ;
			myD.getAlertDialog().show() ;
		}
	};
	OnClickListener sortby_l = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			PopupMenu popup = new PopupMenu(Notepad.this, sortBy_s);  
			//Inflating the Popup using xml file  
			popup.getMenuInflater().inflate(R.menu.sortby, popup.getMenu());
			popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

				@Override
				public boolean onMenuItemClick(MenuItem arg0) {
					// TODO Auto-generated method stub

					if(arg0.getTitle().toString().equals("Date")){
						mla = new listAdap(Notepad.this, 0);
						list.setAdapter(mla);
						Cursor cursor = mDbHelper.getNotesByDate();
						drawNotes(cursor);
					}
					else{
						mla = new listAdap(Notepad.this, 0);
						list.setAdapter(mla);
						Cursor cursor = mDbHelper.getNotesByTitle();
						drawNotes(cursor);

					}
					return false;
				}
			});
			popup.show();
		}
	};

	OnClickListener overflow_l = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			PopupMenu popup = new PopupMenu(Notepad.this, overflow_b);  
			//Inflating the Popup using xml file  
			popup.getMenuInflater().inflate(com.example.layoutt.R.menu.overflow_menu, popup.getMenu());
			popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

				@Override
				public boolean onMenuItemClick(MenuItem arg0) {
					// TODO Auto-generated method stub

					if(arg0.getTitle().toString().equals("Settings")){

					}
					else if (arg0.getTitle().toString().equals("Remove")){
						Intent intent = new Intent(Notepad.this,Delete_Activity.class);
						startActivity(intent) ;

					}
					else if (arg0.getTitle().toString().equals("New place")){
						MyDialog myD = new MyDialog(Notepad.this, R.layout.new_place) ;
						myD.getAlertDialog().show() ;

					}
					return false;
				}
			});
			popup.show();
		}
	};
	OnItemSelectedListener sortBy_l = new OnItemSelectedListener(){

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			// TODO Auto-generated method stub
			//			Toast.makeText(getBaseContext(),arg0.getItemAtPosition(arg2).toString() ,
			//					Toast.LENGTH_LONG).show();
			if (arg0.getItemAtPosition(arg2).toString().equals("Date")){
				mla = new listAdap(Notepad.this, 0);
				list.setAdapter(mla);
				Cursor cursor = mDbHelper.getNotesByDate();
				drawNotes(cursor);
			}
			else{
				mla = new listAdap(Notepad.this, 0);
				list.setAdapter(mla);
				Cursor cursor = mDbHelper.getNotesByTitle();
				drawNotes(cursor);

			}



		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
		}


	};

	OnQueryTextListener search_title_b_l =new OnQueryTextListener() {

		@Override
		public boolean onQueryTextSubmit(String arg0) {
			// TODO Auto-generated method stub
			Intent intent =  new Intent(Notepad.this,Search_Activity.class);
			intent.putExtra("titleS", arg0) ;
			startActivity(intent) ;
			return false;
		}

		@Override
		public boolean onQueryTextChange(String arg0) {
			// TODO Auto-generated method stub
			return false;
		}
	} ;

}


