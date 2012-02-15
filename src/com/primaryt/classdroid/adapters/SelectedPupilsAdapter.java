package com.primaryt.classdroid.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.primaryt.classdroid.R;
import com.primaryt.classdroid.bo.Pupil;

public class SelectedPupilsAdapter extends BaseAdapter {
	private ArrayList<Pupil> pupils;
	private Context context;

	public SelectedPupilsAdapter(Context context) {
		this.context = context;
		this.pupils = new ArrayList<Pupil>();
	}

	@Override
	public int getCount() {
		return pupils.size();
	}

	@Override
	public Object getItem(int position) {
		return pupils.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		Pupil pupil = (Pupil) getItem(position);
		if (convertView == null) {
			LayoutInflater infalter = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalter.inflate(R.layout.selected_pupils_adapter,
					null);
		}

		TextView tv = (TextView) convertView.findViewById(R.id.textViewName);
		tv.setText(pupil.getName());

		convertView.setTag(pupil);
		return convertView;
	}
	
	public void addPupil(Pupil pupil){
		pupils.add(pupil);
	}

}
