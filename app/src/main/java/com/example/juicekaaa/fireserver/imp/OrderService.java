package com.example.juicekaaa.fireserver.imp;

import android.content.Context;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OrderService {
    private Retrofit retrofit;
    private static final String BASE_URL = "10.101.80.134:23303";
    private Context context;
    private String mac;

    public OrderService(Context context, String mac) {
        this.context = context;
        this.mac = mac;
    }

    public void sendMac() {
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        IpService ipService = retrofit.create(IpService.class);
        Call<ResponseBody> call = ipService.getCall(mac);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    System.out.println(response.body().bytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
