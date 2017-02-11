package com.phoenix.download;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.phoenix.download.service.FileService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by flashing on 2017/2/11.
 */
public class FileActivity extends AppCompatActivity {
    EditText et;
    Button btn;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    Toast.makeText(FileActivity.this, "下载失败", Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    Toast.makeText(FileActivity.this, "下载成功", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        et = (EditText)findViewById(R.id.et);
        btn = (Button)findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ServerThread().start();
            }
        });
    }

    class ServerThread extends Thread{
        private FileOutputStream fos = null;

        public void run(){
            String path = et.getText().toString().trim();
            try {
                byte[] data = FileService.getFile(path);
                String fileName = Environment.getExternalStorageDirectory().getPath()+File.separator+System.currentTimeMillis()+path.substring(path.lastIndexOf("."));
                Log.e("TAG", fileName);
                File file = new File(fileName);
                if (!file.exists()){
                    file.createNewFile();
                }
                fos = new FileOutputStream(file);
                fos.write(data, 0, data.length);
                handler.sendEmptyMessage(1);
            } catch (final Exception e) {
                e.printStackTrace();
                handler.sendEmptyMessage(0);
            } finally {
                if(fos!=null){
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
