package com.primaryt.classdroid.temp;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.primaryt.classdroid.R;

public class ConfirmDialog extends Dialog implements OnClickListener {

	public final static int ACTION_YES = 1;
	public final static int ACTION_NO = 2;
	public int result;
	public ConfirmDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		init();
	}

	public ConfirmDialog(Context context, int theme) {
		super(context, theme);
		init();
	}

	public ConfirmDialog(Context context) {
		super(context);
		init();
	}

	private void init() {
		setCancelable(false);
		setCanceledOnTouchOutside(false);
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.confirm_dialog, null);
		Button btnYes = (Button) view.findViewById(R.id.btnYes);
		btnYes.setOnClickListener(this);
		btnYes.setId(0);

		Button btnNo = (Button) view.findViewById(R.id.btnNo);
		btnNo.setOnClickListener(this);
		btnNo.setId(1);
		
		setContentView(view);
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==0){
			result = ACTION_YES;
		}else if(v.getId() == 1){
			result = ACTION_NO;
		}
		dismiss();
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

}
