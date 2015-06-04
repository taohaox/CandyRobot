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
	
	public static int user_id; // 用户id
	
	private User user; //保存用户信息
	
	private UserDao dao;//数据库操作dao
	
	public static boolean isNetworkConnected; //是否有网络连接   在onresume()里面判断  若为false 则不会发送消息到服务器
	
	private BroadcastReceiver mReceiver = new MyReceiver(); 

		
		 
	
	
	 
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			//等待接收  子线程完成的数据的返回
			ChatMassage getmsg = (ChatMassage) msg.obj;
			int id = (int) dao.insertMsg(getmsg);
			getmsg.setId(id);
			list.add(getmsg);
			//通知适配器更新listview
			madapter.notifyDataSetChanged();
			//设置适配器  定位到最后一条 
			mlistview.setSelection(madapter.getCount()-1);
			
			
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除标题头 需要在setcontentview之前  
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//完成页面渲染
		setContentView(R.layout.activity_main);
		//设置标题的名称
		View view  = findViewById(R.id.title_main);
		TextView tv_title = (TextView)view.findViewById(R.id.tv_title);
		tv_title.setText("小y°");
		
		//tv_title.setText("小y°");
		initView();
		initData();
		initListener();
		mlistview.setAdapter(madapter);
		
		//注册网络监听
		IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, mFilter);
		
		//RelativeLayout main = (RelativeLayout) findViewById(R.id.main);
		
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//取消监听
		unregisterReceiver(mReceiver);
		
	}
	/**
	 * activity的生命周期 onCreate onStart onResume onPause onStop onDestory 之一  
	 * 取得焦点 使得该activity能响应各种事件
	 * 
	 */
	@Override
	protected void onResume() {
		super.onResume();
		
		
		//检查网络连接是否有效
		ConnectivityManager mManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = mManager.getActiveNetworkInfo();
		if(networkinfo!=null){
			isNetworkConnected = networkinfo.isAvailable();
		}
		if(!isNetworkConnected){
			openSetting();
		}
		
		//通知适配器更新listview
		madapter.notifyDataSetChanged();
		//设置适配器  定位到最后一条 
		mlistview.setSelection(madapter.getCount()-1);
		
	}
	/**
	 *无网络时调用的方法  显示对话框  前往设置界面
	 */
	public void openSetting(){
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage("本服务需要连接网络,您的网络连接异常,是否前往设置?");
		builder.setCancelable(false);//无法通过返回键取消对话框
		builder.setPositiveButton("是", new android.content.DialogInterface.OnClickListener(){
			/**
			 * 前往设置网络
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
		//留在当前页面
		builder.setNegativeButton("否", new android.content.DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				
			}
		});
		builder.show();
	}
	//创建菜单
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	//菜单点击事件
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent intent = new Intent(this, SettingActivity.class);
			intent.putExtra("user_id", user_id);
			//期待下一个页面销毁后 会返回数据到前一个页面
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
	 * 监听事件
	 */
	private void initListener() {
		bt_send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String toMsg = et_send_msg_info.getText().toString();
				if(TextUtils.isEmpty(toMsg)){
					Toast.makeText(MainActivity.this, "输入内容不能为空", Toast.LENGTH_SHORT).show();
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
					Toast.makeText(MainActivity.this, "网络连接不可用,消息发送失败!", 0).show();
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
	 * 初始化view
	 */
	private void initView() {
		mlistview = (ListView) findViewById(R.id.lv_listview_msg);
		list = new ArrayList<ChatMassage>();
		
		//item 需要动态加载
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
		msg_from.setMsg("hello,"+user.getName()+", 你来啦!");
		msg_from.setType(Type.INCOMING);
		list.add(msg_from);
		madapter = new ChatMassageAdapter(this, list);
		
		
		
	}
	
	/**
	 * 用来接收前一个页面返回的数据
	 * @requestCode 请求代码  在本页面填写的   一般为1
	 * @resultCode 返回代码 在前一个页面写好的  一般为 RESULT_OK;
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
