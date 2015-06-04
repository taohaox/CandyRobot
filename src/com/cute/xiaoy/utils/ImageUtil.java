package com.cute.xiaoy.utils;

import java.io.File;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Display;
import android.view.WindowManager;

public class ImageUtil {
	public static Bitmap scaleImg(Context context,File file){
		Options opts = new Options();
		opts.inJustDecodeBounds=true;
		BitmapFactory.decodeFile(file.getPath(), opts);
		//img_load.setImageBitmap(bitmap);
		double width = opts.outHeight;
		double height = opts.outHeight;
		
		WindowManager manager = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		
		//api版本太高  不能向下兼容,所以使用过时的方法
		//Point point = new Point();
		//display.getSize(point);
		//int screenWidth = point.x;
		//int screenHeight = point.y;
		
		double screenWidth = display.getHeight();
		double screenHeight = display.getWidth();
		System.out.println("图片宽 width:"+width);
		System.out.println("图片高 height:"+height);
		System.out.println("屏幕宽 screenWidth:"+screenWidth);
		System.out.println("屏幕高 screenHeight:"+screenHeight);
		//缩放比列
		double scaledWidth =  width/screenWidth;   
		double scaledHeight =  height/screenHeight;
		
		double scaled = 1;
		if(scaledWidth>1 && scaledHeight>scaledWidth){
			scaled = scaledHeight;
		}
		if(scaledHeight>1 && scaledWidth>scaledHeight){
			scaled = scaledWidth;
		}
		//设置图片总的缩放比
		opts.inSampleSize= (int) scaled;
		System.out.println("缩放比列为:"+scaled);
		opts.inJustDecodeBounds=false;
		Bitmap bm = BitmapFactory.decodeFile(file.getPath(), opts);
		return bm;
	}
	
	
	
}
