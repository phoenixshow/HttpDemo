package com.phoenix.asynctask;

import android.os.AsyncTask;

/**
 * Created by flashing on 2017/2/11.
 */
public class FirstAsyncTask extends AsyncTask<Void, Void, Void> {
    @Override
    protected Void doInBackground(Void... params) {
        NetOperator netOperator = new NetOperator();
        netOperator.operate();
        return null;
    }
}
