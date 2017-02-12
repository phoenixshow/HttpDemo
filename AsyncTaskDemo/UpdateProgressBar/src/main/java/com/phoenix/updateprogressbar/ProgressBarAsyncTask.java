package com.phoenix.updateprogressbar;

import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 生成该类的对象，并调用其execute方法之后
 * 首先执行的的onPreExecute方法
 * 其次是执行doInBackground方法
 */
public class ProgressBarAsyncTask extends AsyncTask<Integer, Integer, String> {

    private TextView textView = null;
    private ProgressBar progressBar = null;

    public ProgressBarAsyncTask(TextView textView,ProgressBar progressBar) {
        this.textView = textView;
        this.progressBar = progressBar;
    }

    //该方法并不运行在UI线程当中，所以在该方法当中，不能对UI当中的控件进行设置和修改
    //主要用于进行异步操作。
    @Override
    protected String doInBackground(Integer... param) {
        NetOperator netOperator = new NetOperator();
        int total = param[0].intValue();
        for(int i = total/10; i <= total; i+=total/10){
            netOperator.operate();
            //用于发布更新消息
            publishProgress(i);
        }
        return "共"+total;
    }

    //在doInBackground方法执行结束之后再运行，并且运行在UI线程当中。
    //主要用于将异步任务执行的结果展示给客户
    @Override
    protected void onPostExecute(String result) {
        textView.setText("异步操作执行结束，" + result);
    }

    //该方法运行在UI线程当中,主要用于进行异步操作之前的UI准备工作
    @Override
    protected void onPreExecute() {
        textView.setText("开始执行异步操作");
    }

    //在doInBackground方法当中，每次调用publishProgress()方法之后，都会触发该方法
    //用于在异步任务执行的过程当中，对用户进行提示，例如控制进度条等
    @Override
    protected void onProgressUpdate(Integer... values) {
        int value = values[0];
        progressBar.setProgress(value);
        textView.setText("当前：" + value);
    }
}