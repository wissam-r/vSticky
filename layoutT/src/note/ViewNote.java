package note;

import com.example.layoutt.Notepad;
import com.example.layoutt.R;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ViewNote extends Activity{

	int id ;
	TextView t1 ;
	TextView t2 ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note_view);
		Button b1  = (Button) findViewById(R.id.backv) ;
		Button b2  = (Button) findViewById(R.id.deletev) ;
		Button b3  = (Button) findViewById(R.id.sharev) ;


		t1 = (TextView) findViewById(R.id.titlev);
		t2 = (TextView) findViewById(R.id.contentv);
		TextView t3 = (TextView) findViewById(R.id.datev);
		id = getIntent().getExtras().getInt("id");
		Cursor cursor = Notepad.getDb().getNote(id);
		if (cursor == null)
			return;
		t1.setText(cursor.getString(1));
		t2.setText(cursor.getString(2));
		t3.setText(cursor.getString(3));

		b1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				buttons.Buttons.done(ViewNote.this) ;

			}
		}) ;
		b2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				buttons.Buttons.delete(id, Notepad.getDb()) ;
				buttons.Buttons.done(ViewNote.this) ;
			}
		}) ;
		b3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				buttons.Buttons.share(t1.getText().toString(), t2.getText().toString(), ViewNote.this) ;

			}
		}) ;




	}

}
