package com.phoenix.upload;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn1;
    private Button btn2;

    private String uploadUrl = "http://192.168.155.104:8080/web/servlet/UploadFileServlet";
    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                chooseFile();
                break;
            case R.id.btn2:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        UploadUtil.uploadFile(new File(filePath), uploadUrl);
                    }
                }).start();
                break;
            default:
                break;
        }
    }

    //选择文件
    private void chooseFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");//不筛选任何类型，所有类型全部获取
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    filePath = uri.getPath();
                    Log.e("TAG", "filePath:" + filePath);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
