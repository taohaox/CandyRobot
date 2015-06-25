package com.cute.xiaoy.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

import com.cute.xiaoy.entity.ChatMassage;
import com.cute.xiaoy.entity.ChatMassage.Type;
import com.cute.xiaoy.entity.ResultJson;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;


public class Utils {
	public static final String API_KEY = "9b34455d38812f8f146a699a26869508";
	
	public static final String API_URL = "http://www.tuling123.com/openapi/api";
	
	public static ChatMassage sendMsg(ChatMassage msg){
		ChatMassage cmsg = new ChatMassage();
		String jrs = getResult(msg.getMsg());
		Gson gson = new Gson();
		ResultJson rsj= null;
		
		try {
			rsj = gson.fromJson(jrs, ResultJson.class);
			if(rsj!=null){
				cmsg.setMsg(rsj.getText());
			}else{
				cmsg.setMsg("网络好像出了点状况.....");
			}
			
			cmsg.setType(Type.INCOMING);
			cmsg.setDate(new Date());
		} catch (JsonSyntaxException e) {
			cmsg.setMsg("服务器繁忙,请稍后再试!");
			e.printStackTrace();
		}
		return cmsg;
	}
	
	public static String getResult(String msg){
		String result = "";
		
		String resultURL = getResultURL(msg);
		BufferedInputStream in = null;
		ByteArrayOutputStream bos = null;
		try {
			URL url = new URL(resultURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(5000);
			conn.setReadTimeout(5000);
			conn.setRequestMethod("GET");
			
			in = new BufferedInputStream(conn.getInputStream());
			int len = 0;
			byte b[] = new byte[256];
			bos = new ByteArrayOutputStream();
			while((len = in.read(b))!=-1){
				
				bos.write(b,0,len);
			}
			bos.flush();
			result = new String(bos.toByteArray());
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(in!=null){
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(bos!=null){
					bos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		
		
		return result;
		
	}

	private static String getResultURL(String msg) {
		try {
			msg = URLEncoder.encode(msg, "utf-8");
			return API_URL+"?key="+API_KEY+"&info="+msg;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "编码转化错误";
	}
			
}
