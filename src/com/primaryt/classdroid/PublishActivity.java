package com.primaryt.classdroid;

import android.os.Bundle;

public class PublishActivity extends ClassdroidActivity {

	private String imagePath;
	private int pupilId;
	private int grade;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.publish_activity);

		Bundle data = getIntent().getExtras();
		if (data.containsKey("image")) {
			imagePath = data.getString("image");
		}
		if (data.containsKey("pupil")) {
			pupilId = data.getInt("pupil");
		}
		if (data.containsKey("pgrade")) {
			grade = data.getInt("pgrade");
		}
		System.out.println(imagePath);
		System.out.println("Pupil ID "+pupilId);
		System.out.println("Grade "+grade);
	}

}
