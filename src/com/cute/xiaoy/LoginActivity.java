package com.cute.xiaoy;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cute.xiaoy.db.UserDao;

public class LoginActivity extends BaseActivity {
	EditText et_username;
	EditText et_password;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		
		//֪ͨ������ȡ��1��֪ͨ
		NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		manager.cancel(1);
		
		
		//���ñ��������
		View view  = findViewById(R.id.title_login);
		TextView tv_title = (TextView)view.findViewById(R.id.tv_title);
		tv_title.setText("�û���¼");
		//�������ð�ť
		Button bt_edit = (Button) view.findViewById(R.id.bt_title_edit);
		bt_edit.setVisibility(View.INVISIBLE);
		
		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);
		
		String username2register = getIntent().getStringExtra("username");
		if(username2register==null){
			SharedPreferences sp = getSharedPreferences("login",Context.MODE_PRIVATE);
			et_username.setText(sp.getString("username", "")) ;
			et_password.setText(sp.getString("password", "")) ;
		}else{
			et_username.setText(username2register) ;
		}
		
	}
	
	public void login(View view){
		UserDao dao = new UserDao(this);
		String username = et_username.getText().toString().trim();
		if("".equals(username)){
			Toast.makeText(this, "�û�������Ϊ��", 0).show();
			return ;
		}
		String password = et_password.getText().toString().trim();
		if("".equals(password)){
			Toast.makeText(this, "���벻��Ϊ��", 0).show();
			return ;
		}
		String user = dao.findUserByName(username, password);
		if(user==null){
			Toast.makeText(this, "�˺Ż��������", 0).show();
			return;
		}
		
		//�����½��Ϣ
		SharedPreferences sp = getSharedPreferences("login",Context.MODE_PRIVATE);
		if(sp!=null){
			Editor editor = sp.edit();
			editor.putString("username", username);
			editor.putString("password", password);
			editor.commit();
		}
		
		
		
		//��ת����ҳ��
		int user_id = dao.getIdByUsername(username);
		
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("username", username);
		intent.putExtra("user_id", user_id);
		startActivity(intent);
		finish();
	}
	
	public void register(View view){
		Intent intent = new Intent(this, RegisterActivity.class);
		startActivity(intent);
	}
}
