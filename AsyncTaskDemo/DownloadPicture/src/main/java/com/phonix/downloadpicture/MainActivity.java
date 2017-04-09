package com.phonix.downloadpicture;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import com.phonix.downloadpicture.adapter.PhotoWallAdapter;
import com.phonix.downloadpicture.constant.Images;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private GridView mPhotoWall;
    private PhotoWallAdapter adapter;
    private ArrayList<String> list;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = new ArrayList<>();
        initData();
        mPhotoWall = (GridView) findViewById(R.id.photo_wall);
        adapter = new PhotoWallAdapter(this, 0, list, mPhotoWall);
        mPhotoWall.setAdapter(adapter);
    }

    private void initData() {
        for (int i = 0; i < Images.imageUrls.length; i++) {
            list.add(Images.imageUrls[i]);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        adapter.cancelAllTasks();// 退出程序时结束所有的下载任务
    }

}