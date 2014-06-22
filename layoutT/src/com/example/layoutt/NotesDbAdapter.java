package com.example.layoutt;

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

public class NotesDbAdapter {

	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_BODY = "body";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_DATE = "date";

	public static final String TAG = NotesDbAdapter.class.getSimpleName();
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	private static final String DATABASE_NAME = "notes.db";
	private static final String TABLE_NAME = "notes";
	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_CREATE =
			"create table notes ("+COLUMN_ID+" integer primary key autoincrement, "
					+COLUMN_TITLE+" text not null, "
					+COLUMN_BODY+" text not null , "
					+COLUMN_BODY+" DATETIME not null );";
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

	public NotesDbAdapter(Context context) {
		this.mContext = context;
	}

	public NotesDbAdapter open() throws SQLException {
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
			values.put(COLUMN_TITLE, title) ;
			values.put(COLUMN_BODY, text) ;
			values.put(COLUMN_DATE,getDateTime());
			mDb.insertOrThrow("notes", null, values) ;
			return true ;
		}
		catch (Exception ex){
			return false ;
		}

	}
	public boolean deleteTitle(String title) 
	{
		String where = COLUMN_TITLE+" =?";
		String[] whereArgs = new String[] { title };
		return mDb.delete(TABLE_NAME,
				where, whereArgs) > 0;              
	}
	
	public boolean deleteID(int id) 
	{
		String where = COLUMN_ID+" =?";
		String[] whereArgs = new String[] { String.valueOf(id) };
		return mDb.delete(TABLE_NAME,
				where, whereArgs) > 0;              
	}

	public boolean deleteAll(){
		return mDb.delete(TABLE_NAME, null, null)>0; 
	}
	
	public Cursor getNote(int id){
		String[] FROM = {COLUMN_ID,COLUMN_TITLE,COLUMN_BODY,COLUMN_DATE};

		String [] ide = {String.valueOf(id)} ;
		//mDb.query(distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit, cancellationSignal)
		Cursor cursor = mDb.query(true, TABLE_NAME, FROM, COLUMN_ID +" =?" , ide , null, null,
				null,null);
		if (cursor.getCount()>0){
			cursor.moveToFirst() ;

			return cursor;
		}
		else
			return null ;
	}

	public Cursor getNote(String title) {
		String[] FROM = {COLUMN_ID,COLUMN_TITLE,COLUMN_BODY,COLUMN_DATE};
		String [] titleo = {title} ;
		//mDb.query(distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit, cancellationSignal)

		Cursor cursor = mDb.query(true, TABLE_NAME, FROM, COLUMN_TITLE +" =?" , titleo , null, null,
				null,null);
		if (cursor.getCount()>0){
			cursor.moveToFirst() ;
			return cursor;
		}
		else
			return null ;
	}
	public Cursor getAllNotes(){
		String[] FROM = {COLUMN_ID,COLUMN_TITLE,COLUMN_BODY,COLUMN_DATE};
		Cursor cursor = mDb.query(TABLE_NAME, FROM, null, null, null, null, COLUMN_DATE+" DESC");
		//		mDb.query(table, columns, selection, selectionArgs, groupBy, having, orderBy)

		if (cursor.getCount()>0){
			cursor.moveToFirst() ;

			return cursor;
		}
		else
			return null ;

	}
	public void updateNote(int id,String title,String body){

		try{
			mDb = mDbHelper.getWritableDatabase();
			String where = COLUMN_ID+" =?";
			String[] whereArgs = new String[] { String.valueOf(id) };
			ContentValues initialValues = new ContentValues();
			initialValues.put(COLUMN_TITLE, title);
			initialValues.put(COLUMN_BODY, body);
			initialValues.put(COLUMN_DATE, getDateTime());
			mDb.update(TABLE_NAME, initialValues, where, whereArgs);		 
		}
		catch(Exception ex)
		{

		}

	}


}