package com.phoenix.download;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private boolean isGrant = true;
    private final int SDK_PERMISSION_REQUEST = 127;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getPersimmions();
    }

    public void downloadImg(View view) {
        if (isGrant) {
            Intent intent = new Intent(this, ImageActivity.class);
            startActivity(intent);
        }else {
            Toast.makeText(this,getString(R.string.permisstion_deny),Toast.LENGTH_SHORT).show();
        }
    }

    public void downloadHtml(View view) {
        if (isGrant) {
            Intent intent = new Intent(this, HtmlActivity.class);
            startActivity(intent);
        }else {
            Toast.makeText(this,getString(R.string.permisstion_deny),Toast.LENGTH_SHORT).show();
        }
    }

    public void downloadFile(View view) {
        if (isGrant) {
            Intent intent = new Intent(this, FileActivity.class);
            startActivity(intent);
        }else {
            Toast.makeText(this,getString(R.string.permisstion_deny),Toast.LENGTH_SHORT).show();
        }
    }

    @TargetApi(23)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<>();
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
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
