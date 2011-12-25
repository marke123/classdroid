package com.primaryt.classdroid;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.primaryt.classdroid.temp.AppUtils;

public class DefaultValuesDialog extends Dialog implements
		android.view.View.OnClickListener {
	private final static int ACTION_SAVE = 0;
	private final static int ACTION_CANCEL = 1;

	public final static int TYPE_PRI_BLOGGER = 0;
	public final static int TYPE_XP_ARENA = 1;

	private boolean isSaved;

	private int type;
	private Context context;
	private EditText editURL;
	private EditText editUsername;
	private EditText editPassword;

	public DefaultValuesDialog(Context context) {
		super(context);
		initializeUI(context);
	}

	public DefaultValuesDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		initializeUI(context);
	}

	public DefaultValuesDialog(Context context, int theme) {
		super(context, theme);
		initializeUI(context);
	}

	private void initializeUI(Context context) {
		this.context = context;
		setContentView(R.layout.default_values);

		editURL = (EditText) findViewById(R.id.editUrl);

		editUsername = (EditText) findViewById(R.id.editUsername);

		editPassword = (EditText) findViewById(R.id.editPassword);

		Button btnSave = (Button) findViewById(R.id.btnSave);
		btnSave.setId(ACTION_SAVE);
		btnSave.setOnClickListener(this);

		Button btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setId(ACTION_CANCEL);
		btnCancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case ACTION_CANCEL:
			isSaved = false;
			dismiss();
			break;

		case ACTION_SAVE:
			if (TextUtils.isEmpty(editURL.getText().toString().trim())) {
				Toast.makeText(context, context.getString(R.string.lab_enter_default_url),
						Toast.LENGTH_LONG).show();
			} else if (TextUtils.isEmpty(editUsername.getText().toString()
					.trim())) {
				Toast.makeText(context, context.getString(R.string.lab_enter_default_username),
						Toast.LENGTH_LONG).show();
			} else if (TextUtils.isEmpty(editPassword.getText().toString()
					.trim())) {
				Toast.makeText(context, context.getString(R.string.lab_enter_default_password),
						Toast.LENGTH_LONG).show();
			} else {
				AppUtils utils = new AppUtils(context);
				if (type == TYPE_PRI_BLOGGER) {
					utils.setDefaultCredsPrimary(editUsername.getText()
							.toString().trim(), editPassword.getText()
							.toString().trim());
					utils.setDefaultPrimaryURL(editURL.getText().toString()
							.trim());
				} else if (type == TYPE_XP_ARENA) {
					utils.setDefaultCredsXP(editUsername.getText().toString()
							.trim(), editPassword.getText().toString().trim());
					utils.setDefaultXPURL(editURL.getText().toString().trim());
				}
				isSaved = true;
				dismiss();
			}
			break;
		}
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean getSaved() {
		return isSaved;
	}
}
