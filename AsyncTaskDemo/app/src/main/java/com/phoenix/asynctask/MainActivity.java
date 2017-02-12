package com.phoenix.asynctask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void netOperate(View view) {
        /*直接阻塞UI线程会导致ANR
        NetOperator netOperator = new NetOperator();
        netOperator.operate();*/

        /*放在异步任务中执行，阻塞的不是主线程，就不会出现ANR了*/
        FirstAsyncTask asyncTask = new FirstAsyncTask();
        asyncTask.execute();
    }

    public void print(View view) {
        Log.e("TAG", getString(R.string.print));
    }
}
