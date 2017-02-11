package com.phoenix.download.service;

import com.phoenix.download.utils.StreamTool;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class HtmlService {
	public static String getHtml(String path) throws Exception {
		//URL类专门用来封装我们要访问的路径
		URL url = new URL(path);
		//打开链接
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setRequestMethod("GET");//设置请求方式
		conn.setConnectTimeout(5*1000);//设置超时时间
		InputStream is = conn.getInputStream();
		byte[] data = StreamTool.readInputStream(is);//得到html的二进制数据
		String html = new String(data, "gb2312");//注意编码格式与网站一致
		return html;
	}
}
