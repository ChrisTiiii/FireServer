package com.example.juicekaaa.fireserver;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bjw.bean.ComBean;
import com.bjw.utils.FuncUtil;
import com.bjw.utils.SerialHelper;
import com.example.juicekaaa.fireserver.firebox.FireBox;
import com.example.juicekaaa.fireserver.imp.OrderService;
import com.example.juicekaaa.fireserver.tcp.TCPSocket;
import com.example.juicekaaa.fireserver.udp.HeartbeatTimer;
import com.example.juicekaaa.fireserver.util.EncodingConversionTools;
import com.example.juicekaaa.fireserver.util.GetMac;
import com.example.juicekaaa.fireserver.util.GlideImageLoader;
import com.example.juicekaaa.fireserver.util.MessageEvent;
import com.youth.banner.Banner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android_serialport_api.SerialPortFinder;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {
//    private static final int PORT = 12342;//接收客户端的监听端口
    private static TCPSocket tcpSocket;
    public String SERVICE_IP = "10.101.208.155";//10.101.208.78   10.101.80.134
    public int SERVICE_PORT = 23303;
    private Socket socket = new Socket();
    private OrderService orderService;
    private String MAC = "";
    HeartbeatTimer timer;


    private List<Integer> bannerList = new ArrayList();
    @BindView(R.id.video)
    VideoView video;

    @BindView(R.id.banner1)
    Banner banner1;
    @BindView(R.id.banner2)
    Banner banner2;

    private FireBox fireBox;
    public static final String SHEBEI_IP = "10.101.208.101";
    public static final String SHEBEI_PORT = "28327";
    private static final String CHUAN = "/dev/ttymxc2";
    private static final String BOTE = "9600";
//        private static final String CHUAN = "/dev/ttyS0";
    private static final int TCP_BACK_DATA = 0x213;

    private SerialPortFinder serialPortFinder;
    private SerialHelper serialHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        initView();
        initSeceive();
    }

    //初始化TCP通讯
    private void initSeceive() {
        //用于接收命令
        tcpSocket = new TCPSocket(socket, SERVICE_IP, SERVICE_PORT, 2);
        tcpSocket.start();

        //用于发送心跳包
        TCPSocket sendHeart = new TCPSocket(EncodingConversionTools.HexString2Bytes(MAC));
//        tcpSocket.setPriority(Thread.NORM_PRIORITY + 3);
        sendHeart.start();

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void backData(MessageEvent messageEvent) {
        switch (messageEvent.getTAG()) {
            case TCP_BACK_DATA:
                String order = messageEvent.getMessage();
                order = order.replaceAll(" ", "");
                Toast.makeText(MainActivity.this, "收到信息啦！" + order, Toast.LENGTH_LONG).show();
                if (serialHelper.isOpen()) {
                    Toast.makeText(this, "开门成功", Toast.LENGTH_LONG).show();
                    serialHelper.sendHex(order);
                } else
                    Toast.makeText(this, "串口没打开", Toast.LENGTH_LONG).show();
                break;

        }

    }

    private void initView() {
        initData();
        //加载图片
        banner1.setImages(bannerList).setImageLoader(new GlideImageLoader()).start();
        banner2.setImages(bannerList).setImageLoader(new GlideImageLoader()).start();
        setVideo();
        initSerial();
    }

    /**
     * 初始化串口
     */
    private void initSerial() {
        serialPortFinder = new SerialPortFinder();
        serialHelper = new SerialHelper() {
            @Override
            protected void onDataReceived(final ComBean comBean) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //接收设备回传数据
                        Toast.makeText(getBaseContext(), FuncUtil.ByteArrToHex(comBean.bRec), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        try {
//            设置串口信息
            serialHelper.setBaudRate(BOTE);
            serialHelper.setPort(CHUAN);
            serialHelper.open();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * 设置视频参数
     */
    private void setVideo() {
        MediaController mediaController = new MediaController(this);
        mediaController.setVisibility(View.GONE);//隐藏进度条
        video.setMediaController(mediaController);
        video.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.sanleng));
        video.start();
        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
                mediaPlayer.setLooping(true);
            }
        });
    }


    //静态初始化数据
    void initData() {
        MAC = GetMac.getMacAddress().replaceAll(":", "");
        System.out.println("mac: " + MAC);
//        socket = new Socket();
        bannerList = new ArrayList<>();
        bannerList.add(R.drawable.banner_1);
        bannerList.add(R.drawable.banner_2);
        bannerList.add(R.drawable.banner_3);
        bannerList.add(R.drawable.banner_4);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        serialHelper.close();//关闭串口
        tcpSocket.stopSocket();//关闭tcp通讯
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }


}
