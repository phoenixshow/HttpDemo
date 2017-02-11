package com.phoenix.download;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.phoenix.download.service.ImageService;

/**
 * Created by flashing on 2017/2/11.
 */
public class ImageActivity extends AppCompatActivity {
    EditText et;
    Button btn;
    ImageView iv;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    Toast.makeText(ImageActivity.this, "获取失败", Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    iv.setImageBitmap((Bitmap)msg.obj);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img);
        et = (EditText)findViewById(R.id.et);
        btn = (Button)findViewById(R.id.btn);
        iv = (ImageView)findViewById(R.id.iv);
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
            Message msg = handler.obtainMessage();
            try {
                byte[] data = ImageService.getImage(path);
                //生成位图
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                msg.obj = bitmap;
                msg.what = 1;
                handler.sendMessage(msg);
            } catch (final Exception e) {
                e.printStackTrace();
                handler.sendEmptyMessage(0);
            }
        }
    }
}
