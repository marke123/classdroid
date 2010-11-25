
package com.mclear.classdroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class GuidedTour_0 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guided_tour_0);
        
        Button btnYes = (Button)findViewById(R.id.btnYes);
        btnYes.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), GuidedTour_1.class);
                startActivity(intent);
                finish();
            }
        });
        
        Button btnNo = (Button)findViewById(R.id.btnNo);
        btnNo.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // Write code to take the user directly to the default configuration screen
                Intent intent = new Intent(getBaseContext(), DefaultConfigurationActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
