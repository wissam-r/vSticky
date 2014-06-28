package com.example.layoutt;

import java.util.ArrayList;

import note.Note;

import android.app.Activity;
//import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class listAdap extends BaseAdapter{

	private ArrayList<Note> mData = new ArrayList<Note>();
	private ArrayList<String> times = new ArrayList<String>();
	private LayoutInflater mInflater;

	public listAdap(Context context, int resource) {

		// TODO Auto-generated constructor stub
		mInflater =  ((Activity)context).getLayoutInflater();
	}
	@Override
	public View getView(int position , View convertV, ViewGroup parent ){		
		
		ViewHolder holder = null;
		if (convertV == null) {
			convertV = mInflater.inflate(R.layout.row, null);
			holder = new ViewHolder();
			holder.title = (TextView)convertV.findViewById(R.id.titleId);
			holder.context = (TextView)convertV.findViewById(R.id.titleCon);
			holder.time = (TextView)convertV.findViewById(R.id.date) ;

			convertV.setTag(holder);
		} else {
			holder = (ViewHolder)convertV.getTag();
		}
		holder.title.setText( mData.get(position).getT());
		holder.context.setText( mData.get(position).getC());
		holder.time.setText(times.get(position));
		return convertV;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mData.get(arg0);
	}
	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return  arg0;
	}
	public void add(Note note,String time){
		mData.add(note);
		times.add(time);
	}


}

class ViewHolder {
	public TextView title;
	public TextView context;
	public TextView time ;
}
