package com.phoenix.upload;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private boolean isGrant = true;
    private final int SDK_PERMISSION_REQUEST = 127;

    private Button btn1;
    private Button btn2;

    private String uploadUrl = "http://192.168.0.104:8080/web/servlet/UploadFileServlet";
    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);

        getPersimmions();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                if (isGrant) {
                    chooseFile();
                }else {
                    Toast.makeText(this,getString(R.string.permisstion_deny),Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn2:
                if (isGrant) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            UploadUtil.uploadFile(new File(filePath), uploadUrl);
                        }
                    }).start();
                }else {
                    Toast.makeText(this,getString(R.string.permisstion_deny),Toast.LENGTH_SHORT).show();
                }
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

    @TargetApi(23)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<>();
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case SDK_PERMISSION_REQUEST:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // 允许
                    Toast.makeText(this,getString(R.string.permisstion_grant),Toast.LENGTH_SHORT).show();
                }else{
                    // 不允许
                    isGrant = false;
                    Toast.makeText(this,getString(R.string.permisstion_deny),Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
