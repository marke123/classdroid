
package com.primaryt.classdroid;

import java.util.Calendar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.primaryt.classdroid.bo.Post;
import com.primaryt.classdroid.db.DBAdapter;
import com.primaryt.classdroid.services.ClassdroidService;

public class GradeActivity extends ClassdroidActivity implements OnItemClickListener {

    private final static int REQUEST_PUBLISH = 1;

    private ProgressDialog pDialog;

    private String imagePath;

    private int pupilId;

    private int grade;
    private String[] grades;

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
        ListView listView = (ListView) findViewById(R.id.listViewGrades);
        listView.setOnItemClickListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1);
        grades = getResources().getStringArray(R.array.array_grades);
        for (String value : grades) {
            adapter.add(value);
        }
        listView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long index) {
        grade = position;
        // pDialog = new ProgressDialog(this);
        // pDialog.setTitle("Please wait...");
        // pDialog.setMessage("Uploading Assignment");
        // pDialog.show();

        System.out.println(imagePath);
        Post post = new Post();
        post.setIsPosted(0);
        post.setLocalImagePath(imagePath);
        post.setPupilId(pupilId);
        post.setTimestamp(Calendar.getInstance().toString());
        post.setGrade(grades[grade]);

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
}
