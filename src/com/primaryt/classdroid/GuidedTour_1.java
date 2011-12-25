
package com.primaryt.classdroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class GuidedTour_1 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guided_tour_1);
        
        Button btnNext = (Button)findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), GuidedTour_2.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
