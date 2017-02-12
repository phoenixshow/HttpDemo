package com.phoenix.updateprogressbar;

/**
 * 模拟访问网络的操作
 */
public class NetOperator {
    public void operate(){
        try {
            Thread.sleep(1 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}