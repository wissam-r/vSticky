package note;

import java.io.File;

import com.example.layoutt.R;
import com.example.vsticky.Delete_Activity;
import com.example.vsticky.MyDialog;
import com.example.vsticky.Notepad;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupMenu.OnMenuItemClickListener;

public class ViewNote extends Activity{

	int id ;
	TextView title ;
	TextView body ;
	Button overflow  ;
	Integer photo_ID ;
	Integer	mode_ID ;
	Integer place_ID ;
	String mode_name ;
	String place_name ;
	Double latitude ;
	Double longitude ;
	File currentPhoto  ;
	MyDialog myD;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note_view);
		myD = new MyDialog(ViewNote.this, R.layout.details) ;
		Button back  = (Button) findViewById(R.id.backv) ;
		Button delete  = (Button) findViewById(R.id.deletev) ;
		Button share  = (Button) findViewById(R.id.sharev) ;
		overflow = (Button) findViewById(R.id.view_note_overflow) ;


		title = (TextView) findViewById(R.id.titlev);
		body = (TextView) findViewById(R.id.contentv);
		TextView date = (TextView) findViewById(R.id.datev);
		id = getIntent().getExtras().getInt("id");
		Cursor cursor = Notepad.getDb().getNoteById(id);
		if (cursor == null)
			return;
		title.setText(cursor.getString(1));
		body.setText(cursor.getString(2));
		date.setText(cursor.getString(3));
		mode_ID = cursor.getInt(4) ;
		photo_ID = cursor.getInt(5) ;
		place_ID = cursor.getInt(6) ;
		if (photo_ID>0){
			cursor = Notepad.getDb().getPhotoById(photo_ID) ;
			currentPhoto =  new File(cursor.getString(1)) ;
		}
		if (mode_ID>0){
			cursor =  Notepad.getDb().getModeById(mode_ID);
			mode_name = cursor.getString(1) ;
		}
		if (place_ID>0){
			cursor =  Notepad.getDb().getPlacesById(place_ID);
			place_name = cursor.getString(1) ;
			longitude = cursor.getDouble(2) ;
			latitude =cursor.getDouble(3) ;
		}
		
		
		Toast.makeText(getBaseContext(), "mode : "+mode_ID+"  photo : "
				+photo_ID+"  palce :  "+place_ID, Toast.LENGTH_LONG).show();
		

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				actions.Buttons.done(ViewNote.this) ;

			}
		}) ;
		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				actions.Buttons.delete(id, Notepad.getDb()) ;
				actions.Buttons.done(ViewNote.this) ;
			}
		}) ;
		share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				actions.Buttons.share(title.getText().toString(), body.getText().toString(), ViewNote.this) ;

			}
		}) ;
		overflow.setOnClickListener(overflow_L) ;
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if (myD.getAlertDialog().isShowing())
			outState.putBoolean("myD", true) ;
		else
			outState.putBoolean("myD", false) ;
		outState.putSerializable("currentFile", currentPhoto) ;
		outState.putSerializable("place_ID", place_ID);
		outState.putSerializable("mode_ID", mode_ID);
		
	};
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		place_ID = (Integer) savedInstanceState.getSerializable("place_ID") ;
		mode_ID = (Integer) savedInstanceState.getSerializable("mode_ID") ;
		currentPhoto = (File) savedInstanceState.getSerializable("currentFile") ;
		if (savedInstanceState.getBoolean("myD")){
			Log.d("restor", "myd") ;
			myDL() ;	
		}
		super.onRestoreInstanceState(savedInstanceState);
	}

	OnClickListener overflow_L =  new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			PopupMenu popup = new PopupMenu(ViewNote.this, overflow);  
			popup.getMenu().addSubMenu(0, 1, 0, "Details") ;	
			popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

				@Override
				public boolean onMenuItemClick(MenuItem arg0) {

					if(arg0.getItemId()==1){
						myDL() ;	
					}

					return false;
				}
			});
			popup.show();

		}
	};
	
	public void myDL(){
		ImageView myDImVeiw = (ImageView) myD.getDialoglayout().findViewById(R.id.photo_d);
		if (currentPhoto!=null){
			Bitmap b = BitmapFactory.decodeFile(currentPhoto.getAbsolutePath());
			myDImVeiw.setImageBitmap(b);
		}
		TextView mode_TV = (TextView) myD.getDialoglayout().findViewById(R.id.mode_d) ;
		mode_TV.setText("Mode : "+mode_name) ;
		TextView place_TV = (TextView) myD.getDialoglayout().findViewById(R.id.place_d) ;
		place_TV.setText("place : "+place_name + "\n" + 
		"	Latitude : " + latitude + "\n" +  
		"	Longitude : "+ longitude) ;
		myD.getAlertDialog().show() ;
	}
	
}
