package com.cute.xiaoy.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDB extends SQLiteOpenHelper{

	public UserDB(Context context) {
		super(context, "UserDB.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table user(id integer primary key autoincrement,username varchar(100) not null,password varchar(100) not null," +
				"sex varchar(10),age integer(3),createDate varchar(100),robotname varchar(100))");
		db.execSQL("create table messageinfo(id integer primary key autoincrement," +
				"user_id integer(10),msg varchar(500),name varchar(100),date varchar(20),type varchar(10))");
		db.execSQL("alter table user add name varchar(100)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		
		
	}

}
