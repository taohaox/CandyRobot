package com.cute.xiaoy.entity;

import java.util.Date;

public class ChatMassage {
	private int id;
	private String name;
	private String msg;
	private Type type;
	private Date date;
	private int user_id;
	
	public ChatMassage(int id, String name, String msg, Type type, Date date,
			int user_id) {
		super();
		this.id = id;
		this.name = name;
		this.msg = msg;
		this.type = type;
		this.date = date;
		this.user_id = user_id;
	}
	public ChatMassage(String name, String msg, Type type, Date date,
			int user_id) {
		super();
		this.name = name;
		this.msg = msg;
		this.type = type;
		this.date = date;
		this.user_id = user_id;
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public ChatMassage() {
		super();
	}

	public ChatMassage( String msg, Type type, Date date) {
		super();
		
		this.msg = msg;
		this.type = type;
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public enum Type{
		INCOMING,OUTCOMING;
	}

	@Override
	public String toString() {
		return "ChatMassage [name=" + name + ", msg=" + msg + ", type=" + type
				+ ", date=" + date + "]";
	}
	
}
