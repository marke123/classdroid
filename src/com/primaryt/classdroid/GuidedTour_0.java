package com.primaryt.classdroid;

import java.util.ArrayList;

import com.primaryt.classdroid.bo.Pupil;
import com.primaryt.classdroid.db.DBAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class GuidedTour_0 extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guided_tour_0);

		Button btnYes = (Button) findViewById(R.id.btnYes);
		btnYes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getBaseContext(), GuidedTour_1.class);
				startActivity(intent);
				finish();
			}
		});

		Button btnNo = (Button) findViewById(R.id.btnNo);
		btnNo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				check();
			}
		});
	}

	private void check() {
		DBAdapter adapter = new DBAdapter(this);
		adapter.open();
		ArrayList<Pupil> pupils = adapter.getAllPupils();
		adapter.close();
		if (pupils.size() > 0) {
			Intent intent = new Intent(this, SelectPupilActivity.class);
			startActivity(intent);
		} else if (pupils.size() == 0) {
			Intent intent = new Intent(getBaseContext(),
					DefaultConfigurationActivity.class);
			startActivity(intent);
			finish();
		}
	}

}
