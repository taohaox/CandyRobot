package com.cute.xiaoy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cute.xiaoy.adapter.ChatMassageAdapter;
import com.cute.xiaoy.db.User;
import com.cute.xiaoy.db.UserDao;
import com.cute.xiaoy.entity.ChatMassage;
import com.cute.xiaoy.entity.ChatMassage.Type;
import com.cute.xiaoy.receiver.MyReceiver;
import com.cute.xiaoy.test.ImageLoadActivity;
import com.cute.xiaoy.utils.Utils;

public class MainActivity extends BaseActivity {
	private List<ChatMassage> list;
	private ListView mlistview;
	private ChatMassageAdapter madapter;
	
	private Button bt_send;
	private EditText et_send_msg_info;
	private TextView to_name;
	
	public static int user_id; // �û�id
	
	private User user; //�����û���Ϣ
	
	private UserDao dao;//���ݿ����dao
	
	public static boolean isNetworkConnected; //�Ƿ�����������   ��onresume()�����ж�  ��Ϊfalse �򲻻ᷢ����Ϣ��������
	
	private BroadcastReceiver mReceiver = new MyReceiver(); 

		
		 
	
	
	 
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			//�ȴ�����  ���߳���ɵ����ݵķ���
			ChatMassage getmsg = (ChatMassage) msg.obj;
			int id = (int) dao.insertMsg(getmsg);
			getmsg.setId(id);
			list.add(getmsg);
			//֪ͨ����������listview
			madapter.notifyDataSetChanged();
			//����������  ��λ�����һ�� 
			mlistview.setSelection(madapter.getCount()-1);
			
			
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//ȥ������ͷ ��Ҫ��setcontentview֮ǰ  
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//���ҳ����Ⱦ
		setContentView(R.layout.activity_main);
		//���ñ��������
		View view  = findViewById(R.id.title_main);
		TextView tv_title = (TextView)view.findViewById(R.id.tv_title);
		tv_title.setText("Сy��");
		
		//tv_title.setText("Сy��");
		initView();
		initData();
		initListener();
		mlistview.setAdapter(madapter);
		
