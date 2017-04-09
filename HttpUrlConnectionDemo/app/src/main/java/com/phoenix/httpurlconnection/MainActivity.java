package com.phoenix.httpurlconnection;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.password;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn1;
    private Button btn2;
    private EditText usernameEt;
    private EditText passwordEt;
    private Button loginBtn;

    private String urlAddress = "http://cloud.bmob.cn/c284de47021c899d/";
    private String method = "getMemberBySex";

    private String path = "http://192.168.0.103:8080/web/servlet/UserServlet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        assignViews();

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
    }

    private void assignViews() {
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        usernameEt = (EditText) findViewById(R.id.username_et);
        passwordEt = (EditText) findViewById(R.id.password_et);
        loginBtn = (Button) findViewById(R.id.login_btn);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                doGet("girl");
                break;
            case R.id.btn2:
                doPost("boy");
                break;
            case R.id.login_btn:
//                login();
                loginPost();
                break;
            default:
                break;
        }
    }

    private void loginPost() {
        new Thread(){
            @Override
            public void run() {
                String username = usernameEt.getText().toString().trim();
                String pwd = passwordEt.getText().toString().trim();
                //表单验证略
                try {
                    //由于传递的Content-Type内容是urlencoded，参数的中文内容需要进行url编码，否则获取的Content-Length长度会有问题
//                    String params = "username="+username+"&password="+pwd;
                    String params = "username="+URLEncoder.encode(username, "utf-8") +"&password="+ URLEncoder.encode(pwd, "utf-8");
                    URL url = new URL(path);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setConnectTimeout(10000);
                    //设置跟POST请求相关的请求头
                    httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");//要传递的数据类型
                    httpURLConnection.setRequestProperty("Content-Length", String.valueOf(params.length()));//传递的数据长度
                    httpURLConnection.setDoOutput(true);//打开输出流
                    httpURLConnection.getOutputStream().write(params.getBytes());//通过流把请求体写到服务端
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
        }.start();
    }

    private void login() {
        new Thread(){
            @Override
            public void run() {
                String username = usernameEt.getText().toString().trim();
                String pwd = passwordEt.getText().toString().trim();
                //表单验证略
                try {
                    //get方式提交，参数放到url的后面
//                String tempUrl = path+"?username="+username+"&password="+pwd;
                    /**
                     * 如果提交的参数包含中文，且服务端接收到的为乱码，要先进行url编码，然后再提交
                     * URLEncoder.encode(要提交的内容, 用到的字符集)
                     */
                    String tempUrl = path+"?username="+ URLEncoder.encode(username, "utf-8") +"&password="+ URLEncoder.encode(pwd, "utf-8");
                    URL url = new URL(tempUrl);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(10000);
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
        }.start();
    }

    // doPost
    private void doPost(final String s) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String content = "sex="+s+"&maxAge="+20;
                    URL url = new URL(urlAddress + method);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setConnectTimeout(10000);
                    //设置跟POST请求相关的请求头
                    httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");//要传递的数据类型
                    httpURLConnection.setRequestProperty("Content-Length", String.valueOf(content.length()));//传递的数据长度
                    httpURLConnection.setDoOutput(true);//打开输出流
                    httpURLConnection.getOutputStream().write(content.getBytes());//通过流把请求体写到服务端
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


//                try {
//                    String content = "sex="+s+"&maxAge="+20;
//
//                    URL url = new URL(urlAddress + method);
//                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//                    httpURLConnection.setDoInput(true);
//                    httpURLConnection.setDoOutput(true);
//                    httpURLConnection.setRequestMethod("POST");
//                    httpURLConnection.setUseCaches(false);
//                    httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
//                    httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
////                    httpURLConnection.setRequestProperty("Content-Length", String.valueOf(content.length()));//传递的数据长度
//                    httpURLConnection.connect();
//
//                    DataOutputStream outputStream = new DataOutputStream(httpURLConnection.getOutputStream());
//                    outputStream.writeBytes(content);
//                    outputStream.flush();
//                    outputStream.close();
//
//                    if (httpURLConnection.getResponseCode() == 200){
//                        InputStream inputStream = httpURLConnection.getInputStream();
//                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//                        StringBuffer stringBuffer = new StringBuffer();
//                        String readLine = "";
//                        while ((readLine = bufferedReader.readLine()) != null){
//                            stringBuffer.append(readLine);
//                        }
//                        inputStream.close();
//                        bufferedReader.close();
//                        httpURLConnection.disconnect();
//                        Log.e("TAG", stringBuffer.toString());
//                    }else {
//                        Log.e("TAG", "faile");
//                    }
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        }).start();
    }

    // doget
    private void doGet(String s) {
        final String getUrl = urlAddress + method + "?sex=" + s+"&maxAge="+20;

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
