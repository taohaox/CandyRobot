package com.cute.xiaoy.db;

import java.io.Serializable;


public class User implements Serializable{
	private String username;
	private String password;
	private int age;
	private String sex;
	private String createDate;
	private String robotname = "doubi";
	private String name = username;
	
	
	public User(String username, int age, String sex, String robotname,
			String name) {
		super();
		this.username = username;
		this.age = age;
		this.sex = sex;
		this.robotname = robotname;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public User(String username, String password, int age, String sex,
			String createDate, String robotname, String name) {
		super();
		this.username = username;
		this.password = password;
		this.age = age;
		this.sex = sex;
		this.createDate = createDate;
		this.robotname = robotname;
		this.name = name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User(String username, String password, int age, String sex,
			String createDate, String robotname) {
		super();
		this.username = username;
		this.password = password;
		this.age = age;
		this.sex = sex;
		this.createDate = createDate;
		this.robotname = robotname;
	}

	public String getRobotname() {
		return robotname;
	}

	public void setRobotname(String robotname) {
		this.robotname = robotname;
	}

	public User(String username, String password, int age, String sex,
			String createDate) {
		super();
		this.username = username;
		this.password = password;
		this.age = age;
		this.sex = sex;
		this.createDate = createDate;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public User() {
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	
}
