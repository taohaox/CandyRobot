package com.cute.xiaoy;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.cute.xiaoy.utils.ImageUtil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class SettingActivity_HeadImg extends BaseActivity{
	Button choice;
	Button photograph;
	File dir;
	String imgname = "me.jpg";
	Uri imageuri;
	File file ; 
	public static final int TAKE_PHOTO=0;  
	public static final int CROP_PHOTO=1;
	int screen_width = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_setting_headimg);
		
		//��ȡ��Ļ���
		WindowManager wmanager = (WindowManager) getSystemService(WINDOW_SERVICE);
		screen_width = wmanager.getDefaultDisplay().getWidth();
		System.out.println("screen_width:"+screen_width);
		choice = (Button) findViewById(R.id.bt_choice_by_photos);
		photograph = (Button) findViewById(R.id.bt_photograph);
		dir = Environment.getExternalStorageDirectory();//��ȡӦ�ó�����ļ�����·��
		//����һ���ļ�  ���������ĺõ���Ƭ
		file = new File(dir,imgname);
		if(file.exists()){
			file.delete();
		}
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		imageuri = Uri.fromFile(file);
		initListener();
		
	}
	private void initListener() {
		//�����ѡ��
		choice.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.putExtra("crop", "true");  //set�ü�
                intent.setDataAndType(imageuri, "image/*");
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri);  //���·��
				intent.putExtra("aspectX", 1);//�ü������ 
	            intent.putExtra("aspectY", 1); 
	            intent.putExtra("outputX", screen_width);//���ͼƬ��С 
	            intent.putExtra("outputY", screen_width); 
				
				startActivityForResult(intent, CROP_PHOTO);
			}
		});
		//����
		photograph.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//System.out.println(imageuri.getEncodedSchemeSpecificPart());
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  //���յ���ͼ
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri);  //�������·��
				startActivityForResult(intent, TAKE_PHOTO);  //�����������
				
				
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		File file = new File(dir,imgname);
		switch (requestCode) {
		case TAKE_PHOTO:
			Toast.makeText(this, "���ش�����Ƭ", 0).show();
			if(resultCode==RESULT_OK){
				
				Intent intent = new Intent("com.android.camera.action.CROP");//���òü�
				intent.setDataAndType(imageuri, "image/*");
				intent.putExtra("scale", true);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri);
				intent.putExtra("aspectX", 1);//�ü������ 
	            intent.putExtra("aspectY", 1); 
	            intent.putExtra("outputX", screen_width);//���ͼƬ��С 
	            intent.putExtra("outputY", screen_width); 
				startActivityForResult(intent, CROP_PHOTO);
			}
			break;
		case CROP_PHOTO:
			if(resultCode==RESULT_OK){
				try {
					BufferedInputStream bis = new BufferedInputStream(getContentResolver().openInputStream(imageuri)); 
					//��ͼƬд��Ӳ��
					BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
					byte buffer[] =new byte[1024];
					int len = 0;
					while((len = bis.read(buffer))!=-1){
						bos.write(buffer, 0, len);
					}
					bos.close();
					bis.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Intent intent = new Intent();
				intent.putExtra("filepath", file.getPath());
				setResult(RESULT_OK, intent);
				finish();
			}
			break;
		default:
			break;
		}
	}
}
