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
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //	[{"name":"张三","age":20},{"name":"李四","age":30}]
    String jsonData1 = "[{\"name\":\"张三\",\"age\":20},{\"name\":\"李四\",\"age\":30}]";
	String jsonData2 = "{\"name\":\"张三\",\"age\":20}";
    String jsonData3 = "[{\"name\":\"张三\",\"age\":20,\"new\":true},{\"name\":\"李四\",\"age\":30,\"new\":false}]";
	String jsonData4 = "{name=张三,age=20}";
    String jsonData5 = "[{name=张三,age=20},{name=李四,age=30}]";
    String jsonData6 = "{\"name\":\"张三\",\"age\":\"\"}";
    String jsonData7 = "[{\"name\":\"张三\",\"age\":\"\"},{\"name\":\"李四\",\"age\":\"30\"}]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void click(View view){
        JsonUtils jsonUtils = new JsonUtils();

		jsonUtils.parseJson(jsonData1);
		jsonUtils.parseJson(jsonData5);

//		User user = jsonUtils.parseUserFormJson(jsonData2);
		User user = jsonUtils.parseUserFormJson(jsonData4);
        Log.e("TAG", "name--->"+user.getName());
        Log.e("TAG", "age--->"+user.getAge());

		LinkedList<User> users = jsonUtils.parseUserListFromJson(jsonData3);
		Iterator<User> iterator = users.iterator();
		while(iterator.hasNext()){
			User user3 = iterator.next();
            Log.e("TAG", "name--->"+user3.getName());
            Log.e("TAG", "age--->"+user3.getAge());
            Log.e("TAG", "isNew--->"+user3.getIsNew());
		}

		jsonUtils.toJson();

		jsonUtils.toJsonByGson();

        jsonUtils.parseJsonByJsonObjectAndJsonArray(jsonData1);

        jsonUtils.parseByCustomTypeAdapter(jsonData6);

        List<User> list = jsonUtils.parseByGlobalRegister(jsonData7);
        for (User user7 : list) {
            Log.e("TAG", user7.toString());
        }
    }
}
