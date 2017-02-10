package com.phoenix.httpurlconnection;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Member;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn1;
    private Button btn2;

    private String urlAddress = "http://cloud.bmob.cn/c284de47021c899d/";
    private String method = "getMemberBySex";

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
                doGet("boy");
                break;
            case R.id.btn2:
                doPost("girl");
                break;
            default:
                break;
        }
    }

    // doPost
    private void doPost(final String s) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlAddress + method);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setUseCaches(false);
                    httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
                    httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    httpURLConnection.connect();

                    DataOutputStream outputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                    String content = "sex="+s;
                    outputStream.writeBytes(content);
                    outputStream.flush();
                    outputStream.close();

                    if (httpURLConnection.getResponseCode() == 200){
                        InputStream inputStream = httpURLConnection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        StringBuffer stringBuffer = new StringBuffer();
                        String readLine = "";
                        while ((readLine = bufferedReader.readLine()) != null){
                            stringBuffer.append(readLine);
                        }
                        inputStream.close();
                        bufferedReader.close();
                        httpURLConnection.disconnect();
                        Log.e("TAG", stringBuffer.toString());
                    }else {
                        Log.e("TAG", "faile");
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // doget
    private void doGet(String s) {
        final String getUrl = urlAddress + method + "?sex=" + s;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(getUrl);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    //connect()函数会根据HttpURLConnection对象的配置值生成http头部信息，因此在调用connect函数之前，就必须把所有的配置准备好
                    httpURLConnection.connect();

                    if (httpURLConnection.getResponseCode() == 200){
                        //connect()函数，实际上只是建立了一个与服务器的tcp连接，并没有实际发送http请求。无论是post还是get，http请求实际上直到HttpURLConnection的getInputStream()这个函数里面才正式发送出去
                        InputStream inputStream = httpURLConnection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        StringBuffer stringBuffer = new StringBuffer();
                        String readLine = "";
                        while ((readLine = bufferedReader.readLine()) != null){
                            stringBuffer.append(readLine);
                        }
                        inputStream.close();
                        bufferedReader.close();
                        httpURLConnection.disconnect();
                        Log.e("TAG", stringBuffer.toString());
                    }else {
                        Log.e("TAG", "faile");
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
