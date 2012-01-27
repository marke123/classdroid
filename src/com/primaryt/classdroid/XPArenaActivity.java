package com.primaryt.classdroid;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
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

public class XPArenaActivity extends ClassdroidActivity implements OnDismissListener {
	private EditText editURL;
	private CheckBox cbDefault;
	private EditText editUsername;
	private EditText editPassword;
	private EditText editNickname;

	private long id;
	private PupilServices service;
	private boolean edit;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xparenaconfiguration);

		Bundle bundle = getIntent().getExtras();
		if (bundle.containsKey("id")) {
			id = bundle.getLong("id");
			DBAdapter dbAdapter = new DBAdapter(this);
			dbAdapter.open();
			service = dbAdapter.getPupilServicesById(id);
			dbAdapter.close();
		}
		if (bundle.containsKey("edit")) {
			edit = bundle.getBoolean("edit");
		}
		initializeUIElements();

		if (edit) {
			populate();
		}
	}

	private void populate() {
		if (service.getUrl() != null) {
			editURL.setText(service.getUrl());
		}
		editUsername.setText(service.getUsername() == null ? "" : service
				.getUsername());
		editPassword.setText(service.getPassword() == null ? "" : service
				.getPassword());
		editNickname.setText(service.getNickname());

		if (service.getUseDefault() == PupilServices.USE_DEFAULT) {
			cbDefault.setChecked(true);
		} else {
			cbDefault.setChecked(false);
		}
	}

	private void initializeUIElements() {
		editURL = (EditText) findViewById(R.id.editUrl);
		AppUtils utils = new AppUtils(this);
		editURL.setText(utils.getDefaultXPURL());

		editUsername = (EditText) findViewById(R.id.editTeacherUsername);

		editPassword = (EditText) findViewById(R.id.editTeacherPassword);

		editNickname = (EditText) findViewById(R.id.editNickname);

		cbDefault = (CheckBox) findViewById(R.id.cbUseDefault);
		if (TextUtils.isEmpty(utils.getDefXPUser())) {
			cbDefault.setChecked(false);
		}

		if (cbDefault.isChecked()) {
			showDefaultOptions();
		} else {
			showCustomOptions();
		}
		cbDefault.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					showDefaultOptions();
				} else {
					showCustomOptions();
				}
			}
		});

		Button btnSave = (Button) findViewById(R.id.btnSave);
		btnSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				saveAction();
			}
		});

		Button btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				cancelAction();
			}
		});
	}

	private void saveAction() {
		AppUtils utils = new AppUtils(this);
		service.setUrl(editURL.getText().toString().trim());
		utils.setDefaultXPURL(service.getUrl());

		service.setNickname(editNickname.getText().toString().trim());
		if (!cbDefault.isChecked()) {
			service.setUsername(editUsername.getText().toString());
			service.setPassword(editPassword.getText().toString());
			service.setUseDefault(PupilServices.USE_CUSTOM);
			utils.setDefaultCredsXP(editUsername.getText().toString(),
					editPassword.getText().toString());
		} else {
			service.setUsername(utils.getDefXPUser());
			service.setPassword(utils.getDefXPPass());
			service.setUseDefault(PupilServices.USE_DEFAULT);
		}
		if (!cbDefault.isChecked() && isUsernamePasswordOK()
				|| cbDefault.isChecked() && isURLOK()) {
			DBAdapter dbAdapter = new DBAdapter(this);
			dbAdapter.open();
			dbAdapter.updatePupilService(service);
			dbAdapter.close();
			setResult(RESULT_OK);
			finish();
		}
	}

	private void cancelAction() {
		setResult(RESULT_CANCELED);
		finish();
	}

	private boolean isUsernamePasswordOK() {
		boolean value = true;
		if (!isURLOK()) {
			value = false;
		} else if (TextUtils.isEmpty(editUsername.getText().toString())) {
			value = false;
			Toast.makeText(this, getString(R.string.lab_enter_username),
					Toast.LENGTH_LONG).show();
		} else if (TextUtils.isEmpty(editPassword.getText().toString())) {
			value = false;
			Toast.makeText(this, getString(R.string.lab_enter_password),
					Toast.LENGTH_LONG).show();
		}
		return value;
	}

	private boolean isURLOK() {
		boolean value = true;
		if (TextUtils.isEmpty(editURL.getText().toString().trim())) {
			Toast.makeText(this, getString(R.string.lab_enter_url), Toast.LENGTH_LONG).show();
			value = false;
		} else if (TextUtils.isEmpty(editNickname.getText().toString().trim())) {
			Toast.makeText(this, getString(R.string.lab_enter_nick), Toast.LENGTH_LONG)
					.show();
			value = false;
		}
		return value;
	}

	private void showDefaultOptions() {
		AppUtils utils = new AppUtils(this);
		if(TextUtils.isEmpty(utils.getDefXPUser())){
			DefaultValuesDialog dialog = new DefaultValuesDialog(this);
			dialog.setTitle(getString(R.string.lab_xp_arena_default));
			dialog.setType(DefaultValuesDialog.TYPE_XP_ARENA);
			dialog.setOnDismissListener(this);
			dialog.show();
		}else{
			editURL.setText(utils.getDefaultXPURL());
			editUsername.setText(utils.getDefXPUser());
			editPassword.setText(utils.getDefXPPass());
			editUsername.setEnabled(false);
			editPassword.setEnabled(false);	
		}
	}

	private void showCustomOptions() {
		editUsername.setEnabled(true);
		editPassword.setEnabled(true);
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		if(dialog instanceof DefaultValuesDialog){
			DefaultValuesDialog dia = (DefaultValuesDialog)dialog;
			if(dia.getSaved()){
				AppUtils utils = new AppUtils(XPArenaActivity.this);
				editURL.setText(utils.getDefaultXPURL());
				editUsername.setText(utils.getDefXPUser());
				editPassword.setText(utils.getDefXPPass());
				editUsername.setEnabled(false);
				editPassword.setEnabled(false);	
			}else{
				cbDefault.setChecked(false);
			}
		}
	}

}

