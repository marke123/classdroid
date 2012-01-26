package com.primaryt.classdroid;

import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.primaryt.classdroid.bo.Post;
import com.primaryt.classdroid.db.DBAdapter;
import com.primaryt.classdroid.services.ClassdroidService;

public class GradeActivity extends ClassdroidActivity implements
		OnItemClickListener {

	private final static int REQUEST_ADD_NOTE = 1;

	private String imagePath;

	private int pupilId;

	private int grade;
	private String[] grades;
	private String gradeString;
	private ArrayAdapter<String> adapter;
	private ListView listView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grade);

		Bundle data = getIntent().getExtras();
		if (data.containsKey("image")) {
			imagePath = data.getString("image");
		}
		if (data.containsKey("pupil")) {
			pupilId = data.getInt("pupil");
		}

		initializeUIElements();
	}

	private void initializeUIElements() {
		listView = (ListView) findViewById(R.id.listViewGrades);
		listView.setOnItemClickListener(this);

		adapter = new MyArrayAdapter<String>(this,
				android.R.layout.simple_list_item_checked);
		grades = getResources().getStringArray(R.array.array_grades);
		for (String value : grades) {
			adapter.add(value);
		}
		listView.setAdapter(adapter);

		Button buttonUploadWithoutNote = (Button) findViewById(R.id.buttonUploadWithoutNote);
		buttonUploadWithoutNote.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startUpload();
			}
		});

		Button buttonAddNote = (Button) findViewById(R.id.buttonAddNote);
		buttonAddNote.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startAddNoteActivity(gradeString);
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long index) {
		grade = position;
		gradeString = grades[grade];
		adapter.notifyDataSetChanged();
	}

	private void startAddNoteActivity(String grade) {
		Intent intent = new Intent(this, NotesActivity.class);
		intent.putExtra("grade", grade);
		intent.putExtra("image", imagePath);
		intent.putExtra("pupil", pupilId);
		startActivityForResult(intent, REQUEST_ADD_NOTE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_ADD_NOTE) {
			if (resultCode == RESULT_CANCELED) {
				// Do nothing
			} else if (resultCode == RESULT_OK) {
				setResult(RESULT_OK);
				finish();
			}
		}
	}

	private void startUpload() {
		Post post = new Post();
		post.setIsPosted(0);
		post.setLocalImagePath(imagePath);
		post.setPupilId(pupilId);
		post.setTimestamp(Calendar.getInstance().toString());
		post.setGrade(gradeString);
		post.setNote("");

		DBAdapter dbAdapter = new DBAdapter(this);
		dbAdapter.open();

		post = dbAdapter.addPost(post);
		dbAdapter.close();

		Intent service = new Intent(this, ClassdroidService.class);
		service.putExtra("id", post.getId());
		startService(service);
		setResult(RESULT_OK);
		finish();
	}

	private class MyArrayAdapter<String> extends ArrayAdapter<String> {

		public MyArrayAdapter(Context context, int resource,
				int textViewResourceId, List<String> objects) {
			super(context, resource, textViewResourceId, objects);
		}

		public MyArrayAdapter(Context context, int resource,
				int textViewResourceId, String[] objects) {
			super(context, resource, textViewResourceId, objects);
		}

		public MyArrayAdapter(Context context, int resource,
				int textViewResourceId) {
			super(context, resource, textViewResourceId);
		}

		public MyArrayAdapter(Context context, int textViewResourceId,
				List<String> objects) {
			super(context, textViewResourceId, objects);
		}

		public MyArrayAdapter(Context context, int textViewResourceId,
				String[] objects) {
			super(context, textViewResourceId, objects);
		}

		public MyArrayAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = super.getView(position, convertView, parent);

			CheckedTextView textView = (CheckedTextView) view;
			textView.setChecked(position == grade);
			return view;
		}

	};

}
