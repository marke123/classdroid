
package com.primaryt.classdroid;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

import com.primaryt.classdroid.bo.PupilServices;
import com.primaryt.classdroid.db.DBAdapter;
import com.primaryt.classdroid.temp.AppUtils;

public class AddPupilActivity extends ClassdroidActivity {

    private String name = "";

    private long pupilId;

    private final static int REQ_IMAGE_TO_PORTFOLIO = 1;

    private final static int REQ_XPARENA_CONFIG = 2;

    private PupilServices servicePrimaryBlogger;

    private PupilServices serviceXPArena;

    private CheckBox cbImageToPortfolio;

    private CheckBox cbXPArena;

    private EditText editName;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_pupil);

        DBAdapter dbAdapter = new DBAdapter(this);
        dbAdapter.open();
        pupilId = dbAdapter.addPupil(name);
        SelectPupilActivity.newPupilId = pupilId;

        servicePrimaryBlogger = new PupilServices();
        servicePrimaryBlogger.setIsEnabled(PupilServices.SERVICE_DISABLED);
        servicePrimaryBlogger.setServiceId(PupilServices.TYPE_PRIMARYBLOGGER);
        servicePrimaryBlogger.setUseDefault(PupilServices.USE_CUSTOM);
        servicePrimaryBlogger.setPupilId(pupilId);

        // Retrieve and check if default configuration is saved
        AppUtils utils = new AppUtils(this);
        if (utils.getDefPriUser() != null && utils.getDefPriPass() != null) {
            servicePrimaryBlogger.setIsEnabled(PupilServices.SERVICE_ENABLED);
            servicePrimaryBlogger.setUsername(utils.getDefPriUser());
            servicePrimaryBlogger.setPassword(utils.getDefPriPass());
            servicePrimaryBlogger.setUrl(utils.getDefaultPrimaryURL());
            
            DBAdapter adapter = new DBAdapter(AddPupilActivity.this);
            adapter.open();
            adapter.addPupilService(servicePrimaryBlogger);
//            adapter.updatePupilService(servicePrimaryBlogger);
            adapter.close();
        }

        // XP Arena is disable right now
        /*
         * serviceXPArena = new PupilServices();
         * serviceXPArena.setIsEnabled(PupilServices.SERVICE_DISABLED);
         * serviceXPArena.setServiceId(PupilServices.TYPE_XPARENA);
         * serviceXPArena.setUseDefault(PupilServices.USE_CUSTOM);
         * serviceXPArena.setPupilId(pupilId); servicePrimaryBlogger =
         * dbAdapter.addPupilService(servicePrimaryBlogger); serviceXPArena =
         * dbAdapter.addPupilService(serviceXPArena);
         */

        dbAdapter.close();

        initializeUIElements();
    }

    private void initializeUIElements() {
        editName = (EditText) findViewById(R.id.editName);

        Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (saveAction(false)) {
                    finish();
                }
            }
        });
        Button btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                cancelAction();
                finish();
            }
        });

        Button btnSaveAdd = (Button) findViewById(R.id.btnSaveAdd);
        btnSaveAdd.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (saveAction(true)) {
                    finish();
                }
            }
        });

        Button btnEditPupil = (Button) findViewById(R.id.btnEditAdd);
        btnEditPupil.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(AddPupilActivity.this, getString(R.string.lab_long_tap_edit),
                        Toast.LENGTH_LONG).show();
                cancelAction();
                finish();
            }
        });

        // Issue (Feature request)
        // https://github.com/johnyma22/classdroid/issues/3
        
        cbImageToPortfolio = (CheckBox) findViewById(R.id.cbImageToPortfolio);
        cbImageToPortfolio.setVisibility(View.GONE);

        cbImageToPortfolio
                .setChecked(servicePrimaryBlogger.getIsEnabled() == PupilServices.SERVICE_ENABLED ? true
                        : false);

        cbImageToPortfolio.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    servicePrimaryBlogger.setIsEnabled(PupilServices.SERVICE_ENABLED);
                    Intent intent = new Intent(AddPupilActivity.this, PrimaryBloggerActivity.class);
                    intent.putExtra("id", servicePrimaryBlogger.getId());
                    startActivityForResult(intent, REQ_IMAGE_TO_PORTFOLIO);
                } else {
                    servicePrimaryBlogger.setIsEnabled(PupilServices.SERVICE_DISABLED);
                }
                DBAdapter dbAdapter = new DBAdapter(AddPupilActivity.this);
                dbAdapter.open();
                dbAdapter.updatePupilService(servicePrimaryBlogger);
                dbAdapter.close();
            }
        });

        // XP Arena is disabled right now
        /*
         * cbXPArena = (CheckBox) findViewById(R.id.cbExpPoints);
         * cbXPArena.setOnCheckedChangeListener(new OnCheckedChangeListener() {
         * @Override public void onCheckedChanged(CompoundButton buttonView,
         * boolean isChecked) { if (isChecked) {
         * serviceXPArena.setIsEnabled(PupilServices.SERVICE_ENABLED); Intent
         * intent = new Intent(AddPupilActivity.this, XPArenaActivity.class);
         * intent.putExtra("id", serviceXPArena.getId());
         * startActivityForResult(intent, REQ_XPARENA_CONFIG); } else {
         * serviceXPArena.setIsEnabled(PupilServices.SERVICE_DISABLED); }
         * DBAdapter dbAdapter = new DBAdapter(AddPupilActivity.this);
         * dbAdapter.open(); dbAdapter.updatePupilService(serviceXPArena);
         * dbAdapter.close(); } });
         */
    }

    private boolean saveAction(boolean value) {
        boolean success = false;
        name = editName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(AddPupilActivity.this, getString(R.string.lab_enter_name),
                    Toast.LENGTH_LONG).show();
        } else {
            DBAdapter dbAdapter = new DBAdapter(AddPupilActivity.this);
            dbAdapter.open();
            dbAdapter.updatePupil(pupilId, name);
            dbAdapter.close();

            Intent intent = new Intent();
            intent.putExtra("id", pupilId);
            intent.putExtra("add", value);
            setResult(RESULT_OK, intent);
            success = true;
        }
        return success;
    }

    private void cancelAction() {
        Intent intent = new Intent();
        intent.putExtra("id", pupilId);
        setResult(RESULT_CANCELED, intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_IMAGE_TO_PORTFOLIO) {
            if (resultCode == RESULT_CANCELED) {
                servicePrimaryBlogger.setIsEnabled(PupilServices.SERVICE_DISABLED);
                cbImageToPortfolio.setChecked(false);
                DBAdapter dbAdapter = new DBAdapter(AddPupilActivity.this);
                dbAdapter.open();
                dbAdapter.updatePupilService(servicePrimaryBlogger);
                dbAdapter.close();
            }
        }
        if (requestCode == REQ_XPARENA_CONFIG) {
            if (resultCode == RESULT_CANCELED) {
                serviceXPArena.setIsEnabled(PupilServices.SERVICE_DISABLED);
                cbXPArena.setChecked(false);
                DBAdapter dbAdapter = new DBAdapter(AddPupilActivity.this);
                dbAdapter.open();
                dbAdapter.updatePupilService(serviceXPArena);
                dbAdapter.close();
            }
        }
    }
}
