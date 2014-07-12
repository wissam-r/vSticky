package note;

import gps.GPSProvider;
import gps.NetworkProvider;
import gps.Region;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import com.example.layoutt.R;
import com.example.vsticky.MyDialog;
import com.example.vsticky.Notepad;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupMenu.OnMenuItemClickListener;

public class NoteEditer extends Activity {


	static final int REQUEST_TAKE_PHOTO = 1;
	static final int RESULT_LOAD_IMAGE = 2;
	private File mCurrentPhoto;
	private ImageView myDImVeiw;


	private Integer mode_ID =null ;
	private Integer place_ID =null;
	private Integer photo_ID =null ;
	int modee_ID ;
	int placee_ID ;
	File currentFile ;

	int foundPlace ;

	private boolean pickedPlace = false ;

	MyDialog myD;

	GPSProvider gps;
	NetworkProvider netGPS;

	EditText t1;
	EditText t2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note);



		myD = new MyDialog(NoteEditer.this, R.layout.tags_layout) ;
		t1 = (EditText) findViewById(R.id.titleo);
		t2 = (EditText) findViewById(R.id.conto);
		Button save = (Button) findViewById(R.id.b2);
		Button cancel = (Button) findViewById(R.id.b3);
		Button share = (Button) findViewById(R.id.share) ;
		Button tag = (Button) findViewById(R.id.add_tags) ;
		tag.setOnClickListener(tag_l) ;


		if (getIntent().getExtras().getString("mode").equals("edit")) {
			Cursor cursor = Notepad.getDb().getNoteById(getIntent().getExtras().getInt("id"),Notepad.getUser_ID());
			if (cursor == null)
				return;
			t1.setText(cursor.getString(1));
			t2.setText(cursor.getString(2));
		}

		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (t1.getText().toString().equals("")||t2.getText().toString().equals(""))
				{
					Toast.makeText(getApplicationContext(), "Plz fill the Boxes",
							Toast.LENGTH_LONG).show();
					return ;
				}
				try{
					if (mCurrentPhoto!=null)
					{
						Cursor cursor = Notepad.getDb().getPhotoByPath(mCurrentPhoto.getAbsolutePath());
						if (cursor.getCount()>0)
							photo_ID = cursor.getInt(0) ;
						else
						{
							Notepad.getDb().insertPhoto(mCurrentPhoto.getAbsolutePath()) ;
							cursor = Notepad.getDb().getPhotoByPath(mCurrentPhoto.getAbsolutePath());
							photo_ID = cursor.getInt(0) ;
//							Toast.makeText(getBaseContext(),"photo_ID = "+ photo_ID , Toast.LENGTH_LONG).show() ;
						}
					}
					if (getIntent().getExtras().getString("mode").equals("edit")) {
						//						actions.Buttons.save(getIntent().getExtras().getInt("id"), t1.getText().toString().trim(), t2.getText().toString().trim(),Notepad.getDb());
						Notepad.getDb().updateTagedNote(getIntent().getExtras().getInt("id"),t1.getText().toString(), t2.getText().toString(), photo_ID , place_ID , mode_ID ) ;
						Log.d("update", "id : "+getIntent().getExtras().getInt("id") + " title : " + t1.getText().toString() + " body : "+  t2.getText().toString()
								+" photo : "+ photo_ID + " palce : "+place_ID+ " mode : "+mode_ID) ;

					}
					else {
						if (mCurrentPhoto!=null)
						{
							Cursor cursor = Notepad.getDb().getPhotoByPath(mCurrentPhoto.getAbsolutePath());
							if (cursor.getCount()>0)
								photo_ID = cursor.getInt(0) ;
							else
							{
								Notepad.getDb().insertPhoto(mCurrentPhoto.getAbsolutePath()) ;
								cursor = Notepad.getDb().getPhotoByPath(mCurrentPhoto.getAbsolutePath());
								photo_ID = cursor.getInt(0) ;
//								Toast.makeText(getBaseContext(),"photo_ID = "+ photo_ID , Toast.LENGTH_LONG).show() ;
							}
						}
						//						actions.Buttons.save(t1.getText().toString().trim(), t2.getText().toString().trim(),Notepad.getDb());

						//						Cursor cursor = Notepad.getDb().getPhotoByPath(mCurrentPhoto.getAbsolutePath()) ;
						//						int photo_ID = cursor.getInt(0) ;
						Random roro = new Random() ;
						int x = roro.nextInt() ;
						Cursor testIDC = Notepad.getDb().getAllNotes(Notepad.getUser_ID()) ;
						int i = 0;
						while(i < testIDC.getCount()){
							i++ ;
							if (testIDC.getInt(0)==Math.abs(x) + 1){
								x +=1 ;
								i= 0 ;
								testIDC.moveToFirst() ;
							}
							else
								testIDC.moveToNext() ;
						}
//						Notepad.getDb().insertTagedNote(Math.abs(x) + 1 ,t1.getText().toString(), t2.getText().toString(), photo_ID , place_ID , mode_ID ) ;
						Notepad.getDb().insertTagedNoteUser(Math.abs(x) + 1 ,t1.getText().toString(), t2.getText().toString(), photo_ID , place_ID , mode_ID ,Notepad.getUser_ID()) ;

						//						Toast.makeText(getBaseContext(), cursor.getString(0) +cursor.getString(1)+cursor.getString(2) +cursor.getString(3)+"mode name " +cursor2.getString(1), Toast.LENGTH_LONG).show() ;
					}
				}
				catch(Exception ex){
					ex.printStackTrace() ;
					Toast.makeText(getApplicationContext(), "Error",
							Toast.LENGTH_LONG).show();
					actions.Buttons.done(NoteEditer.this) ;
				}
				Toast.makeText(getApplicationContext(), "Saved",
						Toast.LENGTH_LONG).show();
				actions.Buttons.done(NoteEditer.this) ;
			}
		});

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				actions.Buttons.done(NoteEditer.this) ;
			}
		});

		share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				actions.Buttons.share(t1.getText().toString(), t2.getText().toString(),NoteEditer.this);
			}
		});

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if (myD.getAlertDialog().isShowing()){
			
			outState.putBoolean("myD", true) ;
		}
		else
			outState.putBoolean("myD", false) ;
		outState.putString("title", t1.getText().toString()) ;
		outState.putString("body", t2.getText().toString()) ;
		outState.putSerializable("currentPic", mCurrentPhoto) ;
		//		outState.putSerializable("photo_ID", photo_ID);
		outState.putSerializable("place_ID", place_ID);
		outState.putSerializable("mode_ID", mode_ID);
		outState.putSerializable("pickedPlace", pickedPlace) ;
		outState.putSerializable("placee_ID", placee_ID);
		outState.putSerializable("modee_ID", modee_ID);
		outState.putSerializable("curentFile", currentFile);
		
		




	}
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		t1.setText(savedInstanceState.getString("title"));
		t2.setText(savedInstanceState.getString("body"));

		//		photo_ID = (Integer) savedInstanceState.getSerializable("photo_ID") ;
		place_ID = (Integer) savedInstanceState.getSerializable("place_ID") ;
		mode_ID = (Integer) savedInstanceState.getSerializable("mode_ID") ;
		pickedPlace = savedInstanceState.getBoolean("pickedPlace") ;
		mCurrentPhoto = (File) savedInstanceState.getSerializable("currentPic") ;
		placee_ID = (Integer) savedInstanceState.getSerializable("placee_ID") ;
		modee_ID = (Integer) savedInstanceState.getSerializable("modee_ID") ;
		currentFile = (File) savedInstanceState.getSerializable("currentFile") ;
		if (savedInstanceState.getBoolean("myD")){
			Log.d("restor", "myd") ;
			myDL() ;	
		}



		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK){
			Bitmap b = BitmapFactory.decodeFile(mCurrentPhoto.getAbsolutePath());
			if(b.getWidth()>1921 || b.getHeight()>1281){
				Toast.makeText(this, "size too big, please reduis to max of 1920*1280", Toast.LENGTH_SHORT).show();
			}else{
				myDImVeiw.setImageBitmap(b);
			}			
		}else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {

			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			mCurrentPhoto = new File(cursor.getString(columnIndex));
			cursor.close();
			Bitmap b = BitmapFactory.decodeFile(mCurrentPhoto.getAbsolutePath());
			if(b.getWidth()>1921 || b.getHeight()>1280){
				Toast.makeText(this, "size too big, please reduis to max of 1920*1280", Toast.LENGTH_SHORT).show();
			}else{
				myDImVeiw.setImageBitmap(b);
			}		
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public File getmCurrentPhoto() {
		return mCurrentPhoto;
	}

	public int getMode_ID() {
		return mode_ID;
	}

	public int getPlace_ID() {
		return place_ID;
	}




	OnClickListener tag_l = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			myDL() ;
		}
	};

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

	@Override
	protected void onStart() {
		super.onStart();
		gps = new GPSProvider(this, new TextView(NoteEditer.this)) ;
		netGPS = new NetworkProvider(this, new TextView(NoteEditer.this)) ;
	}

	public void myDL(){
		//		if(t1.getText().toString().isEmpty() || t2.getText().toString().isEmpty())
		//			return ;
		//			myD = new MyDialog(NoteEditer.this, R.layout.tags_layout) ;
		//TODO here u have to declire the text view and add listener to it
		 
		if (mode_ID==null)
			modee_ID =-1;
		else
			modee_ID = mode_ID ;
		if (place_ID==null)
			placee_ID =-1;
		else
			placee_ID = place_ID ;
		if (mCurrentPhoto==null)
			currentFile =null;
		else
			currentFile = mCurrentPhoto ;

		myDImVeiw = (ImageView) myD.getDialoglayout().findViewById(R.id.showImg);
		if (mCurrentPhoto!=null){	
			Bitmap b = BitmapFactory.decodeFile(mCurrentPhoto.getAbsolutePath());
			Log.d("enterd here", "draw") ;
			myDImVeiw.setImageBitmap(b);
			}
		Button takePic = (Button) myD.getDialoglayout().findViewById(R.id.new_pic);
		takePic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dispatchTakePictureIntent();		
			}					

			private File createImageFile() throws IOException {
				// Create an image file name
				String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
				String imageFileName = "JPEG_" + timeStamp + "_";
				File storageDir = Environment.getExternalStoragePublicDirectory(
						Environment.DIRECTORY_PICTURES);
				File image = File.createTempFile(
						imageFileName,  /* prefix */
						".jpg",         /* suffix */
						storageDir      /* directory */
						);

				// Save a file: path for use with ACTION_VIEW intents
				mCurrentPhoto = /*"file:" +*/ image;
				return image;
			}

			private void dispatchTakePictureIntent() {

				Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				// Ensure that there's a camera activity to handle the intent
				if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
					// Create the File where the photo should go
					File photoFile = null;
					try {
						photoFile = createImageFile();
					} catch (IOException ex) {
						// Error occurred while creating the File
						ex.printStackTrace();
					}
					// Continue only if the File was successfully created
					if (photoFile != null) {
						takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
								Uri.fromFile(photoFile));
						startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
					}
				}
			}
		});

		Button loadPic = (Button) myD.getDialoglayout().findViewById(R.id.picfga);
		loadPic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(
						Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, RESULT_LOAD_IMAGE);						
			}
		});

		final TextView usedLocation = (TextView) myD.getDialoglayout().findViewById(R.id.location_mode);


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

				PopupMenu popup = new PopupMenu(NoteEditer.this, arg0);
				for (Mode modee : modes) {
					popup.getMenu().addSubMenu(0, modee.getId(), 0, modee.getName()) ;		
				}
				//			popup.getMenu().addSubMenu(0, mode.getId(), 0, mode.getName()) ;
				OnMenuItemClickListener	modeClick = new OnMenuItemClickListener() {

					@Override
					public boolean onMenuItemClick(MenuItem arg1) {	
						mode_ID = arg1.getItemId() ;
						((Button)arg0).setText(arg1.getTitle());
//						Toast.makeText(getBaseContext(), mode_ID + "", Toast.LENGTH_LONG).show();
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

				PopupMenu popup = new PopupMenu(NoteEditer.this, arg0);
				for (Place placee : places) {
					popup.getMenu().addSubMenu(0, placee.getId(), 0, placee.getName()) ;		
				}
				OnMenuItemClickListener placeClick = new OnMenuItemClickListener() {

					@Override
					public boolean onMenuItemClick(MenuItem arg1) {	
						place_ID = arg1.getItemId() ;
						((Button) arg0).setText(arg1.getTitle());
						pickedPlace=true ;

						return false;
					}

				} ;
				popup.setOnMenuItemClickListener(placeClick) ;
				popup.show() ;


			}
		};




		final Button choose_mode = (Button) myD.getDialoglayout().findViewById(R.id.mode_chose) ;
		choose_mode.setOnClickListener(choose_mode_l) ;
		if (mode_ID!=null)
		{
			Cursor cursor  = Notepad.getDb().getModeById(mode_ID) ;
			choose_mode.setText(cursor.getString(1)) ;
		}

		final Button choose_place = (Button) myD.getDialoglayout().findViewById(R.id.loc_chose) ;
		choose_place.setOnClickListener(choose_place_l);

		Button new_place = (Button) myD.getDialoglayout().findViewById(R.id.new_loc);
		NewPlaceAdder npa = new NewPlaceAdder(NoteEditer.this);
		npa.addOnAddListener(new NewPlaceAdder.onAddListener() {
			
			@Override
			public void onAdd(Place place) {
				usedLocation.setText(place.getName());
				place_ID = place.getId();
			}
		});
		new_place.setOnClickListener(npa) ;

		Button new_mode = (Button) myD.getDialoglayout().findViewById(R.id.add_mode) ;
		NewModeAdder nma = new NewModeAdder(NoteEditer.this);
		nma.addOnAddListener(new NewModeAdder.onAddListener() {
			
			@Override
			public void onAdd(Mode mode) {
				choose_mode.setText(mode.getName());
				mode_ID = mode.getId();				
			}
		});
		new_mode.setOnClickListener(nma) ;

		Button done_tag = (Button) myD.getDialoglayout().findViewById(R.id.done_tag);
		done_tag.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				myD.getAlertDialog().dismiss() ;

			}
		}) ;

		foundPlace = getGPSID();
		Log.d("place","getGPSID :: "+place_ID);
		if(foundPlace>-1){
			pickedPlace = false ;
			new_place.setEnabled(false);
			choose_place.setEnabled(false);
			usedLocation.setText(Notepad.getDb().getPlacesById(foundPlace).getString(1));
			place_ID = foundPlace;
		}else if(foundPlace==-2){//we know our gps locatoin but not where we are
			choose_place.setEnabled(false);
		}

		if ((pickedPlace)&&(place_ID!=null))
		{

			Cursor cursor  = Notepad.getDb().getPlacesById(place_ID) ;
			Log.d("palce_ID", place_ID+"");
			choose_place.setText(cursor.getString(1)) ;

		}
		Button clear_tag = (Button) myD.getDialoglayout().findViewById(R.id.clear_tag) ;
		clear_tag.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mCurrentPhoto = null ;
				place_ID = null ;
				mode_ID = null ;
				choose_mode.setText(getResources().getString(R.string.choose_mode)) ;
				choose_place.setText(getResources().getString(R.string.choose_place)) ;
				myDImVeiw.setImageBitmap(null) ;



			}
		}) ;

	
		myD.getAlertDialog().setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface arg0) {
				// TODO Auto-generated method stub
				if (modee_ID==-1){
					mode_ID = null ;
					choose_mode.setText(getResources().getString(R.string.choose_mode)) ;

				}
				else
					mode_ID = modee_ID ;
				if (placee_ID==-1){
					place_ID = null ;
					choose_place.setText(getResources().getString(R.string.choose_place)) ;
					pickedPlace = false ;
				}
				else
					place_ID = placee_ID ;
				if (currentFile==null){
					mCurrentPhoto = null ;
					Log.d("current", "null") ;
					myDImVeiw.setImageBitmap(null) ;
				}
				else
					mCurrentPhoto = currentFile ;

			}
		});



		myD.getAlertDialog().show() ;
	}
}

