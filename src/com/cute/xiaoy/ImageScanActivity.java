package com.cute.xiaoy;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;


public class ImageScanActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.action_bigheadimg);
		ImageView img_bigheadimg = (ImageView) findViewById(R.id.img_bigheadimg);
		
		String filepath = getIntent().getStringExtra("filepath");
		img_bigheadimg.setImageBitmap(BitmapFactory.decodeFile(filepath));
	}
}
