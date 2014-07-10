package buttons;



import dp.NotesDbAdapter;

import android.app.Activity;
import android.content.Intent;

public class Buttons {

	public static void share(String t1 , String t2 , Activity activity) {
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND); 
		sharingIntent.setType("text/plain");
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, t1);
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, t2);
		activity.startActivity(Intent.createChooser(sharingIntent, "Share via"));
	}

	public static void save(String t1 , String t2 ,NotesDbAdapter dp){
//		dp.insertNote(t1, t2);
	}

	public static void save(int id, String t1 , String t2  ,NotesDbAdapter dp){
		//		dp.updateNote(id, t1, t2);
	}

	public static void done(Activity activity) {
		activity.finish() ;
	}

	public static void delete(int id , NotesDbAdapter dp ){
		dp.deleteNoteByID(id) ;
	}
}
