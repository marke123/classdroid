package com.primaryt.classdroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

import com.primaryt.classdroid.bo.Pupil;
import com.primaryt.classdroid.bo.PupilServices;
import com.primaryt.classdroid.db.DBAdapter;

public class EditPupilActivity extends ClassdroidActivity {

	private int pupilId;
	private Pupil pupil;
	private EditText editName;

	private final static int REQ_IMAGE_TO_PORTFOLIO = 1;
	private final static int REQ_XPARENA_CONFIG = 2;
	private PupilServices servicePrimaryBlogger;
	private PupilServices serviceXPArena;
	private CheckBox cbImageToPortfolio;
	private CheckBox cbXPArena;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_pupil);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			pupilId = bundle.getInt("id");
		}
		DBAdapter dbAdapter = new DBAdapter(this);
		dbAdapter.open();
		pupil = dbAdapter.getPupilById(pupilId);
		servicePrimaryBlogger = dbAdapter.getPupilServiceByPupilId(pupilId,
				PupilServices.TYPE_PRIMARYBLOGGER);
		serviceXPArena = dbAdapter.getPupilServiceByPupilId(pupilId,
				PupilServices.TYPE_XPARENA);
		dbAdapter.close();

		initializeUIElements();
	}

	@Override
	protected void onResume() {
		super.onResume();
		DBAdapter dbAdapter = new DBAdapter(this);
		dbAdapter.open();
		servicePrimaryBlogger = dbAdapter.getPupilServiceByPupilId(pupilId,
				PupilServices.TYPE_PRIMARYBLOGGER);
		serviceXPArena = dbAdapter.getPupilServiceByPupilId(pupilId,
				PupilServices.TYPE_XPARENA);
		dbAdapter.close();
	}

	private void saveAction() {
		if (cbImageToPortfolio.isChecked()) {
			servicePrimaryBlogger.setIsEnabled(PupilServices.SERVICE_ENABLED);
		} else {
			servicePrimaryBlogger.setIsEnabled(PupilServices.SERVICE_DISABLED);
		}
		if (cbXPArena.isChecked()) {
			serviceXPArena.setIsEnabled(PupilServices.SERVICE_ENABLED);
		} else {
			serviceXPArena.setIsEnabled(PupilServices.SERVICE_DISABLED);
		}

		pupil.setName(editName.getText().toString());
		DBAdapter dbAdapter = new DBAdapter(this);
		dbAdapter.open();
		dbAdapter.updatePupil(pupilId, pupil.getName());
		dbAdapter.updatePupilService(servicePrimaryBlogger);
		dbAdapter.updatePupilService(serviceXPArena);
		dbAdapter.close();
	}

	private void deleteAction() {
		DBAdapter dbAdapter = new DBAdapter(this);
		dbAdapter.open();
		dbAdapter.deletePupil(pupilId);
		dbAdapter.close();
	}

	private void initializeUIElements() {
		editName = (EditText) findViewById(R.id.editName);
		editName.setText(pupil.getName());

		Button btnSave = (Button) findViewById(R.id.btnSave);
		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				saveAction();
				Toast.makeText(EditPupilActivity.this,
						getString(R.string.lab_pupil_updated),
						Toast.LENGTH_LONG).show();
				finish();
			}
		});
		Button btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		Button btnDelete = (Button) findViewById(R.id.btnDelete);
		btnDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				deleteAction();
				setResult(RESULT_OK);
				Toast.makeText(EditPupilActivity.this,
						getString(R.string.lab_pupil_deleted),
						Toast.LENGTH_LONG).show();
				finish();
			}
		});

		cbImageToPortfolio = (CheckBox) findViewById(R.id.cbImageToPortfolio);
		
		 // Issue (Feature request)
        // https://github.com/johnyma22/classdroid/issues/3
        cbImageToPortfolio.setVisibility(View.GONE);
        
		if (servicePrimaryBlogger.getIsEnabled() == PupilServices.SERVICE_ENABLED) {
			cbImageToPortfolio.setChecked(true);
		} else {
			cbImageToPortfolio.setChecked(false);
		}
		cbImageToPortfolio
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							servicePrimaryBlogger
									.setIsEnabled(PupilServices.SERVICE_ENABLED);
							Intent intent = new Intent(EditPupilActivity.this,
									PrimaryBloggerActivity.class);
							intent.putExtra("id", servicePrimaryBlogger.getId());
							intent.putExtra("edit", true);
							startActivityForResult(intent,
									REQ_IMAGE_TO_PORTFOLIO);
						} else {
							servicePrimaryBlogger
									.setIsEnabled(PupilServices.SERVICE_DISABLED);
						}
						DBAdapter dbAdapter = new DBAdapter(
								EditPupilActivity.this);
						dbAdapter.open();
						dbAdapter.updatePupilService(servicePrimaryBlogger);
						dbAdapter.close();
					}
				});

		cbXPArena = (CheckBox) findViewById(R.id.cbExpPoints);
		if (serviceXPArena.getIsEnabled() == PupilServices.SERVICE_ENABLED) {
			cbXPArena.setChecked(true);
		} else {
			cbXPArena.setChecked(false);
		}
		cbXPArena.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					serviceXPArena.setIsEnabled(PupilServices.SERVICE_ENABLED);
					Intent intent = new Intent(EditPupilActivity.this,
							XPArenaActivity.class);
					intent.putExtra("id", serviceXPArena.getId());
					intent.putExtra("edit", true);
					startActivityForResult(intent, REQ_XPARENA_CONFIG);
				} else {
					serviceXPArena.setIsEnabled(PupilServices.SERVICE_DISABLED);
				}
				DBAdapter dbAdapter = new DBAdapter(EditPupilActivity.this);
				dbAdapter.open();
				dbAdapter.updatePupilService(serviceXPArena);
				dbAdapter.close();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQ_IMAGE_TO_PORTFOLIO) {
			if (resultCode == RESULT_CANCELED) {
				servicePrimaryBlogger
						.setIsEnabled(PupilServices.SERVICE_DISABLED);
				cbImageToPortfolio.setChecked(false);
				DBAdapter dbAdapter = new DBAdapter(EditPupilActivity.this);
				dbAdapter.open();
				dbAdapter.updatePupilService(servicePrimaryBlogger);
				dbAdapter.close();
			}
		}
		if (requestCode == REQ_XPARENA_CONFIG) {
			if (resultCode == RESULT_CANCELED) {
				serviceXPArena.setIsEnabled(PupilServices.SERVICE_DISABLED);
				cbXPArena.setChecked(false);
				DBAdapter dbAdapter = new DBAdapter(EditPupilActivity.this);
				dbAdapter.open();
				dbAdapter.updatePupilService(serviceXPArena);
				dbAdapter.close();
			}
		}
	}

}
