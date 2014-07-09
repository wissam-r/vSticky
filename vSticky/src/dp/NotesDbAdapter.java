package dp;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NotesDbAdapter {

	public static final String MODE_ID = "mode_id";
	public static final String MODE_NAME = "mode_name";
	public static final String PHOTO_ID = "photo_id";
	public static final String PHOTO_PATH = "photo_path";
	public static final String GPS_ID = "gps_id";
	public static final String GPS_long = "gps_long";
	public static final String GPS_lat = "gps_lat";
	public static final String GPS_RADUIS = "gps_raduis" ;
	public static final String GPS_NAME = "gps_name" ;
	public static final String NOTE_TITLE = "note_title";
	public static final String NOTE_BODY = "note_body";
	public static final String NOTE_ID = "note_id";
	public static final String NOTE_DATE = "note_date";

	public static final String TAG = NotesDbAdapter.class.getSimpleName();
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	private static final String DATABASE_NAME = "notes.db";
	private static final String TABLE_NAME[] = {"modes", "photos", "gps","notes"};

	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_CREATE =
			"create table " + TABLE_NAME[0] + " ("+MODE_ID+" integer primary key autoincrement, "
					+MODE_NAME+" text not null); " +
					"create table " + TABLE_NAME[1] + " ("+PHOTO_ID+" integer primary key autoincrement, "
					+PHOTO_PATH+" text not null); " +
					"create table " + TABLE_NAME[2] + " ("+GPS_ID+" integer primary key autoincrement, "
					+GPS_long+" real not null," +GPS_lat+" real not null, " +GPS_RADUIS+" integer not null  ); " +
					"create table " + TABLE_NAME[3] + " ("+NOTE_ID+" integer primary key autoincrement, "
					+NOTE_TITLE+" text not null,"
					+NOTE_BODY+" text not null, "
					+NOTE_DATE+" DATETIME not null,"
					+PHOTO_ID+" integer references "+TABLE_NAME[1]+" ("+ PHOTO_ID+"),"
					+MODE_ID+" integer references "+TABLE_NAME[0]+" ("+ MODE_ID+"),"
					+GPS_ID+" integer references "+TABLE_NAME[2]+" ("+ GPS_ID+"));";
	private static final String Create_Mode_Table  = "create table "+TABLE_NAME[0]+" ("+MODE_ID+" integer primary key autoincrement, "
			+MODE_NAME+" text not null );" ;
	private static final String Create_Photos_Table  = "create table "+TABLE_NAME[1]+" ("+PHOTO_ID+" integer primary key autoincrement, "
			+PHOTO_PATH+" text not null );" ;
	private static final String Create_GPS_Table  = "create table "+TABLE_NAME[2]+" ("+GPS_ID+" integer primary key autoincrement, "
			+GPS_long+" real not null, "+GPS_lat+" real not null, "+GPS_NAME+" text not null, "+GPS_RADUIS+" integer not null );" ;

	private static final String Create_Notes_Table = "create table " + TABLE_NAME[3] + " (" +NOTE_ID+" integer primary key autoincrement, "
			+NOTE_TITLE+" text not null, "
			+NOTE_BODY+" text not null, "
			+NOTE_DATE+" DATETIME not null, "
			+PHOTO_ID+" integer references "+TABLE_NAME[1]+" ("+ PHOTO_ID+"), "
			+MODE_ID+" integer references "+TABLE_NAME[0]+" ("+ MODE_ID+"), "
			+GPS_ID+" integer references "+TABLE_NAME[2]+" ( "+ GPS_ID+" ));";

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
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME[0] + 
					" DROP TABLE IF EXISTS " + TABLE_NAME[1] +
					" DROP TABLE IF EXISTS " + TABLE_NAME[2] +
					" DROP TABLE IF EXISTS " + TABLE_NAME[3] );
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
		mDb.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME[0] ) ;
		mDb.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME[1] ) ;
		mDb.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME[2] ) ;
		mDb.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME[3] ) ;
		return true ;

	}
	public boolean createTable(){
		mDb.execSQL(Create_Mode_Table);	
		mDb.execSQL(Create_Photos_Table);		
		mDb.execSQL(Create_GPS_Table);		
		mDb.execSQL(Create_Notes_Table);		

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
	public boolean insertPhoto(String path){
		try {
			ContentValues values = new ContentValues() ;
			mDb = mDbHelper.getWritableDatabase();
			values.put(PHOTO_PATH, path) ;
			mDb.insertOrThrow(TABLE_NAME[1], null, values) ;
			return true ;
		}
		catch (Exception ex){
			return false ;
		}

	}
	public boolean insertTagedNote(String title , String text , Integer photo_id , Integer gps_id , Integer mode_id) {
		try {
			ContentValues values = new ContentValues() ;
			mDb = mDbHelper.getWritableDatabase();
			values.put(NOTE_TITLE, title) ;
			values.put(NOTE_BODY, text) ;
			values.put(NOTE_DATE,getDateTime());
			values.put(PHOTO_ID, photo_id); 
			values.put(MODE_ID, mode_id) ;
			values.put(GPS_ID, gps_id) ;
			mDb.insertOrThrow(TABLE_NAME[3], null, values) ;
			return true ;
		}
		catch (Exception ex){
			return false ;
		}	

	}
	public boolean insertMode( String modeName){
		try {
			ContentValues values = new ContentValues() ;
			mDb = mDbHelper.getWritableDatabase();
			values.put(MODE_NAME, modeName) ;
			mDb.insertOrThrow(TABLE_NAME[0], null, values) ;
			return true ;
		}
		catch (Exception ex){
			return false ;
		}

	}
	public boolean insertPlace( String place_name ,  Double gpsLong , Double gpsLat , int raduis){
		try {
			ContentValues values = new ContentValues() ;
			mDb = mDbHelper.getWritableDatabase();
			values.put(GPS_NAME, place_name) ;
			values.put(NotesDbAdapter.GPS_long, gpsLong) ;
			values.put(NotesDbAdapter.GPS_lat,gpsLat) ;
			values.put(GPS_RADUIS, raduis) ;
			mDb.insertOrThrow(TABLE_NAME[2], null, values) ;
			return true ;
		}
		catch (Exception ex){
			return false ;
		}

	}

	public boolean deleteMode(int id) 
	{
		String where = MODE_ID+" =?";
		String[] whereArgs = new String[] { String.valueOf(id) };
		return mDb.delete(TABLE_NAME[0],
				where, whereArgs) > 0;              
	}
	public boolean deletePhoto(int id) 
	{
		String where = PHOTO_ID+" =?";
		String[] whereArgs = new String[] { String.valueOf(id) };
		return mDb.delete(TABLE_NAME[1],
				where, whereArgs) > 0;              
	}

	public boolean deletePlace(int id) 
	{
		String where = GPS_ID+" =?";
		String[] whereArgs = new String[] { String.valueOf(id) };
		return mDb.delete(TABLE_NAME[2],
				where, whereArgs) > 0;              
	}
	public boolean deleteNotesByTitle(String title) 
	{
		String where = NOTE_TITLE+" =?";
		String[] whereArgs = new String[] { title };
		return mDb.delete(TABLE_NAME[3],
				where, whereArgs) > 0;              
	}

	public boolean deleteNoteByID(int id) 
	{
		String where = NOTE_ID+" =?";
		String[] whereArgs = new String[] { String.valueOf(id) };
		return mDb.delete(TABLE_NAME[3],
				where, whereArgs) > 0;              
	}

	public boolean deleteAllNotes(){
		return mDb.delete(TABLE_NAME[3], null, null)>0; 
	}

	public Cursor getNoteById(int id){
		String[] FROM = {NOTE_ID,NOTE_TITLE,NOTE_BODY,NOTE_DATE};

		String [] ide = {String.valueOf(id)} ;
		//mDb.query(distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit, cancellationSignal)
		Cursor cursor = mDb.query(true, TABLE_NAME[3], FROM, NOTE_ID +" =?" , ide , null, null,
				null,null);
		if (cursor.getCount()>0){
			cursor.moveToFirst() ;
		}
		return cursor ;
	}
	public Cursor getNoteByTitle(String title) {
		String[] FROM = {NOTE_ID,NOTE_TITLE,NOTE_BODY,NOTE_DATE};
		String [] titleo = {title} ;
		//mDb.query(distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit, cancellationSignal)

		Cursor cursor = mDb.query(true, TABLE_NAME[3], FROM, NOTE_TITLE +" =?" , titleo , null, null,
				null,null);
		if (cursor.getCount()>0){
			cursor.moveToFirst() ;
		}


		return cursor ;
	}
	public Cursor getNotesByDate(){
		String[] FROM = {NOTE_ID,NOTE_TITLE,NOTE_BODY,NOTE_DATE};
		Cursor cursor = mDb.query(TABLE_NAME[3], FROM, null, null, null, null, NOTE_DATE+" DESC");
		//		mDb.query(table, columns, selection, selectionArgs, groupBy, having, orderBy)

		if (cursor.getCount()>0){
			cursor.moveToFirst() ;

		}
		return cursor ;
	}

	public Cursor getNotesByTitle(){
		String[] FROM = {NOTE_ID,NOTE_TITLE,NOTE_BODY,NOTE_DATE};
		Cursor cursor = mDb.query(TABLE_NAME[3], FROM, null, null, null, null, NOTE_TITLE+" ASC");
		//		mDb.query(table, columns, selection, selectionArgs, groupBy, having, orderBy)

		if (cursor.getCount()>0){
			cursor.moveToFirst() ;
		}
		return cursor ;}

	public Cursor getAllNotes(){
		String[] FROM = {NOTE_ID,NOTE_TITLE,NOTE_BODY,NOTE_DATE,MODE_ID,PHOTO_ID,GPS_ID};
		Cursor cursor = mDb.query(TABLE_NAME[3], FROM, null, null, null, null, NOTE_DATE+" DESC");
		//		mDb.query(table, columns, selection, selectionArgs, groupBy, having, orderBy)

		if (cursor.getCount()>0){
			cursor.moveToFirst() ;

		}

		return cursor ;
	}
	public Cursor getAllModes(){
		String[] FROM = {MODE_ID,MODE_NAME};
		Cursor cursor = mDb.query(TABLE_NAME[0], FROM, null, null, null, null, MODE_NAME + " ASC");
		//		mDb.query(table, columns, selection, selectionArgs, groupBy, having, orderBy)

		if (cursor.getCount()>0){
			cursor.moveToFirst() ;

		}

		return cursor ;
	}
	public Cursor getAllPlaces(){
		String[] FROM = {GPS_ID,GPS_NAME,GPS_long,GPS_lat,GPS_RADUIS};
		Cursor cursor = mDb.query(TABLE_NAME[2], FROM, null, null, null, null,null);
		//		mDb.query(table, columns, selection, selectionArgs, groupBy, having, orderBy)
//		 GPS_NAME+" ASC"
		if (cursor.getCount()>0){
			cursor.moveToFirst() ;

		}

		return cursor ;
	}


	public Cursor getAllPhotos(){
		String[] FROM = {PHOTO_ID,PHOTO_PATH};
		Cursor cursor = mDb.query(TABLE_NAME[1], FROM, null, null, null, null, PHOTO_ID+" ASC");
		//		mDb.query(table, columns, selection, selectionArgs, groupBy, having, orderBy)

		if (cursor.getCount()>0){
			cursor.moveToFirst() ;

		}

		return cursor ;
	}

	public Cursor getPhotoById(int id){
		String[] FROM = {PHOTO_ID,PHOTO_PATH};

		String [] ide = {String.valueOf(id)} ;
		//mDb.query(distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit, cancellationSignal)
		Cursor cursor = mDb.query(true, TABLE_NAME[1], FROM, PHOTO_ID +" =?" , ide , null, null,
				null,null);
		if (cursor.getCount()>0){
			cursor.moveToFirst() ;
		}
		return cursor ;
	}
	public Cursor getNoteByPhotoId(int id){
		String[] FROM = {NOTE_ID,NOTE_TITLE,NOTE_BODY,NOTE_DATE,PHOTO_ID,GPS_ID,MODE_ID};

		String [] ide = {String.valueOf(id)} ;
		//mDb.query(distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit, cancellationSignal)
		Cursor cursor = mDb.query(true, TABLE_NAME[3], FROM, PHOTO_ID +" =?" , ide , null, null,
				null,null);
		if (cursor.getCount()>0){
			cursor.moveToFirst() ;
		}
		return cursor ;
	}
	
	public Cursor getNoteByModePlace(int mode_ID, int place_ID){
		String[] FROM = {NOTE_ID,NOTE_TITLE,NOTE_BODY,NOTE_DATE,PHOTO_ID,GPS_ID,MODE_ID};
		String [] ide = {String.valueOf(mode_ID),String.valueOf(place_ID)} ;
		//mDb.query(distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit, cancellationSignal)
		Cursor cursor = mDb.query(true, TABLE_NAME[3], FROM, MODE_ID +" =? "+"and "+ GPS_ID +" =?" , ide , null, null,
				null,null);
		if (cursor.getCount()>0){
			cursor.moveToFirst() ;
		}
		return cursor ;
	}
	public Cursor getNoteByMode(int mode_ID){
		String[] FROM = {NOTE_ID,NOTE_TITLE,NOTE_BODY,NOTE_DATE,PHOTO_ID,GPS_ID,MODE_ID};
		String [] ide = {String.valueOf(mode_ID)} ;
		//mDb.query(distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit, cancellationSignal)
		Cursor cursor = mDb.query(true, TABLE_NAME[3], FROM, MODE_ID +" =? " , ide , null, null,
				null,null);
		if (cursor.getCount()>0){
			cursor.moveToFirst() ;
		}
		return cursor ;
	}
	public Cursor getNoteByPlace(int place_ID){
		String[] FROM = {NOTE_ID,NOTE_TITLE,NOTE_BODY,NOTE_DATE,PHOTO_ID,GPS_ID,MODE_ID};
		String [] ide = {String.valueOf(place_ID)} ;
		//mDb.query(distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit, cancellationSignal)
		Cursor cursor = mDb.query(true, TABLE_NAME[3], FROM, GPS_ID +" =? " , ide , null, null,
				null,null);
		if (cursor.getCount()>0){
			cursor.moveToFirst() ;
		}
		return cursor ;
	}
	
	public Cursor getNoteByModePlacePhoto(Integer mode_ID2, Integer place_ID ,Integer photo_ID2){
		String[] FROM = {NOTE_ID,NOTE_TITLE,NOTE_BODY,NOTE_DATE,PHOTO_ID,GPS_ID,MODE_ID};
		String [] ide = {String.valueOf(mode_ID2),String.valueOf(place_ID),String.valueOf(photo_ID2)} ;
		//mDb.query(distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit, cancellationSignal)
		Cursor cursor = mDb.query(true, TABLE_NAME[3], FROM, MODE_ID +" =? "+"and "+ GPS_ID +" =? "+"and "+ PHOTO_ID +" =?" , ide , null, null,
				null,null);
		if (cursor.getCount()>0){
			cursor.moveToFirst() ;
		}
		return cursor ;
	}
	
	public Cursor getPhotoByPath(String path){
		String[] FROM = {PHOTO_ID,PHOTO_PATH};

		String [] pathes = {path} ;
		//mDb.query(distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit, cancellationSignal)
		Cursor cursor = mDb.query(true, TABLE_NAME[1], FROM, PHOTO_PATH +" =?" , pathes , null, null,
				null,null);
		if (cursor.getCount()>0){
			cursor.moveToFirst() ;
		}
		return cursor ;
	}
	public Cursor getPlacesById(int id){
		String[] FROM = {GPS_ID,GPS_NAME,GPS_long,GPS_lat,GPS_RADUIS};

		String [] ide = {String.valueOf(id)} ;
		//mDb.query(distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit, cancellationSignal)
		Cursor cursor = mDb.query(true, TABLE_NAME[2], FROM, GPS_ID +" =?" , ide , null, null,
				null,null);
		if (cursor.getCount()>0){
			cursor.moveToFirst() ;
		}
		return cursor ;
	}
	public Cursor getPlacesByName(String name){
		String[] FROM = {GPS_ID,GPS_NAME,GPS_long,GPS_lat,GPS_RADUIS};

		String [] namee = {name} ;
		//mDb.query(distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit, cancellationSignal)
		Cursor cursor = mDb.query(true, TABLE_NAME[2], FROM, GPS_NAME +" =?" , namee , null, null,
				null,null);
		if (cursor.getCount()>0){
			cursor.moveToFirst() ;
		}
		return cursor ;
	}
	public Cursor getModeById(int id){
		String[] FROM = {MODE_ID,MODE_NAME};

		String [] ide = {String.valueOf(id)} ;
		//mDb.query(distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit, cancellationSignal)
		Cursor cursor = mDb.query(true, TABLE_NAME[0], FROM, MODE_ID +" =?" , ide , null, null,
				null,null);
		if (cursor.getCount()>0){
			cursor.moveToFirst() ;
		}
		return cursor ;
	}
	public Cursor getModeByName(String name){
		String[] FROM = {MODE_ID,MODE_NAME};

		String [] namee = {name} ;
		//mDb.query(distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit, cancellationSignal)
		Cursor cursor = mDb.query(true, TABLE_NAME[0], FROM, MODE_NAME +" =?" , namee , null, null,
				null,null);
		if (cursor.getCount()>0){
			cursor.moveToFirst() ;
		}
		return cursor ;
	}
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
	public void updateMode(int id,String mode_name){

		try{
			mDb = mDbHelper.getWritableDatabase();
			String where = NOTE_ID+" =?";
			String[] whereArgs = new String[] { String.valueOf(id) };
			ContentValues initialValues = new ContentValues();
			initialValues.put(MODE_NAME, mode_name);

			mDb.update(TABLE_NAME[0], initialValues, where, whereArgs);		 
		}
		catch(Exception ex)
		{

		}
	}
	public void updatePlace(int id,String place_Name){

		try{
			mDb = mDbHelper.getWritableDatabase();
			String where = NOTE_ID+" =?";
			String[] whereArgs = new String[] { String.valueOf(id) };
			ContentValues initialValues = new ContentValues();
			initialValues.put(GPS_NAME, place_Name);

			mDb.update(TABLE_NAME[2], initialValues, where, whereArgs);		 
		}
		catch(Exception ex)
		{

		}
	}
	public void updatePhoto(int id,String path){

		try{
			mDb = mDbHelper.getWritableDatabase();
			String where = NOTE_ID+" =?";
			String[] whereArgs = new String[] { String.valueOf(id) };
			ContentValues initialValues = new ContentValues();
			initialValues.put(PHOTO_PATH, path);

			mDb.update(TABLE_NAME[0], initialValues, where, whereArgs);		 
		}
		catch(Exception ex)
		{

		}
	}
	public void updateTagedNote(int id,String title,String body, int photo_id , int gps_id , int mode_id){

		try{
			mDb = mDbHelper.getWritableDatabase();
			String where = NOTE_ID+" =?";
			String[] whereArgs = new String[] { String.valueOf(id) };
			ContentValues initialValues = new ContentValues();
			initialValues.put(NOTE_TITLE, title);
			initialValues.put(NOTE_BODY, body);
			initialValues.put(NOTE_DATE, getDateTime());
			initialValues.put(PHOTO_ID, photo_id);
			initialValues.put(GPS_ID, gps_id);
			initialValues.put(MODE_ID, mode_id);
			mDb.update(TABLE_NAME[3], initialValues, where, whereArgs);		 
		}
		catch(Exception ex)
		{

		}

	}


}