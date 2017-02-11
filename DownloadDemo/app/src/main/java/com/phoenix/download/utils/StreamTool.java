package com.phoenix.download.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class StreamTool {
	/**
	 * 从输入流中获取数据
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public static byte[] readInputStream(InputStream is) throws Exception{
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		//定义一个缓冲区
		byte[] buffer = new byte[1024];
		int len = 0;
		while((len=is.read(buffer))!=-1){
			outStream.write(buffer, 0 , len);
		}
		is.close();
		return outStream.toByteArray();
	}
}
