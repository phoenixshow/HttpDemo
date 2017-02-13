package com.phoenix.json.utils;

import android.util.Log;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.phoenix.json.bean.User;

//import android.util.JsonReader;

public class JsonUtils {
	/**
	 * 生成JSON
	 */
	public void toJson(){
		try {
			List<User> list = new ArrayList<User>();
			list.add(new User("张飞", 10));
			list.add(new User("王飞", 20));
			list.add(new User("老毕", 30));

			JSONArray array = new JSONArray();
			for(User user : list){
				JSONObject item = new JSONObject();
				item.put("name", user.getName());
				item.put("age", user.getAge());
				array.put(item);//array.add()
			}
			String json = array.toString();
			Log.e("TAG", json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 使用JSONArray、JSONObject解析JSON
	 * @param jsonData
	 */
	public void parseJsonByJsonObjectAndJsonArray(String jsonData){
		try {
			JSONArray arr = new JSONArray(jsonData);
			for(int i=0; i<arr.length(); i++){
				JSONObject obj = arr.getJSONObject(i);
				Log.e("TAG", "获取姓名："+obj.getString("name"));
				Log.e("TAG", "获取年龄："+obj.getInt("age"));
				int age = obj.optInt("age", 0);
				int height = obj.optInt("height", 0);
				Log.e("TAG", "属性age:"+age+",height:"+height);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 使用JsonReader解析JSON
	 * 如果用3.0及以上 导包import android.util.JsonReader;
	 * 如果用3.0以下 依赖gson，再导包import com.google.gson.stream.JsonReader;
	 * @param jsonData
	 */
	public void parseJson(String jsonData){
		JsonReader reader = null;
		try {
			reader = new JsonReader(new StringReader(jsonData));
			reader.setLenient(true);//容错
			reader.beginArray();//开始解析一个数组
			while(reader.hasNext()){
				reader.beginObject();//开始解析对象
				while(reader.hasNext()){
					//开始解析键值对
					String tagName = reader.nextName();//获取键
					if(tagName.equals("name")){
						Log.e("TAG", "name--->"+reader.nextString());
					}else if(tagName.equals("age")){
						Log.e("TAG", "age--->"+reader.nextInt());
					}
				}
				reader.endObject();//解析对象结束
			}
			reader.endArray();//解析数组结束
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}







	/**
	 * 使用GSON生成JSON
	 */
	public void toJsonByGson(){
		Gson gson = new Gson();

		//单个对象生成JSON字符串
		User user = new User("范冰冰", 35);
		String jsonstr1 = gson.toJson(user);
		Log.e("TAG", jsonstr1);

		//对象集合生成JSON字符串
		Type listType = new TypeToken<List<User>>(){}.getType();
		List<User> list = new ArrayList<User>();
		list.add(new User("张飞", 10));
		list.add(new User("王飞", 20));
		list.add(new User("老毕", 30));
		String jsonstr2 = gson.toJson(list, listType);
		Log.e("TAG", jsonstr2);
	}

	/**
	 * 使用GSON解析JSON并封装为Java对象
	 * @param jsonData
	 * @return
	 */
	public User parseUserFormJson(String jsonData){
//		Gson gson = new Gson();
		Gson gson = new GsonBuilder()
				.setLenient()// json宽松
				.create();
		User user = gson.fromJson(jsonData, User.class);
		return user;
	}

	/**
	 * 使用GSON解析JSON数组并封装为Java对象集合
	 * @param jsonData
	 * @return
	 */
	public LinkedList<User> parseUserListFromJson(String jsonData){
		Type listType = new TypeToken<LinkedList<User>>(){}.getType();
		Gson gson = new Gson();
		LinkedList<User> users = gson.fromJson(jsonData, listType);
		return users;
	}

	public static class UserTypeAdapter extends TypeAdapter<User> {
		@Override
		public void write(JsonWriter out, User value) throws IOException {
			out.beginObject();
			out.name("name").value(value.getName());
			out.name("age").value(value.getAge());
			out.endObject();
		}

		@Override
		public User read(JsonReader in) throws IOException {
			User user = new User();
			in.beginObject();
			while (in.hasNext()) {
				switch (in.nextName()) {
					case "name":
						user.setName(in.nextString());
						break;
					case "age":
						try {
							String str = in.nextString();
							user.setAge(Integer.valueOf(str));
						} catch (Exception e) {
						}
						break;
				}
			}
			in.endObject();
			return user;
		}
	}
}
