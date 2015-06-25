package com.cute.xiaoy.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cute.xiaoy.entity.ChatMassage;
import com.cute.xiaoy.entity.ChatMassage.Type;

public class UserDao {
	UserDB userDB;

	public UserDao(Context context) {
		userDB = new UserDB(context);
	}
	public boolean insertUser(User user){
		SQLiteDatabase db  = userDB.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("username", user.getUsername());
		values.put("password", user.getPassword());
		values.put("age", user.getAge());
		values.put("sex", user.getSex());
		values.put("createDate", user.getCreateDate());
		long i = db.insert("user", null, values);
		db.close();
		if(i==-1){
			return false;
		}
		return true;
	}
	
	public User getUserById(int id){
		SQLiteDatabase db  = userDB.getReadableDatabase();
		Cursor cursor = db.query("user", null, "id="+id, null, null, null, null);
		User user = null;
		if(cursor.moveToNext()){
			String name = cursor.getString(cursor.getColumnIndex("name"));
			int age = cursor.getInt(cursor.getColumnIndex("age"));
			String robotname = cursor.getString(cursor.getColumnIndex("robotname"));
			String sex = cursor.getString(cursor.getColumnIndex("sex"));
			String username = cursor.getString(cursor.getColumnIndex("username"));
			user = new User(username, age, sex, robotname, name);
		}
		cursor.close();
		db.close();
		return user;
	}
	public boolean updateUser(User user){
		SQLiteDatabase db  = userDB.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("name", user.getName());
		values.put("age", user.getAge());
		values.put("sex", user.getSex());
		values.put("robotname", user.getRobotname());
		int row = db.update("user", values, "username='"+user.getUsername()+"'", null);
		db.close();
		if(row>0){
			return true;
		}
		
		
		return false;
	}
	public String findUserByName(String username,String password){
		SQLiteDatabase db  = userDB.getReadableDatabase();
		Cursor cursor = db.query("user", null, "username='"+username+"' and password='"+password+"'", null, null, null, null);
		String s = null;
		if(cursor.moveToNext()){
			s =  cursor.getString(cursor.getColumnIndex("username"));
		}
		cursor.close();
		db.close();
		return s;
	}
	//检查username是否重复
	public boolean checkUsername(String username){
		SQLiteDatabase db  = userDB.getWritableDatabase();
		Cursor cursor = db.query("user", null, "username='"+username+"'", null, null, null, null);
		String s = null;
		if(cursor.moveToNext()){
			s =  cursor.getString(cursor.getColumnIndex("username"));
		}
		cursor.close();
		db.close();
		if(s==null){
			return true;
		}
		return false;
	}
	//根据用户名获取用户id
	public int getIdByUsername(String username){
		SQLiteDatabase db  = userDB.getWritableDatabase();
		Cursor cursor = db.query("user", null, "username='"+username+"'", null, null, null, null);
		int i = -1;
		if(cursor.moveToNext()){
			i = cursor.getInt(cursor.getColumnIndex("id"));
		}
		cursor.close();
		db.close();
		
		return i;
	}
	
	
	//插入消息到数据库
	public long insertMsg(ChatMassage msg){
		SQLiteDatabase db  = userDB.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("user_id", msg.getUser_id());
		values.put("msg", msg.getMsg());
		values.put("name", msg.getName());
		values.put("date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(msg.getDate()));
		values.put("type", msg.getType().toString());
		long id = db.insert("messageinfo", null, values);
		db.close();
		return id;
	}
	//读取messageinfo记录封装成list返回
	public List<ChatMassage> getMsg(int user_id){
		List<ChatMassage> list =  new ArrayList<ChatMassage>();
		SQLiteDatabase db  = userDB.getReadableDatabase();
		Cursor cursor = db.query("messageinfo", null, "user_id='"+user_id+"'", null, null, null, null);
		while(cursor.moveToNext()){
			int id = cursor.getInt(cursor.getColumnIndex("id"));
			String name =  cursor.getString(cursor.getColumnIndex("name"));
			String stype =  cursor.getString(cursor.getColumnIndex("type"));
			Type type;
			if(stype.equals("INCOMING")){
				type = Type.INCOMING;
			}else{
				type = Type.OUTCOMING;
			}
			
			String date =  cursor.getString(cursor.getColumnIndex("date"));
			String msg =  cursor.getString(cursor.getColumnIndex("msg"));
			
			try {
				list.add(new ChatMassage(id,name, msg, type, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date), user_id));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;
	}
	public int deleteMsgById(int id){
		SQLiteDatabase db  = userDB.getWritableDatabase();
		int row = db.delete("messageinfo", "id=?", new String[]{id+""});
		db.close();
		return row;
	} 
}
