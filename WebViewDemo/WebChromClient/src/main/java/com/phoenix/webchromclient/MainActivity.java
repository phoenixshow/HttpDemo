package com.phoenix.webchromclient;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebIconDatabase;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR2;
import static com.phoenix.webchromclient.R.id.iv;
import static com.phoenix.webchromclient.R.id.pb;
import static com.phoenix.webchromclient.R.id.tv;

public class MainActivity extends AppCompatActivity {
    ProgressBar pb;
    ImageView iv;
    TextView tv;
    WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pb = (ProgressBar) findViewById(R.id.pb);
        iv = (ImageView) findViewById(R.id.iv);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR2){
            //必须先打开图标数据库，否则无法获取到页面图标
            WebIconDatabase.getInstance().open(getDir("icons", MODE_PRIVATE).getPath());
        }

        tv = (TextView) findViewById(R.id.tv);
        wv = (WebView) findViewById(R.id.wv);

        wv.setWebChromeClient(new WebChromeClient(){

            @Override
            public void onCloseWindow(WebView window) {
                finish();
            }

            //获得网页的加载进度，显示在TextView控件中
            @Override
            public void onProgressChanged(WebView view, final int newProgress) {
				if(newProgress<100){
					tv.setText(newProgress + "%");
					pb.setProgress(newProgress);
				}else{
					tv.setText("加载完毕");
					pb.setProgress(100);
				}
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                MainActivity.this.setTitle(title);
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
                iv.setImageBitmap(icon);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Alert警示框")
                        .setMessage(message)
                        .setPositiveButton("确定", null)
                        .show();
                result.confirm();
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url,
                                       String message, final JsResult result) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Confirm确认信息")
                        .setMessage(message)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm();
                            }
                        })
                        .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.cancel();
                            }
                        })
                        .show();
                return true;
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message,
                                      String defaultValue, final JsPromptResult result) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.dialog);
                dialog.setTitle("Prompt输入信息");

                TextView tv = (TextView)dialog.findViewById(R.id.tv);
                final EditText et = (EditText)dialog.findViewById(R.id.et);
                Button ok = (Button)dialog.findViewById(R.id.ok);
                Button cancle = (Button)dialog.findViewById(R.id.cancle);

                tv.setText(message);
                et.setText(defaultValue);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        result.confirm(et.getText().toString().trim());
                        dialog.cancel();
                    }
                });
                cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        result.cancel();
                        dialog.cancel();
                    }
                });

                //需要监听Dialog的back事件，否则会出错
                dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if(keyCode==KeyEvent.KEYCODE_BACK){
                            result.cancel();
                            dialog.cancel();
                            return true;
                        }
                        return false;
                    }
                });
                dialog.show();
                //此处需要消费事件，必须返回true，否则会弹出html的对话框
                return true;
            }

        });

//        String url = "http://192.168.0.104:8080/webview/alert.html";
//        String url = "http://192.168.0.104:8080/webview/confrim.html";
        String url = "http://192.168.0.104:8080/webview/prompt.html";
//        String url = "http://192.168.0.104:8080/webview/close.html";

        //弹出系统浏览器的解决方法
        wv.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url){
//                Log.e("TAG", "shouldOverrideUrlLoading Thread ID--->" + Thread.currentThread().getId());
//                Log.e("TAG", "shouldOverrideUrlLoading Thread Name--->" + Thread.currentThread().getName());
//                return false;
//            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
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
}
