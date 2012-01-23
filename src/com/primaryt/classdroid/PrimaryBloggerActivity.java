package com.primaryt.classdroid;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
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

import com.primaryt.classdroid.bo.PupilServices;
import com.primaryt.classdroid.db.DBAdapter;
import com.primaryt.classdroid.temp.AppUtils;
import com.primaryt.classdroid.threads.WPLoginCheckThread;

public class PrimaryBloggerActivity extends ClassdroidActivity implements
		OnDismissListener {

	private EditText editURL;

	private CheckBox cbDefault;

	private EditText editUsername;

	private EditText editPassword;

	private long id;

	private PupilServices service;

	private boolean edit;

	private WPLoginCheckThread thread;

	private Button btnSave;

	private ProgressDialog dialog;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.primarybloggerconfiguration);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
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
		if (service.getUseDefault() == PupilServices.USE_DEFAULT) {
			cbDefault.setChecked(true);
		} else {
			cbDefault.setChecked(false);
		}
	}

	private void initializeUIElements() {
		editURL = (EditText) findViewById(R.id.editUrl);
		editURL.setSingleLine();
		AppUtils utils = new AppUtils(this);
		editURL.setText(utils.getDefaultPrimaryURL());

		editUsername = (EditText) findViewById(R.id.editTeacherUsername);
		editUsername.setSingleLine();

		editPassword = (EditText) findViewById(R.id.editTeacherPassword);

		cbDefault = (CheckBox) findViewById(R.id.cbUseDefault);
		if (TextUtils.isEmpty(utils.getDefPriUser())) {
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

		btnSave = (Button) findViewById(R.id.btnSave);
		btnSave.setEnabled(false);
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

		Button btnTest = (Button) findViewById(R.id.btnTest);
		btnTest.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				testAction();
			}
		});

		CheckBox cbShowPassword = (CheckBox) findViewById(R.id.cbShowPassword);
		cbShowPassword
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean value) {
						if (value) {
							editPassword.setTransformationMethod(null);
						} else {
							editPassword
									.setTransformationMethod(PasswordTransformationMethod
											.getInstance());
						}
					}
				});
	}

	private void testAction() {
		dialog = new ProgressDialog(this);
		dialog.setTitle(getString(R.string.lab_please_wait));
		dialog.setMessage(getString(R.string.lab_checking_credentials));
		dialog.show();
		btnSave.setEnabled(false);
		thread = new WPLoginCheckThread(editURL.getText().toString().trim(),
				editUsername.getText().toString().trim(), editPassword
						.getText().toString().trim(), handler);
		thread.start();
	}

	private void saveAction() {
		AppUtils utils = new AppUtils(this);
		service.setUrl(editURL.getText().toString().trim());
		utils.setDefaultPrimaryURL(service.getUrl());

		if (!cbDefault.isChecked()) {
			service.setUsername(editUsername.getText().toString());
			service.setPassword(editPassword.getText().toString());
			service.setUseDefault(PupilServices.USE_CUSTOM);
			utils.setDefaultCredsPrimary(editUsername.getText().toString(),
					editPassword.getText().toString());
		} else {
			service.setUsername(utils.getDefPriUser());
			service.setPassword(utils.getDefPriPass());
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
		if (TextUtils.isEmpty(editURL.getText().toString().trim())) {
			Toast.makeText(this, getString(R.string.lab_enter_url),
					Toast.LENGTH_LONG).show();
			return false;
		} else {
			return true;
		}
	}

	private void showDefaultOptions() {
		AppUtils utils = new AppUtils(this);
		if (TextUtils.isEmpty(utils.getDefPriUser())) {
			DefaultValuesDialog dialog = new DefaultValuesDialog(this);
			dialog.setTitle(getString(R.string.lab_pri_blogger_default));
			dialog.setType(DefaultValuesDialog.TYPE_PRI_BLOGGER);
			dialog.setOnDismissListener(this);
			dialog.show();
		} else {
			editURL.setText(utils.getDefaultPrimaryURL());
			editUsername.setText(utils.getDefPriUser());
			editPassword.setText(utils.getDefPriPass());
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
		if (dialog instanceof DefaultValuesDialog) {
			DefaultValuesDialog dia = (DefaultValuesDialog) dialog;
			if (dia.getSaved()) {
				AppUtils utils = new AppUtils(PrimaryBloggerActivity.this);
				editURL.setText(utils.getDefaultPrimaryURL());
				editUsername.setText(utils.getDefPriUser());
				editPassword.setText(utils.getDefPriPass());
				editUsername.setEnabled(false);
				editPassword.setEnabled(false);
			} else {
				cbDefault.setChecked(false);
			}
		}
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			dialog.dismiss();
			if (msg.what == 1) {
				if (thread.isOK()) {
					Toast.makeText(getBaseContext(),
							getString(R.string.lab_credentials_ok),
							Toast.LENGTH_LONG).show();
					enableAllControls();
				} else {
					Toast.makeText(getBaseContext(),
							getString(R.string.lab_credentials_wrong),
							Toast.LENGTH_LONG).show();
				}
			}

			if (msg.getData().containsKey("ex")) {
				Toast.makeText(getBaseContext(), msg.getData().getString("ex"),
						Toast.LENGTH_LONG).show();
			}
			super.handleMessage(msg);
		}

	};

	private void enableAllControls() {
		btnSave.setEnabled(true);
	}
}
