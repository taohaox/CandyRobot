package com.cute.xiaoy;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class TitleLayout extends LinearLayout {

	public TitleLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.title, this);
		ImageView bt_title_back = (ImageView) findViewById(R.id.bt_title_back);
		ImageView bt_title_edit = (ImageView) findViewById(R.id.bt_title_edit);
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
