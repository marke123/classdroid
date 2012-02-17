package com.primaryt.classdroid;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.primaryt.classdroid.adapters.SelectedPupilsAdapter;
import com.primaryt.classdroid.bo.Pupil;
import com.primaryt.classdroid.db.DBAdapter;

public class DashboardActivity extends Activity {

	private File file;
	private String imagePath;
	private final static int TAKE_PICTURE = 1;
	private final static int REQUEST_GRADE_ACTIVITY = 2;
	public static long newPupilId = 0;
	private final static String TAG = "DashboardActivity";
	private ListView listViewPupils;
	private SelectedPupilsAdapter adapter;

	private ArrayList<Pupil> pupils;
	private int pupilId;

	private Button buttonAddPupil;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard_activity);

		Bundle data = getIntent().getExtras();
		if (data.containsKey("pupil")) {
			pupilId = data.getInt("pupil");
		}

		Button buttonTakePicture = (Button) findViewById(R.id.buttonTakePhoto);
		buttonTakePicture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startCamera();
			}
		});

		buttonAddPupil = (Button) findViewById(R.id.buttonAddPupil);
		buttonAddPupil.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showAddPupilDialog();
			}
		});

		listViewPupils = (ListView) findViewById(R.id.listViewPupils);
		adapter = new SelectedPupilsAdapter(this);
		listViewPupils.setAdapter(adapter);

		pupils = new ArrayList<Pupil>();

		DBAdapter dbAdapter = new DBAdapter(this);
		dbAdapter.open();
		Pupil p = dbAdapter.getPupilById(pupilId);
		dbAdapter.close();
		pupils.add(p);

		adapter.addPupil(p);
		adapter.notifyDataSetChanged();
	}

	private void startCamera() {
		File tempFolder = new File(Environment.getExternalStorageDirectory()
				+ "/wordpress");
		tempFolder.mkdir();
		file = new File(Environment.getExternalStorageDirectory()
				+ "/wordpress", String.valueOf(System.currentTimeMillis())
				+ ".jpg");
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		startActivityForResult(intent, TAKE_PICTURE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == TAKE_PICTURE) {
			if (resultCode == RESULT_OK) {
				imagePath = file.getAbsolutePath();
				startGradeActivity();
				Log.i(TAG, imagePath);
			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(this, getString(R.string.lab_action_cancelled),
						Toast.LENGTH_LONG).show();
				finish();
			}
		} else if (requestCode == REQUEST_GRADE_ACTIVITY) {
			if (resultCode == RESULT_OK) {
				setResult(RESULT_OK);
				finish();
			} else if (resultCode == RESULT_CANCELED) {
				// Do nothing
			}
		}
	}

	private void startGradeActivity() {
		Intent intent = new Intent(this, GradeActivity.class);
		intent.putExtra("image", imagePath);
		ArrayList<String> pupilIDs = new ArrayList<String>();
		for (Pupil p : pupils) {
			pupilIDs.add(p.getId() + "");
		}
		intent.putExtra("pupils", pupilIDs);
		startActivityForResult(intent, REQUEST_GRADE_ACTIVITY);
	}

	private void showAddPupilDialog() {
		DBAdapter dbAdapter = new DBAdapter(this);
		dbAdapter.open();
		int pupilCount = dbAdapter.getAllPupils().size();
		dbAdapter.close();
		if (pupilCount > 1) {
			SelectPupilsDialog dialog = new SelectPupilsDialog(this);
			dialog.setSelectedPupils(pupils);
			dialog.show();
			dialog.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					SelectPupilsDialog d = (SelectPupilsDialog) dialog;
					Pupil p = d.getSelectedPupil();
					if (p != null) {
						pupils.add(p);
						adapter.addPupil(p);
						adapter.notifyDataSetChanged();
					}
					if (adapter.getCount() == 3) {
						buttonAddPupil.setEnabled(false);
					}
				}
			});
		} else {
			Toast.makeText(this,
					getString(R.string.lab_no_more_available_pupils),
					Toast.LENGTH_LONG).show();
		}
	}
}
