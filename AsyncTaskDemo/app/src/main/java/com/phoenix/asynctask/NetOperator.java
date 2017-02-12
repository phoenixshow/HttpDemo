package com.phoenix.asynctask;

/**
 * 模拟访问网络的操作
 */
public class NetOperator {
    public void operate(){
        try {
            Thread.sleep(8 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}