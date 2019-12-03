package com.fda_sampling.service;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class HttpUtils {
    //private static String ip = "http://test.3tpi.com:7050/";
    private static String ip = "http://124.117.209.42:8088/WebApi/";
    private static String ip_WebReport = "http://124.117.209.42:8088/WebReport/";
    //private static String ip_problem = "http://192.168.0.141:5555/";
    private static String ip_problem = "http://10.200.102.40:5555/";

    public static FDA_API JsonApi() {
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)//默认重试一次，若需要重试N次，则要实现拦截器。
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(200, TimeUnit.SECONDS)
                .writeTimeout(200, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ip)
                .addConverterFactory(GsonConverterFactory.create()) //设置使用Gson解析
                .client(client)
                .build();
        //创建 网络请求接口 的实例
        return retrofit.create(FDA_API.class);
    }

    public static FDA_API XmlApi() {
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)//默认重试一次，若需要重试N次，则要实现拦截器。
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(200, TimeUnit.SECONDS)
                .writeTimeout(200, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ip)
                .addConverterFactory(SimpleXmlConverterFactory.create()) //设置使用SimpleXml解析
                .client(client)
                .build();
        //创建 网络请求接口 的实例
        return retrofit.create(FDA_API.class);
    }

    public static FDA_API StreamApi() {
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)//默认重试一次，若需要重试N次，则要实现拦截器。
                .connectTimeout(1000, TimeUnit.SECONDS)
                .readTimeout(2000, TimeUnit.SECONDS)
                .writeTimeout(2000, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ip)
                .client(client)
                .build();
        //创建 网络请求接口 的实例
        return retrofit.create(FDA_API.class);
    }

    public static FDA_API ReportApi() {
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)//默认重试一次，若需要重试N次，则要实现拦截器。
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(200, TimeUnit.SECONDS)
                .writeTimeout(200, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ip_WebReport)
                .addConverterFactory(GsonConverterFactory.create()) //设置使用Gson解析
                .client(client)
                .build();
        //创建 网络请求接口 的实例
        return retrofit.create(FDA_API.class);
    }

    public static String getIp_WebReport() {
        return ip_WebReport;
    }

    public static FDA_API JsonApi_send() {
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)//默认重试一次，若需要重试N次，则要实现拦截器。
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(200, TimeUnit.SECONDS)
                .writeTimeout(200, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ip_problem)
                .addConverterFactory(GsonConverterFactory.create()) //设置使用Gson解析
                .client(client)
                .build();
        //创建 网络请求接口 的实例
        return retrofit.create(FDA_API.class);
    }
}
