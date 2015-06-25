package com.cute.xiaoy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class TitleLayout extends LinearLayout {

	public TitleLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.title, this);
		Button bt_title_back = (Button) findViewById(R.id.bt_title_back);
		Button bt_title_edit = (Button) findViewById(R.id.bt_title_edit);
		if(bt_title_back!=null)
		bt_title_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((Activity)getContext()).finish();
			}
		});
		if(bt_title_edit!=null)
		bt_title_edit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(TitleLayout.this.getContext(), "next try agin", 0).show();
			}
		});
	}


}
