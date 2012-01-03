package com.mclear.classdroid;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.mclear.classdroid.bo.Pupil;
import com.mclear.classdroid.bo.PupilServices;
import com.mclear.classdroid.db.DBAdapter;

public class DataExportActivity extends Activity {

	private Button btnExport;
	private final static int NO_SDCARD = 1;
	private final static int BACKUP_COMPLETE = 2;

	private boolean isAuto = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.data_export_activity);

		if (getIntent().getExtras() != null
				&& getIntent().getExtras().containsKey("auto")) {
			isAuto = getIntent().getExtras().getBoolean("auto");
		}
		initializeUIElements();

		if (isAuto) {
			startExporting();
		}
	}

	private void initializeUIElements() {
		Button btnCancel = (Button) findViewById(R.id.buttonCancel);
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DataExportActivity.this,
						SelectPupilActivity.class);
				startActivity(intent);
				finish();
			}
		});

		btnExport = (Button) findViewById(R.id.buttonExport);
		btnExport.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startExporting();
			}
		});
	}

	private void startExporting() {
		btnExport.setEnabled(false);
		BackupThread thread = new BackupThread();
		thread.start();
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case NO_SDCARD:
				actionNoSDCard();
				break;
			case BACKUP_COMPLETE:
				actionBackupComplete();
				break;
			}
		}

	};

	private void actionBackupComplete() {
		if (!isAuto) {
			showMessage("Data backup completed");
		}
		setResult(RESULT_OK);
		finish();
	}

	private void actionNoSDCard() {
		btnExport.setEnabled(true);
		showMessage("Data backup not possible. No SD Card found");
		if (isAuto) {
			setResult(RESULT_CANCELED);
			finish();
		}
	}

	private void showMessage(String message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

	private class BackupThread extends Thread {

		@Override
		public void run() {
			// Check if SDCard present
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				startBackup();
				handler.sendEmptyMessage(BACKUP_COMPLETE);
			} else {
				handler.sendEmptyMessage(NO_SDCARD);
			}
		}

	}

	private void startBackup() {
		String backupDIRPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + File.separator + "classdroid";
		File backupDIR = new File(backupDIRPath);
		if (!backupDIR.exists()) {
			backupDIR.mkdir();
		}

		String backupFilePath = backupDIRPath + File.separator + "backup.xml";

		StringBuilder xmlString = new StringBuilder();
		xmlString.append("<backup>");
		xmlString.append(preparePupilString());
		xmlString.append(preparePupilServicesString());
		xmlString.append("</backup>");

		File backupFile = new File(backupFilePath);
		try {
			FileWriter writer = new FileWriter(backupFile);
			writer.write(xmlString.toString());
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String preparePupilString() {
		StringBuilder builder = new StringBuilder();

		builder.append("<pupils>");

		DBAdapter adapter = new DBAdapter(this);
		adapter.open();
		ArrayList<Pupil> pupils = adapter.getAllPupils();
		adapter.close();

		for (Pupil pupil : pupils) {
			builder.append("<pupil>");
			builder.append("<id>" + pupil.getId() + "</id>");
			builder.append("<name>" + pupil.getName() + "</name>");
			builder.append("</pupil>");
		}

		builder.append("</pupils>");

		return builder.toString();
	}

	private String preparePupilServicesString() {
		StringBuilder builder = new StringBuilder();

		builder.append("<pupil-services>");
		DBAdapter adapter = new DBAdapter(this);
		adapter.open();
		ArrayList<PupilServices> services = adapter.getAllPupilServices();
		for (PupilServices service : services) {
			builder.append("<service>");
			builder.append("<id>" + service.getId() + "</id>");
			builder.append("<is-enabled>" + service.getIsEnabled()
					+ "</is-enabled>");
			builder.append("<pupil-id>" + service.getPupilId() + "</pupil-id>");
			builder.append("<service-id>" + service.getServiceId()
					+ "</service-id>");
			builder.append("<nickname>" + service.getNickname() + "</nickname>");
			builder.append("<url>" + service.getUrl() + "</url>");
			builder.append("<username>" + service.getUsername() + "</username>");
			builder.append("<password>" + service.getPassword() + "</password>");
			builder.append("<use-default>" + service.getUseDefault()
					+ "</use-default>");
			builder.append("</service>");
		}
		adapter.close();
		builder.append("</pupil-services>");

		return builder.toString();
	}
}
