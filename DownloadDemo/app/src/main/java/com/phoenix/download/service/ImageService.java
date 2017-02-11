package com.phoenix.download.service;

import com.phoenix.download.utils.StreamTool;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class ImageService {
	public static byte[] getImage(String path) throws Exception{
		//URL类专门用来封装我们要访问的路径
		URL url = new URL(path);
		//打开链接
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setRequestMethod("GET");//设置请求方式
		conn.setConnectTimeout(5*1000);//设置连接超时时间
//			conn.setReadTimeout(5000);//设置读超时
		InputStream is = conn.getInputStream();
		byte[] data = StreamTool.readInputStream(is);
		return data;//得到图片的二进制数据
	}
}
