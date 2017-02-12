package com.phoenix.xml;

import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.phoenix.xml.bean.Person;
import com.phoenix.xml.service.DOMService;
import com.phoenix.xml.service.PullService;
import com.phoenix.xml.service.SAXService;

import java.io.InputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void click(View view){
//		SAXService saxService = new SAXService();
//        DOMService domService = new DOMService();
        PullService pullService = new PullService();

        try {

            //通过类加载器来加载assets目录底下的xml文件
//        InputStream inStream = getClass().getClassLoader().getResourceAsStream("assets/users.xml");

            //getAssets()可拿到AssetManager对象
            InputStream inStream = getAssets().open("users.xml");

//			List<Person> list = saxService.getPersons(inStream);
//            List<Person> list = domService.getPersons(inStream);
            List<Person> list = pullService.getPersons(inStream);

            for(Person person : list){
                Log.e("TAG", person.toString());
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
