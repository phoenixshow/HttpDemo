package com.phoenix.json;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.phoenix.json.bean.User;
import com.phoenix.json.utils.JsonUtils;

import java.util.Iterator;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    //	[{"name":"张三","age":20},{"name":"李四","age":30}]
//    String jsonData1 = "[{\"name\":\"张三\",\"age\":20},{\"name\":\"李四\",\"age\":30}]";
//	String jsonData2 = "{\"name\":\"张三\",\"age\":20}";
//    String jsonData3 = "[{\"name\":\"张三\",\"age\":20,\"new\":true},{\"name\":\"李四\",\"age\":30,\"new\":false}]";
//	String jsonData4 = "{name=张三,age=20}";
//    String jsonData5 = "[{name=张三,age=20},{name=李四,age=30}]";
    String jsonData6 = "{\"name\":\"张三\",\"age\":\"\"}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void click(View view){
        JsonUtils jsonUtils = new JsonUtils();

////		jsonUtils.parseJson(jsonData1);
//		jsonUtils.parseJson(jsonData5);

////		User user = jsonUtils.parseUserFormJson(jsonData2);
//		User user = jsonUtils.parseUserFormJson(jsonData4);
//        Log.e("TAG", "name--->"+user.getName());
//        Log.e("TAG", "age--->"+user.getAge());

//		LinkedList<User> users = jsonUtils.parseUserListFromJson(jsonData3);
//		Iterator<User> iterator = users.iterator();
//		while(iterator.hasNext()){
//			User user = iterator.next();
//            Log.e("TAG", "name--->"+user.getName());
//            Log.e("TAG", "age--->"+user.getAge());
//            Log.e("TAG", "isNew--->"+user.getIsNew());
//		}

//		jsonUtils.toJson();

//		jsonUtils.toJsonByGson();

//        jsonUtils.parseJsonByJsonObjectAndJsonArray(jsonData1);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(User.class, new JsonUtils.UserTypeAdapter()).create();
        try {
            User user = gson.fromJson(jsonData6, User.class);
            Log.e("TAG", "------>自定义adapter 解析:" + user);
        } catch (JsonParseException e) {//java.lang.NumberFormatException: empty String
            Log.e("TAG", "------>自定义adapter 异常:" + e);
        }
    }
}
