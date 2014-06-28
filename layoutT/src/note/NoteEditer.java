package note;

import com.example.layoutt.MainActivity;
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
		Button b1 = (Button) findViewById(R.id.b2);
		Button b2 = (Button) findViewById(R.id.b3);
		Button b3 = (Button) findViewById(R.id.share) ;
		
		if (getIntent().getExtras().getString("mode").equals("edit")) {
			Cursor cursor = MainActivity.getDb().getNote(getIntent().getExtras().getInt("id"));
			if (cursor == null)
				return;
			t1.setText(cursor.getString(1));
			t2.setText(cursor.getString(2));
		}

		b1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try{
					if (getIntent().getExtras().getString("mode").equals("edit")) {
						buttons.Buttons.save(getIntent().getExtras().getInt("id"), t1.getText().toString(), t2.getText().toString(),MainActivity.getDb());
						
					}
					else {
						buttons.Buttons.save(t1.getText().toString(), t2.getText().toString(),MainActivity.getDb());
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

		b2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				buttons.Buttons.done(NoteEditer.this) ;
			}
		});

		b3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				buttons.Buttons.share(t1.getText().toString(), t2.getText().toString(),NoteEditer.this);
			}
		});}

}
