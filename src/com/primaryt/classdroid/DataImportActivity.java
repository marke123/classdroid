package com.primaryt.classdroid;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DataImportActivity extends Activity {
	private final static String TAG = "DataImportActivity";

	private static final int REQUEST_AUTO_IMPORT = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.data_import_activity);

		initializeUIElements();
	}

	private void initializeUIElements() {
		Button btnCancel = (Button) findViewById(R.id.buttonCancel);
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		Button btnExport = (Button) findViewById(R.id.buttonExport);
		btnExport.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startExporting();
			}
		});
	}

	private void startExporting() {
		startImportActivity();
	}

	private void startImportActivity() {
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setClassName("com.mclear.classdroid",
				"com.mclear.classdroid.DataExportActivity");
		intent.putExtra("auto", true);
		startActivityForResult(intent, REQUEST_AUTO_IMPORT);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_AUTO_IMPORT) {
			retrieveBackupDataAndImport();
		}
	}

	private void retrieveBackupDataAndImport() {
		String localFilePath = Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ File.separator
				+ "classdroid"
				+ File.separator + "backup.xml";
		StringBuffer stringBuffer = new StringBuffer();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(
					localFilePath)));
			char[] buffer = new char[1024];
			int numRead = 0;
			while ((numRead = reader.read(buffer)) != -1) {
				String readData = String.valueOf(buffer, 0, numRead);
				stringBuffer.append(readData);
				buffer = new char[1024];
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Log.i(TAG, stringBuffer.toString());
	}
}
