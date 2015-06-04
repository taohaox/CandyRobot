package com.cute.xiaoy;

import java.io.File;
import java.io.FileNotFoundException;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cute.xiaoy.db.User;
import com.cute.xiaoy.db.UserDao;

public class SettingActivity extends BaseActivity{
	private UserDao dao; //�������ݿ��dao
	private User user;//�����û���Ϣ
	
	private EditText et_name;
	private EditText et_age;
	private EditText et_robotname;
	private RadioGroup rg_sex;
	private RadioButton rb_sex_man;
	private RadioButton rb_sex_woman;
	private RadioButton rb_sex;
	
	private Button bt_save;
	private Button bt_cancel;
	
	private ImageView img_head; //ͷ��
	private RelativeLayout rl_head;  //ͷ�����ڵ���
	
	private String filepath ; //����ͷ����ļ�·��
	//��ȡͷ���code
	public static final int GETHEADIMG=1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//ȥ������ͷ ��Ҫ��setcontentview֮ǰ  
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//���ҳ����Ⱦ
		setContentView(R.layout.acticity_setting);
		//���ñ��������
		View view  = findViewById(R.id.title_setting);
		TextView tv_title = (TextView)view.findViewById(R.id.tv_title);
		tv_title.setText("������Ϣ");
		//�������ð�ť
		Button bt_edit = (Button) view.findViewById(R.id.bt_title_edit);
		bt_edit.setVisibility(View.INVISIBLE);
		initView();
		initData();
		ininListener();
	}
	
	

	private void initView() {
		et_name = (EditText) findViewById(R.id.set_et_name);
		et_age = (EditText) findViewById(R.id.set_et_userage);
		et_robotname = (EditText) findViewById(R.id.set_et_robotname);
		
		rg_sex = (RadioGroup) findViewById(R.id.set_rg_sex);
		rb_sex_man = (RadioButton) findViewById(R.id.set_radio0);
		rb_sex_woman = (RadioButton) findViewById(R.id.set_radio1);
		
		bt_cancel = (Button) findViewById(R.id.set_bt_cancel);
		bt_save = (Button) findViewById(R.id.set_bt_save);
		
		img_head = (ImageView) findViewById(R.id.img_head);
		rl_head =(RelativeLayout) findViewById(R.id.rl_head);
	}
	
	private void initData()  {
		Intent intent = getIntent();
		int user_id = intent.getIntExtra("user_id", -1);
		dao = new UserDao(this);
		user = dao.getUserById(user_id);
		if(user.getName()!=null){
			et_name.setText(user.getName());
		}else{
			et_name.setText(user.getUsername());
		}
		if(user.getRobotname()!=null){
			et_robotname.setText(user.getRobotname());
		}else{
			et_robotname.setText("doubi");
		}
		et_age.setText(user.getAge()+"");
		
		if("��".equals(user.getSex())){
			rb_sex_man.setChecked(true);
		}else{
			rb_sex_woman.setChecked(true);
		}
		
		//�������ļ��ж�ȡ�� ͷ���uri��string
		SharedPreferences  sf = getSharedPreferences("config_headimg.xml", MODE_PRIVATE);
		filepath = sf.getString("filepath", "");
		System.out.println(filepath);
		if(!"".equals(filepath)){
			Uri uri = Uri.parse(filepath);
			img_head.setImageBitmap(BitmapFactory.decodeFile(filepath));
		}
		
		
	}
	private void ininListener() {
		bt_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		bt_save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String name = et_name.getText().toString().trim();
				String robotname = et_robotname.getText().toString().trim();
				String ages = et_age.getText().toString().trim();
				int age = 0;
				
				if(!ages.matches("\\d+")){
					Toast.makeText(SettingActivity.this, "�Ƿ�������!", 0).show();
					return;
				}else{
					age = Integer.parseInt(ages);
					if(age<0 || age>120){
						Toast.makeText(SettingActivity.this, "�������䳬��������Χ!!��ȷ���㲻��������!!", 0).show();
						return;
					}
				}
				if(name.length()>4){
					Toast.makeText(SettingActivity.this, "�������ֳ��Ȳ��ܳ���4���ַ�!!", 0).show();
					return;
				}
				if(robotname.length()>4){
					Toast.makeText(SettingActivity.this, "�����˵����ֳ��Ȳ��ܳ���4���ַ�!!", 0).show();
					return;
				}
				rb_sex = (RadioButton) findViewById(rg_sex.getCheckedRadioButtonId());
				String sex = rb_sex.getText().toString().trim();
				
				
				user.setAge(age);
				user.setName(name);
				user.setRobotname(robotname);
				user.setSex(sex);
				
				boolean isSave = dao.updateUser(user);
				if(isSave){
					Toast.makeText(SettingActivity.this, "�������ϱ���ɹ�!", 0).show();
				}else{
					Toast.makeText(SettingActivity.this, "�������ϱ���ʧ��!", 0).show();
					return;
				}
				
				//�������ҳ�� ��ǰһ��ҳ�淵������
				Intent intent = new Intent();
				intent.putExtra("user", user);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		//���ͷ���ʱ��
		img_head.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SettingActivity.this,ImageScanActivity.class);
				intent.putExtra("filepath", filepath);
				startActivity(intent);
			}
		});
		
		//���ͷ�����ڵ���
		rl_head.setOnClickListener(new OnClickListener() {
			
			//setͷ��
			@Override
			public void onClick(View v) {
				//���������ý��� activity
				Intent intent = new Intent(SettingActivity.this,SettingActivity_HeadImg.class);
				startActivityForResult(intent, GETHEADIMG);
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
			switch (requestCode) {
			case GETHEADIMG:
				if(resultCode==RESULT_OK){
					try {
						String filepath = data.getStringExtra("filepath");
						Uri imageuri = Uri.fromFile(new File(filepath));
						img_head.setImageBitmap(BitmapFactory.decodeStream(getContentResolver().openInputStream(imageuri)));
						SharedPreferences  sf = getSharedPreferences("config_headimg.xml", MODE_PRIVATE);
						Editor editor = sf.edit();
						editor.putString("filepath", filepath);
						editor.commit();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Toast.makeText(this, "ͷ�����óɹ�", 0).show();
					break;
				}
			default:
				break;
			}
	}
	
}
