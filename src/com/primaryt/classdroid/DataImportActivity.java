package com.primaryt.classdroid;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.primaryt.classdroid.bo.Pupil;
import com.primaryt.classdroid.bo.PupilServices;

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

		ArrayList<Pupil> pupils = new ArrayList<Pupil>();
		ArrayList<PupilServices> services = new ArrayList<PupilServices>();

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new FileInputStream(new File(
					localFilePath)));

			Element element = document.getDocumentElement();
			NodeList nodelist = element.getChildNodes();

			Element pupilsElement = (Element) nodelist.item(0);
			Element pupilServicesElement = (Element) nodelist.item(1);

			NodeList listPupils = pupilsElement.getChildNodes();
			NodeList listServices = pupilServicesElement.getChildNodes();

			for (int i = 0; i < listPupils.getLength(); i++) {
				Element p1 = (Element) listPupils.item(i);
				Pupil p = new Pupil();
			}

			for (int i = 0; i < listServices.getLength(); i++) {

			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
