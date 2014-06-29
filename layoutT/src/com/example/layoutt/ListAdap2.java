package com.example.layoutt;

import java.util.ArrayList;

import note.Note;

import android.app.Activity;
//import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class ListAdap2 extends BaseAdapter{

	private ArrayList<Note> mData = new ArrayList<Note>();
	private ArrayList<String> times = new ArrayList<String>();
	private ArrayList<Boolean> checked =new  ArrayList<Boolean>();

	public ArrayList<Boolean> getChecked() {
		return checked;
	}
	private LayoutInflater mInflater;

	public ListAdap2(Context context, int resource) {

		// TODO Auto-generated constructor stub
		mInflater =  ((Activity)context).getLayoutInflater();
	}
	@Override
	public View getView(final int position , View convertV, ViewGroup parent ){		
		
		ViewHolder2 holder = null;
		if (convertV == null) {
			convertV = mInflater.inflate(R.layout.check_row, null);
			holder = new ViewHolder2();
			holder.title = (TextView)convertV.findViewById(R.id.titleId);
			holder.context = (TextView)convertV.findViewById(R.id.titleCon);
			holder.time = (TextView)convertV.findViewById(R.id.date) ;
			holder.check = (CheckBox) convertV.findViewById(R.id.textViewc) ;
			holder.check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					// TODO Auto-generated method stub
					checked.set(position, true) ;
				}
			}) ;

			convertV.setTag(holder);
		} else {
			holder = (ViewHolder2)convertV.getTag();
		}
		holder.title.setText( mData.get(position).getT());
		holder.context.setText( mData.get(position).getC());
		holder.time.setText(times.get(position));
		holder.check.setChecked(checked.get(position));
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
	public void add(Note note,String time,boolean check){
		mData.add(note);
		times.add(time);
		checked.add(check);
		
	}
	
	


}

class ViewHolder2 {
	public TextView title;
	public TextView context;
	public TextView time ;
	public CheckBox check ;
}
