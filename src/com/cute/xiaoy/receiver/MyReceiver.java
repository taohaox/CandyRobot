package com.cute.xiaoy.receiver;

import com.cute.xiaoy.MainActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if(ConnectivityManager.CONNECTIVITY_ACTION.equals(action)){
			 Log.d("GYB", "网络状态已经改变");
			//检查网络连接是否有效
			ConnectivityManager mManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkinfo = mManager.getActiveNetworkInfo();
			if(networkinfo!=null){
				MainActivity.isNetworkConnected = networkinfo.isAvailable();
			}else{
				MainActivity.isNetworkConnected = false;
				return;
			}
			if(MainActivity.isNetworkConnected){
				Log.d("GYB", "连接可用"+networkinfo.getTypeName()+","+networkinfo.getExtraInfo());
			}else{
				Log.d("GYB", "连接不可用");
			}
		}
	}
}
