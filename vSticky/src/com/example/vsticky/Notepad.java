package com.example.vsticky;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

import com.example.layoutt.R;

import note.Mode;
import note.Note;
import note.NoteEditer;
import note.Place;
import note.ViewNote;
import db.NotesDbAdapter;
import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;
import auth.SessionManager;
import filters.UserTakeActivity5;
import gps.*;

public class Notepad extends Activity{

	private static String user_ID = "-1" ;

	public static String getUser_ID() {
		return user_ID;
	}

	public static void setUser_ID(String user_ID) {
		Notepad.user_ID = user_ID;
	}

	private static listAdap mla = null ;
	private static NotesDbAdapter mDbHelper;
	ListView list ;	
	Button sortBy_s ;
	Button overflow_b ;
	private Integer mode_ID =-1 ;
	private Integer place_ID =-1;
	private Integer photo_ID =-1 ;
	MyDialog myDa ;
	ListView listView1;

	final int DETECTE_NOTE = 1;
	ArrayList<String> photoPaths = new ArrayList<String>();
	ArrayList<Integer> photoIDs = new ArrayList<Integer>();

	GPSProvider gps;
	NetworkProvider netGPS;

	public static NotesDbAdapter getDb(){
		return mDbHelper;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note_pado);
//TODO here we statrt trying to get the id
		try{
			String user_name = getIntent().getExtras().getString("username") ;
			String user_id = getIntent().getExtras().getString("userId") ;
			String user_token = getIntent().getExtras().getString("token") ;
			Log.d("NOTEPAD",user_name);
			Log.d("NOTEPAD",user_id);
			Log.d("NOTEPAD",user_token);
			Cursor cursor  = getDb().getAllUsers() ;
			int i = 0 ;
			while (i<cursor.getCount()){
				i++ ;
				if (cursor.getString(0).equals(user_id)){
					user_ID = user_id ;
					break ;
				}
				else
					cursor.moveToNext() ;
			}
			if (i==cursor.getCount()){
				getDb().insertUser(user_id, user_name, user_token) ;
				user_ID = user_id ;
			}
		}
		catch (Exception ex){
			if (user_ID.equals("-1"))
				Toast.makeText(getBaseContext(), "no user", Toast.LENGTH_LONG).show() ;}
		myDa = new MyDialog(Notepad.this, R.layout.search_tags_layout) ;

		mDbHelper = new NotesDbAdapter(this);
		mDbHelper.open();
		//						mDbHelper.dropTable() ;
		//						mDbHelper.createTable() ;

		list = (ListView) findViewById(R.id.note_pad_noteslist);	
		Button new_note_b  = (Button) findViewById(R.id.note_pad_add);
		Button search_t_b = (Button) findViewById(R.id.note_pad_search);
		Button search_tag_b = (Button) findViewById(R.id.note_pad_search_tag);
		sortBy_s = (Button) findViewById(R.id.note_pad_s_sortby) ;
		overflow_b = (Button) findViewById(R.id.note_pad_overflow) ;

		overflow_b.setOnClickListener(overflow_l) ;
		sortBy_s.setOnClickListener(sortby_l) ;
		new_note_b.setOnClickListener(new_note_b_l) ;
		search_t_b.setOnClickListener(search_t_b_l) ;
		search_tag_b.setOnClickListener(search_tag_b_l) ;


