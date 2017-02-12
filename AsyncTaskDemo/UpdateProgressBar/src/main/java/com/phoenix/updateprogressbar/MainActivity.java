package com.phoenix.updateprogressbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView textView = null;
    private ProgressBar progressBar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.tv);
        progressBar = (ProgressBar)findViewById(R.id.pb);
    }

    public void startAsyncTask(View view) {
        ProgressBarAsyncTask asyncTask = new ProgressBarAsyncTask(textView,progressBar);
        asyncTask.execute(100);
    }
}
