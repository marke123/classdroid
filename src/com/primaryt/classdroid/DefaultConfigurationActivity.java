
package com.primaryt.classdroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

import com.primaryt.classdroid.temp.AppUtils;
import com.primaryt.classdroid.threads.WPLoginCheckThread;

public class DefaultConfigurationActivity extends ClassdroidActivity {

    private EditText editURL;

    private EditText editUsername;

    private EditText editPassword;

    private ProgressDialog dialog;

    private WPLoginCheckThread thread;

    private Button btnSave;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.default_configuration_activity);
        initializeUIElements();
    }

    private void initializeUIElements() {
        editURL = (EditText) findViewById(R.id.editUrl);
        editUsername = (EditText) findViewById(R.id.editTeacherUsername);
        editPassword = (EditText) findViewById(R.id.editTeacherPassword);

        CheckBox cbShowPassword = (CheckBox) findViewById(R.id.cbShowPassword);
        cbShowPassword.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editPassword.setTransformationMethod(null);
                } else {
                    editPassword
                            .setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        Button btnTest = (Button) findViewById(R.id.btnTest);
        btnTest.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                testCredentials();
            }
        });

        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                saveDefaultValues();
            }
        });

        Button btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean isURLOK() {
        if (TextUtils.isEmpty(editURL.getText().toString().trim())) {
            Toast.makeText(this, getString(R.string.lab_enter_url), Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private void saveDefaultValues() {
        AppUtils utils = new AppUtils(this);
        utils.setDefaultCredsPrimary(editUsername.getText().toString().trim(), editPassword
                .getText().toString().trim());
        utils.setDefaultPrimaryURL(editURL.getText().toString().trim());
        utils.setFirstRun(false);
        // Start the Take screenshot activity
        
        Intent intent = new Intent(this, SelectPupilActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean isDataOK() {
        boolean value = true;
        if (!isURLOK()) {
            value = false;
        } else if (TextUtils.isEmpty(editUsername.getText().toString())) {
            value = false;
            Toast.makeText(this, getString(R.string.lab_enter_username), Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(editPassword.getText().toString())) {
            value = false;
            Toast.makeText(this, getString(R.string.lab_enter_password), Toast.LENGTH_LONG).show();
        }
        return value;
    }

    private void testCredentials() {
        if (isDataOK()) {
            dialog = new ProgressDialog(this);
            dialog.setTitle(getString(R.string.lab_please_wait));
            dialog.setMessage(getString(R.string.lab_checking_credentials));
            dialog.show();
            btnSave.setEnabled(false);
            thread = new WPLoginCheckThread(editURL.getText().toString().trim(), editUsername
                    .getText().toString().trim(), editPassword.getText().toString().trim(), handler);
            thread.start();
        }
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            dialog.dismiss();
            if (msg.what == 1) {
                if (thread.isOK()) {
                    Toast.makeText(getBaseContext(), getString(R.string.lab_credentials_ok),
                            Toast.LENGTH_LONG).show();
                    enableAllControls();
                } else {
                    Toast.makeText(getBaseContext(), getString(R.string.lab_credentials_wrong),
                            Toast.LENGTH_LONG).show();
                }
            }

            if (msg.getData().containsKey("ex")) {
                Toast.makeText(getBaseContext(), msg.getData().getString("ex"), Toast.LENGTH_LONG)
                        .show();
            }
            super.handleMessage(msg);
        }

    };

    private void enableAllControls() {
        btnSave.setEnabled(true);
    }
}
