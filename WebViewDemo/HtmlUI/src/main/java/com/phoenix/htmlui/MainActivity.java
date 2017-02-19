package com.phoenix.htmlui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.phoenix.htmlui.bean.Contact;
import com.phoenix.htmlui.business.ContactBusiness;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 0;
    WebView wv;
    ContactBusiness business;
    private String mobile = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        business = new ContactBusiness();

        wv = (WebView)findViewById(R.id.wv);
        wv.getSettings().setJavaScriptEnabled(true);

        wv.addJavascriptInterface(new ContactPlugin(), "contact");

        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
        });

//        wv.loadUrl("file:///android_asset/index.html");
        wv.loadUrl("http://192.168.0.101:8080/webview/index.html");
    }


    private final class ContactPlugin{
        @JavascriptInterface
        public void getContacts(){
            List<Contact> list = business.getContacts();
            try {
                JSONArray array = new JSONArray();
                for(Contact contact : list){
                    JSONObject item = new JSONObject();
                    item.put("id", contact.getId());
                    item.put("name", contact.getName());
                    item.put("mobile", contact.getMobile());
                    array.put(item);
                }
                final String json = array.toString();
                Log.e("TAG", json);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        wv.loadUrl("javascript:show('"+ json +"')");
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @JavascriptInterface
        public void call(String mobile){
            MainActivity.this.mobile = mobile;
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                // 获取wifi连接需要定位权限,没有获取权限
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                        Manifest.permission.CALL_PHONE,
                },MY_PERMISSIONS_REQUEST_CALL_PHONE);
                return;
            }
            callPhone();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_CALL_PHONE:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // 允许
                    Toast.makeText(this,getString(R.string.permisstion_obtained),Toast.LENGTH_SHORT).show();
                    callPhone();
                }else{
                    // 不允许
                    Toast.makeText(this,getString(R.string.permisstion_denied),Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+ mobile));
        startActivity(intent);
    }
}
