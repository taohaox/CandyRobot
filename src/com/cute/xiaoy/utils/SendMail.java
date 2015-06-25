package com.cute.xiaoy.utils;


import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
public class SendMail {
	
	//使用的邮箱需要开通smtp服务
	String username = "csyouxiang71";
	String password = "zaazzkfblqabpayu";
	String smtpHost = "smtp.163.com";
	String sendText = "this is a test mail";
	String sendTitle = "hello, javamail";
	String addressFrom = "csyouxiang71@163.com";
	String addressTo = "csyouxiang71@163.com";
	
	 public SendMail(String sendText, String sendTitle) {
		super();
		this.sendText = sendText;
		this.sendTitle = sendTitle;
	}
	public SendMail() {
		super();
	}
	public void send(Context context,Handler handler) throws MessagingException, UnsupportedEncodingException {
		 //设置必要的配置,为new出session做准备
		 Properties props = new Properties();
		 //配置 mail.smtp.host  一般是邮件服务器  smtp.xx.com
		 props.put("mail.smtp.host", smtpHost);
		 //配置是否需要验证
		 props.put("mail.smtp.auth", true);
		 //拿到session实例 , 第二个参数用来验证身份 需要定义一个类来继承Authenticator,并重写方法
		 //将账号和密码放进去.
		 Session session = Session.getInstance(props, new MyAuth());
		 //创建邮件体:
		 MimeMessage message = new MimeMessage(session); 
		 Address addressF;
		
			addressF = new InternetAddress(addressFrom,"神秘人");
			Address addressT = new InternetAddress(addressTo,"神秘人");
			 
			 message.setFrom(addressF);
			 message.setText(sendText);
			 message.setSubject(sendTitle);
			 message.addRecipient(Message.RecipientType.TO, addressT);
			 message.saveChanges();
			 
			 //发送邮件
			 if(context==null){
				 Transport transport = session.getTransport("smtp");
				 transport.connect(smtpHost, username, password);
				 Log.e("HK", "邮箱服务器登陆成功");
				 transport.send(message);
				 Log.e("HK", "邮件发送成功成功");
				 transport.close();
			 }else{
				 Transport transport = session.getTransport("smtp");
				 transport.connect(smtpHost, username, password);
				 
				// Toast.makeText(context, "邮箱服务器登陆成功", 1500).show();
				 transport.send(message);
				// Toast.makeText(context, "邮件发送成功成功", 1500).show();
				 transport.close();
			 }
			
	 }
	 
	 class MyAuth extends Authenticator{
		@Override
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(username,password);
		}
		 
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
		public String getStmpHost() {
			return smtpHost;
		}
		public void setStmpHost(String stmpHost) {
			this.smtpHost = stmpHost;
		}
		public String getSendText() {
			return sendText;
		}
		public void setSendText(String sendText) {
			this.sendText = sendText;
		}
		public String getSendTitle() {
			return sendTitle;
		}
		public void setSendTitle(String sendTitle) {
			this.sendTitle = sendTitle;
		}
		public String getAddressFrom() {
			return addressFrom;
		}
		public void setAddressFrom(String addressFrom) {
			this.addressFrom = addressFrom;
		}
		public String getAddressTo() {
			return addressTo;
		}
		public void setAddressTo(String addressTo) {
			this.addressTo = addressTo;
		}
		public SendMail(String username, String password, String smtpHost,
				String sendText, String sendTitle, String addressFrom,
				String addressTo) {
			super();
			this.username = username;
			this.password = password;
			this.smtpHost = smtpHost;
			this.sendText = sendText;
			this.sendTitle = sendTitle;
			this.addressFrom = addressFrom;
			this.addressTo = addressTo;
		}
		public SendMail(String sendText, String sendTitle, String addressTo) {
			super();
			
			this.sendText = sendText;
			this.sendTitle = sendTitle;
			this.addressTo = addressTo;
		}
}
