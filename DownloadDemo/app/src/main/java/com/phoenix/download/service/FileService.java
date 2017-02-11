package com.phoenix.download.service;

import com.phoenix.download.utils.StreamTool;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileService {
	public static byte[] getFile(String path)  throws Exception{
		//URL类专门用来封装我们要访问的路径
		URL url = new URL(path);
		//打开链接
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setRequestMethod("GET");//设置请求方式
		conn.setConnectTimeout(5*1000);//设置连接超时时间
//			conn.setReadTimeout(5000);//设置读超时
		InputStream is = conn.getInputStream();
		byte[] data = StreamTool.readInputStream(is);
		System.out.println(data);
		return data;//得到文件的二进制数据
	}
}
