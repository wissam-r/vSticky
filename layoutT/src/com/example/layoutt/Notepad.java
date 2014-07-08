package com.example.layoutt;


import java.util.ArrayList;
import java.util.Scanner;

import note.Note;
import note.NoteEditer;
import note.Place;
import note.ViewNote;
import dp.NotesDbAdapter;
import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;
import filters.UserTakeActivity5;
import gps.*;

public class Notepad extends Activity{

	private static listAdap mla = null ;
	private static NotesDbAdapter mDbHelper;
	ListView list ;	
	Button sortBy_s ;
	Button overflow_b ;

	//TODO fill this objects
	final int DETECTE_NOTE = 1;
	ArrayList<String> photoPaths = new ArrayList<String>();
	ArrayList<Integer> photoIDs = new ArrayList<Integer>();

	public static NotesDbAdapter getDb(){
		return mDbHelper;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note_pado);
		mDbHelper = new NotesDbAdapter(this);
		mDbHelper.open();
		mDbHelper.dropTable() ;
		mDbHelper.createTable() ;

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
		//					Toast.makeText(getBaseContext(),arg0.getItemAtPosition(arg2).toString() , Toast.LENGTH_LONG).show();
		//
		//					
		//				}
		//
		//				@Override
		//				public void onNothingSelected(AdapterView<?> arg0) {
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
			//					Toast.makeText(getBaseContext(),arg0.getItemAtPosition(arg2).toString() , Toast.LENGTH_LONG).show();
			//
			//					
			//				}
			//
			//				@Override
			//				public void onNothingSelected(AdapterView<?> arg0) {
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK && requestCode == DETECTE_NOTE){
			int id = data.getExtras().getInt("id");
			//TODO id of the photo detected
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

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
	//			return false;
	//		}
	//		@Override
	//		public void onDestroyActionMode(ActionMode mode) {
	//			 
	//		}
	//		@Override
	//		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
	//			 
	//			getMenuInflater().inflate(R.menu.main, menu);
	//			return true;
	//		}
	//		@Override
	//		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
	//			 
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
			Button searchByTag = (Button) myD.getDialoglayout().findViewById(R.id.search_by_tag) ;
			searchByTag.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(Notepad.this,UserTakeActivity5.class);
					intent.putExtra("data",true);
					intent.putStringArrayListExtra("images", photoPaths);
					intent.putIntegerArrayListExtra("ids", photoIDs);
					startActivityForResult(intent, DETECTE_NOTE);
				}
			}) ;
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


					if(arg0.getTitle().toString().equals("Settings")){

					}
					else if (arg0.getTitle().toString().equals("Remove")){
						Intent intent = new Intent(Notepad.this,Delete_Activity.class);
						startActivity(intent) ;

					}
					else if (arg0.getTitle().toString().equals("New mode")){
						new_mode();

					}
					else if (arg0.getTitle().toString().equals("New place")){
						new_palce();
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

		}


	};

	OnQueryTextListener search_title_b_l =new OnQueryTextListener() {

		@Override
		public boolean onQueryTextSubmit(String arg0) {
			Intent intent =  new Intent(Notepad.this,Search_Activity.class);
			intent.putExtra("titleS", arg0) ;
			startActivity(intent) ;
			return false;
		}

		@Override
		public boolean onQueryTextChange(String arg0) {
			return false;
		}
	} ;

	public void new_palce(){
		MyDialog myD = new MyDialog(Notepad.this, R.layout.new_place) ;
		TextView currentGps = (TextView) myD.getDialoglayout().findViewById(R.id.current_pos);
		TextView currentGpsNet = (TextView) myD.getDialoglayout().findViewById(R.id.current_posnet);
		Button saveB =  (Button) myD.getDialoglayout().findViewById(R.id.save_place) ;
		final EditText rangeGPS = (EditText) myD.getDialoglayout().findViewById(R.id.range_gps) ;
		final EditText placeName = (EditText) myD.getDialoglayout().findViewById(R.id.name_place) ;


		final GPSProvider gps = new GPSProvider(Notepad.this, currentGps) ;
		final NetworkProvider netGPS = new NetworkProvider(Notepad.this, currentGpsNet) ;

		saveB.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Place newplace ;
				final int minRaduis = 10;
				if ((GPSProvider.isGPS_ConToSatil()) && (gps.getLatitude()!=null)&&(gps.getLongitude()!=null))
				{
					try{
						newplace = new Place(-1, placeName.getText().toString(), gps.getLongitude(), gps.getLatitude(), Integer.parseInt(rangeGPS.getText().toString()));
					}catch(NumberFormatException e){
						Toast.makeText(getBaseContext(), "Plz enter unsigned number net", Toast.LENGTH_LONG).show() ;
						return;
					}
				}
				else if ((NetworkProvider.isInternet_con()) && (netGPS.getLatitude()!=null)&&(netGPS.getLongitude()!=null))
				{
					try{
						newplace = new Place(-1, placeName.getText().toString(), netGPS.getLongitude(), netGPS.getLatitude(), Integer.parseInt(rangeGPS.getText().toString()));
					}catch(NumberFormatException e){
						Toast.makeText(getBaseContext(), "Plz enter unsigned number net", Toast.LENGTH_LONG).show() ;
						return;
					}
				}else{
					Toast.makeText(getBaseContext(), "fail", Toast.LENGTH_LONG).show() ;
					return;
				}
					
				if ((newplace.getRaduis() >= minRaduis)&&(!newplace.getName().equals("")) ){
					Cursor cursor = mDbHelper.getAllPlaces() ;
					Region  reg;

					int i = 0 ;
					while (i < cursor.getCount())
					{
						reg = new Region(cursor.getDouble(2), cursor.getDouble(3), cursor.getInt(4));
						if (reg.isInside(newplace.getRegion()))
						{
							if(reg.isInside(newplace.getRegion().getLoc())){
								Toast.makeText(getBaseContext(), "fail: you are inside other place: "+cursor.getString(1), Toast.LENGTH_LONG).show();
							}else{
								Toast.makeText(getBaseContext(), "fail: region collision with "+cursor.getString(1), Toast.LENGTH_LONG).show();
							}
							return ;
						}
						if(cursor.getString(1).equals(newplace.getName())){
							Toast.makeText(getBaseContext(), "fail: chose other name for this palce", Toast.LENGTH_LONG).show();
							return;
						}
						i++ ;
						cursor.moveToNext() ;
					}
					mDbHelper.insertPlace(newplace.getName(), newplace.getX(), newplace.getY(), newplace.getRaduis()) ;
					Toast.makeText(getBaseContext(), "success", Toast.LENGTH_LONG).show() ;
				}

			}
		});

		myD.getAlertDialog().show() ;
	}

	public void new_mode(){
		MyDialog myD = new MyDialog(Notepad.this, R.layout.new_mode) ;
		final EditText mode_name = (EditText) myD.getDialoglayout().findViewById(R.id.new_mode_name) ;
		Button save_mode = (Button) myD.getDialoglayout().findViewById(R.id.save_mode) ;
		save_mode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (!mode_name.getText().toString().equals("")){
					mDbHelper.insertMode(mode_name.getText().toString()) ;
					Cursor cursor = mDbHelper.getAllModes() ;
					Toast.makeText(getBaseContext(), String.valueOf(cursor.getCount()), Toast.LENGTH_LONG).show();
				}
			}
		}) ;
		myD.getAlertDialog().show() ;
	}

}



