package com.trilink.androidlib.update;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.LinearLayout;

import com.trilink.androidlib.R;


public class MyVersionDialog  extends Dialog implements DialogInterface {
	private PriorityListener listener;
	LinearLayout linearOk;
	LinearLayout linearCancel;

	public MyVersionDialog(Context context) {
		this(context, R.style.dialogNeed);
	}

	public MyVersionDialog(Context context, int theme) {
		super(context, theme);
		setContentView(R.layout.a_version_selector);
		initView();
		initListener();
	}

	public MyVersionDialog(Context context, int theme, PriorityListener listener) {
		this(context, theme);
		this.listener = listener;

		setContentView(R.layout.a_version_selector);
		initView();
		initListener();
	}


	private void initView() {
		linearOk = (LinearLayout) findViewById(R.id.ll_button_ok);
		linearCancel = (LinearLayout) findViewById(R.id.ll_button_cancel);
	}



	private void initListener() {
		linearOk.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				listener.refreshPriorityUI();
				MyVersionDialog.this.dismiss();
			}
		});
		linearCancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				listener.cancelPriority();
				MyVersionDialog.this.dismiss();
			}
		});

	}

	public interface PriorityListener {
		public void refreshPriorityUI();
		public void cancelPriority();
	}

}