		list.setOnItemClickListener(listItemL);
		list.setOnItemLongClickListener(listItemLongL);

	}

	OnItemClickListener listItemL = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent intent = new Intent(Notepad.this, ViewNote.class);
			Note note = (Note) mla.getItem(arg2);
			intent.putExtra("id", note.getId());
			startActivity(intent) ;
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
	OnItemLongClickListener listItemLongLm = new OnItemLongClickListener() {

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
		switch (v.getId()) {

		case R.id.modes_place_list :
			Log.d("onlong", "itemse") ;
			getMenuInflater().inflate(R.menu.remove, menu) ;
			break ;
		case R.id.note_pad_noteslist:
			getMenuInflater().inflate(R.menu.submenu, menu);
			break;


		default:
			break;
		}




	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		Log.d("selected", "entered here ") ;

		return (applyMenuChoice(item));

	}
	public boolean applyMenuChoice(MenuItem item){
		Log.d("herherp", "entered here ") ;
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

		Note note ;
		switch (item.getItemId()) {
		case R.id.menu_remove_items :
			Log.d("delete", "entered here ") ;
			getDb().deleteModeByname(item.getTitle().toString()) ;

			return true ;
		case R.id.menu_delete:
			note = (Note) mla.getItem(info.position);
			actions.Buttons.delete(note.getId(), getDb()) ;
			mla = new listAdap(this, 0);
			list.setAdapter(mla);
			Cursor cursor = mDbHelper.getNotesByDate(Notepad.getUser_ID());
			drawNotes(cursor);
			Toast.makeText(getApplicationContext(), "Deleted",
					Toast.LENGTH_LONG).show();
			return (true) ;
		case R.id.menu_edit:
			note = (Note) mla.getItem(info.position);
			Intent intent = new Intent(Notepad.this, NoteEditer.class);
			intent.putExtra("mode", "edit") ;
			intent.putExtra("id", note.getId());
			startActivity(intent);
			return true ;
		case R.id.menu_share :
			note = (Note) mla.getItem(info.position);
			actions.Buttons.share(note.getT(), note.getC(), this) ;
			return true ;

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
	public boolean onOptionsItemSelected( MenuItem item) {
		switch (item.getItemId()) {

		case R.id.menu_remove:
			Intent intent = new Intent(Notepad.this,Delete_Activity.class);
			startActivity(intent) ;
			return true ;
		case R.id.menu_places:
			MyDialog dialog = new MyDialog(Notepad.this, R.layout.modes_places) ;
			TextView title = (TextView) dialog.getDialoglayout().findViewById(R.id.dialog_name) ;
			title.setText("Places") ;
			ListView listView = (ListView) dialog.getDialoglayout().findViewById(R.id.modes_place_list) ;
			Cursor cursor = getDb().getAllPlaces() ;
			ArrayList<String> values =  new ArrayList<String>() ;
			int i= 0;
			while(i<cursor.getCount()){
				i++ ;
				values.add(cursor.getString(1)+"\n"+
						"	Latitude : "+cursor.getDouble(2)+"\n"+
						"	Longitude : "+cursor.getDouble(3)+"\n"+	
						"	Range : "+cursor.getDouble(4)) ;
				cursor.moveToNext() ;

			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, android.R.id.text1, values);
			listView.setBackgroundColor(Color.LTGRAY) ;
			listView.setAdapter(adapter); 
			dialog.getAlertDialog().show() ;

			return true ;

		case R.id.menu_modes :
			MyDialog dialog1 = new MyDialog(Notepad.this, R.layout.modes_places) ;
			TextView title1 = (TextView) dialog1.getDialoglayout().findViewById(R.id.dialog_name) ;
			title1.setText("Modes") ;
			listView1 = (ListView) dialog1.getDialoglayout().findViewById(R.id.modes_place_list) ;
			Cursor cursor1 = getDb().getAllModes() ;
			ArrayList<String> values1 =  new ArrayList<String>() ;
			int qi= 0;
			while(qi<cursor1.getCount()){
				qi++ ;
				values1.add(cursor1.getString(1)) ;
				cursor1.moveToNext() ;

			}
			ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, android.R.id.text1, values1);
			listView1.setBackgroundColor(Color.LTGRAY) ;
			listView1.setAdapter(adapter1); 
			listView1.setOnItemLongClickListener(listItemLongLm) ;
			dialog1.getAlertDialog().show() ;
			return true ;


		default:
			return super.onOptionsItemSelected(item);
		}
	}


	@Override
	protected void onStart() {
		super.onStart();

		gps = new GPSProvider(this, new TextView(Notepad.this)) ;
		netGPS = new NetworkProvider(this, new TextView(Notepad.this)) ;

		mla = new listAdap(this, 0);
		list.setAdapter(mla);
		try{
			Cursor cursor = mDbHelper.getNotesByDate(Notepad.getUser_ID());
			drawNotes(cursor);

		}
		catch (Exception ex){
			getDb().dropTable() ;
			getDb().createTable() ;
			getDb().insertUser(user_ID, "test_user", "qwerttyy") ;
		}

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
			photo_ID = id ;
			Intent intent = new Intent(Notepad.this , Search_ByTag_Activity.class) ;
			Log.d("koko","   "+ mode_ID+ "    "+place_ID) ;
			intent.putExtra("search_mode", "camera") ;
			intent.putExtra("photo_ID", photo_ID) ;
			intent.putExtra("mode_ID", mode_ID) ;
			intent.putExtra("place_ID", place_ID) ;
			//TODO id of the photo detected
			startActivity(intent) ;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}


	OnClickListener new_note_b_l = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent =  new Intent(Notepad.this, NoteEditer.class);
			intent.putExtra("mode", "add") ;
			startActivity(intent)  ;

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
			myDaL() ;
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
						Cursor cursor = mDbHelper.getNotesByDate(Notepad.getUser_ID());
						drawNotes(cursor);
					}
					else{
						mla = new listAdap(Notepad.this, 0);
						list.setAdapter(mla);
						Cursor cursor = mDbHelper.getNotesByTitle(Notepad.getUser_ID());
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
					else if (arg0.getTitle().toString().equals("Sync")){
						sync();
					}
					else if (arg0.getTitle().toString().equals("Signout")){
						signout() ;
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

			if (arg0.getItemAtPosition(arg2).toString().equals("Date")){
				mla = new listAdap(Notepad.this, 0);
				list.setAdapter(mla);
				Cursor cursor = mDbHelper.getNotesByDate(Notepad.getUser_ID());
				drawNotes(cursor);
			}
			else{
				mla = new listAdap(Notepad.this, 0);
				list.setAdapter(mla);
				Cursor cursor = mDbHelper.getNotesByTitle(Notepad.getUser_ID());
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
		final MyDialog myD = new MyDialog(Notepad.this, R.layout.new_place) ;
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
				//				if (((GPSProvider.isGPS_ConToSatil()) && (gps.getLatitude()!=null)&&(gps.getLongitude()!=null))&&((NetworkProvider.isInternet_con()) && (netGPS.getLatitude()!=null)&&(netGPS.getLongitude()!=null)))
				//				{
				//					Location loc1  = new Location("") ;
				//					Location loc2 =  new Location("") ;
				//					loc1.setLatitude(gps.getLatitude()); loc1.setLongitude(gps.getLongitude()) ;
				//					loc2.setLatitude(netGPS.getLatitude()) ; loc2.setLongitude(netGPS.getLongitude()) ;
				//					Toast.makeText(getBaseContext(), "distance : "+loc1.distanceTo(loc2 )+"\n"+
				//							"longt : " +(loc2.getLongitude()-loc1.getLongitude()+"\n"+
				//									"latt : " +(loc2.getLatitude()-loc1.getLatitude())), Toast.LENGTH_LONG).show() ;
				//				}
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
					//					Toast.makeText(getBaseContext(), "fail", Toast.LENGTH_LONG).show() ;
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
					myD.getAlertDialog().cancel() ;
				}

			}
		});

		myD.getAlertDialog().show() ;
	}

	public void new_mode(){
		final MyDialog myD = new MyDialog(Notepad.this, R.layout.new_mode) ;
		final EditText mode_name = (EditText) myD.getDialoglayout().findViewById(R.id.new_mode_name) ;
		Button save_mode = (Button) myD.getDialoglayout().findViewById(R.id.save_mode) ;
		save_mode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (!mode_name.getText().toString().equals("")){
					mDbHelper.insertMode(mode_name.getText().toString()) ;
					myD.getAlertDialog().cancel() ;

				}
			}
		}) ;
		myD.getAlertDialog().show() ;
	}

	OnClickListener choose_mode_l = new OnClickListener() {

		@Override
		public void onClick(final View arg0) {
			ArrayList<Mode> modes =  new ArrayList<Mode>() ;
			Mode mode ;
			Cursor cursor =  Notepad.getDb().getAllModes() ;
			int i = 0 ;
			while(i<cursor.getCount()){
				i++;
				mode = new Mode(cursor.getInt(0),cursor.getString(1)) ;
				modes.add(mode) ;
				cursor.moveToNext() ;
			}

			PopupMenu popup = new PopupMenu(Notepad.this, arg0);
			for (Mode modee : modes) {
				popup.getMenu().addSubMenu(0, modee.getId(), 0, modee.getName()) ;		
			}
			//			popup.getMenu().addSubMenu(0, mode.getId(), 0, mode.getName()) ;


			//			if (mode_ID != -1 )
			//				((Button) arg0 ).setText(popup.getMenu().getItem(mode_ID-1).getTitle());
			OnMenuItemClickListener	modeClick = new OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem arg1) {	
					mode_ID = arg1.getItemId() ;
					((Button) arg0).setText(arg1.getTitle()) ;

					return false;
				}

			} ;
			popup.setOnMenuItemClickListener(modeClick) ;
			popup.show() ;

		}
	};
	OnClickListener choose_place_l = new OnClickListener() {

		@Override
		public void onClick(final View arg0) {
			ArrayList<Place> places =  new ArrayList<Place>() ;
			Place place ;
			Cursor cursor =  Notepad.getDb().getAllPlaces() ;
			int i = 0 ;
			while(i<cursor.getCount()){
				i++;
				place = new Place(cursor.getInt(0),cursor.getString(1),cursor.getDouble(2),cursor.getDouble(3),cursor.getInt(4)) ;
				places.add(place) ;
				cursor.moveToNext() ;
			}

			PopupMenu popup = new PopupMenu(Notepad.this, arg0);
			for (Place placee : places) {
				popup.getMenu().addSubMenu(0, placee.getId(), 0, placee.getName()) ;		
			}
			//			popup.getMenu().addSubMenu(0, mode.getId(), 0, mode.getName()) ;

			OnMenuItemClickListener placeClick = new OnMenuItemClickListener() {

				@Override
				public boolean onMenuItemClick(MenuItem arg1) {	
					place_ID = arg1.getItemId() ;
					((Button) arg0).setText(arg1.getTitle());
					return false;
				}

			} ;
			popup.setOnMenuItemClickListener(placeClick) ;

			popup.show() ;
		}


	};




	OnClickListener search_By_mode_tag =  new OnClickListener(){
		@Override
		public void onClick(View arg0){
			Intent intent = new Intent(Notepad.this , Search_ByTag_Activity.class) ;
			intent.putExtra("mode_ID", mode_ID) ;
			intent.putExtra("place_ID", place_ID) ;
			intent.putExtra("photo_ID", -1) ;
			intent.putExtra("search_mode", "mode_place") ;
			startActivity(intent) ;

		}
	} ;

	OnClickListener search_By_cam =  new OnClickListener() {

		@Override
		public void onClick(View arg0){
			int thisPlace = getGPSID();
			if (place_ID<0 && thisPlace >=0)
			{
				place_ID = thisPlace;
			}
			if(place_ID < 0){
				Toast.makeText(Notepad.this, "unknown place Plz use Internet \n or GPS or Choose palce", Toast.LENGTH_SHORT).show();
				return;
			}
			if(mode_ID < 0){
				Toast.makeText(Notepad.this, "you need to chose mode to use the camera", Toast.LENGTH_SHORT).show();
				return;
			}

			//			Log.d("place","getGPSID :: "+thisPlace);
			//			if(((place_ID != thisPlace)&&(thisPlace>0))||((place_ID<0)&&(thisPlace==-1))|| mode_ID < 0)
			//			{
			//				if((place_ID < 0)&&(thisPlace==-1)){
			//					Toast.makeText(Notepad.this, "unknown place Plz use Internet \n or GPS or Choose palce", Toast.LENGTH_SHORT).show();
			//				}else if((place_ID != thisPlace)&&(thisPlace>0)){
			//					Toast.makeText(Notepad.this, "you can't search with place you are not in", Toast.LENGTH_SHORT).show();
			//				}else if( mode_ID < 0){
			//					Toast.makeText(Notepad.this, "you need to chose mode to use the camera", Toast.LENGTH_SHORT).show();
			//				}
			//				return ;
			//			}

			Cursor cursor = Notepad.getDb().getNoteByModePlace(mode_ID, place_ID,Notepad.getUser_ID()) ;
			Log.d("soso","   "+ mode_ID+ "    "+place_ID) ;
			Toast.makeText(getBaseContext(),"Number of selected notes " + cursor.getCount(), Toast.LENGTH_LONG).show() ;
			int i = 0 ;
			ArrayList<Integer> photoesID = new ArrayList<Integer>() ;

			while(i <cursor.getCount()){
				i++ ;
				photoesID.add(cursor.getInt(4)) ;
				cursor.moveToNext() ;
				//						photoPaths.add(cursor.getS)
			}
			// add elements to al, including duplicates
			HashSet<Integer> temp = new HashSet<Integer>();
			temp.addAll(photoesID);
			photoesID.clear();
			photoesID.addAll(temp);
			for (int photoID : photoesID) {
				//						Toast.makeText(getBaseContext(), photoID +"", Toast.LENGTH_LONG).show() ;
				Cursor cursor2 = Notepad.getDb().getPhotoById(photoID) ;
				if (cursor2.getCount()>0){
					photoPaths.add(cursor2.getString(1));
					photoIDs.add(cursor2.getInt(0)) ;
					Log.d("jojo", cursor2.getInt(0) + "     " + cursor2.getString(1)) ;
				}
			}
			if(photoesID.size()<1){
				Toast.makeText(Notepad.this, "no notes associated with your settings", Toast.LENGTH_SHORT).show();
				return;
			}

			Intent intent = new Intent(Notepad.this,UserTakeActivity5.class);
			intent.putExtra("data",true);
			intent.putStringArrayListExtra("images", photoPaths);
			intent.putIntegerArrayListExtra("ids", photoIDs);
			startActivityForResult(intent, DETECTE_NOTE);
		}


	};
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		if (myDa.getAlertDialog().isShowing()){
			outState.putBoolean("myDa", true) ;
			outState.putSerializable("photo_ID", photo_ID);
			outState.putSerializable("place_ID", place_ID);
			outState.putSerializable("mode_ID", mode_ID);
		}
		else
			outState.putBoolean("myDa", false) ;

		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (savedInstanceState.getBoolean("myDa")){

			photo_ID = (Integer) savedInstanceState.getSerializable("photo_ID") ;
			place_ID = (Integer) savedInstanceState.getSerializable("place_ID") ;
			mode_ID = (Integer) savedInstanceState.getSerializable("mode_ID") ;
			myDaL() ;
		}

		super.onRestoreInstanceState(savedInstanceState);
	}


	public void sync(){
		//TODO ammar
		startService(new Intent(Notepad.this,SyncService.class));
	}

	public int getGPSID(){

		Location loc;
		if ((GPSProvider.isGPS_ConToSatil()) && (gps.getLatitude()!=null)&&(gps.getLongitude()!=null)){
			loc = new Location("");
			loc.setLongitude(gps.getLongitude());
			loc.setLatitude(gps.getLatitude());
			Log.d("place", "find locatoin by gps");
		}else if ((NetworkProvider.isInternet_con()) && (netGPS.getLatitude()!=null)&&(netGPS.getLongitude()!=null)){
			loc = new Location("");
			loc.setLongitude(netGPS.getLongitude());
			loc.setLatitude(netGPS.getLatitude());
			Log.d("place", "find locatoin by net");
		}else{
			//			Toast.makeText(getBaseContext(), "fail", Toast.LENGTH_LONG).show() ;
			return -1;
		}
		Cursor cursor = Notepad.getDb().getAllPlaces() ;

		Region  reg;
		if(cursor.moveToFirst()){
			do{
				reg = new Region(cursor.getDouble(2), cursor.getDouble(3), cursor.getInt(4));
				if (reg.isInside(loc)){
					Log.d("place", "inside locatoin " + cursor.getInt(0));
					return cursor.getInt(0);				
				}else{
					Log.d("place", "out side locatoin " + cursor.getInt(0));
				}
			}while(cursor.moveToNext());
		}
		return -2;
	}

	public void myDaL(){


		TextView placeLocation = (TextView) myDa.getDialoglayout().findViewById(R.id.location_modes);

		final Button choose_mode = (Button) myDa.getDialoglayout().findViewById(R.id.mode_choses) ;
		choose_mode.setOnClickListener(choose_mode_l) ;
		if (mode_ID!=-1)
		{
			Cursor cursor  = getDb().getModeById(mode_ID) ;
			choose_mode.setText(cursor.getString(1)) ;
		}

		final Button choose_place = (Button) myDa.getDialoglayout().findViewById(R.id.loc_choses) ;
		choose_place.setOnClickListener(choose_place_l);
		if (place_ID!=-1)
		{
			Cursor cursor  = getDb().getPlacesById(place_ID) ;
			choose_place.setText(cursor.getString(1)) ;
		}

		Button searchByMode = (Button) myDa.getDialoglayout().findViewById(R.id.search_by_mode_palce) ;
		searchByMode.setOnClickListener(search_By_mode_tag) ;

		Button searchByCam = (Button) myDa.getDialoglayout().findViewById(R.id.search_by_tag) ;
		searchByCam.setOnClickListener(search_By_cam) ;

		int thisplace = getGPSID();
		if(thisplace<0){
			placeLocation.setText("unknown place");
		}else{
			placeLocation.setText("Place: "+Notepad.getDb().getPlacesById(thisplace).getString(1));
		}

		myDa.getAlertDialog().setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface arg0) {
				photo_ID = -1 ;
				mode_ID =-1 ;
				place_ID = -1 ;
				choose_mode.setText(getResources().getString(R.string.choose_mode));
				choose_place.setText(getResources().getString(R.string.choose_place)) ;			

			}
		});

		myDa.getAlertDialog().show() ;

	}

	public void signout(){
		SessionManager.logoutUser();
	}

}



