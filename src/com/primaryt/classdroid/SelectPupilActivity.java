package com.primaryt.classdroid;

import java.io.File;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.primaryt.classdroid.adapters.PupilAdapter;
import com.primaryt.classdroid.bo.Pupil;
import com.primaryt.classdroid.bo.PupilServices;
import com.primaryt.classdroid.db.DBAdapter;
import com.primaryt.classdroid.temp.AppUtils;
import com.primaryt.classdroid.temp.ConfirmDialog;
import com.primaryt.classdroid.utils.ExportUtils;

public class SelectPupilActivity extends ClassdroidActivity implements
		OnItemClickListener, OnDismissListener {

	private final static String TAG = "Classdroid";

	private String imagePath;

	private final static int TAKE_PICTURE = 1;

	private final static int GRADE_ASSESSMENT = 2;

	private final static int REQ_ADD_PUPIL = 3;

	private int pupilId;

	public static long newPupilId = 0;

	private PupilAdapter adapter;

	private ListView listView;

	private File file;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selectpupil);

		initializeUIElemets();
		reloadListView();
		AppUtils util = new AppUtils(this);
		String backupFilePath = Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ File.separator
				+ "classdroid"
				+ File.separator + "backup.xml";
		File backupFile = new File(backupFilePath);
		ExportUtils exportUtils = new ExportUtils(this);
		if (exportUtils.isFirstRun()) {
			startImportDataActivity();
			finish();
		} else {
			if (util.isFirstRun()) {
				startGuidedTour();
				finish();
			} else {
				startCamera();
			}
		}
	}

	private void startImportDataActivity() {
		Intent intent = new Intent(this, DataImportActivity.class);
		startActivity(intent);
	}

	private void startGuidedTour() {
		Intent intent = new Intent(this, GuidedTour_0.class);
		startActivity(intent);
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

	private void initializeUIElemets() {
		listView = (ListView) findViewById(R.id.listViewPupil);

		Button btnAddPupil = new Button(this);
		btnAddPupil.setText(getString(R.string.lab_add_new_pupil));
		btnAddPupil.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addPupil();
			}
		});
		listView.addFooterView(btnAddPupil);

		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				pupilId = arg1.getId();
				return false;
			}
		});

		listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				menu.setHeaderTitle(getString(R.string.lab_select_option));
				menu.add(getString(R.string.lab_edit));
				menu.add(getString(R.string.lab_delete));
			}
		});
	}

	private void reloadListView() {
		DBAdapter dbAdapter = new DBAdapter(this);
		dbAdapter.open();
		adapter = new PupilAdapter(this, dbAdapter.getAllPupils());
		dbAdapter.close();
		listView.setAdapter(adapter);
	}

	private void addPupil() {
		Intent intent = new Intent(SelectPupilActivity.this,
				AddPupilActivity.class);
		startActivityForResult(intent, REQ_ADD_PUPIL);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == TAKE_PICTURE) {
			if (resultCode == RESULT_OK) {
				imagePath = file.getAbsolutePath();
				Log.i(TAG, imagePath);
			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(this, getString(R.string.lab_action_cancelled),
						Toast.LENGTH_LONG).show();
				finish();
			}
		} else if (requestCode == GRADE_ASSESSMENT) {
			if (resultCode == RESULT_OK) {
				Toast.makeText(SelectPupilActivity.this,
						getString(R.string.lab_upload_new), Toast.LENGTH_LONG)
						.show();
				startCamera();
			}
		} else if (requestCode == REQ_ADD_PUPIL) {
			if (resultCode == RESULT_OK) {
				DBAdapter dbAdapter = new DBAdapter(SelectPupilActivity.this);
				dbAdapter.open();
				Pupil newPupil = dbAdapter.getPupilById(newPupilId);
				adapter.addPupil(newPupil);
				adapter.notifyDataSetChanged();
				dbAdapter.close();
				Toast.makeText(SelectPupilActivity.this,
						getString(R.string.lab_new_pupil_added),
						Toast.LENGTH_LONG).show();
				if (data.getBooleanExtra("add", false)) {
					addPupil();
				}
				reloadListView();
			} else if (resultCode == RESULT_CANCELED) {
				DBAdapter dbAdapter = new DBAdapter(SelectPupilActivity.this);
				dbAdapter.open();
				dbAdapter.deletePupil(newPupilId);
				dbAdapter.deletePupilServiceByPupilId(newPupilId);
				dbAdapter.close();
				Toast.makeText(SelectPupilActivity.this,
						getString(R.string.lab_action_cancelled),
						Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long index) {
		pupilId = view.getId();
		if (checkServices()) {
			Intent intent = new Intent(this, GradeActivity.class);
			intent.putExtra("image", imagePath);
			intent.putExtra("pupil", view.getId());
			startActivityForResult(intent, GRADE_ASSESSMENT);
		} else {
			Toast.makeText(this, getString(R.string.lab_configure_settings),
					Toast.LENGTH_LONG).show();
			editAction();
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getTitle().equals(getString(R.string.lab_edit))) {
			editAction();
		}
		if (item.getTitle().equals(getString(R.string.lab_delete))) {
			deleteAction();
		}
		return super.onContextItemSelected(item);
	}

	private void deleteAction() {
		DBAdapter dbAdapter = new DBAdapter(this);
		dbAdapter.open();
		dbAdapter.deletePupil(pupilId);
		dbAdapter.deletePupilServiceByPupilId(pupilId);
		dbAdapter.close();
		reloadListView();
		Toast.makeText(this, getString(R.string.lab_pupil_deleted),
				Toast.LENGTH_LONG).show();
	}

	private void editAction() {
		Intent intent = new Intent(SelectPupilActivity.this,
				EditPupilActivity.class);
		intent.putExtra("id", pupilId);
		startActivity(intent);
	}

	private boolean checkServices() {
		boolean value = false;
		DBAdapter dbAdapter = new DBAdapter(this);
		dbAdapter.open();
		PupilServices service = dbAdapter.getPupilServiceByPupilId(pupilId,
				PupilServices.TYPE_PRIMARYBLOGGER);
		if (service.getIsEnabled() == PupilServices.SERVICE_ENABLED) {
			value = true;
		}
		service = dbAdapter.getPupilServiceByPupilId(pupilId,
				PupilServices.TYPE_XPARENA);
		if (service.getIsEnabled() == PupilServices.SERVICE_ENABLED) {
			value = true;
		}
		dbAdapter.close();
		return value;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean value = false;
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			value = true;
			if (!TextUtils.isEmpty(imagePath)) {
				ConfirmDialog dialog = new ConfirmDialog(this);
				dialog.setTitle("Confirm action");
				dialog.setOnDismissListener(this);
				dialog.show();
			}
		}
		return value;
	}

	@Override
	public void onDismiss(DialogInterface arg0) {
		if (arg0 instanceof ConfirmDialog) {
			ConfirmDialog dia = (ConfirmDialog) arg0;
			if (dia.getResult() == ConfirmDialog.ACTION_YES) {
				finish();
			}
		}
	}

}
