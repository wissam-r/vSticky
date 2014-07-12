package db;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbAdapter {

	public static final String MODE_ID = "mode_id";
	public static final String MODE_NAME = "mode_name";
	public static final String PHOTO_ID = "photo_id";
	public static final String PHOTO_PATH = "photo_path";
	public static final String GPS_ID = "gps_id";
	public static final String GPS_X = "gps_x";
	public static final String GPS_Y = "gps_y";
	public static final String NOTE_TITLE = "note_title";
	public static final String NOTE_BODY = "note_body";
	public static final String NOTE_ID = "note_id";
	public static final String NOTE_DATE = "note_date";

	public static final String TAG = DbAdapter.class.getSimpleName();
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	private static final String DATABASE_NAME = "notes.db";
	private static final String TABLE_NAME[] = {"modes", "photos", "gps","notes"};
	
	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_CREATE =
			"create table " + TABLE_NAME[0] + "("+MODE_ID+" integer primary key autoincrement, "
					+MODE_NAME+" text not null;" +
		    "create table " + TABLE_NAME[1] + "("+PHOTO_ID+" integer primary key autoincrement, "
		    		+PHOTO_PATH+" text not null;" +
		    "create table " + TABLE_NAME[2] + "("+GPS_ID+" integer primary key autoincrement, "
		    		+GPS_X+" integer not null," +GPS_Y+" integer not null;" +
		    "create table " + TABLE_NAME[3] + "("+NOTE_ID+" integer primary key autoincrement, "
		    		+NOTE_TITLE+" text not null,"
		    		+NOTE_BODY+" text not null , "
		    		+NOTE_DATE+" DATETIME not null )"
		    		+PHOTO_ID+" integer foreign key references("+TABLE_NAME[1]+"),"
		    		+MODE_ID+" integer foreign key references("+TABLE_NAME[0]+"),"
		    		+GPS_ID+" integer foreign key references("+TABLE_NAME[2]+");";

		    
	//	DEFAULT CURRENT_TIMESTAMP

	private final Context mContext;

	public static String getDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		Date date = new Date();
		return dateFormat.format(date);
	}
	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
			Log.d(TAG, "onCreate() database");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS notes");
			onCreate(db);
			Log.d(TAG, "onUpdate() database");
		}
	}

	public DbAdapter(Context context) {
		this.mContext = context;
	}

	public DbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mContext);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public boolean dropTable(){
		mDb.execSQL("DROP TABLE IF EXISTS notes");
		return true ;

	}
	public boolean createTable(){
		mDb.execSQL(DATABASE_CREATE);		
		return true ;
	}
	
	public boolean insertNote(String title , String text){
		try {
			ContentValues values = new ContentValues() ;
			mDb = mDbHelper.getWritableDatabase();
			values.put(NOTE_TITLE, title) ;
			values.put(NOTE_BODY, text) ;
			values.put(NOTE_DATE,getDateTime());
			mDb.insertOrThrow(TABLE_NAME[3], null, values) ;
			return true ;
		}
		catch (Exception ex){
			return false ;
		}

	}
	public boolean deleteTitle(String title) 
	{
		String where = NOTE_TITLE+" =?";
		String[] whereArgs = new String[] { title };
		return mDb.delete(TABLE_NAME[3],
				where, whereArgs) > 0;              
	}
	public boolean deleteID(int id) 
	{
		String where = NOTE_ID+" =?";
		String[] whereArgs = new String[] { String.valueOf(id) };
		return mDb.delete(TABLE_NAME[3],
				where, whereArgs) > 0;              
	}
	public boolean deleteAll(){
		return mDb.delete(TABLE_NAME[3], null, null)>0; 
	}
	public Cursor getNote(int id){
		String[] FROM = {NOTE_ID,NOTE_TITLE,NOTE_BODY,NOTE_DATE};

		String [] ide = {String.valueOf(id)} ;
		//mDb.query(distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit, cancellationSignal)
		Cursor cursor = mDb.query(true, TABLE_NAME[3], FROM, NOTE_ID +" =?" , ide , null, null,
				null,null);
		if (cursor.getCount()>0){
			cursor.moveToFirst() ;

			return cursor;
		}
		else
			return null ;
	}
	public Cursor getNote(String title) {
		String[] FROM = {NOTE_ID,NOTE_TITLE,NOTE_BODY,NOTE_DATE};
		String [] titleo = {title} ;
		//mDb.query(distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit, cancellationSignal)

		Cursor cursor = mDb.query(true, TABLE_NAME[3], FROM, NOTE_TITLE +" =?" , titleo , null, null,
				null,null);
		if (cursor.getCount()>0){
			cursor.moveToFirst() ;
			return cursor;
		}
		else
			return null ;
	}
	public Cursor getAllNotes(){
		String[] FROM = {NOTE_ID,NOTE_TITLE,NOTE_BODY,NOTE_DATE};
		Cursor cursor = mDb.query(TABLE_NAME[3], FROM, null, null, null, null, NOTE_DATE+" DESC");
		//		mDb.query(table, columns, selection, selectionArgs, groupBy, having, orderBy)

		if (cursor.getCount()>0){
			cursor.moveToFirst() ;

		}
		return cursor ;}
	public void updateNote(int id,String title,String body){

		try{
			mDb = mDbHelper.getWritableDatabase();
			String where = NOTE_ID+" =?";
			String[] whereArgs = new String[] { String.valueOf(id) };
			ContentValues initialValues = new ContentValues();
			initialValues.put(NOTE_TITLE, title);
			initialValues.put(NOTE_BODY, body);
			initialValues.put(NOTE_DATE, getDateTime());
			mDb.update(TABLE_NAME[3], initialValues, where, whereArgs);		 
		}
		catch(Exception ex)
		{

		}

	}


}