class NewModeAdder implements OnClickListener{
	public NewModeAdder(Activity activity){
		this.activity = activity;
	}
	Activity activity;
	@Override
	public void onClick(View v) {
		final MyDialog myD = new MyDialog(activity, R.layout.new_mode) ;
		final EditText mode_name = (EditText) myD.getDialoglayout().findViewById(R.id.new_mode_name) ;
		Button save_mode = (Button) myD.getDialoglayout().findViewById(R.id.save_mode) ;
		save_mode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (!mode_name.getText().toString().equals("")){
					Notepad.getDb().insertMode(mode_name.getText().toString()) ;
					Cursor cursor = Notepad.getDb().getAllModes() ;
//					Toast.makeText(activity, String.valueOf(cursor.getCount()), Toast.LENGTH_LONG).show();
					myD.getAlertDialog().cancel() ;
					cursor = Notepad.getDb().getModeByName(mode_name.getText().toString());
					Mode newmode = new Mode(cursor.getInt(0), cursor.getString(1));
					for(int i = 0;i<listeners.size();i++){
						listeners.get(i).onAdd(newmode);
					}
				}
			}
		}) ;
		myD.getAlertDialog().show() ;

	}

	private List<onAddListener> listeners = new LinkedList<onAddListener>();

	public static interface onAddListener{
		void onAdd(Mode mode);
	}
	
	public void addOnAddListener(onAddListener oal){
		listeners.add(oal);
	}
}