		//ע���������
		IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, mFilter);
		
		//RelativeLayout main = (RelativeLayout) findViewById(R.id.main);
		
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//ȡ������
		unregisterReceiver(mReceiver);
		
	}
	/**
	 * activity���������� onCreate onStart onResume onPause onStop onDestory ֮һ  
	 * ȡ�ý��� ʹ�ø�activity����Ӧ�����¼�
	 * 
	 */
	@Override
	protected void onResume() {
		super.onResume();
		
		
		//������������Ƿ���Ч
		ConnectivityManager mManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = mManager.getActiveNetworkInfo();
		if(networkinfo!=null){
			isNetworkConnected = networkinfo.isAvailable();
		}
		if(!isNetworkConnected){
			openSetting();
		}
		
		//֪ͨ����������listview
		madapter.notifyDataSetChanged();
		//����������  ��λ�����һ�� 
		mlistview.setSelection(madapter.getCount()-1);
		
	}
	/**
	 *������ʱ���õķ���  ��ʾ�Ի���  ǰ�����ý���
	 */
	public void openSetting(){
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage("��������Ҫ��������,�������������쳣,�Ƿ�ǰ������?");
		builder.setCancelable(false);//�޷�ͨ�����ؼ�ȡ���Ի���
		builder.setPositiveButton("��", new android.content.DialogInterface.OnClickListener(){
			/**
			 * ǰ����������
			 */
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = null;
				if (android.os.Build.VERSION.SDK_INT > 10) {
					intent = new Intent(
						android.provider.Settings.ACTION_SETTINGS);
				} else {
					intent = new Intent();
					ComponentName component = new ComponentName(
							"com.android.settings",
							"com.android.settings.WirelessSettings");
					intent.setComponent(component);
					intent.setAction("android.intent.action.VIEW");
				}
				startActivity(intent);
			}
		});
		//���ڵ�ǰҳ��
		builder.setNegativeButton("��", new android.content.DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				
			}
		});
		builder.show();
	}
	//�����˵�
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	//�˵�����¼�
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent intent = new Intent(this, SettingActivity.class);
			intent.putExtra("user_id", user_id);
			//�ڴ���һ��ҳ�����ٺ� �᷵�����ݵ�ǰһ��ҳ��
			startActivityForResult(intent, 1);
			break;

		case R.id.action_back2login:
			Intent intent1 = new Intent(this, LoginActivity.class);
			
			startActivity(intent1);
			finish();
			break;
		case R.id.action_remove:
			Toast.makeText(this, "You clicked remove,you can remove anything!", 0).show();
			break;
		case R.id.action_forceoffline:
			Intent filterIntent = new Intent("com.cute.xiaoy.receiver.FORCE_OFFLINE");
			sendBroadcast(filterIntent);
			break;
		case R.id.action_notification:
			NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			Notification noti = new Notification(R.drawable.icon, "You have a Message", System.currentTimeMillis());
			PendingIntent pendingintent = PendingIntent.getActivity(this, 0, new Intent(this,LoginActivity.class), PendingIntent.FLAG_CANCEL_CURRENT);
			noti.setLatestEventInfo(this, "You have a Message", "hello! "+user.getName()+", Wellcome to China!", pendingintent);
			noti.defaults=Notification.DEFAULT_ALL;
			manager.notify(1, noti);
			break;
		case R.id.action_loadbigimage:
			Intent intent_load = new Intent(this,ImageLoadActivity.class);
			startActivity(intent_load);
			break;
		}
		return true;
	}
	/**
	 * �����¼�
	 */
	private void initListener() {
		bt_send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String toMsg = et_send_msg_info.getText().toString();
				if(TextUtils.isEmpty(toMsg)){
					Toast.makeText(MainActivity.this, "�������ݲ���Ϊ��", Toast.LENGTH_SHORT).show();
					return ;
				}
				
				final ChatMassage msg = new ChatMassage(toMsg, Type.OUTCOMING, new Date());
				msg.setName(user.getName());
				msg.setUser_id(user_id);
				
				int id = (int) dao.insertMsg(msg);
				msg.setId(id);
				list.add(msg);
				et_send_msg_info.setText("");
				
				madapter.notifyDataSetChanged();
				mlistview.setSelection(madapter.getCount()-1);
				if(isNetworkConnected){
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							ChatMassage fromMsg = Utils.sendMsg(msg);
							fromMsg.setName(user.getRobotname());
							fromMsg.setUser_id(user_id);
							Message m = Message.obtain();
							m.obj = fromMsg;
							handler.sendMessage(m);
						}
					}).start();
				}else{
					Toast.makeText(MainActivity.this, "�������Ӳ�����,��Ϣ����ʧ��!", 0).show();
				}
				
				
			}
		});
		
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			if(event.getDownTime()>1000){
				float x = event.getX();
				float y = event.getY();
				Log.e("GYB", "X="+x+",Y="+y);
			}
		}
		return super.onTouchEvent(event);
	}
	/**
	 * ��ʼ��view
	 */
	private void initView() {
		mlistview = (ListView) findViewById(R.id.lv_listview_msg);
		list = new ArrayList<ChatMassage>();
		
		//item ��Ҫ��̬����
		//findViewById(R.layout.item_to_msg);
		//to_name = (TextView) findViewById(R.id.to_name);
		
		et_send_msg_info = (EditText) findViewById(R.id.et_send_msg_info);
		bt_send = (Button) findViewById(R.id.bt_send);
	}

	private void initData() {
		
		dao = new UserDao(this); 
		
		Intent intent = getIntent();
		user_id = intent.getIntExtra("user_id", -1);
		
		user = dao.getUserById(user_id);
		if(user.getRobotname()==null){
			user.setRobotname("doubi");
		}
		if(user.getName()==null){
			user.setName(user.getUsername());
		}
		
		list = dao.getMsg(user_id);
		ChatMassage msg_from = new ChatMassage();
		msg_from.setDate(new Date());
		msg_from.setName(user.getRobotname());
		msg_from.setMsg("hello,"+user.getName()+", ������!");
		msg_from.setType(Type.INCOMING);
		list.add(msg_from);
		madapter = new ChatMassageAdapter(this, list);
		
		
		
	}
	
	/**
	 * ��������ǰһ��ҳ�淵�ص�����
	 * @requestCode �������  �ڱ�ҳ����д��   һ��Ϊ1
	 * @resultCode ���ش��� ��ǰһ��ҳ��д�õ�  һ��Ϊ RESULT_OK;
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			if(resultCode==RESULT_OK){
				user = (User) data.getSerializableExtra("user");
				user_id = dao.getIdByUsername(user.getUsername());
			}
			break;

		default:
			break;
		}
	}
}
