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
			 Log.d("GYB", "����״̬�Ѿ��ı�");
			//������������Ƿ���Ч
			ConnectivityManager mManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkinfo = mManager.getActiveNetworkInfo();
			if(networkinfo!=null){
				MainActivity.isNetworkConnected = networkinfo.isAvailable();
			}else{
				MainActivity.isNetworkConnected = false;
				return;
			}
			if(MainActivity.isNetworkConnected){
				Log.d("GYB", "���ӿ���"+networkinfo.getTypeName()+","+networkinfo.getExtraInfo());
			}else{
				Log.d("GYB", "���Ӳ�����");
			}
		}
	}
}
