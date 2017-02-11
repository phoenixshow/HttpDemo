package com.phoenix.download;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.phoenix.download.service.HtmlService;

/**
 * Created by flashing on 2017/2/11.
 */
public class HtmlActivity extends AppCompatActivity {
    EditText et;
    Button btn;
    WebView wv;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    Toast.makeText(HtmlActivity.this, "获取失败", Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    wv.loadDataWithBaseURL(null, (String)msg.obj, "text/html", "utf-8", null);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html);
        et = (EditText)findViewById(R.id.et);
        btn = (Button)findViewById(R.id.btn);
        wv = (WebView)findViewById(R.id.wv);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ServerThread().start();
            }
        });
    }

    class ServerThread extends Thread{
        public void run(){
            String path = et.getText().toString().trim();
            try {
                String data = HtmlService.getHtml(path);
                Message msg = handler.obtainMessage();
                msg.obj = data;
                msg.what = 1;
                handler.sendMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
                handler.sendEmptyMessage(0);
            }
        }
    }
}
