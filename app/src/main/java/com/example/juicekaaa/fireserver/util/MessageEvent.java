package com.example.juicekaaa.fireserver.util;

/**
 * author: ZhongMing
 * DATE: 2018/11/16 0016
 * Description:
 **/
public class MessageEvent {
    private int TAG;
    private String message;

    public MessageEvent(int TAG, String message) {
        this.TAG = TAG;
        this.message = message;
    }

    public int getTAG() {
        return TAG;
    }

    public void setTAG(int TAG) {
        this.TAG = TAG;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
