package com.fda_sampling.service;

import com.fda_sampling.model.LoginStatus;

import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Post_Login {
    @Headers({
            "accept: application/json",
            "Content-Type: application/json"})
    @POST("Login")
    Call<LoginStatus> getCall(@Query("data") String data);
}
