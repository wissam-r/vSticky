package note;

import gps.GPSProvider;
import gps.NetworkProvider;
import gps.Region;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import com.example.layoutt.MyDialog;
import com.example.layoutt.Notepad;
import com.example.layoutt.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor.OnCloseListener;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupMenu.OnMenuItemClickListener;

public class NoteEditer extends Activity {


	static final int REQUEST_TAKE_PHOTO = 1;
	static final int RESULT_LOAD_IMAGE = 2;
	private File mCurrentPhoto;
	private ImageView myDImVeiw;
	private int saving_type  =0;


	private Integer mode_ID =null ;
	private Integer place_ID =null;
	private Integer photo_ID =null ;

	MyDialog myD;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note);

		final EditText t1 = (EditText) findViewById(R.id.titleo);
		final EditText t2 = (EditText) findViewById(R.id.conto);
		Button save = (Button) findViewById(R.id.b2);
		Button cancel = (Button) findViewById(R.id.b3);
		Button share = (Button) findViewById(R.id.share) ;
		Button tag = (Button) findViewById(R.id.add_tags) ;
		tag.setOnClickListener(tag_l) ;


		if (getIntent().getExtras().getString("mode").equals("edit")) {
			Cursor cursor = Notepad.getDb().getNoteById(getIntent().getExtras().getInt("id"));
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
					if (getIntent().getExtras().getString("mode").equals("edit")) {
						buttons.Buttons.save(getIntent().getExtras().getInt("id"), t1.getText().toString().trim(), t2.getText().toString().trim(),Notepad.getDb());

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
								Toast.makeText(getBaseContext(),"photo_ID = "+ photo_ID , Toast.LENGTH_LONG).show() ;
							}
						}
						//						buttons.Buttons.save(t1.getText().toString().trim(), t2.getText().toString().trim(),Notepad.getDb());

						//						Cursor cursor = Notepad.getDb().getPhotoByPath(mCurrentPhoto.getAbsolutePath()) ;
						//						int photo_ID = cursor.getInt(0) ;
						Notepad.getDb().insertTagedNote(t1.getText().toString(), t2.getText().toString(), photo_ID , place_ID , mode_ID ) ;
						Cursor cursor   = Notepad.getDb().getAllNotes() ;
						Cursor cursor2 = Notepad.getDb().getModeById(cursor.getInt(4)) ;

						Toast.makeText(getBaseContext(), cursor.getString(0) +cursor.getString(1)+cursor.getString(2) +cursor.getString(3)+"mode name " +cursor2.getString(1), Toast.LENGTH_LONG).show() ;
					}
				}
				catch(Exception ex){
					ex.printStackTrace() ;
					Toast.makeText(getApplicationContext(), "Error",
							Toast.LENGTH_LONG).show();
					buttons.Buttons.done(NoteEditer.this) ;
				}
				Toast.makeText(getApplicationContext(), "Saved",
						Toast.LENGTH_LONG).show();
				buttons.Buttons.done(NoteEditer.this) ;
			}
		});

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				buttons.Buttons.done(NoteEditer.this) ;
			}
		});

		share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				buttons.Buttons.share(t1.getText().toString(), t2.getText().toString(),NoteEditer.this);
			}
		});

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK){
			//BitmapFactory.Options opts = new Options();
			//opts.inSampleSize = 4;
			//			opts.outWidth = myDImVeiw.getWidth();
			//			opts.outHeight = myDImVeiw.getHeight();
			//Bitmap b = BitmapFactory.decodeFile(mCurrentPhoto.getAbsolutePath(), opts);
			Bitmap b = BitmapFactory.decodeFile(mCurrentPhoto.getAbsolutePath());
			if(b.getWidth()>1921 || b.getHeight()>1281){
				Toast.makeText(this, "size too big, please reduis to max of 1920*1280", Toast.LENGTH_SHORT).show();
			}else{
				myDImVeiw.setImageBitmap(b);
			}			
		}else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {

			Uri selectedImage = data.getData();
			//myDImVeiw.setImageURI(selectedImage);
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

	OnClickListener add_place = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			new_place() ;

		}
	};
	OnClickListener add_mode = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			new_mode();
		}
	};
	OnClickListener choose_mode_l = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
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
			popup.setOnMenuItemClickListener(modeClick) ;

			popup.show() ;

		}
	};
	OnClickListener choose_place_l = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
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
			//			popup.getMenu().addSubMenu(0, mode.getId(), 0, mode.getName()) ;
			popup.setOnMenuItemClickListener(placeClick) ;
			popup.show() ;


		}
	};



	public File getmCurrentPhoto() {
		return mCurrentPhoto;
	}

	public int getMode_ID() {
		return mode_ID;
	}

	public int getPlace_ID() {
		return place_ID;
	}

	public void new_mode(){
		MyDialog myD = new MyDialog(NoteEditer.this, R.layout.new_mode) ;
		final EditText mode_name = (EditText) myD.getDialoglayout().findViewById(R.id.new_mode_name) ;
		Button save_mode = (Button) myD.getDialoglayout().findViewById(R.id.save_mode) ;
		save_mode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (!mode_name.getText().toString().equals("")){
					Notepad.getDb().insertMode(mode_name.getText().toString()) ;
					Cursor cursor = Notepad.getDb().getAllModes() ;
					Toast.makeText(getBaseContext(), String.valueOf(cursor.getCount()), Toast.LENGTH_LONG).show();
				}
			}
		}) ;
		myD.getAlertDialog().show() ;
	}

	public void new_place(){
		MyDialog myD = new MyDialog(NoteEditer.this, R.layout.new_place) ;
		TextView currentGps = (TextView) myD.getDialoglayout().findViewById(R.id.current_pos);
		TextView currentGpsNet = (TextView) myD.getDialoglayout().findViewById(R.id.current_posnet);
		Button saveB =  (Button) myD.getDialoglayout().findViewById(R.id.save_place) ;
		final EditText rangeGPS = (EditText) myD.getDialoglayout().findViewById(R.id.range_gps) ;
		final EditText placeName = (EditText) myD.getDialoglayout().findViewById(R.id.name_place) ;


		final GPSProvider gps = new GPSProvider(NoteEditer.this, currentGps) ;
		final NetworkProvider netGPS = new NetworkProvider(NoteEditer.this, currentGpsNet) ;

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
					Cursor cursor = Notepad.getDb().getAllPlaces() ;
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
					Notepad.getDb().insertPlace(newplace.getName(), newplace.getX(), newplace.getY(), newplace.getRaduis()) ;
					Toast.makeText(getBaseContext(), "success", Toast.LENGTH_LONG).show() ;
				}

			}
		});

		myD.getAlertDialog().show() ;
	}

	OnMenuItemClickListener placeClick = new OnMenuItemClickListener() {

		@Override
		public boolean onMenuItemClick(MenuItem arg0) {	
			place_ID = arg0.getItemId() ;
			return false;
		}

	} ;
	OnMenuItemClickListener	modeClick = new OnMenuItemClickListener() {

		@Override
		public boolean onMenuItemClick(MenuItem arg0) {	
			mode_ID = arg0.getItemId() ;
			Toast.makeText(getBaseContext(), mode_ID + "", Toast.LENGTH_LONG).show();
			return false;
		}

	} ;


	OnClickListener tag_l = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			//			myD = new MyDialog(NoteEditer.this, R.layout.tags_layout) ;
			myDImVeiw = (ImageView) myD.getDialoglayout().findViewById(R.id.showImg);
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
			Button new_place = (Button) myD.getDialoglayout().findViewById(R.id.new_loc);
			new_place.setOnClickListener(add_place) ;
			Button new_mode = (Button) myD.getDialoglayout().findViewById(R.id.add_mode) ;
			new_mode.setOnClickListener(add_mode) ;
			Button choose_mode = (Button) myD.getDialoglayout().findViewById(R.id.mode_chose) ;
			choose_mode.setOnClickListener(choose_mode_l) ;
			Button choose_place = (Button) myD.getDialoglayout().findViewById(R.id.loc_chose) ;
			choose_place.setOnClickListener(choose_place_l);
			Button done_tag = (Button) myD.getDialoglayout().findViewById(R.id.done_tag);
			done_tag.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (mode_ID!=null )
						saving_type+=1 ;
					if (place_ID!=null)
						saving_type+=2 ;
					if (mCurrentPhoto!= null)
						saving_type+=4  ;
					myD.getAlertDialog().cancel() ;

				}
			}) ;
			myD.getAlertDialog().show() ;
		}
	};


	@Override
	protected void onStart() {
		myD = new MyDialog(NoteEditer.this, R.layout.tags_layout) ;
		super.onStart();
	}

}


