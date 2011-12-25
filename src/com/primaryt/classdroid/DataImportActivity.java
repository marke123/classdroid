package com.primaryt.classdroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DataImportActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_import_activity);
       
        initializeUIElements();
    }
    
    private void initializeUIElements(){
        Button btnCancel = (Button)findViewById(R.id.buttonCancel);
        btnCancel.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        
        Button btnExport = (Button)findViewById(R.id.buttonExport);
        btnExport.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                startExporting();
            }
        });
    }
    
    private void startExporting(){
        
    }

}
