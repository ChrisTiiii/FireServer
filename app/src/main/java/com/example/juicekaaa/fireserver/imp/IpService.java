package com.example.juicekaaa.fireserver.imp;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IpService {
    @GET("/")
    Call<ResponseBody> getCall(@Query("mac") String mac);//接收网络请求数据的方法

}
