package com.example.vsticky;

import java.util.ArrayList;

import note.Mode;
import note.Note;
import note.Photo;
import note.Place;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import auth.ServerApi;

public class SyncService extends Service {
	private final static String TAG = "SYNC::";

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		Log.d(TAG, "onCreate");
	}

	private void syncNotes() {
		// Delete notes from server
		int i = 0;
		Cursor cursor1 = Notepad.getDb().getAllDeletedNotes();
		while (i < cursor1.getCount()) {
			ServerApi.delNote(cursor1.getInt(0));
			cursor1.moveToNext();
			i++;
		}
		// Get Locally and Server stored notes ids
		ArrayList<Integer> storedRemotely = ServerApi.getNotesIds();
		Cursor cursor2 = Notepad.getDb().getAllNotes(Notepad.getUser_ID());
		ArrayList<Integer> storedLocally = new ArrayList<Integer>();
		i = 0;
		while (i < cursor2.getCount()) {
			storedLocally.add(cursor2.getInt(0));
			cursor2.moveToNext();
			i++;
		}
		ArrayList<Integer> tmp = (ArrayList<Integer>) storedLocally.clone();
		storedLocally.removeAll(storedRemotely);
		storedRemotely.removeAll(tmp);
		// Send local notes to server
		for (Integer id : storedLocally) {
			Cursor n = Notepad.getDb().getNoteById(id,Notepad.getUser_ID());
			ServerApi.sendNote(n.getInt(0), n.getString(1), n.getString(2),
					n.getString(3), n.getInt(4), n.getInt(5), n.getInt(6));
		}
		// Get remote notes from server
		for (Integer id : storedRemotely) {
			Note note = ServerApi.getNote(id);
			Notepad.getDb().insertTagedNoteUser(note.getId(),note.getT(),note.getC() , note.getPhoto_ID(), note.getPlace_ID(), note.getMode_ID(),ServerApi.userId);
		}
	}

	private void syncModes() {
		// Delete modes from server
		int i = 0;
//		Cursor cursor1 = Notepad.getDb().getAllDeletedModes();
//		while (i < cursor1.getCount()) {
//			ServerApi.delMode(cursor1.getInt(0));
//			cursor1.moveToNext();
//			i++;
//		}
		// Get Locally and Server stored modes ids
		ArrayList<Integer> storedRemotely = ServerApi.getModesIds();
		Cursor cursor2 = Notepad.getDb().getAllModes();
		ArrayList<Integer> storedLocally = new ArrayList<Integer>();
		i = 0;
		while (i < cursor2.getCount()) {
			storedLocally.add(cursor2.getInt(0));
			cursor2.moveToNext();
			i++;
		}
		ArrayList<Integer> tmp = (ArrayList<Integer>) storedLocally.clone();
		storedLocally.removeAll(storedRemotely);
		storedRemotely.removeAll(tmp);
		// Send local modes to server
		for (Integer id : storedLocally) {
			Cursor n = Notepad.getDb().getModeById(id);
			ServerApi.sendMode(n.getInt(0), n.getString(1));
		}
		// Get remote modes from server
		for (Integer id : storedRemotely) {
			Mode mode = ServerApi.getMode(id);
			//Notepad.getDb().insertMode(mode.getId(),mode.getName());
			Notepad.getDb().insertMode(mode.getName());
		}
		
	}

	private void syncPlaces() {
		// Delete places from server
		int i = 0;
//		Cursor cursor1 = Notepad.getDb().getAllDeletedPlaces();
//		while (i < cursor1.getCount()) {
//			ServerApi.delPlace(cursor1.getInt(0));
//			cursor1.moveToNext();
//			i++;
//		}
		// Get Locally and Server stored notes ids
		ArrayList<Integer> storedRemotely = ServerApi.getPlacesIds();
		Cursor cursor2 = Notepad.getDb().getAllPlaces();
		ArrayList<Integer> storedLocally = new ArrayList<Integer>();
		i = 0;
		while (i < cursor2.getCount()) {
			storedLocally.add(cursor2.getInt(0));
			cursor2.moveToNext();
			i++;
		}
		ArrayList<Integer> tmp = (ArrayList<Integer>) storedLocally.clone();
		storedLocally.removeAll(storedRemotely);
		storedRemotely.removeAll(tmp);
		// Send local places to server
		for (Integer id : storedLocally) {
			Cursor n = Notepad.getDb().getPlacesById(id);
			ServerApi.sendPlace(n.getInt(0), n.getString(1), n.getDouble(2),
					n.getDouble(3), n.getDouble(4));
		}
		// Get remote places from server
		for (Integer id : storedRemotely) {
			Place place = ServerApi.getPlace(id);
			//Notepad.getDb().insertPlace(place.getId(),place.getName() , place.getX(), place.getY(), place.getRaduis());
			Notepad.getDb().insertPlace(place.getName() , place.getX(), place.getY(), place.getRaduis());
		}
	}

	private void syncPhotos() {
		// Delete photos from server
		int i = 0;
//		Cursor cursor1 = Notepad.getDb().getAllDeletedPhotos();
//		while (i < cursor1.getCount()) {
//			ServerApi.delPhoto(cursor1.getInt(0));
//			cursor1.moveToNext();
//			i++;
//		}
		// Get Locally and Server stored photos ids
		ArrayList<Integer> storedRemotely = ServerApi.getPhotosIds();
		Cursor cursor2 = Notepad.getDb().getAllPhotos();
		ArrayList<Integer> storedLocally = new ArrayList<Integer>();
		i = 0;
		while (i < cursor2.getCount()) {
			storedLocally.add(cursor2.getInt(0));
			cursor2.moveToNext();
			i++;
		}
		ArrayList<Integer> tmp = (ArrayList<Integer>) storedLocally.clone();
		storedLocally.removeAll(storedRemotely);
		storedRemotely.removeAll(tmp);
		// Send local photos to server
		for (Integer id : storedLocally) {
			Cursor n = Notepad.getDb().getPhotoById(id);
			ServerApi.sendPhoto(n.getInt(0), n.getString(1));
		}
		// Get remote photos from server
		for (Integer id : storedRemotely) {
			Photo photo = ServerApi.getPhoto(id);
			//Notepad.getDb().insertPhoto(photo.getId(),photo.getPath());
			Notepad.getDb().insertPhoto(photo.getPath());
		}
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Toast.makeText(this, "Sync Started !", Toast.LENGTH_LONG).show();
		syncNotes();
		syncPlaces();
		syncModes();
		syncPhotos();
		Log.d(TAG, "onStart");
	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, "MyService Stopped", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onDestroy");
	}
}
