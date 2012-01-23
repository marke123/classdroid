package com.primaryt.classdroid;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class DashboardActivity extends Activity {

	private File file;
	private String imagePath;
	private final static int TAKE_PICTURE = 1;
	private final static int REQUEST_GRADE_ACTIVITY = 2;
	public static long newPupilId = 0;
	private final static String TAG = "DashboardActivity";
	
	private int pupilId;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard_activity);
		
		Bundle data = getIntent().getExtras();
		if(data.containsKey("pupil")){
			pupilId = data.getInt("pupil");
		}
		

		Button buttonTakePicture = (Button) findViewById(R.id.buttonTakePhoto);
		buttonTakePicture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startCamera();
			}
		});

		Button buttonAddPupil = (Button) findViewById(R.id.buttonAddPupil);
		buttonAddPupil.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
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
		}else if(requestCode == REQUEST_GRADE_ACTIVITY){
			if(resultCode == RESULT_OK){
				setResult(RESULT_OK);
				finish();
			}else if(resultCode == RESULT_CANCELED){
				// Do nothing
			}
		}
	}
	
	private void startGradeActivity(){
		Intent intent = new Intent(this, GradeActivity.class);
		intent.putExtra("image", imagePath);
		intent.putExtra("pupil", pupilId);
		startActivityForResult(intent, REQUEST_GRADE_ACTIVITY);
	}

}
