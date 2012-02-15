package com.primaryt.classdroid;

import java.util.ArrayList;

import com.primaryt.classdroid.adapters.SelectedPupilsAdapter;
import com.primaryt.classdroid.bo.Pupil;
import com.primaryt.classdroid.db.DBAdapter;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class SelectPupilsDialog extends Dialog implements OnItemClickListener {
	
	private SelectedPupilsAdapter adapter;
	
	private Pupil selectedPupil;

	public SelectPupilsDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		init();
	}

	public SelectPupilsDialog(Context context, int theme) {
		super(context, theme);
		init();
	}

	public SelectPupilsDialog(Context context) {
		super(context);
		init();
	}

	private void init() {
		setContentView(R.layout.select_pupils_dialog);
		setTitle(R.string.lab_add_another_pupil);
		ListView listView = (ListView)findViewById(R.id.listViewPupils);
		adapter = new SelectedPupilsAdapter(getContext());
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}

	public void setSelectedPupils(ArrayList<Pupil> pupils) {
		ArrayList<Pupil> notSelectedPupils = new ArrayList<Pupil>();
		DBAdapter adapter = new DBAdapter(getContext());
		adapter.open();
		ArrayList<Pupil> allPupils = adapter.getAllPupils();
		adapter.close();
		for (Pupil pupil : allPupils) {
			boolean found = false;
			for (Pupil p : pupils) {
				if (p.getId() == pupil.getId()) {
					found = true;
				}
			}
			if (!found) {
				notSelectedPupils.add(pupil);
			}
		}
		
		for(Pupil p: notSelectedPupils){
			this.adapter.addPupil(p);
			this.adapter.notifyDataSetChanged();
		}
		Log.i("TAG", notSelectedPupils.toString());
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long index) {
		selectedPupil = (Pupil) view.getTag();
		dismiss();
	}

	public Pupil getSelectedPupil() {
		return selectedPupil;
	}
	
	
}
