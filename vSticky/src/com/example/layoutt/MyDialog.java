package com.example.layoutt;

import java.io.Serializable;

import org.apache.http.entity.SerializableEntity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

public class MyDialog {
	View dialoglayout ;
	final AlertDialog alertDialog ;
	public AlertDialog getAlertDialog() {
		return alertDialog;
	}
	public View getDialoglayout() {
		return dialoglayout;
	}
	public MyDialog(Activity activity , int layoutId) {
		Context context = activity ;
		AlertDialog.Builder builder = new Builder(context) ;
		final FrameLayout frameView = new FrameLayout(context);
		builder.setView(frameView);
		alertDialog = builder.create()  ;
		LayoutInflater inflater = alertDialog.getLayoutInflater();
		dialoglayout = inflater.inflate(layoutId, frameView);
	}

}
