package note;

import com.example.layoutt.MyDialog;
import com.example.layoutt.Notepad;
import com.example.layoutt.R;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NoteEditer extends Activity {

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
		tag.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				MyDialog myD = new MyDialog(NoteEditer.this, R.layout.tags_layout) ;
				myD.getAlertDialog().show() ;

			}
		}) ;


		if (getIntent().getExtras().getString("mode").equals("edit")) {
			Cursor cursor = Notepad.getDb().getNote(getIntent().getExtras().getInt("id"));
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
						buttons.Buttons.save(t1.getText().toString().trim(), t2.getText().toString().trim(),Notepad.getDb());
					}
				}
				catch(Exception ex){
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
		});}

}
