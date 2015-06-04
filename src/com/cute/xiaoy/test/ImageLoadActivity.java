package com.cute.xiaoy.test;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.cute.xiaoy.BaseActivity;
import com.cute.xiaoy.R;
import com.cute.xiaoy.utils.ImageUtil;

public class ImageLoadActivity extends BaseActivity{
	ImageView img_load;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_image);
		img_load= (ImageView) findViewById(R.id.img_load);
	}
	
	public void load(View view){
		File dir = Environment.getExternalStorageDirectory();
		System.out.println(dir.getPath());
		File file = new File(dir,"Jellyfish.jpg");
		img_load.setImageBitmap(ImageUtil.scaleImg(this, file));
	}
}
