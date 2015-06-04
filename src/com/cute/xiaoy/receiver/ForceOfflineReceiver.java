package com.cute.xiaoy.receiver;

import com.cute.xiaoy.LoginActivity;
import com.cute.xiaoy.utils.ActivityCollector;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.view.WindowManager;

public class ForceOfflineReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(final Context context, Intent intent) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setTitle("Warning!");
		dialog.setMessage("You are forced to be offline.please try to login again!");
		dialog.setCancelable(false);
		dialog.setPositiveButton("OK", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				ActivityCollector.finishAll();
				Intent intent = new Intent(context,LoginActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
			}
		});
		AlertDialog alertdialog = dialog.create();
		//需要设置AlertDialog的类型,保证在广播接收器中可以正常弹出
		
		alertdialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		alertdialog.show();
		
	}

}
