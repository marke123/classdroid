package com.primaryt.classdroid;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class GuidedTour_2 extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guided_tour_2);

		Button btnProceed = (Button) findViewById(R.id.btnProceed);
		btnProceed.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getBaseContext(),
						DefaultConfigurationActivity.class);
				startActivity(intent);
				finish();
			}
		});

		Button btnOptions = (Button) findViewById(R.id.btnOptions);
		btnOptions.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri
						.parse("http://crm.primaryt.co.uk/indexpb.php?registerpb"));
				startActivity(intent);
			}
		});
	}

}
