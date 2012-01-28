package com.primaryt.classdroid;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TutorialActivity_1 extends ClassdroidActivity {

	private TextView textViewHeader;
	private TextView textViewMessage;
	private String[] headers;
	private String[] messages;
	private int currentPage = 0;
	private int totalPages = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tutorial_activity_1);

		initializeUIElements();
		headers = getResources().getStringArray(R.array.array_tutorial_headers);
		messages = getResources().getStringArray(
				R.array.array_tutorial_messages);

		totalPages = headers.length;

		showPage(0);
	}

	private void initializeUIElements() {
		Button btnBack = (Button) findViewById(R.id.buttonBack);
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				actionBack();
			}
		});
		Button btnNext = (Button) findViewById(R.id.buttonNext);
		btnNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				actionNext();
			}
		});

		textViewHeader = (TextView) findViewById(R.id.textViewHeader);
		textViewMessage = (TextView) findViewById(R.id.textViewMessage);
	}

	private void showPage(int page) {
		textViewHeader.setText(headers[page]);
		textViewMessage.setText(messages[page]);
		currentPage = page;
	}

	private void actionBack() {
		if (currentPage == 0) {
			finish();
		} else {
			showPage(currentPage - 1);
		}
	}

	private void actionNext() {
		if (currentPage == totalPages - 1) {
			finish();
		} else {
			showPage(currentPage + 1);
		}
	}

}
