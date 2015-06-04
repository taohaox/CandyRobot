package com.cute.xiaoy;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cute.xiaoy.db.User;
import com.cute.xiaoy.db.UserDao;

public class RegisterActivity extends BaseActivity{
	EditText et_username;
	EditText et_password;
	EditText et_password_again;
	EditText et_userage;
	RadioGroup rg_sex;
	RadioButton rb_sex;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);
		//���ñ��������
		View view  = findViewById(R.id.title_register);
		TextView tv_title = (TextView)view.findViewById(R.id.tv_title);
		tv_title.setText("�û�ע��");
		//�������ð�ť
		Button bt_edit = (Button) view.findViewById(R.id.bt_title_edit);
		bt_edit.setVisibility(View.INVISIBLE);
		
		et_userage = (EditText) findViewById(R.id.et_userage);
		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);
		et_password_again = (EditText) findViewById(R.id.et_password_again);
		rg_sex = (RadioGroup) findViewById(R.id.rg_sex);
		
	}
	public void create_user(View view){
		UserDao dao = new UserDao(this);
		String username = et_username.getText().toString().trim();
		if("".equals(username)){
			Toast.makeText(this, "�û�������Ϊ��", 0).show();
			return ;
		}
		if(!dao.checkUsername(username)){
			Toast.makeText(this, "�û����Ѵ���", 0).show();
			return ;
		}
		String password = et_password.getText().toString().trim();
		String password_again = et_password_again.getText().toString().trim();
		if("".equals(password)){
			Toast.makeText(this, "���벻��Ϊ��", 0).show();
			
			return ;
		}else{
			if(!password.equals(password_again)){
				Toast.makeText(this, "������������벻һ��,����������!", 0).show();
				return;
			}
		}
		String ages = et_userage.getText().toString().trim();
		int age = 0;
		if(!ages.matches("\\d+")){
			Toast.makeText(this, "�Ƿ�������!", 0).show();
			return;
		}else{
			age = Integer.parseInt(ages);
			if(age<0 || age>120){
				Toast.makeText(this, "�������䳬��������Χ!!��ȷ���㲻��������!!", 0).show();
				return;
			}
		}
		rb_sex = (RadioButton) findViewById(rg_sex.getCheckedRadioButtonId());
		String sex = rb_sex.getText().toString().trim();
		
		boolean b = dao.insertUser(new User(username, password,age , sex, 
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
		if(b){
			Toast.makeText(this, "�����û��ɹ�,�����µ�½!", 0).show();
			//��ת����½ҳ�� .
			Intent intent = new Intent(this, LoginActivity.class);
			intent.putExtra("username", username);
			startActivity(intent);
			finish();
		}else{
			Toast.makeText(this, "�Բ���,�����û�ʧ��", 0).show();
		}
	}
	public void back(View view){
		/*Intent intent = new Intent(this,LoginActivity.class);
		startActivity(intent);*/
		finish();
		
	}
}
