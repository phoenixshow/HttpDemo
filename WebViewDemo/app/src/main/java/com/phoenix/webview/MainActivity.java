package com.phoenix.webview;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button back;
    private Button forward;
    private WebView wv;
    /**
     * 进度条
     */
    public ProgressDialog pd;
    private final int SDK_PERMISSION_REQUEST = 127;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getPersimmions();
    }

    private void initWebView(){
        back = (Button) findViewById(R.id.back);
        forward = (Button) findViewById(R.id.forward);
        wv = (WebView) findViewById(R.id.wv);

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(wv.canGoBack()){
                    wv.goBack();
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(), "不能再退啦！", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });
        forward.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(wv.canGoForward()){
                    wv.goForward();
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(), "已经到顶啦！", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });

        /*String html = "";
        html += "<html>";
        	html += "<body>";
        	    html += "<a href=http://www.baidu.com>首页</a>";
        	html += "</body>";
        html += "</html>";
//        wv.loadData(html, "text/html", "utf-8");
        wv.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);

        wv.setWebViewClient(new WebViewClient() {
            //弹出系统浏览器的解决方法
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                return false;
            }
        });*/

//        String url = "file:///mnt/sdcard/searchnoresult.html";
//        String url = "file:///android_asset/searchnoresult.html";
//        String url = "file:///mnt/sdcard/01.png";
//        String url = "http://www.baidu.com";
//        String url = "https://m.baidu.com";
        String url = "http://3g.163.com";
//        String url = "http://192.168.0.104:8080/webview/abc.html";

        wv.setWebViewClient(new WebViewClient() {
            //弹出系统浏览器的解决方法
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
//                //返回false则使用当前的WebView加载链接，返回true则方法中代码决定如何展示
////                view.loadUrl(url);
////                return true;
//                return false;

                if(Uri.parse(url).getHost().equals("m.baidu.com")){
                    return false;
                }
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if(pd!=null){
                    pd.cancel();// 关闭进度条
                }
                //起到过滤拦截的作用
                if(Uri.parse(url).getHost().equals("m.baidu.com")){
                    Log.e("TAG", "---------->Hello!");
                }
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if(pd==null) {
                    pd=new ProgressDialog(MainActivity.this);//创建进度条
                }
                pd.setMessage("正在载入，请稍候……");
                pd.show();//显示进度条
                super.onPageStarted(view, url, favicon);
            }
        });

        WebSettings setting = wv.getSettings();
        setting.setJavaScriptEnabled(true);
        //打开页面时，自适应屏幕
        setting.setUseWideViewPort(true);
        //设置视图是否加载概览模式的网页
        setting.setLoadWithOverviewMode(true);
        //设置显示缩放按钮
        setting.setBuiltInZoomControls(true);
        //使页面支持缩放
        setting.setSupportZoom(true);
        //不使用缓存
        setting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        wv.loadUrl(url);
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
            }else {
                initWebView();
            }
        }else {
            initWebView();
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
                    initWebView();
                }else{
                    // 不允许
                    Toast.makeText(this,getString(R.string.permisstion_deny),Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK && wv.canGoBack()){
            wv.goBack();
            return true;
        }
//		if(keyCode==KeyEvent.KEYCODE_BACK && wv.canGoBackOrForward(-2)){
//			wv.goBackOrForward(-2);
//			return true;
//		}
        return super.onKeyDown(keyCode, event);
    }
}
