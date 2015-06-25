package com.cute.xiaoy.adapter;

import java.text.SimpleDateFormat;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cute.xiaoy.MainActivity;
import com.cute.xiaoy.R;
import com.cute.xiaoy.SettingActivity;
import com.cute.xiaoy.db.UserDao;
import com.cute.xiaoy.entity.ChatMassage;
import com.cute.xiaoy.entity.ChatMassage.Type;

public class ChatMassageAdapter extends BaseAdapter {
	List<ChatMassage> list;
	LayoutInflater infater;
	Context context;
	String mHeadpath;
	public ChatMassageAdapter(Context context,List<ChatMassage> list){
		this.list = list;
		this.infater = LayoutInflater.from(context);
		this.context = context;
		//从配置文件中读取出 头像的uri的string
		SharedPreferences  sf = context.getSharedPreferences("config_headimg.xml", Context.MODE_PRIVATE);
		mHeadpath = sf.getString("filepath", "");
		
	}
	
	@Override
	public int getItemViewType(int position) {
		Type type =list.get(position).getType();
		if(type == Type.INCOMING){
			return 0;
		}else if(type == Type.OUTCOMING){
			return 1;
		}else{
			return -1;
		}
		
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ChatMassage msg = list.get(position);
		
		ViewHolder viewholder = null;
		if(convertView==null){
			if(getItemViewType(position)==0){
				convertView = infater.inflate(R.layout.new_item_from_msg, parent, false);
				viewholder = new ViewHolder();
				viewholder.mDate = (TextView) convertView.findViewById(R.id.from_msg_date);
				viewholder.mMsg = (TextView) convertView.findViewById(R.id.from_msg);
				viewholder.mName = (TextView) convertView.findViewById(R.id.from_name);
				viewholder.mImg = (ImageView) convertView.findViewById(R.id.iv_robotimg);
				//Log.e("tag", msg.getDate().toLocaleString());
			}else if(getItemViewType(position)==1){
				convertView = infater.inflate(R.layout.new_item_to_msg, parent, false);
				viewholder = new ViewHolder();
				viewholder.mDate = (TextView) convertView.findViewById(R.id.to_msg_date);
				viewholder.mMsg = (TextView) convertView.findViewById(R.id.to_msg);
				viewholder.mName = (TextView) convertView.findViewById(R.id.to_name);
				viewholder.mImg = (ImageView) convertView.findViewById(R.id.iv_myimg);
				if(!"".equals(mHeadpath)){
					viewholder.mImg.setImageBitmap(BitmapFactory.decodeFile(mHeadpath));
				}
				
			}else{
				convertView = infater.inflate(R.layout.new_none, parent, false);
			}
			
			convertView.setTag(viewholder);
		}else{
			viewholder = (ViewHolder) convertView.getTag();
		}
		
		
		viewholder.mDate.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(msg.getDate()));
		viewholder.mMsg.setText(msg.getMsg());
		
		final int pos = position;
		
		//infater.inflate(R.layout.longclick_setting, parent);
		viewholder.mMsg.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				Log.e("GYB", pos+"");
				
				AlertDialog.Builder dialog = new AlertDialog.Builder(context);
				dialog.setTitle("提示!");
				dialog.setMessage("是否确认要删除?");
				dialog.setCancelable(false);
				dialog.setPositiveButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}

				});
				dialog.setNegativeButton("确定", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						UserDao dao = new UserDao(context);
						if(msg.getId()!=0){
							
							int row = dao.deleteMsgById(msg.getId());
							if(row==1){
								Toast.makeText(context, "delete success!", 1000).show();
								list.remove(pos);
								ChatMassageAdapter.this.notifyDataSetChanged();
								
							}else{
								Toast.makeText(context, "delete failed!", 1000).show();
							}
						}else{
							list.remove(pos);
							Toast.makeText(context, "delete success!", 1000).show();
							ChatMassageAdapter.this.notifyDataSetChanged();
						}
					}
					
				});
				dialog.show();
				
				
				/*LayoutInflater in = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View view = in.inflate(R.layout.longclick_setting, null);
				//打开窗口 设置窗口的大小
				final PopupWindow pw = new PopupWindow(view,160,100);
				// 使其聚集
				pw.setFocusable(true);
				// // 设置允许在外点击消失
				pw.setOutsideTouchable(true);
				 // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
		        pw.setBackgroundDrawable(new BitmapDrawable());
		       
		        TextView del = (TextView) view.findViewById(R.id.tv_longclick_set_delete);
		        del.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						UserDao dao = new UserDao(context);
						if(msg.getId()!=0){
							
							int row = dao.deleteMsgById(msg.getId());
							if(row==1){
								Toast.makeText(context, "delete success!", 1000).show();
								list.remove(pos);
								ChatMassageAdapter.this.notifyDataSetChanged();
								
							}else{
								Toast.makeText(context, "delete failed!", 1000).show();
							}
						}else{
							list.remove(pos);
							Toast.makeText(context, "delete success!", 1000).show();
							ChatMassageAdapter.this.notifyDataSetChanged();
						}
						if(null != pw && pw.isShowing()) {
						    pw.dismiss();
						}
					}
					
				});
		        pw.showAtLocation( v, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0,0);*/
				return true;
			}
		});
		viewholder.mName.setText(msg.getName());
		viewholder.mImg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,SettingActivity.class);
				intent.putExtra("user_id", MainActivity.user_id);
				//期待下一个页面销毁后 会返回数据到前一个页面
				((Activity) context).startActivityForResult(intent, 1);
			}
		});
		
		return convertView;
		
	}
	private final class ViewHolder{
		int x;
		int y;
		TextView mDate;
		TextView mMsg;
		TextView mName;
		ImageView mImg; 
	}

}