class NewPlaceAdder implements OnClickListener{
	public NewPlaceAdder(Activity activity){
		this.activity = activity;
	}

	Activity activity;
	@Override
	public void onClick(View v) {
		final MyDialog myD = new MyDialog(activity, R.layout.new_place) ;
		TextView currentGps = (TextView) myD.getDialoglayout().findViewById(R.id.current_pos);
		TextView currentGpsNet = (TextView) myD.getDialoglayout().findViewById(R.id.current_posnet);
		Button saveB =  (Button) myD.getDialoglayout().findViewById(R.id.save_place) ;
		final EditText rangeGPS = (EditText) myD.getDialoglayout().findViewById(R.id.range_gps) ;
		final EditText placeName = (EditText) myD.getDialoglayout().findViewById(R.id.name_place) ;


		final GPSProvider gps = new GPSProvider(activity, currentGps) ;
		final NetworkProvider netGPS = new NetworkProvider(activity, currentGpsNet) ;
		final View fv = v;
		saveB.setOnClickListener(new OnClickListener() {
			//TODO look here and use it in the other place
			@Override
			public void onClick(View arg0) {
				Place newplace ;
				final int minRaduis = 10;
				if ((GPSProvider.isGPS_ConToSatil()) && (gps.getLatitude()!=null)&&(gps.getLongitude()!=null))
				{
					try{
						newplace = new Place(-1, placeName.getText().toString(), gps.getLongitude(), gps.getLatitude(), Integer.parseInt(rangeGPS.getText().toString()));
					}catch(NumberFormatException e){
						Toast.makeText(activity, "Plz enter unsigned number net", Toast.LENGTH_LONG).show() ;
						return;
					}
				}
				else if ((NetworkProvider.isInternet_con()) && (netGPS.getLatitude()!=null)&&(netGPS.getLongitude()!=null))
				{
					try{
						newplace = new Place(-1, placeName.getText().toString(), netGPS.getLongitude(), netGPS.getLatitude(), Integer.parseInt(rangeGPS.getText().toString()));
					}catch(NumberFormatException e){
						Toast.makeText(activity, "Plz enter unsigned number net", Toast.LENGTH_LONG).show() ;
						return;
					}
				}else{
//					Toast.makeText(activity, "fail", Toast.LENGTH_LONG).show() ;
					return;
				}

				if ((newplace.getRaduis() >= minRaduis)&&(!newplace.getName().equals("")) ){
					Cursor cursor = Notepad.getDb().getAllPlaces() ;
					Region  reg;

					int i = 0 ;
					while (i < cursor.getCount())
					{
						reg = new Region(cursor.getDouble(2), cursor.getDouble(3), cursor.getInt(4));
						if (reg.isInside(newplace.getRegion()))
						{
							if(reg.isInside(newplace.getRegion().getLoc())){
								Toast.makeText(activity, "fail: you are inside other place: "+cursor.getString(1), Toast.LENGTH_LONG).show();
							}else{
								Toast.makeText(activity, "fail: region collision with "+cursor.getString(1), Toast.LENGTH_LONG).show();
							}
							return ;
						}
						if(cursor.getString(1).equals(newplace.getName())){
							Toast.makeText(activity, "fail: chose other name for this palce", Toast.LENGTH_LONG).show();
							return;
						}
						i++ ;
						cursor.moveToNext() ;
					}
					Notepad.getDb().insertPlace(newplace.getName(), newplace.getX(), newplace.getY(), newplace.getRaduis()) ;
					Toast.makeText(activity, "success", Toast.LENGTH_LONG).show() ;
					myD.getAlertDialog().cancel() ;
					fv.setEnabled(false);
					
					Cursor placec = Notepad.getDb().getPlacesByName(newplace.getName());
					newplace = new Place(placec.getInt(0), placec.getString(1), placec.getDouble(2), placec.getDouble(3), placec.getInt(4));
					for(int k = 0;k<listeners.size();k++){
						listeners.get(k).onAdd(newplace);
					}
				}

			}
		});

		myD.getAlertDialog().show() ;

	}
	
	private List<onAddListener> listeners = new LinkedList<onAddListener>();

	public static interface onAddListener{
		void onAdd(Place place);
	}
	
	public void addOnAddListener(onAddListener oal){
		listeners.add(oal);
	}
}

