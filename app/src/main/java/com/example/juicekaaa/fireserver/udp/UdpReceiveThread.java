package com.example.juicekaaa.fireserver.udp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UdpReceiveThread extends Thread {
    private final String TAG = "UdpReceiveThread";
    private int port;
    private Handler handler;

    public UdpReceiveThread(int port, Handler handler) {
        this.handler = handler;
        this.port = port;
    }

    @Override
    public void run() {
        while (isAlive()) { //循环接收，isAlive() 判断防止无法预知的错误
            try {
                sleep(1000); //让它好好休息一会儿
                DatagramSocket socket = new DatagramSocket(port); //建立 socket
                byte data[] = new byte[1024];
                DatagramPacket packet = new DatagramPacket(data, data.length);
                socket.receive(packet); //阻塞式，接收发送方的 packet
                String result = new String(packet.getData(), packet.getOffset(), packet.getLength()); //packet 转换
                Log.i(TAG, "UDP result: " + result);
                Message msg = new Message();
                msg.what = 0x211;
                Bundle bundle = new Bundle();
                bundle.putString("order", result);
                msg.setData(bundle);
                handler.sendMessage(msg);
                socket.close(); //必须及时关闭 socket，否则会出现 error
            } catch (Exception e) {
                e.printStackTrace();
                break; //当 catch 到错误时，跳出循环
            }
        }
    }
}