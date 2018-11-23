package com.example.juicekaaa.fireserver.firebox;

import android.content.Context;

import com.example.juicekaaa.fireserver.udp.UDPSocket;

import static com.example.juicekaaa.fireserver.util.EncodingConversionTools.HexString2Bytes;


public class FireBox {
    private UDPSocket udpSocket;

    public FireBox(Context context, String ip, int port) {
        udpSocket = new UDPSocket(context, ip, port);
        udpSocket.startUDPSocket();
    }


    public void openBox() {
        final String OPEN_BOX = "68010102ffff16";
        udpSocket.sendBytes(HexString2Bytes(OPEN_BOX));
    }
}
